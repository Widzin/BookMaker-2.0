package com.widzin.bootstrap;

import com.widzin.bootstrap.loaders.Links;
import com.widzin.bootstrap.loaders.services.MainLoadService;
import com.widzin.bootstrap.loaders.services.MatchesLoadService;
import com.widzin.bootstrap.loaders.services.PlayersAndClubLoadService;
import com.widzin.model.*;
import com.widzin.services.ClubService;
import com.widzin.services.GameService;
import com.widzin.services.RoleService;
import com.widzin.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ClubService clubService;
    private UserService userService;
    private RoleService roleService;
    private GameService gameService;
    private Calculations calculations;

    //----------- Loading services ---------------
    private MainLoadService loadService;
    private MatchesLoadService matchesLoadService;
    private PlayersAndClubLoadService playersAndClubLoadService;

	private Logger log = Logger.getLogger(SpringJpaBootstrap.class);
	private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

	//private final int MATCHES_IN_ONE_SEASON = 34;

    @Autowired
	public void setClubRepository (ClubService clubService) {
		this.clubService = clubService;
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
	public void setGameRepository (GameService gameService) {
		this.gameService = gameService;
	}

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadService = new MainLoadService();
        matchesLoadService = new MatchesLoadService();
        playersAndClubLoadService = new PlayersAndClubLoadService();

        loadPlayers();
        loadMatches();
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

	/*private void loadClubs(){
        loadClub("Bayern Monachium",
                "http://www.bettingexpert.com/assets/images/content/sports/football/germany/Bundesliga2015/55x55xBayernmunich.png.pagespeed.ic.UFdQXp7qtu.png",
                23, true, 566.15);

        loadClub("Borussia Dortmund",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Borussia_Dortmund_logo.svg/500px-Borussia_Dortmund_logo.svg.png",
                28, true, 376.35);

        loadClub("Borussia Monchengladbach",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Borussia_M%C3%B6nchengladbach_logo.svg/2000px-Borussia_M%C3%B6nchengladbach_logo.svg.png",
                30, true,163.55);

        loadClub("Bayer Leverkusen",
                "https://upload.wikimedia.org/wikipedia/en/thumb/5/59/Bayer_04_Leverkusen_logo.svg/2400px-Bayer_04_Leverkusen_logo.svg.png",
                27, true, 273.95);

        loadClub("FC Schalke 04",
                "https://upload.wikimedia.org/wikipedia/commons/9/97/FC_Schalke_04_Logo.png",
                32, true, 216.63);

        loadClub("TSG 1899 Hoffenheim",
                "https://upload.wikimedia.org/wikipedia/commons/f/f3/Logo_TSG_Hoffenheim.png",
                25, true, 101.63);

        loadClub("VfL Wolfsburg",
                "https://upload.wikimedia.org/wikipedia/commons/5/56/VfL_Wolfsburg_Logo.png",
                31, true, 154.0);

        loadClub("Hertha Berlin",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Hertha_BSC_Logo_2012.svg/2000px-Hertha_BSC_Logo_2012.svg.png",
                26, true, 86.3);

        loadClub("1.FC Koeln",
                "https://s-media-cache-ak0.pinimg.com/originals/89/cf/4a/89cf4a23591414a0934fd33031fb3197.jpg",
                24, true, 99.3);

        loadClub("RB Lipsk",
                "https://qph.ec.quoracdn.net/main-qimg-fdcb9fba3cada788f244550f0b59ccf3",
                23, true, 123.93);

        loadClub("Werder Bremen",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/SV-Werder-Bremen-Logo.svg/2000px-SV-Werder-Bremen-Logo.svg.png",
                32, true, 69.8);

        loadClub("SC Freiburg",
                "https://upload.wikimedia.org/wikipedia/en/7/7b/SC_Freiburg.png",
                27, true, 58.68);

        loadClub("Eintracht Frankfurt",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Eintracht_Frankfurt_Logo.svg/2000px-Eintracht_Frankfurt_Logo.svg.png",
                30, true, 68.55);

        loadClub("FC Augsburg",
                "https://upload.wikimedia.org/wikipedia/en/thumb/c/c5/FC_Augsburg_logo.svg/368px-FC_Augsburg_logo.svg.png",
                31, true, 62.73);

        loadClub("Hamburger SV",
                "https://upload.wikimedia.org/wikipedia/en/thumb/f/f7/Hamburger_SV_logo.svg/1280px-Hamburger_SV_logo.svg.png",
                31, true, 75.75);

        loadClub("FSV Mainz 05",
                "https://upload.wikimedia.org/wikipedia/commons/d/d6/FSV_Mainz_05_Logo.png",
                30, true, 79.73);

        loadClub("FC Ingolstadt 04",
                "https://upload.wikimedia.org/wikipedia/en/thumb/0/0b/FC_Ingolstadt_04_logo.svg/694px-FC_Ingolstadt_04_logo.svg.png",
                25, false, 34.55);

        loadClub("SV Darmstadt 98",
                "https://upload.wikimedia.org/wikipedia/commons/3/3a/Darmstadt_98_football_club_new_logo_2015.png",
                31, false, 21.5);

        loadClub("VfB Stuttgart",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/VfB_Stuttgart_1893_Logo.svg/2000px-VfB_Stuttgart_1893_Logo.svg.png",
                24, true, 41.9);

        loadClub("Hannover 96", "https://upload.wikimedia.org/wikipedia/an/5/50/Hannover_96.png",
                27, true, 32.15);
	}

    private void loadClub(String clubName, String logoUrl, int playersAmount,
                          boolean inBundesligaNow, double totalValueOfPlayers) {
        Club club = new Club();
        club.setName(clubName);
        club.setImgUrl(logoUrl);
        club.setNumberOfPlayers(playersAmount);
        club.setBundesliga(inBundesligaNow);
        club.setValueOfPlayers(totalValueOfPlayers);
        clubService.saveClub(club);
        log.info("Saved " + club.getName() + " - id: " + club.getId());
    }*/

    private void loadMatches(int from, int to){
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
	}

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


