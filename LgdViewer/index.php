<?php
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

// 159 node
// Excution time : 4 sec

class lgd_main
{
	var $config;
	var $node_list;
	var $node;
	var $ontology_label;
	var $map;
	var $file;
	var $error;
						
	var $time;
	
function lgd_main()
{
	$this->config =& lgd_config::getInstance();
	$this->node_list =& lgd_node_list::getInstance();
	$this->node =& lgd_node::getInstance();
	$this->ontology_label =& lgd_ontology_label::getInstance();
	$this->map =& lgd_map::getInstance();
	$this->file =& lgd_file::getInstance();
	$this->error =& lgd_error::getInstance();
	
	$this->time = time();
}

function main()
{
	$lat_e6 = isset( $_COOKIE["lat"] ) ?  (int)$_COOKIE["lat"] : 0;
	$long_e6 = isset( $_COOKIE["long"] ) ?  (int)$_COOKIE["long"] : 0;

	$lat = isset( $_GET["lat"] ) ?  (float)$_GET["lat"] : ( $lat_e6 / 1e6 ) ;
	$long = isset( $_GET["long"] ) ?  (float)$_GET["long"] : ( $long_e6 / 1e6 );
	$dist = isset( $_GET["dist"] ) ?  (float)$_GET["dist"] : $this->config->GEO_DIST;
	$live = isset( $_GET["live"] ) ?  (boolean)$_GET["live"] : false;
	$cache = isset( $_GET["cache"] ) ?  (boolean)$_GET["cache"] : true;

	if (( $lat == 0 )||( $long == 0 )) {
		$lat = $this->config->GEO_LAT;
		$long = $this->config->GEO_LONG;		
	}

	$lat_cookie = (int)( $lat * 1e6 );
	$long_cookie = (int)( $long * 1e6 );	
	setcookie( "lat", $lat_cookie );
	setcookie( "long", $long_cookie );
	
	$this->header();
	$this->file->make_dir(); 
	$this->file->delete_old_files(); 						
	$this->form( $lat, $long );
	if ( isset( $_GET["submit"] ) ) {
		$this->body( $lat, $long, $dist, $live, $cache );
	}
	$this->footer(); 
}

function body( $center_lat, $center_long, $dist, $live, $cache )
{
	$this->error->clear_errors();
	$array = $this->node_list->get_node_list( $center_lat, $center_long, $dist, $live, $cache );
	if ( !is_array($array) || !count($array) ) {
		$this->error->print_errors();
		return; 
	}

	$type_array = array();
	
	echo "<ul>\n";

	$i = 0;
	foreach ( $array as $a ) {
		$i ++;

		$url = $a["lgdm:Node"] ;
		$lat = $a["geo:lat"] ;
		$long = $a["geo:long"] ;	

		$label_show = $this->get_label_show( $a );
		$type_show = $this->get_type_show( $a );
			
		$link = "node.php?n=". $i. "&url=". urlencode( $url );	
		$osm_link = $this->map->get_osm_link( $lat, $long );
				
		echo '<li>';
		echo '<a name="n'. $i .'">'."\n";
		echo $i.": ";
		echo '<a href="'.$link.'">'.$label_show.'</a><br>'."\n";

		echo $type_show.'<br>'."\n";
		if ( isset( $type_array[ $type_show ] ) ) {
			$type_array[ $type_show ] ++;
		} else {
			$type_array[ $type_show ] = 1;
		}

		echo $osm_link."<br>\n";
		echo '<br></li>'."\n";
	}

	echo "</ul><br>\n";

	echo "Count ".$i."<br><br>\n";

	foreach( 	$type_array as $type => $count ) {
		echo $type." ".$count."<br>\n";
	}
	
	$this->error->print_errors();
}

function get_label_show( $node )
{
	$label = $node["rdfs:label"] ; 
	$label_ja = isset( $node["rdfs:label:ja"] ) ? $node["rdfs:label:ja"] : "";	
	$show = $label;
	if ( $label_ja ) {
		$show = $label_ja;
	}
	return $show;
}

function get_type_show( $node )
{
	$type = $node["type:label"];
	$type_ja = isset( $node["type:label:ja"] ) ? $node["type:label:ja"] : "";
	$show = $type;
	if ( $type_ja ) {
		$show = $type_ja;
	}
	return $show;
}
	
function form( $lat, $long ) 
{

echo <<<EOF
default place: Kannai Station <br>
<form>
latitude <input type="text" name="lat" value="$lat"><br>
longitude <input type="text" name="long" value="$long"><br>
<input type="submit" name="submit" value="submit"><br>
</form>
EOF;
}

function header() 
{

echo <<<EOF
<html>
<head>
<meta charset="utf-8">
<title>LGD Viewer</title>
</head>
<body>
[HOME] [<a href="about.html">About</a>] <br><br>
<h1>Node List - LGD Viewer</h1>
EOF;
}

function footer() 
{
	$time = time() - $this->time;

echo <<<EOF
<br>
Excution time : $time sec<br>
<br>
</body>
</html>
EOF;
}

// class end
}

// main 
	include_once("config.class.php");
	include_once("node_list.class.php");
	include_once("node.class.php");
	include_once("ontology_label.class.php");
	include_once("ontology.class.php");
	include_once("ontology_utility.class.php");
	include_once("query.class.php");
	include_once("map.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$view = new lgd_main();
	$view->main();
	exit();

?>