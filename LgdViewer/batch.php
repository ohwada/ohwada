<?php				
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//
// usage
// php batch.php password 
//=========================================================

class lgd_batch
{
	var $config;
	var $node_list;
	var $node;
	var $ontology;
	var $file;

// --- set password ---
	var $PASSWORD = "123456";

function lgd_batch()
{
	$this->config =& lgd_config::getInstance();
	$this->node_list =& lgd_node_list::getInstance();
	$this->node =& lgd_node::getInstance();
	$this->ontology =& lgd_ontology::getInstance();
	$this->file =& lgd_file::getInstance();
}

function main()
{
	if ( empty( $this->PASSWORD ) ) {
		echo "set password \n";
		exit();
	}

	// check password
	$pass = "";
	if ( $_SERVER['argc'] > 1 ) {
		$pass = $_SERVER['argv'][1];
	}
	if ( $this->PASSWORD != $pass ) {	
		exit();
	}
	
	$this->execute();	
}

function execute()
{
	list( $url_array, $type_array ) = $this->get_node_url_array();
	foreach( $type_array as $url ) {
		$this->ontology->batch_put_file( $url );	
	}
	foreach( $url_array as $url ) {
		$this->node->batch_put_file( $url );		
	}
}

function get_node_url_array()
{
	$i = 0;
	$is_finish_url = false;
	$is_finish_type = false;
	$url_array = array();

	$type_array = array();

	$list_files = $this->file->get_files_in_node_list();
	
	foreach( $list_files	 as $name => $atime ) {
		$fullpath = $this->config->DIR_NODE_LIST ."/". $name ;
		$list_array = $this->node_list->get_file_node_list( $fullpath );

		foreach ( $list_array as $a ) {
			if ( $i < $this->config->LIMIT_NODE ) {
				$url = $a["lgdm:Node"];
				if ( !in_array( $url, $url_array ) ) {
					$url_array[] = $url;
				}
			} else {
				$is_finish_url = true;
			}

			if ( $i < $this->config->LIMIT_ONTOLOGY ) {
				$list = $this->ontology->get_ontology_list_from_node( $a );
				foreach( $list as $type ) {
					if ( !in_array( $type, $type_array ) ) {
						$type_array[] = $type;
					}
				}
			} else {
				$is_finish_type = true;
			}

			if ( $is_finish_url && $is_finish_type ) break;
			$i ++;
		}
		
		if ( $is_finish_url && $is_finish_type ) break;	
	}
	
	return array( $url_array, $type_array );
}

// class end
}

// main 
	include_once("config.class.php");
	include_once("node_list.class.php");
	include_once("node.class.php");
	include_once("ontology.class.php");
	include_once("ontology_utility.class.php");
	include_once("query.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$batch = new lgd_batch();
	$batch->main();
	exit();

?>