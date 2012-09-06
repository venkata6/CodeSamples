
/* Player class - abstract baseclass  so that i can use the inheritance for quaerterback,runningback etc */
public abstract class  Player implements Comparable<Player> {
    public int playerId ;
    public int fantasyPoints;
    public int ranking;
    
    public int getPlayerId() { return playerId;};  
    public int getFantasyPoints() { return fantasyPoints;}; 
    public int getRanking() { return ranking;};
    public String isHighRank() { 
	if (ranking == 1){ return "true";}
	else{ return "false";}};

    abstract public void printData() ; 
    abstract public int compareTo(Player pl); /* needed for sorting for ranking */
    abstract public String getTypeCode();
    abstract public int getNoOfTouchDowns();
    public int getRushAttempts() { return 0;} ;
    public int getRushYards() { return 0;} ;
    public int getPassCompletions() { return 0;};
    public int getPassAttempts() { return 0;};
    public int getPassingYards() { return 0;};
    public int getInterceptions() { return 0;};




} 
