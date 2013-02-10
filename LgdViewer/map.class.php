<?php
// $Id: $

//=========================================================
// LGD Viewer
// 2013-01-20 K.OHWADA
//=========================================================

class lgd_map
{
		
function lgd_map()
{
	// dummy
}

function &getInstance()
{
	static $instance;
	if (!isset($instance)) {
		$instance = new lgd_map();
	}
	return $instance;
}

function get_osm_link( $lat, $long, $zoom=18 )
{
	$url = $this->get_osm_url( $lat, $long, $zoom );
	$str = '<a href="'.$url.'" target="_blank">OpenStrretMap ('.$lat.' '.$long.')</a>';
	return $str;
}

function get_osm_url( $lat, $long, $zoom )
{
	$str = "http://www.openstreetmap.org/?lat=".$lat."&lon=".$long."&zoom=".$zoom."&layers=M";
	return $str;
}
		
// class end
}

?>