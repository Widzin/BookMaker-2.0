package com.widzin.bootstrap;

import com.widzin.bootstrap.loaders.Links;
import com.widzin.bootstrap.loaders.services.MainLoadService;
import com.widzin.bootstrap.loaders.services.MatchesLoadService;
import com.widzin.bootstrap.loaders.services.PlayersAndClubLoadService;
import com.widzin.models.*;
import com.widzin.services.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final static Integer POINTS_FOR_WIN = 3;
    private final static Integer POINTS_FOR_DRAW = 1;
    private final static Integer POINTS_FOR_LOST = 0;

    private ClubService clubService;
    private ClubSeasonService clubSeasonService;
    private MatchService matchService;
    private MatchEventService matchEventService;
    private TeamMatchDetailsService teamMatchDetailsService;
    private SeasonService seasonService;
    private UserService userService;
    private RoleService roleService;
    private PlayerService playerService;
    private PlayerSeasonService playerSeasonService;
    //-----------Singletons ----------------------
    private Calculations calculations;
    private MyNeuralNetwork myNeuralNetwork;

    //----------- Loading services ---------------
    private MainLoadService loadService;
    private MatchesLoadService matchesLoadService;
    private PlayersAndClubLoadService playersAndClubLoadService;

	private Logger log = Logger.getLogger(SpringJpaBootstrap.class);

	@Autowired
    public void setClubService(ClubService clubService) {
        this.clubService = clubService;
    }

    @Autowired
    public void setClubSeasonService(ClubSeasonService clubSeasonService) {
        this.clubSeasonService = clubSeasonService;
    }

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @Autowired
    public void setMatchEventService(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    @Autowired
    public void setTeamMatchDetailsService(TeamMatchDetailsService teamMatchDetailsService) {
        this.teamMatchDetailsService = teamMatchDetailsService;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setPlayerSeasonService(PlayerSeasonService playerSeasonService) {
        this.playerSeasonService = playerSeasonService;
    }

    @Autowired
    public void setLoadService(MainLoadService loadService) {
        this.loadService = loadService;
    }

    @Autowired
    public void setMatchesLoadService(MatchesLoadService matchesLoadService) {
        this.matchesLoadService = matchesLoadService;
    }

    @Autowired
    public void setPlayersAndClubLoadService(PlayersAndClubLoadService playersAndClubLoadService) {
        this.playersAndClubLoadService = playersAndClubLoadService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        myNeuralNetwork = MyNeuralNetwork.getInstance();
        loadPlayers();
        loadMatches();
        loadLogos();
        saveToDatabaseAndGiveDataToNetwork();
        loadUsers();
        loadRoles();
        assignUsersToDefaultRoles();
        setCalculationsOnLoad();
        log.info("Amount of dataRows: " + myNeuralNetwork.getAllMatches().getRows().size());
        myNeuralNetwork.teachNetwork();
	}

    private void loadPlayers() {
        playersAndClubLoadService.startParsing(Links.PLAYERS_2015_2016, loadService);
        playersAndClubLoadService.startParsing(Links.PLAYERS_2016_2017, loadService);
        playersAndClubLoadService.startParsing(Links.PLAYERS_2017_2018, loadService);
    }

    private void loadMatches() {
        matchesLoadService.startParsing(Links.MATCHES_2015_2016, loadService);
        matchesLoadService.startParsing(Links.MATCHES_2016_2017, loadService);
        matchesLoadService.startParsing(Links.MATCHES_2017_2018, loadService);
    }

    private void loadLogos() {
        playersAndClubLoadService.addLogos(Links.LOGOS, loadService);
    }

    private void saveToDatabaseAndGiveDataToNetwork() {
        for (Club club : loadService.getClubs()) {
            clubService.saveClub(club);
            log.info("Saved club id: " + club.getId());
        }
        for (Player player: loadService.getPlayers()) {
            playerService.savePlayer(player);
            log.info("Saved player id: " + player.getId());
        }
        for (Season season: loadService.getSeasons()) {
            for (ClubSeason clubSeason: season.getClubs()) {
                for (PlayerSeason playerSeason: clubSeason.getPlayers()) {
                    playerSeasonService.savePlayerSeason(playerSeason);
                    log.info("Saved playerSeason id: " + playerSeason.getId());
                }
                clubSeasonService.saveClubSeason(clubSeason);
                log.info("Saved clubSeason id: " + clubSeason.getId());
            }
            for (Match match : season.getMatches()) {
                if (!season.getPeriod().startsWith("2015"))
                    myNeuralNetwork.addDataRow(match);
                resolveMatch(match);
                saveTeamMatchDetailsToDatabase(match.getHome());
                saveTeamMatchDetailsToDatabase(match.getAway());
                matchService.saveMatch(match);
                log.info("Saved match id: " + match.getId());
            }
            for (ClubSeason clubSeason: season.getClubs()) {
                clubSeasonService.saveClubSeason(clubSeason);
                log.info("Saved again clubSeason id: " + clubSeason.getId());
            }
            seasonService.saveSeason(season);
            log.info("Saved season id: " + season.getId());
        }
    }

    private void resolveMatch(Match match) {
        //-------------- Adding stats to goalkeepers ---------
        if (match.getHome().getGoals() == 0)
            match.getAway().getLineupGoalkeeper().addCleanSheets();
        if (match.getAway().getGoals() == 0)
            match.getHome().getLineupGoalkeeper().addCleanSheets();

        //-------------- Adding stats to clubs ---------------

        if (match.getHome().getGoals() > match.getAway().getGoals()) {
            match.getHome().getClubSeason().addWin();
            match.getAway().getClubSeason().addLose();

            match.getHome().getClubSeason().addPoints(POINTS_FOR_WIN);
            match.getAway().getClubSeason().addPoints(POINTS_FOR_LOST);
        } else if (match.getHome().getGoals() < match.getAway().getGoals()) {
            match.getHome().getClubSeason().addLose();
            match.getAway().getClubSeason().addWin();

            match.getHome().getClubSeason().addPoints(POINTS_FOR_LOST);
            match.getAway().getClubSeason().addPoints(POINTS_FOR_WIN);
        } else {
            match.getHome().getClubSeason().addDraw();
            match.getAway().getClubSeason().addDraw();

            match.getHome().getClubSeason().addPoints(POINTS_FOR_DRAW);
            match.getAway().getClubSeason().addPoints(POINTS_FOR_DRAW);
        }

        match.getHome().getClubSeason().addMatch();
        match.getAway().getClubSeason().addMatch();

        match.getHome().getClubSeason().addScoredGoals(match.getHome().getGoals());
        match.getAway().getClubSeason().addScoredGoals(match.getAway().getGoals());

        match.getHome().getClubSeason().addLostGoals(match.getAway().getGoals());
        match.getAway().getClubSeason().addLostGoals(match.getHome().getGoals());

        match.getHome().getClubSeason().setBilans();
        match.getAway().getClubSeason().setBilans();
    }

    private void saveTeamMatchDetailsToDatabase (TeamMatchDetails teamMatchDetails) {
        List<MatchEvent> matchEvents = new ArrayList<>();

        matchEvents.addAll(teamMatchDetails.getGoalDetails());
        matchEvents.addAll(teamMatchDetails.getSubDetails());
        matchEvents.addAll(teamMatchDetails.getYellowCardDetails());
        matchEvents.addAll(teamMatchDetails.getRedCardDetails());

        for (MatchEvent matchEvent: matchEvents) {
            matchEventService.saveMatchEvent(matchEvent);
            log.info("Saved matchEvent id: " + matchEvent.getId());
        }
        teamMatchDetailsService.saveTeamMatchDetails(teamMatchDetails);
        log.info("Saved teamMatchDetails id: " + teamMatchDetails.getId());
    }

    private void loadUsers() {
        loadUser("admin");
        loadUser("user");
    }

    private void loadUser(String string) {
        User user = new User();
        user.setUsername(string);
        user.setPassword(string);
        userService.saveOrUpdate(user);
    }

    private void loadRoles() {
        loadRole("ADMIN");
        loadRole("USER");
    }

    private void loadRole(String string) {
        Role role = new Role();
        role.setRole(string);
        roleService.saveOrUpdate(role);
        log.info("Saved role " + role.getRole());
    }

    private void assignUsersToDefaultRoles() {
        assingUsersToRole("USER");
        assingUsersToRole("ADMIN");
    }

    private void assingUsersToRole(String string) {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<User> users = (List<User>) userService.listAll();

        roles.forEach(role -> {
            if (role.getRole().equalsIgnoreCase(string)) {
                users.forEach(user -> {
                    if (user.getUsername().equalsIgnoreCase(string)) {
                        user.addRole(role);
                        user.setMainRole(role.getRole());
                        userService.saveOrUpdate(user);
                    }
                });
            }
        });
    }

    private void setCalculationsOnLoad() {
        calculations = Calculations.getInstance();
        for (Season season: loadService.getSeasons()) {
            if (!season.getPeriod().startsWith("2015")) {
                for (Match match: season.getMatches()) {
                    log.info("Calculating match nr. " + match.getId());
                    if (match.isPlayed()) {
                        calculations.addAllGoalsScoredAtHome(match.getHome().getGoals());
                        calculations.addAllGoalsLostAtHome(match.getAway().getGoals());
                        calculations.addNumberOfAllMatches();
                    }
                }
            }
        }
    }
}


