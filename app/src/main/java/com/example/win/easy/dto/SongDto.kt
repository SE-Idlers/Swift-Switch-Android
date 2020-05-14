package com.example.win.easy.dto

import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongDTO
import com.example.win.easy.network.BackendRequestService

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