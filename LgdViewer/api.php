<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_api
{
	var $json;
	
function lgd_api()
{
	$this->json =& lgd_json::getInstance();
}

function main()
{
	$type = isset( $_GET["type"] ) ?  $_GET["type"] : "";
	if ( $type == "list" ) {
		$lat = isset( $_GET["lat"] ) ?  (float)$_GET["lat"] : 0;
		$long = isset( $_GET["long"] ) ?  (float)$_GET["long"] : 0;
		if (( $lat > 0 )&&( $long >0 )) {
			echo $this->json->get_json_list( $lat, $long );
			return;
		}

	} else	if ( $type == "node" ) {
		$node = isset( $_GET["node"] ) ?  intval( $_GET["node"] ) : "";
		if ( $node ) {
			echo $this->json->get_json_node( $node );
			return;
		}
	}
	
	echo "invalid";	
}

// class end
}

// main 
	include_once("config.class.php");
	include_once("node_list.class.php");
	include_once("node.class.php");
	include_once("ontology_label.class.php");
	include_once("ontology.class.php");
	include_once("query.class.php");
	include_once("json.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$api= new lgd_api();
	$api->main();
	exit();

?>