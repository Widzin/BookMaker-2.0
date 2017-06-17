package com.widzin.bootstrap;

import com.widzin.domain.*;
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

	private Logger log = Logger.getLogger(SpringJpaBootstrap.class);
	private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

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
        loadClubs();
		loadMatches(2015, 2016);
		loadMatches(2016, 2017);
        loadUsers();
        loadRoles();
        assignUsersToUserRole();
        assignUsersToAdminRole();
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
		log.info("Wszystkie bramki stracone: " + calculations.getAllGoalsLostAtHome());
	}

	private void loadClubs(){
		Club club01 = new Club();
		club01.setName("Bayern Monachium");
		club01.setImgUrl("http://www.bettingexpert.com/assets/images/content/sports/football/germany/Bundesliga2015/55x55xBayernmunich.png.pagespeed.ic.UFdQXp7qtu.png");
		club01.setNumberOfPlayers(23);
		club01.setBundesliga(true);
		club01.setValueOfPlayers(566.15);
		clubService.saveClub(club01);
		log.info("Saved " + club01.getName() + " - id: " + club01.getId());

		Club club02 = new Club();
		club02.setName("Borussia Dortmund");
		club02.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Borussia_Dortmund_logo.svg/500px-Borussia_Dortmund_logo.svg.png");
		club02.setNumberOfPlayers(28);
		club02.setBundesliga(true);
		club02.setValueOfPlayers(376.35);
		clubService.saveClub(club02);
		log.info("Saved " + club02.getName() + " - id: " + club02.getId());

		Club club03 = new Club();
		club03.setName("Borussia Monchengladbach");
		club03.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Borussia_M%C3%B6nchengladbach_logo.svg/2000px-Borussia_M%C3%B6nchengladbach_logo.svg.png");
		club03.setNumberOfPlayers(30);
		club03.setBundesliga(true);
		club03.setValueOfPlayers(163.55);
		clubService.saveClub(club03);
		log.info("Saved " + club03.getName() + " - id: " + club03.getId());

		Club club04 = new Club();
		club04.setName("Bayer Leverkusen");
		club04.setImgUrl("https://upload.wikimedia.org/wikipedia/en/thumb/5/59/Bayer_04_Leverkusen_logo.svg/2400px-Bayer_04_Leverkusen_logo.svg.png");
		club04.setNumberOfPlayers(27);
		club04.setBundesliga(true);
		club04.setValueOfPlayers(273.95);
		clubService.saveClub(club04);
		log.info("Saved " + club04.getName() + " - id: " + club04.getId());

		Club club05 = new Club();
		club05.setName("FC Schalke 04");
		club05.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/9/97/FC_Schalke_04_Logo.png");
		club05.setNumberOfPlayers(32);
		club05.setBundesliga(true);
		club05.setValueOfPlayers(216.63);
		clubService.saveClub(club05);
		log.info("Saved " + club05.getName() + " - id: " + club05.getId());

		Club club06 = new Club();
		club06.setName("TSG 1899 Hoffenheim");
		club06.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/f/f3/Logo_TSG_Hoffenheim.png");
		club06.setNumberOfPlayers(25);
		club06.setBundesliga(true);
		club06.setValueOfPlayers(101.63);
		clubService.saveClub(club06);
		log.info("Saved " + club06.getName() + " - id: " + club06.getId());

		Club club07 = new Club();
		club07.setName("VfL Wolfsburg");
		club07.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/5/56/VfL_Wolfsburg_Logo.png");
		club07.setNumberOfPlayers(31);
		club07.setBundesliga(true);
		club07.setValueOfPlayers(154.0);
		clubService.saveClub(club07);
		log.info("Saved " + club07.getName() + " - id: " + club07.getId());

		Club club08 = new Club();
		club08.setName("Hertha Berlin");
		club08.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Hertha_BSC_Logo_2012.svg/2000px-Hertha_BSC_Logo_2012.svg.png");
		club08.setNumberOfPlayers(26);
		club08.setBundesliga(true);
		club08.setValueOfPlayers(86.3);
		clubService.saveClub(club08);
		log.info("Saved " + club08.getName() + " - id: " + club08.getId());

		Club club09 = new Club();
		club09.setName("1.FC Koeln");
		club09.setImgUrl("https://s-media-cache-ak0.pinimg.com/originals/89/cf/4a/89cf4a23591414a0934fd33031fb3197.jpg");
		club09.setNumberOfPlayers(24);
		club09.setBundesliga(true);
		club09.setValueOfPlayers(99.3);
		clubService.saveClub(club09);
		log.info("Saved " + club09.getName() + " - id: " + club09.getId());

		Club club10 = new Club();
		club10.setName("RB Lipsk");
		club10.setImgUrl("https://qph.ec.quoracdn.net/main-qimg-fdcb9fba3cada788f244550f0b59ccf3");
		club10.setNumberOfPlayers(23);
		club10.setBundesliga(true);
		club10.setValueOfPlayers(123.93);
		clubService.saveClub(club10);
		log.info("Saved " + club10.getName() + " - id: " + club10.getId());

		Club club11 = new Club();
		club11.setName("Werder Bremen");
		club11.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/SV-Werder-Bremen-Logo.svg/2000px-SV-Werder-Bremen-Logo.svg.png");
		club11.setNumberOfPlayers(32);
		club11.setBundesliga(true);
		club11.setValueOfPlayers(69.8);
		clubService.saveClub(club11);
		log.info("Saved " + club11.getName() + " - id: " + club11.getId());

		Club club12 = new Club();
		club12.setName("SC Freiburg");
		club12.setImgUrl("https://upload.wikimedia.org/wikipedia/en/7/7b/SC_Freiburg.png");
		club12.setNumberOfPlayers(27);
		club12.setBundesliga(true);
		club12.setValueOfPlayers(58.68);
		clubService.saveClub(club12);
		log.info("Saved " + club12.getName() + " - id: " + club12.getId());

		Club club13 = new Club();
		club13.setName("Eintracht Frankfurt");
		club13.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Eintracht_Frankfurt_Logo.svg/2000px-Eintracht_Frankfurt_Logo.svg.png");
		club13.setNumberOfPlayers(30);
		club13.setBundesliga(true);
		club13.setValueOfPlayers(68.55);
		clubService.saveClub(club13);
		log.info("Saved " + club13.getName() + " - id: " + club13.getId());

		Club club14 = new Club();
		club14.setName("FC Augsburg");
		club14.setImgUrl("https://upload.wikimedia.org/wikipedia/en/thumb/c/c5/FC_Augsburg_logo.svg/368px-FC_Augsburg_logo.svg.png");
		club14.setNumberOfPlayers(31);
		club14.setBundesliga(true);
		club14.setValueOfPlayers(62.73);
		clubService.saveClub(club14);
		log.info("Saved " + club14.getName() + " - id: " + club14.getId());

		Club club15 = new Club();
		club15.setName("Hamburger SV");
		club15.setImgUrl("https://upload.wikimedia.org/wikipedia/en/thumb/f/f7/Hamburger_SV_logo.svg/1280px-Hamburger_SV_logo.svg.png");
		club15.setNumberOfPlayers(31);
		club15.setBundesliga(true);
		club15.setValueOfPlayers(75.75);
		clubService.saveClub(club15);
		log.info("Saved " + club15.getName() + " - id: " + club15.getId());

		Club club16 = new Club();
		club16.setName("FSV Mainz 05");
		club16.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/d/d6/FSV_Mainz_05_Logo.png");
		club16.setNumberOfPlayers(30);
		club16.setBundesliga(true);
		club16.setValueOfPlayers(79.73);
		clubService.saveClub(club16);
		log.info("Saved " + club16.getName() + " - id: " + club16.getId());

		Club club17 = new Club();
		club17.setName("FC Ingolstadt 04");
		club17.setImgUrl("https://upload.wikimedia.org/wikipedia/en/thumb/0/0b/FC_Ingolstadt_04_logo.svg/694px-FC_Ingolstadt_04_logo.svg.png");
		club17.setNumberOfPlayers(25);
		club17.setBundesliga(false);
		club17.setValueOfPlayers(34.55);
		clubService.saveClub(club17);
		log.info("Saved " + club17.getName() + " - id: " + club17.getId());

		Club club18 = new Club();
		club18.setName("SV Darmstadt 98");
		club18.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/3/3a/Darmstadt_98_football_club_new_logo_2015.png");
		club18.setNumberOfPlayers(31);
		club18.setBundesliga(false);
		club18.setValueOfPlayers(21.5);
		clubService.saveClub(club18);
		log.info("Saved " + club18.getName() + " - id: " + club18.getId());

		Club club19 = new Club();
		club19.setName("VfB Stuttgart");
		club19.setImgUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/VfB_Stuttgart_1893_Logo.svg/2000px-VfB_Stuttgart_1893_Logo.svg.png");
		club19.setNumberOfPlayers(24);
		club19.setBundesliga(true);
		club19.setValueOfPlayers(41.9);
		clubService.saveClub(club19);
		log.info("Saved " + club19.getName() + " - id: " + club19.getId());

		Club club20 = new Club();
		club20.setName("Hannover 96");
		club20.setImgUrl("https://upload.wikimedia.org/wikipedia/an/5/50/Hannover_96.png");
		club20.setNumberOfPlayers(27);
		club20.setBundesliga(true);
		club20.setValueOfPlayers(32.15);
		clubService.saveClub(club20);
		log.info("Saved " + club20.getName() + " - id: " + club20.getId());
	}

    private void loadMatches(int from, int to){
		String path = "static/txt/Terminarz_" + from + "_" + to + ".txt";
		ClassLoader classLoader = new SpringJpaBootstrap().getClass().getClassLoader();
		try {
			File file = new File(classLoader.getResource(path).getFile());

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
		User user1 = new User();
		user1.setUsername("admin");
		user1.setPassword("admin");
		userService.saveOrUpdate(user1);

        User user2 = new User();
        user2.setUsername("user");
        user2.setPassword("user");
        userService.saveOrUpdate(user2);
    }

    private void loadRoles() {
		Role adminRole = new Role();
		adminRole.setRole("ADMIN");
		roleService.saveOrUpdate(adminRole);
		log.info("Saved role " + adminRole.getRole());

        Role role = new Role();
        role.setRole("USER");
        roleService.saveOrUpdate(role);
        log.info("Saved role " + role.getRole());
    }
    private void assignUsersToUserRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<User> users = (List<User>) userService.listAll();

        roles.forEach(role -> {
            if (role.getRole().equalsIgnoreCase("USER")) {
                users.forEach(user -> {
                    if (user.getUsername().equals("user")) {
                        user.addRole(role);
                        user.setMainRole(role.getRole());
                        userService.saveOrUpdate(user);
                    }
                });
            }
        });
    }
    private void assignUsersToAdminRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<User> users = (List<User>) userService.listAll();

        roles.forEach(role -> {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                users.forEach(user -> {
                    if (user.getUsername().equals("admin")) {
                        user.addRole(role);
						user.setMainRole(role.getRole());
                        userService.saveOrUpdate(user);
                    }
                });
            }
        });
    }
}


