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
	"ListingSchedule",
	"Authentication",
	"ManagingPerformances",
	"QualityAssurance",
	"ManagingShows",
	"ExposingData",
	"AdditionalResources",
	"TODO"
);
/*
	"",
*/

define("NAV_START", "<wiki:comment>NAV_START</wiki:comment>");
define("NAV_END", "<wiki:comment>NAV_END</wiki:comment>");
define("NAV_ROOT", "http://code.google.com/p/gwt-gae-book/wiki/"); 
//workaround for http://code.google.com/p/support/issues/detail?id=4563

class Chapter 
{
	public $href;
	public $name;
};

$chapters = array(); 

$tocShort = "[TableOfContents Table of contents]\n\n";
$tocDetail = "= Table of contents =\n\n";
foreach ($docs as $doc) {
	$f = fopen($doc . ".wiki", "r");
	$s = fread($f, 1000000);
	fclose($f);

	preg_match("/(?<=^#summary\s)[^\n]*/m", $s, $matches);
	//print_r($matches);
	$summary = trim($matches[0]);

	$c = new Chapter();
	$c->href = $doc;
	$c->name = $summary;
	$chapters[] = $c;
	
	$tocShort .= "[$doc $summary]\n\n";

	$tocDetail .= "\n=== [$doc $summary] ===\n";


	if (preg_match_all("/^([=]+)\s([^=]*)/m", $s, $matches)) {		
		//print_r($matches);
		foreach($matches[2] as $key => $label) {
			$label = trim($label);
			$label = str_replace("!", "", $label);
			$indent = $matches[1][$key];
			$indent = str_replace("=", " ", $indent);
			
//			$tocDetail .= $indent . "==== [$doc#" . str_replace(" ", "_", $label) . " $label] ====\n";
			$tocDetail .= $indent . "* [$doc#" . str_replace(" ", "_", $label) . " $label] <br/>\n";
		}

	}

}

$tocShort .= "\n<wiki:gadget url='http://gwt-gae-book.googlecode.com/svn/wiki/assets/gadget_like.xml' width='250' height='35' frameborder='0'/>\n";
echo $tocShort;
$f = fopen("TableOfContentsShort.wiki", "w");
fwrite($f, $tocShort);
fclose($f);

$f = fopen("TableOfContents.wiki", "w");
fwrite($f, $tocDetail);
fclose($f);

//print_r($chapters);
// generate page navigation
for ($i = 0; $i<count($chapters); $i++) {
	echo "\nProcessing " . $chapters[$i]->href . " " . $chapters[$i]->name;
	$f = fopen($chapters[$i]->href . ".wiki", "r");
	$s = fread($f, 1000000);
	fclose($f);

	$s = preg_replace("/" . str_replace("/", "\/", NAV_START) . "(.*)" . str_replace("/", "\/", NAV_END) . "/ms", "", $s);
	
	$s .= NAV_START . "\n";
	$s .= "<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>" . "\n";
	$s .= "<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />" . "\n";

	if ($i>0) {
		$s .= "<a href='" . NAV_ROOT . $chapters[$i-1]->href . "'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: " . $chapters[$i-1]->name . "' /></a>" . "\n";
	}

	$s .= "<a href='" . NAV_ROOT . "TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>" . "\n";

	if ($i<(count($chapters)-1)) {
		$s .= "<a href='" . NAV_ROOT . $chapters[$i+1]->href . "'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: " . $chapters[$i+1]->name . "' /></a>" . "\n";
	}

	$s .= NAV_END;

	//echo $s;
	$f = fopen($chapters[$i]->href . ".wiki", "w");
	fwrite($f, $s);
	fclose($f);

}



?>
