<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_node_view
{
	var $node;
	var $ontology_label;
	var $map;
	var $error;
		
	var $time;

	var $KEY_EXCEPT_ARRAY = array(
		"lgdo:Node",
		"geo:geometry",
		"geo:lat",
		"geo:long",
	);

function lgd_node_view()
{
	$this->node =& lgd_node::getInstance();
	$this->map =& lgd_map::getInstance();
	$this->ontology_label =& lgd_ontology_label::getInstance();
	$this->error =& lgd_error::getInstance();
			
	$this->time = time();
}

function main()
{
	$this->error->clear_errors();
	
	$url = isset( $_GET["url"] ) ?  $_GET["url"] : "";
	$n = isset( $_GET["n"] ) ?  $_GET["n"] : "";
		
	$link = "index.php?submit=submit#n". $n;
	$this->menu( $link );	
		
	$node = $this->node->get_node( $url, true );	
	if ( $node == false ) {
		echo "no result: ".$url."<br>\n";
		return;
	}

	if ( !is_array($node) || !count($node) ) {
		echo "no result: ".$url."<br>\n";
		return;
	}

	$geo_lat = $node["geo:lat"] ; 
	$geo_long = $node["geo:long"] ; 

	$label_show = $this->get_label_show( $node );
	$direct_label_ja = $this->ontology_label->get_direct_label_ja( $node, true );	
	$osm_link = $this->map->get_osm_link( $geo_lat, $geo_long );
		
	// event name
	echo "<h3>".$label_show."</h3>\n";
	echo $direct_label_ja."<br><br>\n";
		
	echo '<a href="'.$url.'" target="_blank">'.$url."</a><br>\n";
	echo $osm_link."<br><br>\n";

	echo "<table>\n";
	echo "<tr><th>Property</th><th>Value</th></tr>\n";

	foreach( $node as $k => $v ) {
		if ( in_array( $k, $this->KEY_EXCEPT_ARRAY ) ) continue;
		echo "<tr><td>". $k ."</td><td>". $v ."</td></tr>\n";
	}
	
	echo "</table>\n";
	
	$this->error->print_errors();
}

function get_label_show( $node )
{
	$label    = isset( $node["rdf:label"] ) ? $node["rdf:label"] : ""; 
	$label_ja = isset( $node["rdf:label:ja"] ) ? $node["rdf:label:ja"] : "";	
	$show = $label;
	if ( $label_ja ) {
		$show = $label_ja;
	}
	return $show;
}

function print_item( $node, $name )
{
	$value = $node[ $name ];
	if ( $value ) {
		echo $name." : ".$value."<br>\n";
	}
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
EOF;
}

function menu( $link ) 
{
echo <<<EOF
[<a href="$link">HOME</a>] [<a href="about.html">About</a>] <br><br>
<h1>Node - LGD Viewer</h1>
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
	include_once("node.class.php");
	include_once("ontology_label.class.php");
	include_once("ontology.class.php");
	include_once("query.class.php");
	include_once("map.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$node_view = new lgd_node_view();
	$node_view->header(); 
	$node_view->main();
	$node_view->footer();
	exit();

?>