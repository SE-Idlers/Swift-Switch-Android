package com.example.win.easy.dagger.module;

import com.example.win.easy.parser.RegulationFilenameParser;
import com.example.win.easy.parser.filter.CharSequenceFilterStrategy;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.parser.interfaces.FilenameParser;
import com.example.win.easy.parser.interfaces.MatcherProxy;
import com.example.win.easy.parser.matchers.ChineseCharacterMatcherProxy;
import com.example.win.easy.parser.matchers.WordMatcherProxy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ParserModule {

    @Provides
    @Singleton
    FilenameParser<Character> provideCharacterFilenameParser(){
        List<MatcherProxy<Character>> matcherProxies=new ArrayList<>();
        matcherProxies.add(new WordMatcherProxy());
        matcherProxies.add(new ChineseCharacterMatcherProxy());
        return new RegulationFilenameParser(matcherProxies);
    }

    @Provides
    @Singleton
    FilterStrategy<List<Character>> provideCharSequenceFilterStrategy(){
        return new CharSequenceFilterStrategy();
    }

}
