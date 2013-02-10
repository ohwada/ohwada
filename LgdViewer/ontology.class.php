<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class  lgd_ontology
{
	var $config;
	var $query;
	var $file;
	var $error;
	
	var $URL_ONTOLOGY = "http://linkedgeodata.org/ontology/";
		
function lgd_ontology()
{
	$this->config =& lgd_config::getInstance();	
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
			'url'  => $url ,
			'label' => $label ,
			'rdf:label:ja' => "" ,
			'rdfs:type'   => "" ,
			'rdf:subClassOf' => "" ,
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
	if ( !$this->is_ontology( $url ) ) {
		return false;
	}

	$label = $this->get_ontology_label_en( $url );
	$fullpath = $this->get_fullpath( $label ) ;
	if ( $this->is_valid( $fullpath ) ) {
		return;
	}

	$array = $this->query->get_ontology( $url );
	if ( !is_array( $array ) || !count( $array )) {
		return;
	}
		
	$this->put_file_ontology( $fullpath, $url, $array );
}

function get_ontology_label_en( $url )
{
	return str_replace( $this->URL_ONTOLOGY, "", $url );
}

function get_fullpath( $name )
{	
	return $this->config->DIR_ONTOLOGY ."/". strtolower( $name ) .".". LGD_EXT ;
}

function is_valid( $fullpath )
{	
	return $this->file->is_valid( $fullpath, $this->config->VALID_DAY_ONTOLOGY );
}
		
function put_file_ontology( $file, $url, $a )
{		
	$rdfs_type = isset( $a["rdfs:type"] ) ? $a["rdfs:type"] : "";
	$sub_class_of = isset( $a["rdf:subClassOf"] ) ? $a["rdf:subClassOf"] : "";
	$label_ja = isset( $a["rdf:label:ja"] ) ? $a["rdf:label:ja"] : "";

	$label = $this->get_ontology_label_en( $url );
	
	$text = "";			
	$text .= $url  . LGD_TAB;
	$text .= $label . LGD_TAB;	
	$text .= $label_ja  . LGD_TAB;	
	$text .= $rdfs_type  . LGD_TAB;
	$text .= $sub_class_of  . LGD_LF;						

	file_put_contents( $file, $text );	
}

function get_file_ontology( $file )
{
	$line = file_get_contents( $file );
	$c = explode( LGD_TAB, $line );			
	$array = array(
		'url'     => $c[0] ,
		'label'  => $c[1] ,
		'rdf:label:ja'  => $c[2] ,
		'rdfs:type'    => $c[3] ,
		'rdf:subClassOf' => $c[4] ,
	);
	return $array;
}

function is_ontology( $url )
{
	if ( strpos( $url, $this->URL_ONTOLOGY ) === 0 ) {
		return true;
	}
	return false;
}

// class end
}
?>