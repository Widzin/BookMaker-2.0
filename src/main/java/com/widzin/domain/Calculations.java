package com.widzin.domain;

import com.widzin.services.GameService;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

	private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

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

	public void addNumberOfAllMatches () {
		this.numberOfAllMatches ++;
	}

	public void prepareMatch(Club home, Club away) {
		this.home = home;
		this.away = away;
		setHomeDetails();
		setAwayDetails();
		setMatchesBetween();
	}

	private void setHomeDetails() {
		for (Game g: home.getGamesAtHome()){
			try {
				if (g.isPlayed() && g.getDate().after(ft.parse("2016-07-10"))) {
					numberOfMatchesAtHome++;
					goalsScoredByHomeTeam += g.getHomeScore();
					goalsLostByHomeTeam += g.getAwayScore();
				}
			} catch (ParseException e) {
				System.out.println("Something went wrong");
			}
		}
	}

	private void setAwayDetails() {
		for (Game g: away.getGamesAway()){
			try {
				if (g.isPlayed() && g.getDate().after(ft.parse("2016-07-10"))) {
					numberOfMatchesAway++;
					goalsScoredByAwayTeam += g.getAwayScore();
					goalsLostByAwayTeam += g.getHomeScore();
				}
			} catch (ParseException e) {
				System.out.println("Something went wrong");
			}
		}
	}

	private void setMatchesBetween() {
		for (Game g: home.getGamesAtHome()) {
			if (g.getAway().getId() == away.getId()) {
				if (g.isPlayed()) {
					if (g.getHomeScore() > g.getAwayScore())
						winsByHome++;
					else if (g.getHomeScore() < g.getAwayScore())
						winsByAway++;
					else
						drawsBetween++;
				}
			}
		}
		for (Game g: away.getGamesAtHome()) {
			if (g.getAway().getId() == home.getId()) {
				if (g.isPlayed()) {
					if (g.getHomeScore() > g.getAwayScore())
						winsByAway++;
					else if (g.getHomeScore() < g.getAwayScore())
						winsByHome++;
					else
						drawsBetween++;
				}
			}
		}
	}

	public double[] calculateRates() {
		double[] rates = new double[3];
		rates[0] = 0.0;
		rates[1] = 0.0;
		rates[2] = 0.0;

		double[] goalRates = ratesFromGoal();
		double[] matchRates = ratesFromMatches();

		rates[0] = 1/(goalRates[0] * WEIGHT_OF_GOALS + matchRates[0] * WEIGHT_OF_MATCHES_BETWEEN);
		rates[1] = 1/(goalRates[1] * WEIGHT_OF_GOALS + matchRates[1] * WEIGHT_OF_MATCHES_BETWEEN);
		rates[2] = 1/(goalRates[2] * WEIGHT_OF_GOALS + matchRates[2] * WEIGHT_OF_MATCHES_BETWEEN);

		return rates;
	}

	private Logger log = Logger.getLogger(Calculations.class);

	private double[] ratesFromGoal() {
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
				if (i > j)
					rates[0] += chancesOfExactScore[i][j];
				else if (i == j)
					rates[1] += chancesOfExactScore[i][j];
				else
					rates[2] += chancesOfExactScore[i][j];
			}
		}
		return rates;
	}

	private double[] ratesFromMatches(){
		double[] rates = new double[3];
		rates[0] = (double)(winsByHome + 1)/(winsByHome + winsByAway + drawsBetween + 1);
		rates[1] = (double)(drawsBetween + 1)/(winsByHome + winsByAway + drawsBetween + 1);
		rates[2] = (double)(winsByAway + 1)/(winsByHome + winsByAway + drawsBetween + 1);

		log.info("Kurs na bayern tylko z meczy: " + (double)(winsByHome + winsByAway + drawsBetween + 1)/(winsByHome + 1));
		log.info("Kurs na borussie tylko z meczy: " + (double)(winsByHome + winsByAway + drawsBetween + 1)/(drawsBetween + 1));
		log.info("Kurs na remis tylko z meczy: " + (double)(winsByHome + winsByAway + drawsBetween + 1)/(winsByAway + 1));

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
