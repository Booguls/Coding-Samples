/*
    Kevin Ramirez
    Amlan Chatterjee
    CSC 311-01
    
    This program is a team drafting simulator in which 8 teams draft players at random based on order of winning percentage.
    The program drafts the players from a pool of 32 players and assigns them to teams in order until no players remain.
    Players are divided into four rounds, so teams can only select players at a certain round in order.
 */
public class TeamDraftingSimulation {

    public static void main(String[] args) {
        Team[] teams = new Team[8];
        int[] players = new int[32];   //32 players which store what round they'll be selected.

        initializeTeams(teams);     //Initialize the 8 teams.
        assignWinPercentages(teams);    //Assigns winning percentages at random.
        teams = sortTeamsByPercent(teams);  //Organizes teams from lowest to highest win percentage.
        players = assignPlayerRounds(players);  //Assigns each player the round they'll be selected.
        roundSelect(teams, players);    //Assign players randomly per round to the teams.
        printTeams(teams);      //Prints out each team in teams.
    }

    public static void initializeTeams(Team[] teams) {
        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team("Team " + (i + 1)); //Initialize 8 teams with unique names.
        }
    }

    public static void assignWinPercentages(Team[] teams) {
        for (Team team : teams) {
            double tmp = Math.random() * 100;   //Obtain raw winning percentage.
            int tmp2 = (int) (tmp * 100);       //Cut off after two decimal places.
            team.setWinningPercent((double) tmp2 / 100); //Assigns winning percentage to each team with two decimal places.
        }
    }

    public static Team[] sortTeamsByPercent(Team[] teams) {
        //Sort the teams from lowest to highest win percentage.
        for (int i = 0; i < teams.length; i++) {    //Nested loop iterates through the array and sorts them through comparison.
            for (int j = i + 1; j < teams.length; j++) {
                if (teams[j].getWinningPercent() < teams[i].getWinningPercent()) {  //Perform the swap
                    Team temp = teams[i];
                    teams[i] = teams[j];
                    teams[j] = temp;
                }
            }
        }

        //Print teams in order of winning percentage.
        for (int i = 0; i < teams.length; i++) {
            System.out.println(teams[i].getName() + " win percentage: " + teams[i].getWinningPercent());
        }
        System.out.println();

        return teams;
    }

    public static int[] assignPlayerRounds(int[] players) {
        for (int i = 0; i < players.length; i++) {
            players[i] = (int) (Math.random() * 4 + 1); //Assigns a round to each player from 1-4 at random.
        }
        return players;
    }

    public static void roundSelect(Team[] teams, int[] players) {
        int currentRound = 1;   //Counter to determine the current round.
        int teamPointer = 0;    //Pointer to keep track which team is selecting.

        while (currentRound <= 4) { //Prevents while loop from exceeding 4 rounds.
            int currentRoundPlayersCounter = 0; //Create a counter which determines the amount of current round players present.
            for (int i = 0; i < players.length; i++) {
                if (players[i] == currentRound) {
                    currentRoundPlayersCounter++;   //Increment when a player of the current round is present.
                }
            }
            int[] currentRoundPlayers = new int[currentRoundPlayersCounter];    //Create an array which stores the current round players.
            for (int i = 0, arrPointer = 0; i < players.length && arrPointer < currentRoundPlayersCounter; i++) {
                if (players[i] == currentRound) {
                    currentRoundPlayers[arrPointer] = i;     //Store the current round player in currentRoundPlayers.
                    arrPointer++;                           //Increment array pointer.
                }
            }

            for (int i = 0; i < currentRoundPlayers.length; i++) {  //Cycle through the current round's player.
                int selectedPlayer = (int) (Math.random() * currentRoundPlayers.length);    //Select a player within the current round at random.
                if (currentRoundPlayers[selectedPlayer] == -1) {    //Checks whether the selected player is already selected.
                    i--;
                    continue;
                }
                teams[teamPointer].addTeamMember("Player #" + currentRoundPlayers[selectedPlayer]); //Assign the selected player to the team.
                System.out.println("Round " + currentRound + ": " + teams[teamPointer].getName() + " selected Player #" + currentRoundPlayers[selectedPlayer]);
                currentRoundPlayers[selectedPlayer] = -1;   //Flag the selected player as selected.
                if (teamPointer >= teams.length - 1) {     //Resets the teamPointer to 0 when it reaches 7.
                    teamPointer = 0;
                    continue;
                }
                teamPointer++;
            }

            currentRound++; //Moves on to the next selection round.
            System.out.println();
        }
    }

    public static void printTeams(Team[] teams) {
        for (int i = 0; i < teams.length; i++) {    //Prints each team using the toString method.
            System.out.println(teams[i]);
        }
    }
}
