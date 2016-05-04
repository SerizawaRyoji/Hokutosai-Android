package com.hokutosai.hokutosai_android;

/**
 * Created by ryoji on 2016/05/03.
 */
public class Exhibition {

    int exhibition_id;
    String title;
    String exhibitors;
    String displays;
    String image_url;
    AssessedScore assessed_score;
    Boolean liked;
    int like_count;

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

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
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
