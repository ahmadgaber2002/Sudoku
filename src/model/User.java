package model;

import java.util.PriorityQueue;
import java.io.Serializable;


public class User implements Serializable{
    private int winStreak;
    private PriorityQueue<Integer> bestTimes;
    private int totalGamesPlayed;
    private int totalWins;

    public User() {
        this.winStreak = 0;
        this.bestTimes = new PriorityQueue<>();
        this.totalGamesPlayed = 0;
        this.totalWins = 0;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void incrementWinStreak() {
        winStreak++;
    }

    public void resetWinStreak() {
        winStreak = 0;
    }

    public PriorityQueue<Integer> getBestTimes() {
        return bestTimes;
    }

    public void addBestTime(Integer time) {
        bestTimes.add(time);
    }

    public Integer getBestTime() {
        return bestTimes.peek();
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void incrementTotalGamesPlayed() {
        totalGamesPlayed++;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void incrementTotalWins() {
        totalWins++;
    }
	/**
    * Determines the user's percentage winrate.
    *
    * @param The winpercentage as a double value.
    */
    public double getWinRate() {
        if (totalGamesPlayed == 0) {
            return 0;
        }
        return ((double) totalWins) / totalGamesPlayed;
    }
	 /**
     * After a game has been won, it updates the user's statistics.
     *
     * @param time The completion time of the game, in milliseconds.
     */
    public void gameWon(Integer time) {
        incrementTotalWins();
        incrementTotalGamesPlayed();
        incrementWinStreak();
        addBestTime(time);
    }
}
