package com.hokutosai.hokutosai_android;

import java.util.List;

/**
 * Created by ryoji on 2016/05/05.
 */
public class News {

    int news_id;
    String title;
    String datetime;
    EventItem related_event;
    ShopItem related_shop;
    ExhibitionItem related_exhibition;
    Boolean topic;
    String tag;
    String text;
    int likes_count;
    Boolean liked;
    List<Media> medias;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public EventItem getRelated_event() {
        return related_event;
    }

    public void setRelated_event(EventItem related_event) {
        this.related_event = related_event;
    }

    public ExhibitionItem getRelated_exhibition() {
        return related_exhibition;
    }

    public void setRelated_exhibition(ExhibitionItem related_exhibition) {
        this.related_exhibition = related_exhibition;
    }

    public ShopItem getRelated_shop() {
        return related_shop;
    }

    public void setRelated_shop(ShopItem related_shop) {
        this.related_shop = related_shop;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getTopic() {
        return topic;
    }

    public void setTopic(Boolean topic) {
        this.topic = topic;
    }

    class EventItem{
        int event_id;
        String title;
    }
    class ShopItem{
        int shop_id;
        String name;
    }
    class ExhibitionItem{
        int exhibition_id;
        String title;
    }
    class Media{
        String media_id;
        int sequence;
        String url;
        String file_name;
        String type;
    }
}

