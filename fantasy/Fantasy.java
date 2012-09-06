import java.io.*;
import java.util.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/* main class which will encapsulate the players and algorithm used 
** variables are public for easy to use and avoid getters/setters .. in real case, i will use them 
*/

class Fantasy {
    /* define string constants here */
    public static final String FRB = "r"; /* football runningback */
    public static final String FQB = "q"; /* football quarterback */
    public static final String OutputFileName = "results.xml" ;
    public static final String VelocityTemplate = "results.vm" ;
    
    public ArrayList<Player> playersInfo;   /* this will hold all the players  */
    public ScoringAlgorithm algo;   /* this hold the algorithm to be used for calculating the points */

    /* contstructor */
    public Fantasy() { 
	playersInfo = new ArrayList<Player>() ;
    }
    public static void main ( String[] args ) { 
	try {  /* first let us read the data in */
	    if ( args.length == 0 ) {
		System.out.println("Usage: java Fantasy datafile <meanORsimple> ") ;
		System.exit(0);
	    }
	    /* create the main object */
	    Fantasy fantasy = new Fantasy();

	    /* Set the algorithm - do it through a setter later */
	    if ( args.length > 1 && args[1].equals("mean")) {
		fantasy.algo = new MeanBasedScoring();
	    }
	    else {
		fantasy.algo = new SimpleScoring() ;
	    }

	    /* read the input now from the file */
	    FileInputStream fstream = new FileInputStream(args[0]);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
		    
	    /* Read line by line now  and build Players array in playersInfo */
	    while ((strLine = br.readLine()) != null) {
		Player player = Fantasy.processFeed(strLine);
		fantasy.playersInfo.add(player);  // add the playert to the player array
		} 
	    /* close the file stream */
	    in.close();

	    /* calculate the fantasy points now  */
	    fantasy.algo.calculatePoints(fantasy.playersInfo);

	    
	    /* now time to sort and print the data in the Standard o/p  */
	    Fantasy.printData(fantasy.playersInfo);

	    /* last step - now use the Apache Velocity templating engine to create a results.xml */
	    Fantasy.writeToFile(OutputFileName,fantasy);

	}
	catch (Exception e){
	    System.err.println("Error: " + e.getMessage() );
	}
    }

    private static void writeToFile(String filename, Fantasy fantasy) {
	/* create the result.xml using Apache Velocity Template engine */
	try {	

	    /* get the engine */
	    VelocityEngine v = new VelocityEngine();
	    v.init();

	    /* get the results.xml's template - results.vm */
	    Template t = v.getTemplate(VelocityTemplate);
	    VelocityContext vc = new VelocityContext();

	    /* pass the players array to Velocity  */
	    vc.put("playersInfo",fantasy.playersInfo);
	    vc.put("Player",Player.class);  /* this may be  not needed as i think Velocity uses reflection to introspect Player object */

	    StringWriter writer = new StringWriter();
	    /* merge the template and the context  */
	    t.merge(vc,writer);   
	    System.out.println(writer.toString());
	    
	    FileWriter fostream = new FileWriter(filename);
	    BufferedWriter out = new BufferedWriter(fostream);
	    out.write(writer.toString());
	    /* Close the output stream */
	    out.close();
	}
	catch (Exception e){ 
	    System.err.println("Error: " + e.getMessage());
	}
    }

    private static void printData(ArrayList<Player> playersInfo) {
	/* Player object implements 'Comparable' interface and
	   hence the 'sort' functions sorts based on the fantasy points  */
	Collections.sort(playersInfo);
	for (int i=0; i < playersInfo.size();i++) {
	    Player pl = playersInfo.get(i);
	    /* set the ranking here */
	    pl.ranking=i+1; 
	    /* print the data to std o/p  */
	    pl.printData();
	}
    }
    private static Player  processFeed(String str) {
	/* this function instatantiates appropriate 'Player' object
	   and populates the data into it  */
	String delims = "," ;
	String[] tokens = str.split(delims);
	if ( FRB.equals(tokens[0])) {
	   FootballRunningBack frb = new FootballRunningBack();
	   frb.typeCode = tokens[0];
	   frb.playerId = Integer.parseInt(tokens[1]);
	   frb.rushAttempts = Integer.parseInt(tokens[2]);
	   frb.rushYards = Integer.parseInt(tokens[3]);
	   frb.noOfTouchDowns = Integer.parseInt(tokens[4]);
	   return frb ;
	   }
	else if ( FQB.equals(tokens[0])) {
	   FootballQuarterBack fqb = new FootballQuarterBack();
	   fqb.typeCode = tokens[0];
	   fqb.playerId = Integer.parseInt(tokens[1]);
	   fqb.passCompletions = Integer.parseInt(tokens[2]);
	   fqb.passAttempts = Integer.parseInt(tokens[3]);
	   fqb.passingYards = Integer.parseInt(tokens[4]);
	   fqb.interceptions = Integer.parseInt(tokens[5]);
	   fqb.noOfTouchDowns = Integer.parseInt(tokens[6]);
	   return fqb;
	   }
	return null;
    }
}


/* parent interface for diffrent algorithms used for calculating the fantasy points.
   GoF - strategy pattern */
interface ScoringAlgorithm {  
    int calculatePoints(ArrayList<Player> playerArray );
}

/* if i declare the class public, i need to create a new file and hence i am not declaring like that */
class SimpleScoring implements ScoringAlgorithm {  /* simplescoring */
    public int calculatePoints (ArrayList<Player> playerArray) {
	for(Player player : playerArray)
	    {
		if ( player.getTypeCode().equals(Fantasy.FRB)) {  /* running back */
		    player.fantasyPoints = (player.getRushYards() * 2 + player.getNoOfTouchDowns() * 6);
		}
	    
		else if ( player.getTypeCode().equals(Fantasy.FQB)) {  /* quarter back */
		    player.fantasyPoints = ( player.getPassingYards()* 1 + player.getNoOfTouchDowns() * 6 - player.getInterceptions() * 1);
		}
	
	    }
	return 0;
    }
}

/* calculate the mean and subtract from each */
class MeanBasedScoring implements ScoringAlgorithm {  
    public int calculatePoints (ArrayList<Player> playerArray){

	int rmean = 0; /* running back mean  */
	int qmean = 0; /* quarter back mean  */
	int rcnt = 0;
	int qcnt = 0;

	/* first let us calculate the simple scoring  */
	for(Player player : playerArray) {
	    if ( player.getTypeCode().equals(Fantasy.FRB)) {  /* running back */
		player.fantasyPoints = (player.getRushYards() * 2 + player.getNoOfTouchDowns() * 6)-rmean;
	    }
	    else if ( player.getTypeCode().equals(Fantasy.FQB)) {  /* quarter back */
		player.fantasyPoints = ( player.getPassingYards()* 1 + player.getNoOfTouchDowns() * 6 - player.getInterceptions() * 1-qmean);
	    }
	}
	/* now let us calculate the mean  */
	for(Player player : playerArray) {
	    if ( player.getTypeCode().equals("r")) {
		rmean += player.getFantasyPoints(); 
		rcnt++;
	    }
	    else {  
		qmean += player.getFantasyPoints();
		qcnt++;
	    }
	}
	rmean = rmean/rcnt; qmean = qmean/qcnt;
	
	/* now subtract the mean from the actual points  */
	
	for(Player player : playerArray) {
	    if ( player.getTypeCode().equals("r")) {
		player.fantasyPoints  -= rmean ;
	    }
	    else {  
		player.fantasyPoints  -= qmean ;
	    }
	}

	return 0 ;
    }
}

