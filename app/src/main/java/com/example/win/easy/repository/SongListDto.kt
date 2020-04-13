package com.example.win.easy.repository

import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.web.dto.SongListDTO
import com.example.win.easy.web.request.BackendRequestService

class SongListDto(private val apiService: BackendRequestService) {

    suspend fun loadAll(uid: String): List<SongListDO>{
        val dtoData=apiService.getAllSongListsByUid(uid)
        return List(dtoData.size) {index ->
            dto2do(dtoData[index])
        }
    }

    // TODO
    private fun dto2do(dto: SongListDTO): SongListDO{
        throw NotImplementedError()
    }
}