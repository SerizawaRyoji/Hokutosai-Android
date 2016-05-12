package com.hokutosai.hokutosai_android;

import java.io.Serializable;

/**
 * Created by ryoji on 2016/05/04.
 */
public class Place implements Serializable {

    private int place_id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }
}
