<?php
// $Id: $

//=========================================================
// LGD Viewer
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
	$this->node = new lgd_node();
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
	$this->file->delete_files(); 						
	$this->form( $lat, $long );
	if ( isset( $_GET["submit"] ) ) {
		$this->body( $lat, $long );
	}
	$this->footer(); 
}

function body( $center_lat, $center_long )
{
	$this->error->clear_errors();
	$array = $this->node_list->get_node_list( $center_lat, $center_long, true );
	$direct_array = array();
	
	echo "<ul>\n";

	$i = 0;
	foreach ( $array as $a ) {
		$i ++;

		$url      = $a["lgdo:Node"] ;
		$label    = $a["rdf:label"] ;
		$direct  = $a["lgdo:directType"] ;
		$geometry  = $a["geo:geometry"] ;
		$lat  = $a["geo:lat"] ;
		$long  = $a["geo:long"] ;
	
		$label_ja = $this->get_label_ja( $url );
		$direct_label_ja = $this->get_direct_label_ja_from_node( $a );

		$link = "node.php?n=". $i. "&url=". urlencode( $url );	
		$osm_link = $this->map->get_osm_link( $lat, $long );

		$label_show = $label;
		if ( $label_ja ) {
			$label_show = $label_ja;
		}

		if ( isset( $direct_array[ $direct ] ) ) {
			$direct_array[ $direct ] ++;
		} else {
			$direct_array[ $direct ] = 1;
		}
				
		echo '<li>';
		echo '<a name="n'. $i .'">'."\n";
		echo $i.": ";
		echo '<a href="'.$link.'">'.$label_show.'</a><br>'."\n";
		echo $direct_label_ja.'<br>'."\n";
		echo $osm_link."<br>\n";
		echo '<br></li>'."\n";
	}

	echo "</ul><br>\n";

	echo "Count ".$i."<br>\n";
	foreach( 	$direct_array as $direct => $count ) {
		$direct_label_ja = $this->get_direct_label_ja( $direct );
		echo $direct_label_ja." ".$count."<br>\n";
	}
	
	$this->error->print_errors();
}

function get_label_ja( $url )
{
	// get node rapidly without query
	$node = $this->node->get_node( $url, false );	
	$label_ja = isset( $node[ "rdf:label:ja" ] ) ? $node[ "rdf:label:ja" ] : "";
	return $label_ja;
}

function get_direct_label_ja_from_node( $node )
{
	// get node rapidly without query
	return $this->ontology_label->get_direct_label_ja( $node, false );
}

function get_direct_label_ja( $direct )
{
	// get node rapidly without query
	return $this->ontology_label->get_ontology_label( $direct, false );
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
	include_once("query.class.php");
	include_once("map.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$main = new lgd_main();
	$main->main();
	exit();

?>