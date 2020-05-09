package com.example.win.easy.value_object;

import com.example.win.easy.enumeration.DataSource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongVO {

    Long id;

    String name;

    String songFileUrl;

    String songFilePath;

    String avatarUrl;

    String avatarPath;

    DataSource source;
}
