<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_ontology_label
{
	var $ontology;
	var $label_array = array();
		
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

function get_direct_label_ja( $node, $is_query )
{
	$ja =  "";
	$direct = isset( $node[ "lgdo:directType" ] ) ? $node[ "lgdo:directType" ] : "";	
	if ( $direct ) {
		// get label with query	
		$ja =  $this->get_ontology_label( $direct, $is_query );
	}
	return $ja;
}

function get_ontology_label( $url, $is_query )
{
	if ( isset( $this->label_array[ $url ] ) ) {
		return $this->label_array[ $url ];
	}

	$array = $this->ontology->get_ontology( $url, $is_query );		
	$label = $array["label"];

	$label_ja = $array["rdf:label:ja"];
	if ( $label_ja ) {
		$label = $label_ja;
	} 

	$this->label_array[ $url ] = $label;	
	return $label;
}

// class end
}
?>