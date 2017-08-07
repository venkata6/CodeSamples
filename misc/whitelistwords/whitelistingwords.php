<?php
	require_once('./PorterStemmer.inc') ;
        ini_set('memory_limit', '256M');
	define('WHITE_LISTED_WORDS', "/Users/varjunan/Desktop/personal/php/veggiewords");

        class whitelistWords {

                var $return_val = 1;
                var $stats = array();
                var $processed = 0;
                var $fh = FALSE;    // file handle 
		var $fileContents ;
		var $dictionary;

                function __construct() {
                        if(file_exists(WHITE_LISTED_WORDS)) {
                                $this->fh = fopen(WHITE_LISTED_WORDS, 'r');
                        	$this->fileContents = fread($this->fh, filesize(WHITE_LISTED_WORDS)) ;
                     		fclose($this->fh);
				$this->fh=FALSE;
		        }
			 else
			 {
				echo "File not found".PHP_EOL;
			 }
                }

                function init() {
                       echo "--------------init-----------------------------".PHP_EOL;
		       //$explodedArray = $this->explodeX(Array(".","!"," ","?",";","\n","\t"),$this->fileContents); 
		       $explodedArray = $this->explodeX(Array(" ","\n","\t"),$this->fileContents); 
		       #print_r($explodedArray);  		      
                        foreach($explodedArray as $key) {
				$this->dictionary[$key] = 1 ;	       
                        }
                      // echo "Printing dictionary now " . PHP_EOL ;
		      // print_r($this->dictionary);
                    
                }

		function __destruct() {
		//echo "in destruct" .PHP_EOL ;
		if ($this->fh) {
		    echo "file handle is not null, closing" . PHP_EOL ;
		    fclose($this->fh);
		    }
		}
	function explodeX($delimiters,$string) {

		////print_r($this->explodeX(Array(".","!"," ","?",";"),"This.sentence?contains wrong;characters"));  		      

		$return_array = Array($string); // The array to return
    		$d_count = 0;
    		while (isset($delimiters[$d_count])) // Loop to loop through all delimiters
   		{
       		 $new_return_array = Array();
        	 foreach($return_array as $el_to_split) // Explode all returned elements by the next delimiter
        	 {
           	  $put_in_new_return_array = explode($delimiters[$d_count],$el_to_split);
            	  foreach($put_in_new_return_array as $substr) // Put all the exploded elements in array to return
	    	  {
			$new_return_array[] = $substr;
                  }
            	 }
            $return_array = $new_return_array; // Replace the previous return array by the next version
       	     $d_count++;
	     //echo $d_count . PHP_EOL ;
    	    }
    	    return $return_array; // Return the exploded elements
    	}

   }
	$wlw = new whitelistWords() ;
	$wlw->init() ;
	$sentence = $_SERVER['argv'][1];
	$sentenceArray = explode (" ",$sentence) ;
	//print_r($sentenceArray) ;
	$wordsNotFound ;
	echo time() .PHP_EOL  ;
	echo "Going to look for words in dictionary .. " . PHP_EOL ;
	echo "Words passed:   ";
	foreach ( $sentenceArray as $word) {
	   $stemmedWord = PorterStemmer::Stem($word) ;
	   if ( $wlw->dictionary[$stemmedWord] == 1 ) {
	    echo $word . "  " ;
	    }
	    else { $wordsNotFound  .=  " " .  $word  ; }
	}
	echo PHP_EOL ;
	echo "Words failed: " . $wordsNotFound . PHP_EOL ; 
	echo time() .PHP_EOL  ;	
?>