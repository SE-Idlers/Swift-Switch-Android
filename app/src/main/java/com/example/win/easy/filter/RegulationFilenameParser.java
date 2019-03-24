package com.example.win.easy.filter;

import java.io.File;
import java.util.List;

import dagger.Module;

@Module
public class RegulationFilenameParser implements FilenameParser<Character> {

    @Override
    public List<Character> parse(File file) {
        return null;
    }
}
