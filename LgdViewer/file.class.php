<?php
//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_file
{
	var $config;
	
function lgd_file()
{
	$this->config =& lgd_config::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_file();
	}
	return $instance;
}

function make_dir()
{	
	if ( !file_exists( $this->config->DIR_JSON_LIST_SUMMARY ) ) {
		mkdir( $this->config->DIR_JSON_LIST_SUMMARY );
	}
	if ( !file_exists( $this->config->DIR_JSON_LIST_DETAIL ) ) {
		mkdir( $this->config->DIR_JSON_LIST_DETAIL );
	}
	if ( !file_exists( $this->config->DIR_JSON_NODE ) ) {
		mkdir( $this->config->DIR_JSON_NODE );
	}
	if ( !file_exists( $this->config->DIR_NODE_LIST ) ) {
		mkdir( $this->config->DIR_NODE_LIST );
	}
	if ( !file_exists( $this->config->DIR_NODE ) ) {
		mkdir( $this->config->DIR_NODE );
	}
	if ( !file_exists( $this->config->DIR_ONTOLOGY ) ) {
		mkdir( $this->config->DIR_ONTOLOGY );
	}
	if ( !file_exists( $this->config->DIR_LOG ) ) {
		mkdir( $this->config->DIR_LOG );
	}
}

function is_valid( $fullpath, $day )
{
	if ( !file_exists( $fullpath ) ) return false;

	$time = filectime( $fullpath ) + ( $day * $this->config->ONE_DAY );	
	if ( time() > $time ) return false; 
	return true;
}

function print_files( $dir )
{
	$files = $this->get_files( $dir );
	foreach ( $files as $name => $atime ) {
		echo $name." ".$atime."<br>\n";
	}	
}

function delete_old_files()
{
	$this->delete_old_files_in_dir( $this->config->DIR_JSON_LIST_SUMMARY, $this->config->LIMIT_JSON_LIST_SUMMARY ); 
	$this->delete_old_files_in_dir( $this->config->DIR_JSON_LIST_DETAIL, $this->config->LIMIT_JSON_LIST_DETAIL ); 
	$this->delete_old_files_in_dir( $this->config->DIR_JSON_NODE, $this->config->LIMIT_JSON_NODE ); 
	$this->delete_old_files_in_dir( $this->config->DIR_NODE_LIST, $this->config->LIMIT_NODE_LIST ); 
	$this->delete_old_files_in_dir( $this->config->DIR_NODE, $this->config->LIMIT_NODE ); 
	$this->delete_old_files_in_dir( $this->config->DIR_ONTOLOGY, $this->config->LIMIT_ONTOLOGY); 		
}

function delete_old_files_in_dir( $dir, $limit )
{
	$files = $this->get_files( $dir );
	if ( !is_array($files) ) return false;

	$i = 0;
	foreach ( $files as $name => $atme ) {
		if ( $i > $limit ) {
			$fullpath = $dir .'/'. $name;
			unlink( $fullpath );
		}
		$i ++;
	}
}

function get_files_in_node_list()
{
	return $this->get_files( $this->config->DIR_NODE_LIST );
}

function get_files( $dir )
{
	$arr = array();

// check is dir
	if ( !is_dir($dir) ) return false;

// open
	$dh = opendir($dir);
	if ( !$dh ) return false;

// read
	while ( false !== ($name = readdir( $dh )) ) {
		if (( $name == ".." )||( $name == "." )) continue;
		$fullpath = $dir .'/'. $name;
		if ( is_dir($fullpath) || !is_file($fullpath) ) continue;
		$arr[ $name ] = fileatime( $fullpath );
	}

// close
	closedir( $dh );
	
// sort	
	arsort( $arr, SORT_NUMERIC );
	reset( $arr );
	return $arr;
}

function write_log( $msg )
{
	$text = Date("Y-m-d H:i:s")." ".$msg. LGD_LF;
	file_put_contents( $this->config->FILE_ERROR, $text, FILE_APPEND );	
}

// --- class end ---
}

?>