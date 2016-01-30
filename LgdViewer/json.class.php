<?php					
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_json
{
	var $config;
	var $node_list;
	var $node;
	var $ontology_label;
	var $file;
		
function lgd_json()
{
	$this->config =& lgd_config::getInstance();	
	$this->node_list =& lgd_node_list::getInstance();
	$this->node =& lgd_node::getInstance();
	$this->ontology_label =& lgd_ontology_label::getInstance();
	$this->file =& lgd_file::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_json();
	}
	return $instance;
}

function get_json_list( $lat, $long )
{
	$name = $this->node_list->get_file_name( $lat, $long );
	if ( empty( $name )) return false;

	$fullpath_detail = $this->config->DIR_JSON_LIST_DETAIL ."/". $name ;	
	$fullpath_summary = $this->config->DIR_JSON_LIST_SUMMARY ."/". $name ;	
	// if detail is valid
	if ( $this->file->is_valid( $fullpath_detail, $this->config->VALID_DAY_JSON_LIST_DETAIL ) ) {
		$json = file_get_contents( $fullpath_detail );
		if ( $this->check_json_node_list( $json) ) return $json;
	}

	// if summary is valid	
	if ( $this->file->is_valid( $fullpath_summary, $this->config->VALID_DAY_JSON_LIST_SUMMARY ) ) {
		$json = file_get_contents( $fullpath_summary );
		if ( $this->check_json_node_list( $json) ) return $json;
	}
	
	$json_array = $this->make_json_list( $lat, $long );	
	if ( $json_array == false ) {
		return "no result";
	}
	
	$nodes = $json_array[ "nodes" ];
	$status = $json_array[ "status" ] ;
	if ( !is_array( $nodes ) || !count( $nodes ) ) {
		return "no result";
	}

	if ( $status == 1 ) {
		$fullpath = $fullpath_detail ;
	} else {
		$fullpath = $fullpath_summary ;
	}

	$json = json_encode( $json_array );		
	file_put_contents( $fullpath, $json );
	return $json;	
}

function get_json_node( $node )
{
	$name = $node .".". LGD_EXT;
	$fullpath = $this->config->DIR_JSON_NODE ."/". $name ;			
	if ( $this->file->is_valid( $fullpath, $this->config->VALID_DAY_JSON_NODE ) ) {
		$json = file_get_contents( $fullpath );
		if ( $this->check_json_node( $json) ) return $json;
	}
	
	$json_array = $this->make_json_node( $node );
	if ( $json_array == false ) {
		return "no result";
	}

	$json = json_encode( $json_array );		
	file_put_contents( $fullpath, $json );
	return $json;	
}
			
function make_json_list( $lat, $long )
{	
	// get node list rapidly without query
	$array = $this->node_list->get_node_list_without_query( $lat, $long );

	if ( !is_array($array) || !count($array) ) return false;

	$new_array = array();
	// assume that all the nodes have detail. 
	$status = 1 ;	// detail	
	
	foreach ( $array as $a ) {
		$node_url = $a[ "lgdm:Node" ] ;
		
		// get node rapidly without query
		$node = $this->node->get_node_without_query( $node_url );
		if ( isset( $node[ "lgdm:Node" ] ) && $node[ "lgdm:Node" ] ) {
			$temp = $node;
			$temp[ "status" ] = 1 ;	// detail

		} else {
			$temp = $a;
			$temp[ "status" ] = 0 ;	// summary
			// set summry to status, if at least one does not have detail
			$status = 0 ;	// summary
		}

		$new_array[] = $temp;
	}

	$json_array = array(
		"status" => $status ,
		"nodes" => $new_array,
	);	

	return $json_array;
}

function make_json_node( $node )
{
	// get node with query
	$url = $this->node->URL_NODE . $node;
	$node = $this->node->get_node_with_query( $url );
	if ( !is_array($node) || !count($node) ) return false;

	$node[ "status" ] = 1 ;	// detail

	$json_array = array(
		"status" => 1 ,	// detail
		"node" => $node
	);	

	return $json_array;
}

function check_json_node_list( $json )
{
	if ( empty( $json ) ) return false;
	$array = json_decode( $json );
	if ( !is_array( $array ) || !count( $array ) ) return false;
	if ( !isset( $array[ "nodes" ] )) return false; 
	$nodes = $array[ "nodes" ];
	if ( !is_array( $nodes ) || !count( $nodes ) ) return false;
	if ( !isset( $nodes[ 0 ][ "lgdm:Node" ] )) return false; 	
	return true;
}

function check_json_node( $json )
{
	if ( empty( $json ) ) return false;
	$array = json_decode( $json );
	if ( !is_array( $array ) || !count( $array ) ) return false;
	if ( !isset( $array[ "node" ] )) return false; 
	$node = $array[ "node" ];
	if ( !is_array( $node ) || !count( $node ) ) return false;
	if ( !isset( $node[ "lgdm:Node" ] )) return false; 	
	return true;
}

// class end
}

?>