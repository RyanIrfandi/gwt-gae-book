#!/usr/bin/env php
<?php
$docs = array(
	"Introduction",
	"SoYouHaveAnIdea",
	"VisualizingYourApp",
	"TheRightToolForTheJob",
	"GettingStarted",
	"DataModeling",
	"BuildingLandingPage",
	"ClientServer",
	"StoringData",
	"MoreResources",
	"TODO"
);
/*
	"",
*/

$tocShort = "[TableOfContents Table of contents]\n\n";
$tocDetail = "= Table of contents =\n\n";
foreach ($docs as $doc) {
	$f = fopen($doc . ".wiki", "ro");
	$s = fread($f, 1000000);
	fclose($f);

	preg_match("/(?<=^#summary\s)[^\n]*/m", $s, $matches);
	//print_r($matches);
	$summary = trim($matches[0]);
	
	$tocShort .= "[$doc $summary]\n\n";

	$tocDetail .= "\n== [$doc $summary] ==\n";


	if (preg_match_all("/^([=]+)\s([^=]*)/m", $s, $matches)) {		
		//print_r($matches);
		foreach($matches[2] as $key => $label) {
			$label = trim($label);
			$label = str_replace("!", "", $label);
			$indent = $matches[1][$key];
			$indent = str_replace("=", " ", $indent);
			
			$tocDetail .= $indent . "==== [$doc#" . str_replace(" ", "_", $label) . " $label] ====\n";
		}

	}

}

echo $tocShort;
$f = fopen("TableOfContentsShort.wiki", "w");
fwrite($f, $tocShort);
fclose($f);

$f = fopen("TableOfContents.wiki", "w");
fwrite($f, $tocDetail);
fclose($f);


?>
