/*
    Kevin Ramirez
    Amlan Chatterjee
    CSC 311-01

    Team class acts as an object to hold team information which is auto populated in TeamDraftingSimulation. 
 */
public class Team {

    private String name;    //Name of the team.
    private double winningPercent;  //Winning percentage from previous season.
    private String[] players;  //Players selected during selecting round.
    private final int NUMBER_OF_PLAYERS = 4;    //Maximum number of players to a team.
    private int teamCapacity = 0;   //Counter to keep track of a team's capacity.

    public Team(String name) {
        this.name = name;
        players = new String[NUMBER_OF_PLAYERS];
    }

    public void addTeamMember(String player) {
        /*       if (teamCapacity == players.length - 1) {  //Unneccessary check due to each team already restricted on player selecting in roundSelect().
            System.out.println("Error, team capacity full!");
            return;
        }
         */
        this.players[teamCapacity] = player;
        teamCapacity++;
    }

    public void setWinningPercent(double winningPercent) {
        this.winningPercent = winningPercent;
    }

    public String getName() {
        return name;
    }

    public double getWinningPercent() {
        return winningPercent;
    }

    public int getTeamCapacity() {
        return teamCapacity;
    }

    public String toString() {
        String s = name + "(Win Rate: " + winningPercent + ", Players[";
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            if (i == 3) {
                s += players[i];
                continue;
            }
            s += (players[i] + ", ");
        }
        s += "])";
        return s;
    }
}
