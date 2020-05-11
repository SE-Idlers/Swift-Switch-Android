package com.example.win.easy.dagger.module

import com.example.win.easy.parser.RegulationFilenameParser
import com.example.win.easy.parser.filter.CharSequenceFilterStrategy
import com.example.win.easy.parser.filter.FilterStrategy
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.parser.interfaces.MatcherProxy
import com.example.win.easy.parser.matchers.ChineseCharacterMatcherProxy
import com.example.win.easy.parser.matchers.WordMatcherProxy
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class ParserModule {

    @Provides
    @Singleton
    fun provideCharacterFilenameParser(): FilenameParser<Char> {
        val matcherProxies: MutableList<MatcherProxy<Char>> = ArrayList()
        matcherProxies.add(WordMatcherProxy())
        matcherProxies.add(ChineseCharacterMatcherProxy())
        return RegulationFilenameParser(matcherProxies)
    }

    @Provides
    @Singleton
    fun provideCharSequenceFilterStrategy(): FilterStrategy<List<Char>> = CharSequenceFilterStrategy()
}