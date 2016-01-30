<?php				
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_node_list
{
	var $GLUE_TYPE = ";";

	var $config;
	var $query;
	var $node;
	var $ontology;
	var $file;
	var $error;
				
function lgd_node_list()
{
	$this->config =& lgd_config::getInstance();	
	$this->query =& lgd_query::getInstance();
	$this->node =& lgd_node::getInstance();	
	$this->ontology =& lgd_ontology::getInstance();
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_node_list();
	}
	return $instance;
}

function get_node_list_without_query( $lat, $long )
{
	return $this->get_node_list( 
		$lat, $long, $this->config->GEO_DIST, false, false );
}

function get_node_list( $lat, $long, $dist, $is_live, $is_cache )
{
	$name = $this->get_file_name( $lat, $long );
	if ( empty( $name )) return false;

	$fullpath = $this->get_fullpath( $name );	
	if ( $is_cache && $this->is_valid( $fullpath ) ) {
		$nodes = $this->get_file_node_list( $fullpath );
		if ( $this->check_node_list( $nodes ) ) return $nodes;
	}

	$nodes = $this->get_query_node_list( $lat, $long, $dist, $is_live );
	if ( $nodes == false ) {
		return false;
	}

	$this->put_file_node_list( $fullpath, $nodes );
	return $this->get_file_node_list( $fullpath );
}

function get_query_node_list( $lat, $long, $dist, $is_live )
{
	$this->query->set_geo( $lat, $long, $dist );
	if ( $is_live ) {
		$this->query->set_endpoint_live();
	}
	$nodes = $this->query->get_node_array();
	if ( !$this->check_node_list( $nodes ) ) {
		$msg = "no result: ".$lat." ".$long;
		$this->error->set_error( $msg );
		$this->file->write_log(	 $msg );
		return false;
	}
	$array = array();
	foreach( $nodes as $node ) {
		$url = $node["lgdm:Node"]; 
		$type = $this->ontology->get_direct_type_from_node( $node );
		$node["type:url"] = $type["url"];
		$node["type:label"] = $type["label"];
		$node["type:label:ja"] = $type["rdfs:label:ja"];
		$array[] = $node;
	}
	return $array;
}
	
function get_fullpath( $name )
{
	$fullpath = $this->config->DIR_NODE_LIST ."/". $name ;	
	return $fullpath;
}
	
function get_file_name( $lat, $long )
{
	$lat_int = (int)( (float)$lat * 1000.0);
	$long_int = (int)( (float)$long * 1000.0);

	if (( $lat_int == 0 )||( $long_int == 0)) {
		$msg = "llegal location ".$lat." ".$long;
		$this->error->set_error(	$msg );
		$this->file->write_log(	 $msg );
		return false;
	}

	$name = $lat_int ."_". $long_int .".". LGD_EXT ;
	return $name;
}

function is_valid( $fullpath )
{
	return $this->file->is_valid( $fullpath, $this->config->VALID_DAY_NODE_LIST );
}

function put_file_node_list( $file, $array )
{
	$text = "";	
	foreach ( $array as $node ) {	
		$temp = "";
		foreach( $node as $k => $v ) {
			$temp .= $k . LGD_TAB;
			$temp .= $v . LGD_TAB;
		}
		$text .= $temp . LGD_LF;
	}	
	file_put_contents( $file, $text );	
}

function get_file_node_list( $file )
{
	$contents = file_get_contents( $file );
	$lines = explode( LGD_LF, $contents );

	$array = array();
	foreach ( $lines as $line ) {
		if ( empty($line) ) continue;

		$temp = array();
		$c = explode( LGD_TAB, $line );		
		$len = intval( count( $c ) / 2 );
		for ( $i = 0; $i < $len; $i++ ) {
			$ii0 = 2 * $i; 
			$ii1 = $ii0 + 1;
			if ( isset( $c[ $ii0 ] ) && isset( $c[ $ii1 ] ) ) {
				$k = trim( $c[ $ii0 ] );
				$v = trim( $c[ $ii1 ] );
				$temp[ $k ] = $v;	
			}		
		}
		$array[] = $temp;	
	}	
	
	return $array;
}

function check_node_list( $nodes )
{	
	if ( !is_array($nodes) || !count($nodes) ) return false;
	foreach ( $nodes as $node ) {	
		if ( !$this->node->check_node( $node )) return false;
	}	
	return true;
}

// class end
}
?>