package com.hokutosai.hokutosai_android;

import java.io.Serializable;

/**
 * Created by ryoji on 2016/05/03.
 */
public class Exhibition implements Serializable {

    int exhibition_id;
    String title;
    String exhibitors;
    String displays;
    String image_url;
    AssessedScore assessed_score;
    Boolean liked;
    int likes_count;
    String introduction;
    Place place;
    Assessment my_assessment;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public AssessedScore getAssessed_score() {
        return assessed_score;
    }

    public void setAssessed_score(AssessedScore assessed_score) {
        this.assessed_score = assessed_score;
    }

    public String getDisplays() {
        return displays;
    }

    public void setDisplays(String displays) {
        this.displays = displays;
    }

    public int getExhibition_id() {
        return exhibition_id;
    }

    public void setExhibition_id(int exhibition_id) {
        this.exhibition_id = exhibition_id;
    }

    public String getExhibitors() {
        return exhibitors;
    }

    public void setExhibitors(String exhibitors) {
        this.exhibitors = exhibitors;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
