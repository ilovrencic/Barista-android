package com.delfinerija.baristaApp.entities;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public ArrayList<String> strings;

    public Category(ArrayList<String> strings){
        this.strings=strings;
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }
}
