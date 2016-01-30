<?php		
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_node
{
	var $config;
	var $ontology;
	var $query;
	var $file; 
	var $error;

	var $URL_NODE = "http://linkedgeodata.org/triplify/node";
	
function lgd_node()
{
	$this->config =& lgd_config::getInstance();	
	$this->query =& lgd_query::getInstance();
	$this->ontology =& lgd_ontology::getInstance();	
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_node();
	}
	return $instance;
}

function get_node_without_query( $url )
{
	return $this->get_node( $url, false, false, true );
}

function get_node_with_query( $url )
{
	return $this->get_node( $url, true, false, true );
}

function get_node( $url, $is_query, $is_live, $is_cache )
{
	$name = $this->get_file_name( $url );
	if ( empty( $name )) return false;

	$fullpath = $this->get_fullpath( $name ) ;
	if ( $is_cache && $this->is_valid( $fullpath ) ) {
		$node = $this->get_file_node( $fullpath );
		if ( $this->check_node( $node ) ) return $node;
	}

	if ( !$is_query ) {
		return false;
	}

	$node = $this->get_query_node( $url, $is_live );		
	if ( $node == false ) {
		return false;
	}

	$type = $this->ontology->get_direct_type_from_node( $node );
	$node["type:url"] = $type["url"];
	$node["type:label"] = $type["label"];
	$node["type:label:ja"] = $type["rdfs:label:ja"];

	$this->put_file_node( $fullpath, $node );
	return $this->get_file_node( $fullpath );	
}

function batch_put_file( $url )
{
	$name = $this->get_file_name( $url );
	if ( empty( $name )) return false;
	
	$fullpath = $this->get_fullpath( $name ) ;
	if ( $this->is_valid( $fullpath ) ) {
		return true;
	}

	$node = $this->get_query_node( $url, false );	
	if ( $node == false ) {
		return false;
	}

	$this->put_file_node( $fullpath, $node );
	return true;	
}

function get_query_node( $url, $is_live )	
{
	$node = $this->query->get_node( $url );		
	if ( $is_live ) {
		$this->query->set_endpoint_live();
	}
	if ( $node === false ) {
		$msg = "illegal url: ".$url;
		$this->error->set_error( $msg );
		$this->file->write_log(	 $msg );
		return false;
	}
	if ( !$this->check_node( $node ) ) {
		$msg = "no result: ".$url;
		$this->error->set_error( $msg );
		$this->file->write_log(	 $msg );
		return false;
	}

	$type = $this->ontology->get_direct_type_from_node( $node );
	$node["type:url"] = $type["url"];
	$node["type:label"] = $type["label"];
	$node["type:label:ja"] = $type["rdfs:label:ja"];
	return $node;
}

function get_fullpath( $name )
{
	$fullpath = $this->config->DIR_NODE ."/". $name  ;
	return $fullpath; 
}

function get_file_name( $url )
{
	if ( !$this->is_node_format( $url ) ) return false;
	$name = str_replace( $this->URL_NODE, "", $url ) .".". LGD_EXT;
	return $name; 
}

function is_valid( $fullpath )
{
	return $this->file->is_valid( $fullpath, $this->config->VALID_DAY_NODE );
}

function put_file_node( $file, $node )
{
	$text = "";
	foreach( $node as $k => $v ) {
		$text .= $k .LGD_TAB. $v .LGD_LF;
	}	
	file_put_contents( $file, $text );	
}

function get_file_node( $file )
{
	$array = array() ;	
	$data = file_get_contents( $file );
	$lines = explode( LGD_LF, $data );
	foreach ( $lines as $line ) {
		$column = explode( LGD_TAB, $line );
		if ( isset( $column[0] ) && isset( $column[1] ) ) {
			$k = trim( $column[0] );
			$v = trim( $column[1] );
			$array[ $k ] = $v ;
		}	
	}
	return $array;
}

function check_node( $node )
{	
	if ( !is_array($node) || !count($node) ) 	return false;
	if ( !isset( $node[ "lgdm:Node" ] )) return false;
	if ( !isset( $node[ "rdfs:label" ] )) return false;
	if ( !isset( $node[ "geo:lat" ] )) return false;
	if ( !isset( $node[ "geo:long" ] )) return false;
	if ( !$this->is_node_format( $node[ "lgdm:Node" ] )) return false;
	return true;
}

function is_node_format( $url )
{
	if ( strpos( $url, $this->URL_NODE ) === 0 ) return true;
	return false;
}

// class end
}
?>