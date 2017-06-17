package com.widzin.domain;

public class Calculations {
	private static Calculations instance = null;

	public static Calculations getInstance() {
		if(instance == null) {
			instance = new Calculations();
		}
		return instance;
	}

	//variables for calculating
	final double WEIGHT_OF_GOALS = 0.7;
	final double WEIGHT_OF_MATCHES_BETWEEN = 0.3;
	final int MAX_GOALS = 6;

	//league details
	private int allGoalsScoredAtHome;
	private int allGoalsLostAtHome;
	private int numberOfAllMatches;

	//club for which would be calculated rates
	private Club home;
	private Club away;

	//details from this clubs
	private int goalsScoredByHomeTeam;
	private int goalsScoredByAwayTeam;
	private int goalsLostByHomeTeam;
	private int goalsLostByAwayTeam;
	private int numberOfMatchesAtHome;
	private int numberOfMatchesAway;

	//past results between those teams
	private int winsByHome;
	private int winsByAway;
	private int drawsBetween;

	protected Calculations() {
		allGoalsScoredAtHome = 0;
		allGoalsLostAtHome = 0;
		numberOfAllMatches = 0;

		goalsScoredByHomeTeam = 0;
		goalsScoredByAwayTeam = 0;
		goalsLostByHomeTeam = 0;
		goalsLostByAwayTeam = 0;
		numberOfMatchesAtHome = 0;
		numberOfMatchesAway = 0;

		winsByHome = 0;
		winsByAway = 0;
		drawsBetween = 0;
	}

	public void addAllGoalsScoredAtHome (int allGoalsScoredAtHome) {
		this.allGoalsScoredAtHome += allGoalsScoredAtHome;
	}

	public void addAllGoalsLostAtHome (int allGoalsLostAtHome) {
		this.allGoalsLostAtHome += allGoalsLostAtHome;
	}

	public void addNumberOfAllMatches (int numberOfAllMatches) {
		this.numberOfAllMatches += numberOfAllMatches;
	}

	public void setHome (Club home) {
		this.home = home;
	}

	public void setAway (Club away) {
		this.away = away;
	}

	public void addGoalsScoredByHomeTeam (int goalsScoredByHomeTeam) {
		this.goalsScoredByHomeTeam += goalsScoredByHomeTeam;
	}

	public void addGoalsScoredByAwayTeam (int goalsScoredByAwayTeam) {
		this.goalsScoredByAwayTeam += goalsScoredByAwayTeam;
	}

	public void addGoalsLostByHomeTeam (int goalsLostByHomeTeam) {
		this.goalsLostByHomeTeam += goalsLostByHomeTeam;
	}

	public void addGoalsLostByAwayTeam (int goalsLostByAwayTeam) {
		this.goalsLostByAwayTeam += goalsLostByAwayTeam;
	}

	public void addNumberOfMatchesAtHome (int numberOfMatchesAtHome) {
		this.numberOfMatchesAtHome += numberOfMatchesAtHome;
	}

	public void addNumberOfMatchesAway (int numberOfMatchesAway) {
		this.numberOfMatchesAway += numberOfMatchesAway;
	}

	public void addWinsByHome (int winsByHome) {
		this.winsByHome += winsByHome;
	}

	public void addWinsByAway (int winsByAway) {
		this.winsByAway += winsByAway;
	}

	public void addDrawsBetween (int drawsBetween) {
		this.drawsBetween += drawsBetween;
	}

	public double[] calculateRates() {
		double[] rates = new double[3];
		rates[0] = 0.0;
		rates[1] = 0.0;
		rates[2] = 0.0;

		double averageScoredByHomeTeam = (double) goalsScoredByHomeTeam/numberOfMatchesAtHome;
		double averageLostByHomeTeam = (double) goalsLostByHomeTeam/numberOfMatchesAtHome;

		double averageScoredByAwayTeam = (double) goalsScoredByAwayTeam/numberOfMatchesAway;
		double averageLostByAwayTeam = (double) goalsLostByAwayTeam/numberOfMatchesAway;

		double averageScoredAtHome = (double) allGoalsScoredAtHome/numberOfAllMatches;
		double averageLostAtHome = (double) allGoalsLostAtHome/numberOfAllMatches;
		double averageScoredAway = averageLostAtHome;
		double averageLostAway = averageScoredAtHome;

		double powerAttackHomeTeam = averageScoredByHomeTeam/averageScoredAtHome;
		double powerAttackAwayTeam = averageScoredByAwayTeam/averageScoredAway;
		double powerDefenseHomeTeam = averageLostByHomeTeam/averageLostAtHome;
		double powerDefenseAwayTeam = averageLostByAwayTeam/averageLostAway;

		double lambdaHomeTeam = powerAttackHomeTeam * powerDefenseAwayTeam;
		double lambdaAwayTeam = powerAttackAwayTeam * powerDefenseHomeTeam;

		double[] chancesForHomeTeam = calculateChancesForGoals(lambdaHomeTeam);
		double[] chancesForAwayTeam = calculateChancesForGoals(lambdaAwayTeam);

		double[][] chancesOfExactScore = new double[MAX_GOALS][MAX_GOALS];

		for (int i = 0; i < MAX_GOALS; i++){
			for (int j = 0; j < MAX_GOALS; j++){
				chancesOfExactScore[i][j] = chancesForHomeTeam[i] * chancesForAwayTeam[j];
				if (i == j)
					rates[0] += chancesOfExactScore[i][j];
				else if (i < j)
					rates[1] += chancesOfExactScore[i][j];
				else
					rates[2] += chancesOfExactScore[i][j];
			}
		}

		rates[0] = 1/(rates[0] * WEIGHT_OF_GOALS + (drawsBetween + 1) * WEIGHT_OF_MATCHES_BETWEEN/(winsByHome + winsByAway + drawsBetween));
		rates[1] = 1/(rates[1] * WEIGHT_OF_GOALS + (winsByHome + 1) * WEIGHT_OF_MATCHES_BETWEEN/(winsByHome + winsByAway + drawsBetween));
		rates[2] = 1/(rates[2] * WEIGHT_OF_GOALS + (winsByAway + 1) * WEIGHT_OF_MATCHES_BETWEEN/(winsByHome + winsByAway + drawsBetween));

		return rates;
	}

	public double[] calculateChancesForGoals(double lambda) {
		double[] temp = new double[MAX_GOALS];
		for (int i = 0; i < temp.length; i++){
			temp[i] = Math.pow(lambda, i) * Math.pow(Math.E, (-lambda))/factorial(i);
		}
		return temp;
	}

	public int factorial(int n) {
		int fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	public void resetClubs() {
		home = null;
		away = null;

		goalsScoredByHomeTeam = 0;
		goalsScoredByAwayTeam = 0;
		goalsLostByHomeTeam = 0;
		goalsLostByAwayTeam = 0;
		numberOfMatchesAtHome = 0;
		numberOfMatchesAway = 0;

		winsByHome = 0;
		winsByAway = 0;
		drawsBetween = 0;
	}
}
