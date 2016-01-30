<?php
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_ontology_label
{
	var $ontology;
	var $label_array = array();
	var $label_ja_array = array();

function lgd_ontology_label()
{
	$this->ontology =& lgd_ontology::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_ontology_label();
	}
	return $instance;
}

function get_ontology_label_ja( $url, $is_query )
{
	if ( isset( $this->label_ja_array[ $url ] ) ) {
		return $this->label_ja_array[ $url ];
	}

	$array = $this->ontology->get_ontology( $url, $is_query );		
	$label = $array["label"];

	$label_ja = $array["rdfs:label:ja"];
	if ( $label_ja ) {
		$label = $label_ja;
		$this->label_ja_array[ $url ] = $label_ja;	
	} 

	return $label;
}
	
// class end
}
?>