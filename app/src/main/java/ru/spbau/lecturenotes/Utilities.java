package ru.spbau.lecturenotes;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static <T> ArrayList<T> convertListToArrayList(List<T> list) {
        if (list instanceof ArrayList) {
            return (ArrayList<T>) list;
        }
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.addAll(list);
        return arrayList;
    }
}
