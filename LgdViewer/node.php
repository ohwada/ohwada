<?php			
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_node_view
{
	var $node;
	var $ontology_label;
	var $map;
	var $error;
		
	var $time;

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
	$url = isset( $_GET["url"] ) ?  $_GET["url"] : "";
	$n = isset( $_GET["n"] ) ?  $_GET["n"] : "";
	$live = isset( $_GET["live"] ) ?  (boolean)$_GET["live"] : false;
	$cache = isset( $_GET["cache"] ) ?  (boolean)$_GET["cache"] : true;
		
	$link = "index.php?submit=submit#n". $n;
	$this->menu( $link );	

	$node = $this->node->get_node( $url, true, $live, $cache );	
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
	$type_show = $this->get_type_show( $node );
	$osm_link = $this->map->get_osm_link( $geo_lat, $geo_long );
		
	// event name
	echo "<h3>".$label_show."</h3>\n";
	echo $type_show."<br><br>\n";
		
	echo '<a href="'.$url.'" target="_blank">'.$url."</a><br>\n";
	echo $osm_link."<br><br>\n";

	echo "<table>\n";
	echo "<tr><th>Property</th><th>Value</th></tr>\n";

	foreach( $node as $k => $v ) {
		echo "<tr><td>". $k ."</td><td>". $v ."</td></tr>\n";
	}
	
	echo "</table>\n";
	
	$this->error->print_errors();
}

function get_label_show( $node )
{
	$label    = isset( $node["rdfs:label"] ) ? $node["rdfs:label"] : ""; 
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
	include_once("ontology_utility.class.php");
	include_once("query.class.php");
	include_once("map.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");
	
	$view = new lgd_node_view();
	$view->header(); 
	$view->main();
	$view->footer();
	exit();

?>