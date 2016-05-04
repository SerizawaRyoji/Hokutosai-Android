package com.hokutosai.hokutosai_android;

/**
 * Created by ryoji on 2016/05/05.
 */
public class NewsTopicItem {

    String title;
    String media_url;
    int news_id;

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
