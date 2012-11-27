#!/usr/bin/perl
use strict;
use warnings;

#print "hello world \n" ;
#print "$script\n"; 
my $script = qq|
tell application "Microsoft Excel"
  activate
  open workbook workbook file name "Macintosh HD:Users:varjunan:Desktop:Last3MonthsBugList.csv"
  tell worksheet "Last3MonthsBugList.csv" of active workbook
  value of used range
  end tell
  end tell
|;

my $closeScript = qq|
tell application "Microsoft Excel"
  activate
  close the workbooks saving no
end tell
|;


AppleScript($script);
system(qq|/usr/bin/osascript -e '$closeScript'|);

sub AppleScript {
    my($script) = shift @_;


    my $value = `/usr/bin/osascript -e '$script'`;
    my @DateAndSevArray  = split(/,/, $value);
    my $cri=0,my $maj=0, my $nor=0, my $min=0, my $tri=0 ;
    foreach my $token (@DateAndSevArray) {
	if ( $token =~ m/-/ ) {
#	    print "tokens are $token \n" ;
	  
	    if ( $token =~ /^ 1/) {
		$cri++;
	    }
	    elsif ( $token =~ /^ 2/) {
		$maj++;
	    }
	    elsif ( $token =~ /^ 3/) {
		$nor++;
	    }

	    elsif ( $token =~ /^ 4/) {
		$min++;
	    }

	    elsif ( $token =~ /^ 5/) {
		$tri++
	    }
	    
	}
    }
    print ("S1 = $cri, S2 = $maj, S3 = $nor, S4 = $min, S5 = $tri \n" ) ;
}
