package com.example.win.easy.dagger.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.win.easy.display.interfaces.DisplayService
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.enumeration.LoginType
import com.example.win.easy.factory.DaggerFragmentFactory
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.recognization.interfaces.RecognitionService
import com.example.win.easy.view.ImageService
import com.example.win.easy.view.lock.DisplayFragment
import com.example.win.easy.view.lock.HandWritingFragment
import com.example.win.easy.view.lock.SearchFragment
import com.example.win.easy.view.main.*
import com.example.win.easy.web.service.LoginService
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
object FragmentModule {

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(DisplayFragment::class)
    fun provideDisplayFragment(displayService: DisplayService): Fragment = DisplayFragment(displayService)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(HandWritingFragment::class)
    fun provideHandWritingFragment(recognitionService: RecognitionService): Fragment = HandWritingFragment(recognitionService)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(SearchFragment::class)
    fun provideSearchFragment(viewModelFactory: ViewModelProvider.Factory, displayService: DisplayService): Fragment = SearchFragment(viewModelFactory, displayService)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(AddSongToSongListFragment::class)
    fun provideAddSongToSongListFragment(viewModelFactory: ViewModelProvider.Factory): Fragment = AddSongToSongListFragment(viewModelFactory)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(AllSongListsFragment::class)
    fun provideAllSongListsFragment(factory: ViewModelProvider.Factory,imageService: ImageService): Fragment = AllSongListsFragment(factory, imageService)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(AllSongsFragment::class)
    fun provideAllSongsFragment(displayService: DisplayService, downloadServiceAdapter: DownloadServiceAdapter, parser: FilenameParser<Char>, factory: ViewModelProvider.Factory): Fragment =
            AllSongsFragment(displayService, downloadServiceAdapter, parser, factory)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(LoginFragment::class)
    fun provideLoginFragment(loginService: LoginService): Fragment = LoginFragment(loginService, LoginType.Phone)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(MainActivityFragment::class)
    fun provideMainActivityFragment(loginService: LoginService): Fragment = MainActivityFragment(loginService)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(SongListCreationFragment::class)
    fun provideSongListCreationFragment(viewModelFactory: ViewModelProvider.Factory): Fragment = SongListCreationFragment(viewModelFactory)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(SongListFragment::class)
    fun provideSongListFragment(displayService: DisplayService,downloadServiceAdapter: DownloadServiceAdapter,factory: ViewModelProvider.Factory): Fragment = SongListFragment(displayService, downloadServiceAdapter, factory)

    @JvmStatic
    @Provides
    @IntoMap
    @FragmentKey(NavHostFragment::class)
    fun provideNavHostFragment(): Fragment = NavHostFragment()

    @JvmStatic
    @Provides
    fun provideFragmentFactory(providerMap: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>): FragmentFactory = DaggerFragmentFactory(providerMap)

    @MustBeDocumented
    @MapKey
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    internal annotation class FragmentKey(val value: KClass<out Fragment>)
}