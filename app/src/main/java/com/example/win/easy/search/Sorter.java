package com.example.win.easy.search;

import java.util.List;

public interface Sorter<T> {

    List<Integer> sort(List<T> listToSort);

}
