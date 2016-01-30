<?php
//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_config
{
	var $GEO_LAT =  "35.443233";
	var $GEO_LONG = "139.637134";
	var $GEO_DIST = "1.0";
	
	var $LIMIT_JSON_LIST_SUMMARY = 100; 
	var $LIMIT_JSON_LIST_DETAIL = 2000; 
	var $LIMIT_JSON_NODE = 2000; 
	var $LIMIT_NODE_LIST = 2000; 
	var $LIMIT_NODE = 10000; 
	var $LIMIT_ONTOLOGY = 10000; 

	var $ONE_DAY = 86400;	// 24*60*60;
	var $VALID_DAY_JSON_LIST_SUMMARY = 0.5;		// half day 
	var $VALID_DAY_JSON_LIST_DETAIL = 7;		// one week 
	var $VALID_DAY_JSON_NODE = 7;		// one week 
	var $VALID_DAY_NODE = 30;	// one month 
	var $VALID_DAY_NODE_LIST = 30;	// one month 
	var $VALID_DAY_ONTOLOGY = 30;	// one month 
	
	var $DIR_JSON_LIST_SUMMARY = "";	
	var $DIR_JSON_LIST_DETAIL = "";	
	var $DIR_JSON_NODE = "";	
	var $DIR_LIST = "";	
	var $DIR_NODE = "";	
	var $DIR_ONTOLOGY = "";	
	var $DIR_LOG = "";	
	var $FILE_ERROR = "";

function lgd_config()
{
	define( "LGD_TAB", "\t" );
	define( "LGD_LF", "\n" );
	define( "LGD_BAR", "|" );
	define( "LGD_STAR", "*" );
	define( "LGD_EXT", "txt" );
			
	$DIR = dirname( __FILE__ ) ;
	
	$this->DIR_JSON_LIST_SUMMARY = $DIR."/cache_json_summary";	
	$this->DIR_JSON_LIST_DETAIL = $DIR."/cache_json_detail";
	$this->DIR_JSON_NODE = $DIR."/cache_json_node";	
	$this->DIR_NODE_LIST = $DIR."/cache_list";	
	$this->DIR_NODE = $DIR."/cache_node";	
	$this->DIR_ONTOLOGY = $DIR."/cache_ontology";	
	$this->DIR_LOG = $DIR."/log";	
	$this->FILE_ERROR = $this->DIR_LOG."/error.txt";	
	
	date_default_timezone_set('Asia/Tokyo');	
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_config();
	}
	return $instance;
}

// --- class end ---
}

?>