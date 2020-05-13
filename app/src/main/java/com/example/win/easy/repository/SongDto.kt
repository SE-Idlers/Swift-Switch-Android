package com.example.win.easy.repository

import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.web.dto.SongDTO
import com.example.win.easy.web.request.BackendRequestService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SongDto(private val apiService: BackendRequestService,
              private val parser: FilenameParser<Char>){

    suspend fun loadBySongList(songListDO: SongListDO): List<SongDO> =
            ArrayList<SongDO>().apply {
                for (songDTO in apiService.getSongsBySongListId(songListDO.id.toString()))
                    add(dto2do(songDTO))
            }

    private fun dto2do(songDTO: SongDTO) =
            SongDO(name = songDTO.totalName,
                    author = songDTO.author,
                    sequence = parser.parse(songDTO.name),
                    source = DataSource.WangYiYun,
                    songUrl = songDTO.songUrl,
                    avatarUrl = songDTO.avatarUrl,
                    uid = songDTO.uid.toLong(),
                    remoteId = songDTO.remoteId.toLong())
}