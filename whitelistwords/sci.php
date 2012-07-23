<?php
include_once('./simplehtmldom/simple_html_dom.php');

function scraping_ci() {
    // create HTML DOM
    // $html = file_get_html('http://www.cricinfo.com/ci/content/current/match/fixtures/index.html');
    $html = file_get_html('http://www.cricbuzz.com/component/cricket_schedule/All/15/');
    
    // get article block
    foreach($html->find('td[class="tabletxtodi"]') as $fixture) {
        // get fixtures
	print $fixture->innertext . "\n" ;
    }
    
    // clean up memory
    $html->clear();
    unset($html);

    return $ret;
}

// -----------------------------------------------------------------------------
// test it!
$ret = scraping_ci();

?>