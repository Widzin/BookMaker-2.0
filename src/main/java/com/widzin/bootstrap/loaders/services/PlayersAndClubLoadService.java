package com.widzin.bootstrap.loaders.services;

import com.widzin.bootstrap.loaders.parsers.ClubParser;
import com.widzin.bootstrap.loaders.parsers.ParsingMethods;
import com.widzin.bootstrap.loaders.parsers.PlayerParser;
import com.widzin.bootstrap.loaders.xmlModels.XMLAllLogos;
import com.widzin.bootstrap.loaders.xmlModels.XMLClubLogo;
import com.widzin.bootstrap.loaders.xmlModels.XMLClubSeason;
import com.widzin.bootstrap.loaders.xmlModels.XMLPlayer;
import com.widzin.models.*;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Date;

@Service
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
        ClubSeason clubSeason;
        Club club = loadService.getClubFromService(clubParser.getClubName(file));

        if (club == null) {
            clubSeason = new ClubSeason(clubParser.getClubName(file));
            loadService.addClub(clubSeason.getClub());
        } else
            clubSeason = new ClubSeason(club);

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
                clubSeason.addPlayer(playerSeason);
            }
        } catch (JAXBException ex) {
            System.out.println("Cannot create instance of XMLClubSeason Class");
        }
        season.addClub(clubSeason);
    }

    private Date getBirthDate(XMLPlayer xmlPlayer) {
        return playerParser.parseDate(playerParser.cutAge(xmlPlayer.getBirthDateAndAge()));
    }

    public void addLogos(String directory, final MainLoadService loadService) {
        File file = new File(directory);

        try {
            JAXBContext jc = JAXBContext.newInstance(XMLAllLogos.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLAllLogos xmlAllLogos = (XMLAllLogos) unmarshaller.unmarshal(file);
            for (XMLClubLogo clubLogo: xmlAllLogos.getLogos()) {
                loadService.getClubFromService(clubLogo.getClubName())
                        .setImgUrl(clubLogo.getClubUrl());
            }
        } catch (JAXBException ex) {
            ex.printStackTrace();
            //System.out.println("Cannot create instance of XMLAllLogos Class");
        }
    }
}
