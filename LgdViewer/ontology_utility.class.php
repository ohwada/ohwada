<?php
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
//=========================================================

class lgd_ontology_utility
{	
	var $URL_ONTOLOGY = "http://linkedgeodata.org/ontology/";
		
function lgd_ontology_utility()
{
	// dummy
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_ontology_utility();
	}
	return $instance;
}

function get_ontology_label_en( $url )
{
	return str_replace( $this->URL_ONTOLOGY, "", $url );
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