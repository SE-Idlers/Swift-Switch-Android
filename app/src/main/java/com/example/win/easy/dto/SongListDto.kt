package com.example.win.easy.dto

import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongListDTO
import com.example.win.easy.network.BackendRequestService

class SongListDto(private val apiService: BackendRequestService) {

    suspend fun loadAll(uid: String): List<SongListDO> =
            ArrayList<SongListDO>().apply {
                for(songListDTO in apiService.getAllSongListsByUid(uid))
                    add(dto2do(songListDTO))
            }

    private fun dto2do(dto: SongListDTO): SongListDO =
            SongListDO(name = dto.name,
                    source = DataSource.Local,
                    avatarUrl = dto.avatarUrl,
                    uid = dto.uid.toLong(),
                    remoteId = dto.remoteId.toLong())
}