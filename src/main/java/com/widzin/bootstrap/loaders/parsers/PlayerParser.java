package com.widzin.bootstrap.loaders.parsers;

import com.widzin.bootstrap.loaders.xmlModels.XMLPlayer;
import com.widzin.models.PlayerSeason;
import com.widzin.models.Position;
import org.apache.commons.lang.LocaleUtils;

import java.util.Date;

public class PlayerParser {
    public void parseFull(final PlayerSeason player, XMLPlayer xmlPlayer) {
        player.setPosition(getPositionFromEnum(xmlPlayer.getPosition()));
        player.setFromWhenInClub(parseDate(cutAge(xmlPlayer.getFromWhenStr())));
        player.setValue(unformatValue(xmlPlayer.getValueStr()));
    }

    public Date parseDate(String dateStr) {
        return ParsingMethods.parseDate(dateStr, "dd-MMM-yyyy", LocaleUtils.toLocale("pl_PL"));
    }

    public String cutAge(String text) {
        return ParsingMethods.cutAge(text);
    }

    public String getPositionFromEnum(String position) {
        for(Position pos : Position.values())
        {
            if (position.equals(pos.getPosition()))
                return pos.name();
        }
        return "";
    }

    public Double unformatValue(String valueStr) {
        valueStr = valueStr.substring(0, valueStr.indexOf("€") + 1);
        if  (valueStr.endsWith(" mln €")) {
            String onlyDigits = valueStr.split(" ")[0];
            return Double.parseDouble(onlyDigits.split(",")[0]) * Math.pow(10, 6)
                    + Double.parseDouble(onlyDigits.split(",")[1]) * Math.pow(10, 4);
        } else if (valueStr.endsWith(" tys. €")) {
            String onlyDigits = valueStr.split(" ")[0];
            return Double.parseDouble(onlyDigits) * Math.pow(10, 3);
        } else {
            return new Double(0);
        }
    }

    public String getFormattedDate(Date date) {
        return ParsingMethods.getFormattedDate(date);
    }

    public String getFormattedValue(Double value) {
        if (value != null) {
            if (value >= Math.pow(10, 6))
                return value / Math.pow(10, 6) + " mln €";
            else if (value >= Math.pow(10, 3))
                return value / Math.pow(10, 3) + " tys €";
            return value + " €";
        } else {
            return "";
        }
    }
}
