package com.example.win.easy.value_object;

import com.example.win.easy.enumeration.DataSource;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongListVO implements Serializable {

    private long id;

    private String name;

    private String avatarUrl;

    private String avatarPath;

    private DataSource dataSource;
}
