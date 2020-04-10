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

    public String name;

    private String avatarUrl;

    public String avatarPath;

    private DataSource dataSource;
}
