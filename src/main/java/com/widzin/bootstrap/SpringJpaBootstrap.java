package com.widzin.bootstrap;

import com.widzin.bootstrap.loaders.Links;
import com.widzin.bootstrap.loaders.services.MainLoadService;
import com.widzin.bootstrap.loaders.services.MatchesLoadService;
import com.widzin.bootstrap.loaders.services.PlayersAndClubLoadService;
import com.widzin.model.*;
import com.widzin.services.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ClubService clubService;
    private Club2Service club2Service;
    private UserService userService;
    private RoleService roleService;
    private GameService gameService;
    private PlayerService playerService;
    private PlayerSeasonService playerSeasonService;
    private Calculations calculations;

    //----------- Loading services ---------------
    private MainLoadService loadService;
    private MatchesLoadService matchesLoadService;
    private PlayersAndClubLoadService playersAndClubLoadService;

	private Logger log = Logger.getLogger(SpringJpaBootstrap.class);
	private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

	//private final int MATCHES_IN_ONE_SEASON = 34;

    @Autowired
	public void setClubService (ClubService clubService) {
		this.clubService = clubService;
	}

	@Autowired
    public void setClub2Service(Club2Service club2Service) {
        this.club2Service = club2Service;
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
	public void setGameService (GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setPlayerSeasonService(PlayerSeasonService playerSeasonService) {
        this.playerSeasonService = playerSeasonService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadService = new MainLoadService();
        matchesLoadService = new MatchesLoadService();
        playersAndClubLoadService = new PlayersAndClubLoadService();

        loadPlayers();
        loadMatches();
        loadLogos();
        saveToDatabase();
        //loadLogos();
    	/*loadClubs();
		loadMatches(2015, 2016);
		loadMatches(2016, 2017);*/
        loadUsers();
        loadRoles();
        /*assignUsersToDefaultRoles();
		calculations = Calculations.getInstance();
		for (Game g: gameService.findAllGames()) {
			log.info("Sprawdzam mecz nr. " + g.getId());
			try {
				if (g.getDate().after(ft.parse("2016-07-10")) && g.isPlayed()) {
					calculations.addAllGoalsScoredAtHome(g.getHomeScore());
					calculations.addAllGoalsLostAtHome(g.getAwayScore());
					calculations.addNumberOfAllMatches();
				}
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		}
		log.info("Wszystkie bramki strzelone: " + calculations.getAllGoalsScoredAtHome());
		log.info("Wszystkie bramki stracone: " + calculations.getAllGoalsLostAtHome());*/
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

    private void saveToDatabase() {
        for (Club2 club2: loadService.getClubs()) {
            club2Service.saveClub2(club2);
            log.info("Saved club id: " + club2.getId());
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
            }
        }
    }

    /*private void loadMatches(int from, int to){
		String path = "src\\main\\resources\\static\\data\\Terminarz_" + from + "_" + to + ".data";
		try {
			File file = new File(path);

			String content = new String(Files.readAllBytes(file.toPath()));
			String[] matchdays = content.split("Kolejka");
			String[] newMatchdays = new String[matchdays.length - 1];

			for (int i = 0; i < newMatchdays.length; i++) {
				newMatchdays[i] = matchdays[i + 1];
			}

			for (String m: newMatchdays) {
				String[] enter = m.split("\n");
				for (int i = 0; i < (enter.length - 1); i++) {
					enter[i] = enter[i + 1];
				}

				String[] newEnters = new String[enter.length - 1];
				for (int i = 0; i < newEnters.length; i++) {
					newEnters[i] = enter[i].trim();
				}
				addMatchToJavaClass(newEnters);
			}
		} catch (Exception e) {
			log.info("Cannot find file");
		}
	}

	private void addMatchToJavaClass(String[] strings) {
		Date date = new Date();

		for (String s: strings) {
			if (s.length() > 0 ) {
				if (Character.isDigit(s.charAt(0))) {
					String[] temp = s.split(":");
					String[] dates = temp[0].split("\\.");
					int day = Integer.parseInt(dates[0]);
					int month = Integer.parseInt(dates[1]);
					int year = Integer.parseInt(dates[2]);
					try {
						date = ft.parse(year + "-" + month + "-" + day);
					} catch (ParseException e) {
						System.out.println("Cannot convert to date");
					}
				} else {
					String[] details = s.split(" ");
					Club home = findByPieceOfName(details[0]);
					Club away = findByPieceOfName(details[2]);
					String[] score = details[3].split(":");
					int homeScore = Integer.parseInt(score[0]);
					int awayScore = Integer.parseInt(score[1]);
					Game match = new Game(home, away, homeScore, awayScore, date);
					gameService.saveMatch(match);
					log.info("Saved match nr. " + match.getId());
					updateClubs(match);
				}
			}
		}
	}

	private Club findByPieceOfName(String piece) {
		for (Club c: clubService.listAllClubs()) {
			if (c.getName().contains(piece)) {
				return c;
			}
		}
		return null;
	}*/

	private void updateClubs(Game match) {
		for (Club c: clubService.listAllClubs()) {
			if (c.getName().equals(match.getHome().getName())) {
				c.addGameAtHome(match);
			} else if (c.getName().equals(match.getAway().getName())) {
				c.addGameAway(match);
			}
		}

		/*Club home = clubService.getClubById(match.getHome().getId());
		Club away = clubService.getClubById(match.getAway().getId());
		if (match.getHomeScore() > match.getAwayScore()){
			home.addWins();
			away.addLoses();
		} else if (match.getHomeScore() < match.getAwayScore()) {
			home.addLoses();
			away.addWins();
		} else {
			home.addDraws();
			away.addDraws();
		}
		home.addScoredGoals(match.getHomeScore());
		home.addLostGoals(match.getAwayScore());
		away.addScoredGoals(match.getAwayScore());
		away.addLostGoals(match.getHomeScore());
		home.updateStats();
		away.updateStats();
		clubService.saveClub(home);
		clubService.saveClub(away);*/
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
}


