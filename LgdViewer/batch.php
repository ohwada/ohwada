<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_batch
{
	var $config;
	var $node_list;
	var $node;
	var $ontology;
	var $file;

	var $PASSWORD = "e4JZws5i";
	
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
	list( $url_array, $direct_array ) = $this->get_node_url_array();
	foreach( $url_array as $url ) {
		$this->node->batch_put_file( $url );		
	}
	foreach( $direct_array as $url ) {
		$this->ontology->batch_put_file( $url );	
	}
}

function get_node_url_array()
{
	$i = 0;
	$is_finish_url = false;
	$is_finish_direct = false;
	$url_array = array();
	$direct_array = array();
	$list_files = $this->file->get_files_in_node_list();
	
	foreach( $list_files	 as $name => $atime ) {
		$fullpath = $this->config->DIR_NODE_LIST ."/". $name ;
		$list_array = $this->node_list->get_file_node_list( $fullpath );

		foreach ( $list_array as $a ) {
			if ( $i < $this->config->LIMIT_NODE ) {
				$url = $a["lgdo:Node"];
				if ( !in_array( $url, $url_array ) ) {
					$url_array[] = $url;
				}
			} else {
				$is_finish_url = true;
			}

			if ( $i < $this->config->LIMIT_ONTOLOGY ) {
				$direct = $a["lgdo:directType"];
				if ( !in_array( $direct, $direct_array ) ) {
					$direct_array[] = $direct;
				}
			} else {
				$is_finish_direct = true;
			}

			if ( $is_finish_url && $is_finish_direct ) break;
			$i ++;
		}
		
		if ( $is_finish_url && $is_finish_direct ) break;	
	}
	
	return array( $url_array, $direct_array );
}

// class end
}

// main 
	include_once("config.class.php");
	include_once("node_list.class.php");
	include_once("node.class.php");
	include_once("ontology.class.php");
	include_once("query.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$batch = new lgd_batch();
	$batch->main();
	exit();

?>