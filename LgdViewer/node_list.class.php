<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_node_list
{
	var $config;
	var $query;
	var $node;
	var $file;
	var $error;
				
function lgd_node_list()
{
	$this->config =& lgd_config::getInstance();	
	$this->query =& lgd_query::getInstance();
	$this->node = new lgd_node();	
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_node_list();
	}
	return $instance;
}

function get_node_list( $lat, $long )
{
	$name = $this->get_file_name( $lat, $long );
	if ( empty( $name )) return false;

	$fullpath = $this->get_fullpath( $name );	
	if ( $this->is_valid( $fullpath ) ) {
		$nodes = $this->get_file_node_list( $fullpath );
		if ( $this->check_node_list( $nodes ) ) return $nodes;
	}

	$this->query->set_geo( $lat, $long, $this->config->GEO_DIST );
	$nodes = $this->query->get_node_array();
	if ( !$this->check_node_list( $nodes ) ) {
		$msg = "no result: ".$lat." ".$long;
		$this->error->set_error( $msg );
		$this->file->write_log(	 $msg );
		return false;
	}	

	$this->put_file_node_list( $fullpath, $nodes );
	return $this->get_file_node_list( $fullpath );
}

function get_fullpath( $name )
{
	$fullpath = $this->config->DIR_NODE_LIST ."/". $name ;	
	return $fullpath;
}
	
function get_file_name( $lat, $long )
{
	$lat_int = (int)( (float)$lat * 1000.0);
	$long_int = (int)( (float)$long * 1000.0);

	if (( $lat_int == 0 )||( $long_int == 0)) {
		$msg = "llegal location ".$lat." ".$long;
		$this->error->set_error(	$msg );
		$this->file->write_log(	 $msg );
		return false;
	}

	$name = $lat_int ."_". $long_int .".". LGD_EXT ;
	return $name;
}

function is_valid( $fullpath )
{
	return $this->file->is_valid( $fullpath, $this->config->VALID_DAY_NODE_LIST );
}

function put_file_node_list( $file, $array )
{
	$text = "";	
	foreach ( $array as $a ) {				
		$text .= $a["lgdo:Node"]  . LGD_TAB;
		$text .= $a["rdf:label"]  . LGD_TAB;
		$text .= $a["lgdo:directType"]   . LGD_TAB;						
		$text .= $a["geo:geometry"]  . LGD_TAB;
		$text .= $a["geo:lat"]   . LGD_TAB;
		$text .= $a["geo:long"] . LGD_LF;
	}
	file_put_contents( $file, $text );	
}

function get_file_node_list( $file )
{
	$contents = file_get_contents( $file );
	$lines = explode( LGD_LF, $contents );

	$array = array();
	foreach ( $lines as $line ) {
		if ( empty($line) ) continue;

		$c = explode( LGD_TAB, $line );
		$url = $c[0] ;
						
		$temp = array(
			'lgdo:Node'         => $url ,
			'rdf:label'       => $c[1] ,
			'lgdo:directType'       => $c[2] ,
			'geo:geometry'  => $c[3] ,
			'geo:lat'         => $c[4] ,
			'geo:long'        => $c[5] ,
		);
		
		$array[ $url ] = $temp;
	}	
	
	return $array;
}

function check_node_list( $nodes )
{	
	if ( !is_array($nodes) || !count($nodes) ) return false;
	foreach ( $nodes as $node ) {	
		if ( !$this->node->check_node( $node )) return false;
	}	
	return true;
}

// class end
}
?>