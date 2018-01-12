package com.widzin.bootstrap.loaders.services;

import java.io.File;

public interface LoadService {

    void startParsing (String directory, final MainLoadService service);

    void parse(File file, final MainLoadService loadService);
}
