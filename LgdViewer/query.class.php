<?php
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class  lgd_query
{
	var $URL_ENDPOINT_STABLE = "http://linkedgeodata.org/sparql";
	var $URL_ENDPOINT_LIVE = "http://live.linkedgeodata.org/sparql";

	var $URL_GRAPH = "http://linkedgeodata.org";	
	var $URL_TRIPLIFY = "http://linkedgeodata.org/triplify/";	
	var $URL_ONTOLOGY = "http://linkedgeodata.org/ontology/";
	var $URL_PROPERTY = "http://linkedgeodata.org/property/";
				
	var $URL_DC = "http://purl.org/dc/terms/";
	var $URL_OWL = "http://www.w3.org/2002/07/owl#";
	var $URL_GEO = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	var $URL_GEOMETRY = "http://geovocab.org/geometry#";
	var $URL_FOAF = "http://xmlns.com/foaf/0.1/";

	var $URL_DATA = "http://linkedgeodata.org/data/ontology/";
	var $OUTPUT = "?output=xml";
					
	var $snoopy;
	var $ontology_utlity;
	var $file;
	var $error;

	var $URL_ENDPOINT = "";
			
	var $_geo_lat = 0;
	var $_geo_long = 0;
	var $_geo_dist = 0;
	
	var $_is_print_node_json = false;
	var $_is_print_node_binding = false;
	var $_is_print_node_result = false;
		
function lgd_query()
{
	$this->snoopy = new Snoopy();
	$this->ontology_utility =& lgd_ontology_utility::getInstance();
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
	$this->URL_ENDPOINT = $this->URL_ENDPOINT_STABLE;
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

function set_endpoint_live()
{
	$this->URL_ENDPOINT = $this->URL_ENDPOINT_LIVE;
}

function get_node_array()
{	
	$KEY_TYPES = "types";
	$KEY_LABELS = "lables";
	
	$flag_finish = false;				
	$array = array();
	$check_array = array();

$query =<<<EOF
Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
Prefix ogc: <http://www.opengis.net/ont/geosparql#>
Prefix geom: <http://geovocab.org/geometry#>
Prefix lgdm: <http://linkedgeodata.org/meta/>
Prefix lgdo: <http://linkedgeodata.org/ontology/>
SELECT *
{
 ?url a lgdm:Node ;
 rdfs:label ?label ;
 rdf:type ?type ;
 geo:lat ?lat ;
 geo:long ?long ;
 geom:geometry [ ogc:asWKT ?geometry ] .
 Filter( bif:st_intersects (?geometry, bif:st_point ( $this->_geo_long, $this->_geo_lat ), $this->_geo_dist ) )
}
EOF;

	// --- list ---
	$url  = $this->build_url_query( $query );
	$result = $this->fetch_url( $url );
	$obj  = json_decode( $result );		
	if ( !is_object($obj) ) return false; 
	$bindings = $obj->results->bindings;
	if ( !is_array($bindings) || !count($bindings) ) return false; 
	
	foreach ( $bindings as $binding ) {

		$url = $binding->url->value ;
		$label_value = $binding->label->value ;
		$lable_lang = $this->get_lang( $binding , "label" ) ;
		$type = $binding->type->value;

		if ( isset( $check_array[ $url ] ) ) {
			if ( !in_array( $type, $temp[ $KEY_TYPES ] ) ) {
				$temp[ $KEY_TYPES ][] = $type;
			}
			if ( !isset( $temp[ $KEY_LABELS ][ $lable_lang ] )) {
				$temp[ $KEY_LABELS ][ $lable_lang ] = $label_value;
			}
		} else {
			$check_array[ $url ] = true;
			$temp = array(
				"lgdm:Node" => $url ,
				"geo:lat" => $binding->lat->value ,
				"geo:long" => $binding->long->value ,
			);
			$temp[ $KEY_TYPES ] = array();
			$temp[ $KEY_TYPES ][] = $type;
			$temp[ $KEY_LABELS ] = array();
			$temp[ $KEY_LABELS ][ $lable_lang ] = $label_value;
		}

		$array[ $url ] = $temp;
	}

	$ret = array();
	foreach( $array as $url => $node ) {	
		$temp = array(
			"lgdm:Node" => $node["lgdm:Node"] ,
			"geo:lat" => $node["geo:lat"] ,
			"geo:long" => $node["geo:long"] ,
		);
		$types = $node[ $KEY_TYPES ];
		foreach( $types as $k => $type ) {
		 	$temp[ "rdf:type:".$k ] = $type;		 	
		}
		$labels = $node[ $KEY_LABELS ];
		foreach( $labels as $lang => $label ) {
		 	$key = "rdfs:label";
		 	if ( $lang ) {
		 		$key = "rdfs:label:".$lang;
		 	}
		 	$temp[ $key ] = $label;		 	
		}
		$ret[] = $temp;
	}
	return $ret;
}

// =====
// http://linkedgeodata.org/ontology/Amenity
// <rdf:RDF>
//  <owl:Class rdf:about="http://linkedgeodata.org/ontology/Amenity">
//    <lgdm:sourceTag rdf:resource="http://linkedgeodata.org/ontology/Amenity/key/amenity/value/leisure"/>
//    <lgdm:sourceKey>amenity</lgdm:sourceKey>
// </owl:Class>
// </rdf:RDF>
//
// http://linkedgeodata.org/ontology/School
// <rdf:RDF>
// <owl:Class rdf:about="http://linkedgeodata.org/ontology/School">
//    <rdfs:subClassOf rdf:resource="http://linkedgeodata.org/ontology/Amenity"/>
//    <rdfs:label xml:lang="ja">äwçZ</rdfs:label>
//    <rdfs:label xml:lang="en-GB">School</rdfs:label>
//    <lgdm:sourceTag rdf:resource="http://linkedgeodata.org/ontology/School/key/amenity/value/school"/>
// </owl:Class>
// </rdf:RDF>
// =====
function get_ontology( $ontology )
{
	if ( empty($ontology) ) return false;	
	$key = $this->ontology_utility->get_ontology_label_en( $ontology );
	$url = $this->URL_DATA. $key .$this->OUTPUT;

	$ret = array();
	$result = $this->fetch_url( $url );	
	if ( empty($result) ) return false;	
	$xml = simplexml_load_string( $result );
	if ( !is_object($xml) ) return false;
	$nss = $xml->getNamespaces( true );
	if ( !is_array($nss) ) return false;
	$owl = $xml->children( $nss["owl"] );
	if ( !is_object($owl) ) return false;
	$class = $owl->Class;
	if ( !is_object($class) ) return false;
	$lgdm = $class->children( $nss["lgdm"] );
	if ( is_object($lgdm) ) {
		if ( isset($lgdm->sourceKey) ) {
			$ret["lgdm:sourceKey"] = (string) $lgdm->sourceKey;
		}
		if ( isset($lgdm->sourceTag) ) {
			$attribute = $lgdm->sourceTag->attributes( $nss["rdf"] );
			if ( is_object($attribute) ) {
				$ret["lgdm:sourceTag"] = (string) $attribute->resource;
			}
		}
	}		
	$rdfs = $class->children( $nss["rdfs"] );
	if ( is_object($rdfs) ) {
		if ( isset($rdfs->subClassOf) ) {
			$attribute = $rdfs->subClassOf->attributes( $nss["rdf"] );
			if ( is_object($attribute) ) {
				$ret["rdfs:subClassOf"] = (string) $attribute->resource;
			}
		}
		if ( isset($rdfs->label) ) {	
			$labels = $rdfs->label;
			foreach( $labels as $label ) {
				$attribute = $label->attributes( $nss["xml"] );
				if ( is_object($attribute) ) {
					$lang = (string) $attribute->lang;
					$ret[ "rdfs:label:".$lang ] = (string) $label;
				}
			}	
		}
	}
	return $ret;
}

function get_ontology_json( $ontology )
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
		"rdf:type" => $rdfs_type , 
		"rdfs:subClassOf" => $sub_class_of ,
		"rdfs:label:ja" => $label_ja ,							
	);
	return $array;
}

