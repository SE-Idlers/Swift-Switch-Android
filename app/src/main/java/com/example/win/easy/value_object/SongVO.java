package com.example.win.easy.value_object;

import java.io.File;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongVO {

    String name;

    File songFile;

    File avatarFile;

}
