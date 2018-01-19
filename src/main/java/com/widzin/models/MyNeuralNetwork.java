package com.widzin.models;

import org.apache.log4j.Logger;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyNeuralNetwork {

    private static MyNeuralNetwork instance = null;

    public static MyNeuralNetwork getInstance() {
        if(instance == null) {
            instance = new MyNeuralNetwork();
        }
        return instance;
    }

    private static final int MAX_VALUE = 1;
    private static final int MIN_VALUE = 0;

    private static final double BONUS_FOR_GK = 0.5;
    private static final double BONUS_FOR_DEFENDERS = 0.15;
    private static final double BONUS_FOR_MIDFIELDERS = 0.3;
    private static final double BONUS_FOR_FORWARDS = 0.5;

    private Logger log = Logger.getLogger(MyNeuralNetwork.class);

    private DataSet allMatches;
    private MultiLayerPerceptron neuralNet;
    private MomentumBackpropagation learningRule;

    public MyNeuralNetwork() {
        allMatches = new DataSet(10, 3);
        neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 10, 20, 3);

        learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.setLearningRate(0.3);
        learningRule.setMomentum(0.7);

        neuralNet.setLearningRule(learningRule);
    }

    public void teachNetwork() {
        log.info("Training my network...");
        long startTime = System.nanoTime();
        neuralNet.learn(allMatches);
        long elapsedTime = System.nanoTime() - startTime;
        double seconds = BigDecimal.valueOf(elapsedTime / 1000000000.0).doubleValue();
        log.info("Done! Time of learing: " + seconds + " seconds");
    }

    public double[] calculateAndReceiveOutputs(Match match) {
        DataSetRow dsr = new DataSetRow(normalizeInputMatch(match));
        neuralNet.setInput(dsr.getInput());
        neuralNet.calculate();
        return neuralNet.getOutput();
    }

    public void addDataRow(Match match) {
        if (allMatches.size() == 60) {
            List<DataSetRow> oldRows = allMatches.getRows().subList(0, 59);
            allMatches = new DataSet(10, 3);
            for (DataSetRow dsr: oldRows) {
                allMatches.addRow(dsr);
            }
        }
        allMatches.addRow(new DataSetRow(normalizeInputMatch(match), normalizeOutputMatch(match)));
    }

    private double[] normalizeInputMatch(Match match) {
        double[] inputsFromHomeSquad = normalizeSquad(match.getHome(), match.getHome().getClubSeason());
        double[] inputsFromAwaySquad = normalizeSquad(match.getAway(), match.getAway().getClubSeason());
        double inputFromHomePoints = normalizeClubPoints(match.getHome().getClubSeason());
        double inputFromAwayPoints = normalizeClubPoints(match.getAway().getClubSeason());

        double[] inputs = new double[inputsFromHomeSquad.length + inputsFromAwaySquad.length + 2];
        System.arraycopy(inputsFromHomeSquad, 0, inputs, 0, inputsFromHomeSquad.length);
        System.arraycopy(inputsFromAwaySquad, 0, inputs, inputsFromHomeSquad.length, inputsFromAwaySquad.length);
        inputs[inputs.length - 2] = inputFromHomePoints;
        inputs[inputs.length - 1] = inputFromAwayPoints;
        return inputs;
    }

    private double[] normalizeSquad(TeamMatchDetails teamSquad, ClubSeason club) {
        double[] lines = new double[4];
        lines[0] = normalizeGoalkeeper(teamSquad.getLineupGoalkeeper(), getPlayersFromLine(club.getPlayers(), "GK"), BONUS_FOR_GK);

        List<PlayerSeason> all10PlayersWithoutGK = new ArrayList<>();
        all10PlayersWithoutGK.addAll(teamSquad.getLineupDefense());
        all10PlayersWithoutGK.addAll(teamSquad.getLineupMidfield());
        all10PlayersWithoutGK.addAll(teamSquad.getLineupForward());

        List<PlayerSeason> choosenDefenders = new ArrayList<>();
        choosenDefenders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "RB"));
        choosenDefenders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "CB"));
        choosenDefenders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "LB"));

        List<PlayerSeason> allDefenders = new ArrayList<>();
        allDefenders.addAll(getPlayersFromLine(club.getPlayers(), "RB"));
        allDefenders.addAll(getPlayersFromLine(club.getPlayers(), "CB"));
        allDefenders.addAll(getPlayersFromLine(club.getPlayers(), "LB"));
        lines[1] = normalizeLine(choosenDefenders, allDefenders, BONUS_FOR_DEFENDERS);

        List<PlayerSeason> choosenMidfielders = new ArrayList<>();
        choosenMidfielders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "CDM"));
        choosenMidfielders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "RM"));
        choosenMidfielders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "CM"));
        choosenMidfielders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "LM"));
        choosenMidfielders.addAll(getPlayersFromLine(all10PlayersWithoutGK, "CAM"));

        List<PlayerSeason> allMidfielders = new ArrayList<>();
        allMidfielders.addAll(getPlayersFromLine(club.getPlayers(), "CDM"));
        allMidfielders.addAll(getPlayersFromLine(club.getPlayers(), "RM"));
        allMidfielders.addAll(getPlayersFromLine(club.getPlayers(), "CM"));
        allMidfielders.addAll(getPlayersFromLine(club.getPlayers(), "LM"));
        allMidfielders.addAll(getPlayersFromLine(club.getPlayers(), "CAM"));
        lines[2] = normalizeLine(choosenMidfielders, allMidfielders, BONUS_FOR_MIDFIELDERS);

        List<PlayerSeason> choosenForwards = new ArrayList<>();
        choosenForwards.addAll(getPlayersFromLine(all10PlayersWithoutGK, "RW"));
        choosenForwards.addAll(getPlayersFromLine(all10PlayersWithoutGK, "CF"));
        choosenForwards.addAll(getPlayersFromLine(all10PlayersWithoutGK, "LW"));
        choosenForwards.addAll(getPlayersFromLine(all10PlayersWithoutGK, "ST"));

        List<PlayerSeason> allForwards = new ArrayList<>();
        allForwards.addAll(getPlayersFromLine(club.getPlayers(), "RW"));
        allForwards.addAll(getPlayersFromLine(club.getPlayers(), "CF"));
        allForwards.addAll(getPlayersFromLine(club.getPlayers(), "LW"));
        allForwards.addAll(getPlayersFromLine(club.getPlayers(), "ST"));
        lines[3] = normalizeLine(choosenForwards, allForwards, BONUS_FOR_FORWARDS);

        return lines;
    }

    private List<PlayerSeason> getPlayersFromLine(List<PlayerSeason> players, String position) {
        List<PlayerSeason> playerSeasons = new ArrayList<>();
        for (PlayerSeason pl: players) {
            if (pl.getPosition().equals(position))
                playerSeasons.add(pl);
        }
        return playerSeasons;
    }

    private double normalizeGoalkeeper(PlayerSeason goalkeeper, List<PlayerSeason> allGoalkeepers, double bonus) {
        double choosenValue = goalkeeper.getValue();
        choosenValue += bonusForPlayer(goalkeeper, bonus);

        Comparator<PlayerSeason> c = (p, o) -> (-1)*p.getValue().compareTo(o.getValue());
        allGoalkeepers.sort(c);

        double maxValue = allGoalkeepers.get(0).getValue();
        maxValue += bonusForPlayer(allGoalkeepers.get(0), bonus);

        double minValue = allGoalkeepers.get(allGoalkeepers.size() - 1).getValue();
        minValue += bonusForPlayer(allGoalkeepers.get(allGoalkeepers.size() - 1), bonus);

        return ((choosenValue - minValue)/(maxValue - minValue))*(MAX_VALUE - MIN_VALUE) + MIN_VALUE;
    }

    private double bonusForPlayer(PlayerSeason playerSeason, double bonus) {
        if (playerSeason.getMatches() > 5) {
            if (playerSeason.getPosition().equals("GK")) {
                if (playerSeason.getCleanSheets()/playerSeason.getMatches() > bonus)
                    return 5000000.0;
            } else {
                if (playerSeason.getGoals()/playerSeason.getMatches() > bonus)
                    return 5000000.0;
            }
        }
        return 0.0;
    }

    private double normalizeLine(List<PlayerSeason> choosenPlayers, List<PlayerSeason> allPlayersInLine, double bonus) {
        double choosenValue = 0.0;
        for (PlayerSeason pl: choosenPlayers) {
            choosenValue += pl.getValue();
            choosenValue += bonusForPlayer(pl, bonus);
        }

        Comparator<PlayerSeason> sortFromBestToWorst = (p, o) -> (-1)*p.getValue().compareTo(o.getValue());
        allPlayersInLine.sort(sortFromBestToWorst);
        double maxValue = getFullAmount(choosenPlayers, allPlayersInLine, bonus);

        Comparator<PlayerSeason> sortFromWorstToBest = (p, o) -> p.getValue().compareTo(o.getValue());
        allPlayersInLine.sort(sortFromWorstToBest);
        double minValue = getFullAmount(choosenPlayers, allPlayersInLine, bonus);

        return ((choosenValue - minValue)/(maxValue - minValue))*(MAX_VALUE - MIN_VALUE) + MIN_VALUE;
    }

    private double getFullAmount(List<PlayerSeason> choosenPlayers, List<PlayerSeason> allPlayersInLine, double bonus) {
        double value = 0.0;
        for (int i = 0; i < choosenPlayers.size(); i++) {
            value += allPlayersInLine.get(i).getValue();
            value += bonusForPlayer(allPlayersInLine.get(i), bonus);
        }
        return value;
    }

    private double normalizeClubPoints(ClubSeason clubSeason) {
        int actualPoints = clubSeason.getPoints();
        int minPoints = clubSeason.getMatches() * 0;
        int maxPoints = clubSeason.getMatches() * 3;

        if (maxPoints != 0.0)
            return ((actualPoints - minPoints)/(maxPoints - minPoints))*(MAX_VALUE - MIN_VALUE) + MIN_VALUE;
        else
            return 0.0;
    }

    private double[] normalizeOutputMatch(Match match) {
        double[] outputs = new double[3];
        if (match.getHome().getGoals() > match.getAway().getGoals()) {
            outputs[0] = 1.0;
            outputs[1] = 0.0;
            outputs[2] = 0.0;
        } else if (match.getHome().getGoals() < match.getAway().getGoals()) {
            outputs[0] = 0.0;
            outputs[1] = 0.0;
            outputs[2] = 1.0;
        } else {
            outputs[0] = 0.0;
            outputs[1] = 1.0;
            outputs[2] = 0.0;
        }
        return outputs;
    }

    public DataSet getAllMatches() {
        return allMatches;
    }
}
