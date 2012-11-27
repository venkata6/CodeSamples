#!/usr/bin/perl                                                                                                                                           
use CGI;
use strict;
use DBI;
use ysecure;
use Data::Dumper;

my $dbi;
my $dbh;
my $statement;
my $array_ref;

my %attr = ( 11, 'Print Error' => 1 ) ;

ycrKeyDbInit();
my $sql = ' select * from TB_SPORT ; ';

my $key = ycrGetKey('reader');
my $dbpasswd = $key->value();
my $dbhost= 'dbcast1.eurosport.jake.ukl.yahoo.com' ; 
my $dbname= 'DB_ES06' ;
my %tbsport;
my $tbsportref = \%tbsport;

#this needs to be run in one of the production boxes

$dbi = "dbi:mysql:database=DB_ES06;host=dbcast1.eurosport.jake.ukl.yahoo.com";

$dbh = DBI->connect( $dbi, 'reader', $dbpasswd, \%attr )
            or die "Could not connect to database: " . $DBI::errstr;


$statement = $dbh->prepare($sql);
$statement->execute( );

$array_ref = $statement->fetchall_arrayref;
$dbh->disconnect;

for my $row (@$array_ref) {

	my @fields = @$row;
	
	if ( $fields[2] == 1 ){
	  $tbsportref->{$fields[0]}{"DE"} = $fields[1] . " - " . $fields[4] ;
	}
	if ( $fields[2] == 2 ){
	  $tbsportref->{$fields[0]}{"EN"} = $fields[1]  . " - " . $fields[4] ;
	}
	if ( $fields[2] == 3){
	  $tbsportref->{$fields[0]}{"FR"} = $fields[1]  . " - " . $fields[4] ;
	}
	if ( $fields[2] == 4 ){
	  $tbsportref->{$fields[0]}{"IT"} = $fields[1]  . " - " . $fields[4] ;
	}
	if ( $fields[2] == 6 ){
	  $tbsportref->{$fields[0]}{"ES"} = $fields[1]  . " - " . $fields[4] ;
	}
}
 print "SportId, DE(name-url), EN,FR, IT , ES \n " ;
for my $key (sort {$a <=> $b} keys %tbsport) {
  print $key . "," . $tbsport{$key}{"DE"} . "," . $tbsport{$key}{"EN"} . "," . $tbsport{$key}{"FR"} . "," . $tbsport{$key}{"IT"} . "," . $tbsport{$key}{"ES"} . "\n" ; 
}

#print Dumper(%tbsport);
