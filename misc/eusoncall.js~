// DISCLAIMER: this is a simple code written in 8-10 hrs in a weekemd  to log the oncall hrs 
// not necessarily clean code , also newbie  to node and js and mongodb 
// some part of code is scavenged from net 

GLOBAL.DEBUG = true;

test = require("assert");
var http = require('http');

var Db = require('/usr/local/sbin/node_modules/mongodb/lib/mongodb').Db,
  Connection = require('/usr/local/sbin/node_modules/mongodb/lib/mongodb').Connection,
  Server = require('/usr/local/sbin/node_modules/mongodb/lib/mongodb').Server;

var host = process.env['MONGO_NODE_DRIVER_HOST'] != null ? process.env['MONGO_NODE_DRIVER_HOST'] : 'localhost';
var port = process.env['MONGO_NODE_DRIVER_PORT'] != null ? process.env['MONGO_NODE_DRIVER_PORT'] : Connection.DEFAULT_PORT;

console.log("Connecting to " + host + ":" + port);
var db = new Db('Eurosport', new Server(host, port, {}), {});
var devils =""  ;
var cnt = 0 ;
var onCallDetailsChaitanya = [] ;
var onCallDetailsSony = [] ;
var onCallDetailsGokul = [] ;
var onCallDetailsDivya = [] ;
var onCallDetailsVikram = [] ;
var onCallDetailsDhanya = [] ;

var cCnt = 0 ;
var sCnt = 0 ;
var gCnt = 0 ;
var dCnt = 0 ;
var vCnt = 0 ;
var dhCnt = 0 ;

var current = "" ;
var currentDate = new Date() ;


var chTotalDays = 0 ;
var goTotalDays = 0 ;
var diTotalDays = 0 ;
var dhTotalDays = 0 ;
var soTotalDays = 0 ;
var viTotalDays = 0 ;


// Source: http://stackoverflow.com/questions/497790
// this part of code to check a Date fall in between a given Date Range is scavenged from the net 
// Start ***********

var dates = {
    convert:function(d) {
        // Converts the date in d to a date-object. The input can be:
        //   a date object: returned without modification
        //  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
        //   a number     : Interpreted as number of milliseconds
        //                  since 1 Jan 1970 (a timestamp) 
        //   a string     : Any format supported by the javascript engine, like
        //                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
        //  an object     : Interpreted as an object with year, month and date
        //                  attributes.  **NOTE** month is 0-11.
        return (
		d.constructor === Date ? d :
		d.constructor === Array ? new Date(d[0],d[1],d[2]) :
		d.constructor === Number ? new Date(d) :
		d.constructor === String ? new Date(d) :
		typeof d === "object" ? new Date(d.year,d.month,d.date) :
            NaN
		);
    },
    compare:function(a,b) {
        // Compare two dates (could be of any type supported by the convert
        // function above) and returns:
        //  -1 : if a < b
        //   0 : if a = b
        //   1 : if a > b
        // NaN : if a or b is an illegal date
        // NOTE: The code inside isFinite does an assignment (=).
        return (
		isFinite(a=this.convert(a).valueOf()) &&
		isFinite(b=this.convert(b).valueOf()) ?
		(a>b)-(a<b) :
            NaN
		);
    },
    inRange:function(d,start,end) {
        // Checks if date in d is between dates in start and end.
        // Returns a boolean or NaN:
        //    true  : if d is between start and end (inclusive)
        //    false : if d is before start or after end
        //    NaN   : if one or more of the dates is illegal.
        // NOTE: The code inside isFinite does an assignment (=).
	return (
		isFinite(d=this.convert(d).valueOf()) &&
		isFinite(start=this.convert(start).valueOf()) &&
		isFinite(end=this.convert(end).valueOf()) ?
		start <= d && d <= end :
            NaN
		);
    }
}
// End - Source: http://stackoverflow.com/questions/497790                                                                                     


