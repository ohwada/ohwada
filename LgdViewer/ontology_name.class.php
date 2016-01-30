<?php
// 2013-01-20 K.OHWADA
// http://linkedgeodata.org

class lgd_ontology_name
{
	var $ontology;
	var $ontology_name_array = array();
	
function lgd_ontology_name()
{
	$this->ontology =& lgd_ontology::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_ontology_name();
	}
	return $instance;
}

function get_ontology_name( $url, $flag_with_query )
{
	if ( isset( $this->ontology_name_array[ $url ] ) ) {
		return $this->ontology_name_array[ $url ];
	}

	$array = $this->ontology->get_ontology( $url, $flag_with_query );
	$name = $array["name"];

	$label_ja = $array["rdf:labe:ja"];
	if ( $label_ja ) {
		$name = $label_ja;
	} 

	$this->ontology_name_array[ $url ] = $name;	
	return $name;
}

// class end
}
?>