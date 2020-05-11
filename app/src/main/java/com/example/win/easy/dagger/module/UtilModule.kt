package com.example.win.easy.dagger.module

import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.recognization.Image
import com.example.win.easy.view.ImageService
import com.example.win.easy.web.DTOUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UtilModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideDTOUtil(filenameParser: FilenameParser<Char?>?): DTOUtil {
        return DTOUtil(filenameParser)
    }
}