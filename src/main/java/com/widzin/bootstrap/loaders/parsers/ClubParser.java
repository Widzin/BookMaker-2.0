package com.widzin.bootstrap.loaders.parsers;

import java.io.File;

public class ClubParser {
    public String getClubName (File file) {
        return file.getName().substring(0, file.getName().length() - 4).replace("_", " ");
    }
}