function get_node( $node )
{	
	if ( strpos( $node, $this->URL_TRIPLIFY ) !== 0 ) {
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

	$rdf_type_array = array();
	$rdfs_label_array = array();
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
				$rdf_type_array[] = $o;
				break;

			case "http://www.w3.org/2000/01/rdf-schema#label":
				$lang = $this->get_lang( $binding, "o" );
				if ( $lang == "" ) $lang = "default";
				$rdfs_label_array[ $lang ] = $o;							
				break;				
									
			default:
				$name = $this->get_key_name( $p, $this->URL_DC );
				if ( $name ) {
					$standard_array[ "dc:".$name ] = $o;
					break;
				}

				$name = $this->get_key_name( $p, $this->URL_OWL );			
				if ( $name ) {
					$standard_array[ "owl:".$name ] = $o;
					break;
				}

				$name = $this->get_key_name( $p, $this->URL_GEO );			
				if ( $name ) {
					$standard_array[ "geo:".$name ] = $o;
					break;
				}

				$name = $this->get_key_name( $p, $this->URL_GEOMETRY );			
				if ( $name ) {
					$standard_array[ "geom:".$name ] = $o;
					break;
				}

				$name = $this->get_key_name( $p, $this->URL_FOAF );			
				if ( $name ) {
					$standard_array[ "foaf:".$name ] = $o;
					break;
				}

				$name = $this->get_key_name( $p, $this->URL_ONTOLOGY );			
				if ( $name ) {				
					$ontology_array[ $name ] = $o;
					break;
				}

				$name = $this->get_key_name( $p, $this->URL_PROPERTY );			
				if ( $name ) {	
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
	$array[ "lgdm:Node" ] = $node;
	foreach( $rdf_type_array as $k => $v ) {
		$array[ "rdf:type:".$k ] = $v ;
	}	
	foreach( $rdfs_label_array as $k => $v ) {
		if ( $k == "default" ) {
			$key = "rdfs:label";
		} else {	
			$key ="rdfs:label:".$k;
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

function get_key_name( $p, $url )
{
	if ( strpos( $p, $url ) === 0 ) {
		$name = str_replace( $url, "", $p );
		return $name;
	}
	return false;
}

function get_label_key( $obj )
{
	$lang = $this->get_lang( $obj , "label" ) ;
	$key = "rdfs:label";
	if ( $lang ) {
		$key = "rdfs:label:".$lang;
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
	$url  = $this->URL_ENDPOINT;
	$url .= "?default-graph-uri=".urlencode( $this->URL_GRAPH );		
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