db.open(function(err, db) {
    db.collection('test', function(err, collection) {      
        collection.count(function(err, count) {
		//console.log("There are " + count + " records in the test collection. Here they are:");
          collection.find(function(err, cursor) {
            cursor.each(function(err, item) {
              if(item != null) {
		 // console.dir(item);
                //console.log("created at " + new Date(item._id.generationTime) + "\n")
		if ( typeof item.devils !== "undefined")
		{  // this is the list of oncall developers in the team
		     if ( cnt++ != 0 ) { devils +=   " , " } ;
			devils += item.devils ; 
		 }
		else if ( (item.name === "chaitanya") && typeof item !== "undefined" )
		    { // this is the list of days Chaitanya was on call 
			onCallDetailsChaitanya[cCnt] = new Array(3) ;
			onCallDetailsChaitanya[cCnt]["name"] = item.name  ;
			onCallDetailsChaitanya[cCnt]["start"] = item.start ;
			onCallDetailsChaitanya[cCnt]["end"] = item.end ;
			cCnt++ ;
			if ( dates.inRange (currentDate,item.start,item.end) )
			    {
				current="chaitanya" ;
			    }
                         var days = (item.end.getTime() - item.start.getTime()) / (60*60*24*1000) ;
			chTotalDays += days + 1  ;  // plus one is needed since we need to be inclusive of oncall end date 

		    }
		else if ( item.name === "gokul")
		    { // this is the list of days 'Gokul'  was on call
			onCallDetailsGokul[gCnt] = new Array(3) ;
			onCallDetailsGokul[gCnt]["name"] = item.name  ;
			onCallDetailsGokul[gCnt]["start"] = item.start ;
			onCallDetailsGokul[gCnt]["end"] = item.end ;
			gCnt++ ;
			if ( dates.inRange (currentDate,item.start,item.end) )
			    {
				current="gokul" ;
			    }
                        var days = (item.end.getTime() - item.start.getTime()) / (60*60*24*1000) ;
			goTotalDays += days + 1  ;  // plus one is needed since we need to be inclusive of oncall end date 
		    }
		else if ( item.name === "sony")
		    { // this is the list of days 'Sony' was on call 

			onCallDetailsSony[sCnt] = new Array(3) ;
			onCallDetailsSony[sCnt]["name"] = item.name  ;
			onCallDetailsSony[sCnt]["start"] = item.start ;
			onCallDetailsSony[sCnt]["end"] = item.end ;
			sCnt++ ;

			var tmpEndDate = new Date() ;
			tmpEndDate.setDate(item.end.getDate()+1) ; // on the last day of the oncall the check below fails, so to fix it 
 			if ( dates.inRange (currentDate,item.start,tmpEndDate ))
			    {
				current="sony" ;
			    }
                        var days = (item.end.getTime() - item.start.getTime()) / (60*60*24*1000) ;
			soTotalDays += days + 1  ;  // plus one is needed since we need to be inclusive of oncall end date 
		    }


		else if ( item.name === "divya")
		    { // this is the list of days 'Divya' was on call 

			onCallDetailsDivya[dCnt] = new Array(3) ;
			onCallDetailsDivya[dCnt]["name"] = item.name  ;
			onCallDetailsDivya[dCnt]["start"] = item.start ;
			onCallDetailsDivya[dCnt]["end"] = item.end ;
			dCnt++ ;

			console.log(currentDate) ;
			console.log(item.end) ;
			//item.end.setDate(item.end.getDate()+1 ) ; //fix this for the bug that last day of oncall 
			console.log(item.end) ;

			if ( dates.inRange (currentDate,item.start,(item.end)) )
			    {
				current="divya" ;
			    }
                        var days = (item.end.getTime() - item.start.getTime()) / (60*60*24*1000) ;
			diTotalDays += days + 1  ;  // plus one is needed since we need to be inclusive of oncall end date 

		    }


		else if ( item.name === "vikram")
		    { // this is the list of days 'Vikram' was on call 

			onCallDetailsVikram[vCnt] = new Array(3) ;
			onCallDetailsVikram[vCnt]["name"] = item.name  ;
			onCallDetailsVikram[vCnt]["start"] = item.start ;
			onCallDetailsVikram[vCnt]["end"] = item.end ;
			vCnt++ ;
			if ( dates.inRange (currentDate,item.start,item.end) )
			    {
				current="vikram" ;
			    }
                        var days = (item.end.getTime() - item.start.getTime()) / (60*60*24*1000) ;
			viTotalDays += days + 1  ;  // plus one is needed since we need to be inclusive of oncall end date 
		    }
              
	     else if ( item.name === "dhanya")
		    { // this is the list of days 'dhanya' was on call 

			onCallDetailsDhanya[dhCnt] = new Array(3) ;
			onCallDetailsDhanya[dhCnt]["name"] = item.name  ;
			onCallDetailsDhanya[dhCnt]["start"] = item.start ;
			onCallDetailsDhanya[dhCnt]["end"] = item.end ;
			dhCnt++ ;
			if ( dates.inRange (currentDate,item.start,item.end) )
			    {
				current="dhanya" ;
			    }
                        var days = (item.end.getTime() - item.start.getTime()) / (60*60*24*1000) ;
			dhTotalDays += days + 1  ;  // plus one is needed since we need to be inclusive of oncall end date 
		    }
              }

	
              // Null signifies end of iterator
              if(item == null) {                
                // Destory the collection
                  db.close();
              }
            });
          });          
       });
    });
});


