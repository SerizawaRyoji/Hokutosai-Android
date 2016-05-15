package com.hokutosai.hokutosai_android;

import java.io.Serializable;

/**
 * Created by ryoji on 2016/05/05.
 */
public class Assessment implements Serializable {

    int assessment_id;
    Account user;
    String datetime;
    int score;
    String comment;

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }


    private class Account implements Serializable{
        String account_id;
        String user_name;
        String media_url;
    }
}
