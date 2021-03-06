package com.hokutosai.hokutosai_android;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ryoji on 2016/05/02.
 */
public class Shop implements Serializable {

    private int shop_id;
    private String name;
    private String tenant;
    private String sales;
    private String image_url;
    private AssessedScore assesssed_score;
    private Boolean liked;
    private int likes_count;
    private String introduction;
    private Place place;
    List<Menu> menu;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public Assessment getMy_assessment() {
        return my_assessment;
    }

    public void setMy_assessment(Assessment my_assessment) {
        this.my_assessment = my_assessment;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    Assessment my_assessment;

    public AssessedScore getAssesssed_score() {
        return assesssed_score;
    }

    public void setAssesssed_score(AssessedScore assesssed_score) {
        this.assesssed_score = assesssed_score;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    private class Menu implements Serializable{
        int item_id;
        int price;
        String name;
    }
}