http.createServer(function (req, res) {
	res.writeHead(200, {'Content-Type': 'text/plain'});

	res.write("Eurosport Engineers list -  " + devils.toString() );

	res.write("\n\nCurrent Oncall person = " + current ) ;

	res.write("\n\nOnCallDetails - 2012 " ) ;

	res.write("\n\nChaitanya - Total oncalldays  is " + chTotalDays ) ;
       	for ( var i=0; i < cCnt; i++ )
	    {
		var sDate = new Date ( onCallDetailsChaitanya[i].start ) ;
		var eDate = new Date ( onCallDetailsChaitanya[i].end ) ;
		//res.write("\n test - " + onCallDetailsChaitanya[i].start.toString() + " " + onCallDetailsChaitanya[i].end.toString()  ) ;
 	        res.write("\n   From   " + sDate.getDate()+"/"+(sDate.getMonth()+1)+"/" + sDate.getFullYear()  + " To  " + eDate.getDate()+"/"+(eDate.getMonth()+1)+"/" + eDate.getFullYear());
	    }
	res.write("\n\nGokul - Total oncalldays  is " + goTotalDays ) ;
       	for ( var i=0; i < gCnt; i++ )
	    {
		var sDate = new Date ( onCallDetailsGokul[i].start ) ;
		var eDate = new Date ( onCallDetailsGokul[i].end ) ;
 	        res.write("\n   From   " + sDate.getDate()+"/"+ (sDate.getMonth()+1 ) +"/" + sDate.getFullYear()  + " To  " + eDate.getDate()+"/"+(eDate.getMonth()+1)+"/" + eDate.getFullYear());
	    }
	res.write("\n\nSony - Total oncalldays  is " + soTotalDays ) ;
       	for ( var i=0; i < sCnt; i++ )
	    {
		var sDate = new Date ( onCallDetailsSony[i].start ) ;
		var eDate = new Date ( onCallDetailsSony[i].end ) ;
 	        res.write("\n   From   " + sDate.getDate()+"/"+ (sDate.getMonth()+1)+"/" + sDate.getFullYear()  + " To  " + eDate.getDate()+"/"+ (eDate.getMonth()+1)+"/" + eDate.getFullYear());
	    }


	res.write("\n\nDivya - Total oncalldays  is " + diTotalDays ) ;
       	for ( var i=0; i < dCnt; i++ )
	    {
		var sDate = new Date ( onCallDetailsDivya[i].start ) ;
		var eDate = new Date ( onCallDetailsDivya[i].end ) ;
 	        res.write("\n   From   " + sDate.getDate()+"/"+ (sDate.getMonth()+1)+"/" + sDate.getFullYear()  + " To  " + eDate.getDate()+"/"+ (eDate.getMonth()+1)+"/" + eDate.getFullYear());
	    }

	res.write("\n\nVikram - Total oncalldays  is " + viTotalDays ) ;
       	for ( var i=0; i < vCnt; i++ )
	    {
		var sDate = new Date ( onCallDetailsVikram[i].start ) ;
		var eDate = new Date ( onCallDetailsVikram[i].end ) ;
 	        res.write("\n   From   " + sDate.getDate()+"/"+ (sDate.getMonth()+1)+"/" + sDate.getFullYear()  + " To  " + eDate.getDate()+"/"+(eDate.getMonth()+1)+"/" + eDate.getFullYear());
	    }

	res.write("\n\nDhanya - Total oncalldays  is " + dhTotalDays ) ;
       	for ( var i=0; i < dhCnt; i++ )
	    {
		var sDate = new Date ( onCallDetailsDhanya[i].start ) ;
		var eDate = new Date ( onCallDetailsDhanya[i].end ) ;
 	        res.write("\n   From   " + sDate.getDate()+"/"+ (sDate.getMonth()+1)+"/" + sDate.getFullYear()  + " To  " + eDate.getDate()+"/"+(eDate.getMonth()+1)+"/" + eDate.getFullYear());
	    }

	res.end("\n\n\nEnd of oncall details.\n\nhave a nice day!  " ) ; 


    }).listen(8081, "richreturn-dr");
console.log('Server running at http://127.0.0.1:1337/');
