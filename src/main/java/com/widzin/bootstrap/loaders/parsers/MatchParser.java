package com.widzin.bootstrap.loaders.parsers;

import java.io.File;

public class MatchParser {
    public String getSeason(File file) {
        return file.getName()
                .replaceAll("_", "/")
                .replaceAll(".xml", "");
    }

    public String getPartOfPlayerName (String player) {
        if (player.trim().contains(" "))
            return player.trim().split(" ")[0].trim();
        else
            return player.trim();
    }

    public String getRestOfPlayerName (String player) {
        if (player.trim().contains(" ")) {
            String[] parts = player.trim().split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                sb.append(parts[i]);
                if ((parts.length - i) != 1)
                    sb.append(" ");
            }
            return sb.toString();
        }
        else
            return player.trim();
    }
}
