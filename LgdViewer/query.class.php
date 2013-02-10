<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class  lgd_query
{
	var $snoopy;
	var $file;
	var $error;
		
	var $_geo_lat = 0;
	var $_geo_long = 0;
	var $_geo_dist = 0;
	
	var $_is_print_node_json = false;
	var $_is_print_node_binding = false;
	var $_is_print_node_result = false;
		
function lgd_query()
{
	$this->snoopy = new Snoopy();
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_query();
	}
	return $instance;
}

function set_geo( $lat, $long, $dist )
{
	$this->_geo_lat = $lat;
	$this->_geo_long = $long;
	$this->_geo_dist = $dist;
}

function get_node_array()
{	
	$flag_finish = false;				
	$array = array();
	$check_array = array();

$query =<<<EOF
Prefix lgdo: <http://linkedgeodata.org/ontology/>
Select *
From <http://linkedgeodata.org>
{
 ?url a lgdo:Node ;
 rdfs:label ?label ;
 geo:geometry ?geometry ;
 geo:lat ?lat ;
 geo:long ?long .
 OPTIONAL { ?url lgdo:directType ?direct_type . }
 Filter( bif:st_intersects (?geometry, bif:st_point ( $this->_geo_long, $this->_geo_lat ), $this->_geo_dist ) )
}
EOF;

	// --- list ---
	$url  = $this->build_url_query( $query );
	$result = $this->fetch_url( $url );
	$obj  = json_decode( $result );		
	$bindings = $obj->results->bindings;
	if ( !is_array($bindings) || !count($bindings) ) return false; 
	
	foreach ( $bindings as $binding ) {

		$url = $binding->url->value ;
		$label_value = $binding->label->value ;
		$label_key = $this->get_label_key( $binding ) ;
			
		if ( isset( $check_array[ $url ] ) ) {
			$temp[ $label_key ] = $label_value ;
		} else {
			$check_array[ $url ] = true;
			$temp = array(
				'lgdo:Node' => $url ,
				'geo:geometry' => $binding->geometry->value ,
				'geo:lat' => $binding->lat->value ,
				'geo:long' => $binding->long->value ,
				'lgdo:directType' => $binding->direct_type->value ,
			);
			$temp[ $label_key ] = $label_value ;
		}

		$array[ $url ] = $temp;
	}

	return $array;
}

function get_ontology( $ontology )
{
	$query = $this->build_query_triplify( $ontology );	
	$url  = $this->build_url_query( $query );
	$result = $this->fetch_url( $url );
	$obj  = json_decode( $result );		
	$bindings = $obj->results->bindings;
	if ( !is_array($bindings) || !count($bindings) ) return false; 

	$rdfs_type = "";
	$sub_class_of = "";
	$label_en = "";
	$label_ja = "";		
	
	foreach ( $bindings as $binding ) {
	
		$p = $binding->p->value ;
		$o = $binding->o->value ;
			
		switch($p) {
			case "http://www.w3.org/1999/02/22-rdf-syntax-ns#type":
				$rdfs_type = $o;
				break;
			case "http://www.w3.org/2000/01/rdf-schema#subClassOf":
				$sub_class_of = $o;
				break;
			case "http://www.w3.org/1999/02/22-rdf-syntax-ns#type":
				$rdfs_type = $o;
				break;
			case "http://www.w3.org/2000/01/rdf-schema#label":
				$lang = $this->get_lang( $binding, "o" );		
				if ( $lang == "ja" ) {
					$label_ja = $o;
				}	
				break;
		}
	}

	$array = array(
		"rdfs:type" => $rdfs_type , 
		"rdf:subClassOf" => $sub_class_of ,
		"rdf:label:ja" => $label_ja ,							
	);
	return $array;
}

