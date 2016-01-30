<?php							
//=========================================================
// LGD Viewer
// 2014-02-20 K.OHWADA
//=========================================================

class lgd_ontology_view
{
	var $query;
	var $error;
	
function lgd_ontology_view()
{
	$this->query =& lgd_query::getInstance();
	$this->error =& lgd_error::getInstance();
}

function main()
{
	$this->error->clear_errors();

	$url = isset( $_GET["url"] ) ?  $_GET["url"] : "";
	$ret = $this->query->get_ontology( $url );
	if ( $ret == false ) {
		echo "no result: ".$url."<br>\n";
		return;
	}
	if ( !is_array($ret) || !count($ret) ) {
		echo "no result: ".$url."<br>\n";
		return;
	}

	echo "<h3>".$url."</h3>\n";

	echo "<table>\n";
	echo "<tr><th>Property</th><th>Value</th></tr>\n";

	foreach( $ret as $k => $v ) {
		echo "<tr><td>". $k ."</td><td>". $v ."</td></tr>\n";
	}
	
	echo "</table>\n";
			
	$this->error->print_errors();
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
[<a href="index.php">HOME</a>] [<a href="about.html">About</a>] <br><br>
<h1>Node - LGD Viewer</h1>
EOF;
}

function footer() 
{
echo <<<EOF
</body>
</html>
EOF;
}

// class end
}

// main 
	include_once("config.class.php");
	include_once("ontology_utility.class.php");
	include_once("query.class.php");
	include_once("file.class.php");
	include_once("error.class.php");
	include_once("snoopy.php");

	$view = new lgd_ontology_view();
	$view->header(); 
	$view->main();
	$view->footer();
	exit();

?>