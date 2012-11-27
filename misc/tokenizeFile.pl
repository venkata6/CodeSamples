#!/usr/bin/perl
 
# no error checking :-), perl newbie
# first parameter file to open
# second parameter delimiter
#tokenizeFile <inputfile> <delimiter>

open (FILE, $ARGV[0]);
while (<FILE>) {
    chomp;
    @tokens = split($ARGV[1]);
    for (@tokens) {
	print shift @tokens ;
	print "\n" ;
    }
}
close (FILE);
exit;