function get_node( $node, $is_error )
{	
	$URL_TRIPLIFY = "http://linkedgeodata.org/triplify/";	
	if ( strpos( $node, $URL_TRIPLIFY ) !== 0 ) {
		return false;
	}
	
	$query = $this->build_query_triplify( $node );	
	$url  = $this->build_url_query( $query );
	$result = $this->fetch_url( $url );
	$obj  = json_decode( $result );

	if ( $this->_is_print_node_json ) {	
		echo "<pre>\n"	;
		print_r( $obj );
		echo "</pre>\n";
	}
	
	$bindings = $obj->results->bindings;
	if ( !is_array($bindings) || !count($bindings) ) return false; 

	$rdfs_type_array = array();
	$rdf_label_array = array();
	$standard_array = array();	
	$ontology_array = array();	
	$property_array = array();	
	$unknown_array = array();	
													
	foreach ( $bindings as $binding ) {

		$p = $binding->p->value ;
		$o = $binding->o->value ;

		if ( $this->_is_print_node_binding ) {	
			echo $p." : ".$o."<br>\n";
		}
			
		switch( $p ) {
			case "http://www.w3.org/1999/02/22-rdf-syntax-ns#type":
				$rdfs_type_array[] = $o;
				break;
			case "http://www.w3.org/2000/01/rdf-schema#label":
				$lang = $this->get_lang( $binding, "o" );
				if ( $lang == "" ) $lang = "default";
				$rdf_label_array[ $lang ] = $o;							
				break;
			case "http://www.w3.org/2003/01/geo/wgs84_pos#geometry":
				$standard_array[ "geo:geometry" ] = $o;
				break;		
			case "http://www.w3.org/2003/01/geo/wgs84_pos#lat":
				$standard_array[ "geo:lat" ] = $o;
				break;		
			case "http://www.w3.org/2003/01/geo/wgs84_pos#long":
				$standard_array[ "geo:long" ] = $o;
				break;				
			case "http://www.w3.org/2002/07/owl#sameAs":
				$standard_array[ "owl:sameAs" ] = $o;
				break;		

			default:
				if ( strpos( $p, "http://linkedgeodata.org/ontology/" ) === 0 ) {
					$URL = "http://linkedgeodata.org/ontology/";
					$name = str_replace( $URL, "", $p );
					$ontology_array[ $name ] = $o;
					break;
				}

				if ( strpos( $p, "http://linkedgeodata.org/property/" ) === 0 ) {
					$URL = "http://linkedgeodata.org/property/";
					$name = str_replace( $URL, "", $p );
					$property_array[ $name ] = $o;
					break;
				}

				$unknown_array[ $p ] = $o;
				$msg = "unknown : ".$node." ".$p." ".$o;
				$this->error->set_error(	$msg );
				$this->file->write_log( $msg );
				break;	
		}
	}

	$array = array();
	$array[ "lgdo:Node" ] = $node;
	foreach( $rdfs_type_array as $k => $v ) {
		$array[ "rdfs:type:".$k ] = $v ;
	}	
	foreach( $rdf_label_array as $k => $v ) {
		if ( $k == "default" ) {
			$key = "rdf:label";
		} else {	
			$key ="rdf:label:".$k;
		}
		$array[ $key ] = $v;
	}
	foreach( $standard_array as $k => $v ) {
		$array[ $k ] = $v;
	}
	foreach( $ontology_array as $k => $v ) {
		$array[ "lgdo:".$k ] = $v;
	}	
	foreach( $property_array as $k => $v ) {
		$array[ "lgdp:".$k ] = $v;
	}	
	foreach( $unknown_array as $k => $v ) {
		$array[ $k ] = $v;
	}
		
	if ( $this->_is_print_node_result ) {	
		echo "<pre>\n"	;
		print_r( $array );
		echo "</pre>\n";
	}
			
	return $array;		
}

function get_label_key( $obj )
{
	$lang = $this->get_lang( $obj , "label" ) ;
	$key = "rdf:label";
	if ( $lang ) {
		$key = "rdf:label:".$lang;
	}
	return $key;
}

function get_lang( $obj, $name )
{
	$array = (array) $obj->$name;
	$lang = "" ;			
	if ( isset( $array["xml:lang"] ) ) {
		$lang = $array["xml:lang"] ;
	}
	return $lang;
}

function build_query_triplify( $url )
{	
	$triplify = "<".$url.">";
	
$query =<<<EOF
SELECT DISTINCT *
WHERE {
  $triplify ?p ?o .
}
EOF;

	return $query;
}

function build_url_query( $query )
{
	$URL_ENDPOINT = "http://live.linkedgeodata.org/sparql";
	$URL_GRAPH = "http://linkedgeodata.org";

	$url  = $URL_ENDPOINT;
	$url .= "?default-graph-uri=".urlencode( $URL_GRAPH );		
	$url .= "&query=".urlencode( $query );
	$url .= "&output=json";
	return $url;
}

function fetch_url( $url )
{
	$this->snoopy->fetch( $url );
	return $this->snoopy->results;
}
		
// class end
}

?>