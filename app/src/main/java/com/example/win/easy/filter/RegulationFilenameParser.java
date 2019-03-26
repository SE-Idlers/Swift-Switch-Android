package com.example.win.easy.filter;

import java.util.List;

import dagger.Module;

@Module
public class RegulationFilenameParser implements FilenameParser<Character> {

    @Override
    public List<Character> parse(String filename) {
        return null;
    }
}
