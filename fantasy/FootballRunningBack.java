
/* Calulating Fantasy points can be extended to any sports 
by declaring a new playerclass and new algo to score the points */
public class FootballRunningBack extends Player {
    public String typeCode;
    public int rushAttempts;
    public int rushYards;
    public int noOfTouchDowns;

    public String getTypeCode() { return typeCode;};
    public int getRushAttempts() { return rushAttempts;};
    public int getRushYards() { return rushYards;};
    public int getNoOfTouchDowns() { return noOfTouchDowns;};

    public void printData() {
	System.out.printf("Player %d,(2 pts * %d yards) + (6pts * %d td) = %d fantasy points, ranking = %d  \n",playerId,rushYards,noOfTouchDowns,fantasyPoints,ranking)  ;
    }
    public int compareTo(Player pl) {     // needed for sorting for ranking
	return pl.fantasyPoints - fantasyPoints ;
    } ;
}
