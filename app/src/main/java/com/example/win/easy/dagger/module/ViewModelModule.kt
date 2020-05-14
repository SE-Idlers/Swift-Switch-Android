package com.example.win.easy.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.win.easy.factory.ViewModelFactory
import com.example.win.easy.repository.RelationRepository
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.SongRepository
import com.example.win.easy.viewmodel.RelationViewModel
import com.example.win.easy.viewmodel.SongListViewModel
import com.example.win.easy.viewmodel.SongListViewModelImpl
import com.example.win.easy.viewmodel.SongViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
object ViewModelModule {

    @JvmStatic
    @Provides
    @IntoMap
    @ViewModelKey(RelationViewModel::class)
    fun provideRelationViewModel(relationRepository: RelationRepository): ViewModel = RelationViewModel(relationRepository)

    @JvmStatic
    @Provides
    @IntoMap
    @ViewModelKey(SongListViewModel::class)
    fun provideSongListViewModel(songRepository: SongRepository,songListRepository: SongListRepository): ViewModel = SongListViewModelImpl(songRepository,songListRepository)

    @JvmStatic
    @Provides
    @IntoMap
    @ViewModelKey(SongViewModel::class)
    fun provideSongViewModel(songRepository: SongRepository): ViewModel = SongViewModel(songRepository)

    @JvmStatic
    @Provides
    fun provideViewModelFactory(providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory = ViewModelFactory(providerMap)

    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @MustBeDocumented
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
}