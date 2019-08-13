package com.example.win.easy.repository;

import com.example.win.easy.repository.web.OnReadyFunc;
import com.example.win.easy.repository.web.dto.SongListDTO;

import java.util.List;

public interface SongListNetworkService {

    void fetch(OnReadyFunc<List<SongListDTO>> onReadyFunc);

}
