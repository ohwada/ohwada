<?php
//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_error
{
	var $errors = array();
		
function lgd_error()
{
	// dummy
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_error();
	}
	return $instance;
}

function clear_errors()
{	
	$this->errors = array();
}	

function set_error( $msg )
{	
	$this->errors[] = $msg;
}

function print_errors()
{	
	if ( count( $this->errors ) == 0 ) return;
	foreach( $this->errors as $error ) {
		echo $error."<br>\n";
	}
}

// --- class end ---
}

?>