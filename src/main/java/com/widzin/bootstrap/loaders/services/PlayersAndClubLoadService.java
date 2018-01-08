package com.widzin.bootstrap.loaders.services;

import com.widzin.bootstrap.loaders.parsers.ClubParser;
import com.widzin.bootstrap.loaders.parsers.ParsingMethods;
import com.widzin.bootstrap.loaders.parsers.PlayerParser;
import com.widzin.bootstrap.loaders.xmlModels.XMLClubSeason;
import com.widzin.bootstrap.loaders.xmlModels.XMLPlayer;
import com.widzin.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Date;

public class PlayersAndClubLoadService implements LoadService{

    private Season season;
    private ClubParser clubParser;
    private PlayerParser playerParser;

    @Override
    public void startParsing(String directory, final MainLoadService loadService) {
        clubParser = new ClubParser();
        playerParser = new PlayerParser();

        season = new Season(ParsingMethods.getPeriodSeason(directory));
        for (File f: new File(directory).listFiles()) {
            parse(f, loadService);
        }
        loadService.addSeason(season);
    }

    @Override
    public void parse(File file, final MainLoadService loadService) {
        ClubSeason club;
        Club2 club2 = loadService.getClubFromService(clubParser.getClubName(file));

        if (club2 == null)
            club = new ClubSeason(clubParser.getClubName(file));
        else
            club = new ClubSeason(club2);
        try {
            JAXBContext jc = JAXBContext.newInstance(XMLClubSeason.class);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLClubSeason xmlClubSeason = (XMLClubSeason) unmarshaller.unmarshal(file);
            for (XMLPlayer xmlPlayer: xmlClubSeason.getPlayers()) {
                String name = xmlPlayer.getName();
                Date birthDate = getBirthDate(xmlPlayer);

                Player player = loadService.getPlayerFromService(name, birthDate);

                if (player == null) {
                    player = new Player(name, birthDate);
                    loadService.addPlayer(player);
                }

                PlayerSeason playerSeason = new PlayerSeason(player, xmlPlayer.getNumber());
                playerParser.parseFull(playerSeason, xmlPlayer);
                club.addPlayer(playerSeason);
            }
        } catch (JAXBException ex) {
            System.out.println("Cannot create instance of XMLClubSeason Class");
        }
        season.addClub(club);
    }

    private Date getBirthDate(XMLPlayer xmlPlayer) {
        return playerParser.parseDate(playerParser.cutAge(xmlPlayer.getBirthDateAndAge()));
    }
}
