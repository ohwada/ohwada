<?php							
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class  lgd_ontology
{
	var $config;
	var $query;
	var $file;
	var $error;
	var $ontology_utility;
		
function lgd_ontology()
{
	$this->config =& lgd_config::getInstance();	
	$this->ontology_utility =& lgd_ontology_utility::getInstance();
	$this->query =& lgd_query::getInstance();
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_ontology();
	}
	return $instance;
}

function get_ontology( $url, $is_query )
{
	if ( !$this->is_ontology( $url ) ) {
		$msg = "not ontology url ".$url;
		$this->error->set_error(	$msg );
		$this->file->write_log(	 $msg );
		return false;
	}

	$label = $this->get_ontology_label_en( $url );
	$fullpath = $this->get_fullpath( $label ) ;
	if ( $this->is_valid( $fullpath ) ) {
		return $this->get_file_ontology( $fullpath );
	}

	if ( !$is_query ) {
		$array = array(
			"url" => $url ,
			"label" => $label ,
			"rdfs:label:ja" => "" ,
			"rdf:type" => "" ,
			"rdfs:subClassOf" => "" ,
		);
		return $array;
	}

	$array = $this->query->get_ontology( $url );
	if ( !is_array( $array ) || !count( $array )) {
		$msg = "no ontology ".$url;
		$this->error->set_error(	$msg );
		$this->file->write_log(	$msg );	
		return false;
	}
		
	$this->put_file_ontology( $fullpath, $url, $array );
	return $this->get_file_ontology( $fullpath );
}

function batch_put_file( $url )
{
	if ( !$this->is_ontology( $url ) ) return;
	$label = $this->get_ontology_label_en( $url );
	$fullpath = $this->get_fullpath( $label ) ;
	if ( $this->is_valid( $fullpath ) ) return;
	$array = $this->query->get_ontology( $url );
	if ( !is_array( $array ) || !count( $array )) return;
		
	$this->put_file_ontology( $fullpath, $url, $array );
}

function get_fullpath( $name )
{	
	return $this->config->DIR_ONTOLOGY ."/". strtolower( $name ) .".". LGD_EXT ;
}

function is_valid( $fullpath )
{	
	return $this->file->is_valid( $fullpath, $this->config->VALID_DAY_ONTOLOGY );
}
		
function put_file_ontology( $file, $url, $ontology )
{					
	$ontology["url"] = $url;
	$ontology["label"] = $this->get_ontology_label_en( $url );
	
	$text = "";
	foreach( $ontology as $k => $v ) {
		$text .= $k .LGD_TAB. $v .LGD_LF;
	}
	
	file_put_contents( $file, $text );	
}

function get_file_ontology( $file )
{	
	$array = array() ;	
	$data = file_get_contents( $file );
	$lines = explode( LGD_LF, $data );
	foreach ( $lines as $line ) {
		$c = explode( LGD_TAB, $line );
		if ( isset( $c[0] ) && isset( $c[1] ) ) {
			$key = trim( $c[0] );
			$value = trim( $c[1] );
			$array[ $key ] = $value ;
		}	
	}
	if( !isset( $array["rdfs:subClassOf"] )) {
		$array["rdfs:subClassOf"] = "";
	}
	if( !isset( $array["rdfs:label:ja"] )) {
		$array["rdfs:label:ja"] = "";
	}
	return $array;
}

function get_direct_type_from_node( $node )
{
	$array = array();
	$list = $this->get_ontology_list_from_node( $node );
	foreach( $list as $url ) {
		$array[] = $this->get_ontology( $url, true );
	}
	$count = count( $array );
	if ( $count == 0 ) {
		return false;
	}
	if ( $count == 1 ) {
		return $array[ 0 ];
	}
	for( $i=0; $i<$count; $i++ ) {
		$child = $array[ $i ];
		$sub = $child["rdfs:subClassOf"];
		if ( empty( $sub ) ) continue;
		for( $j=0; $j<$count; $j++ ) {
			if ( $i == $j ) continue;
			$parent = $array[ $j ];
			$url = $parent["url"];
			if ( $sub == $url ) {
				$array[ $j ]["parent"] = 1; 
			}
		}
	}
	for( $i=0; $i<$count; $i++ ) {
		if ( !isset( $array[ $i ]["parent"] ) ) {
			return $array[ $i ];
		} 
	}
	return $array[ 0 ];
}

function get_ontology_list_from_node( $node )
{
	$array = array();
	foreach( $node as $k => $v ) {
		if ( strpos( $k, "rdf:type:" ) === 0 ) {
			if ( $this->is_ontology( $v ) ) {
				$array[] = $v;
			}
		}
	}
	return $array;
}
	
function get_ontology_label_en( $url )
{
	return $this->ontology_utility->get_ontology_label_en( $url );
}

function is_ontology( $url )
{
	return $this->ontology_utility->is_ontology( $url );
}

// class end
}
?>