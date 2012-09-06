
/* For Football quarterback */
public class FootballQuarterBack extends Player {
    public String typeCode;
    public int passCompletions;
    public int passAttempts;
    public int passingYards;
    public int interceptions;
    public int noOfTouchDowns;

    public String getTypeCode() { return typeCode;};
    public int getPassCompletions() { return passCompletions;};
    public int getPassAttempts() { return passAttempts;};
    public int getPassingYards() { return passingYards;};
    public int getInterceptions() { return interceptions;};
    public int getNoOfTouchDowns() { return noOfTouchDowns;};
  
    public void printData() {
	System.out.printf("Player %d,(1 pt * %d yards) + (6pts * %d td) - (1 pt * %d interceptions) = %d fantasy points, ranking = %d \n",playerId,passingYards,noOfTouchDowns,interceptions,fantasyPoints,ranking)  ;
    }
    public int compareTo(Player pl) {     // needed for sorting for ranking
	return pl.fantasyPoints - fantasyPoints ;
    } ;

}
