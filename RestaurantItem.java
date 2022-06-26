package com.sml.cowematest.model;

public class RestaurantItem {
    public String id;
    public String titre;
    public String placeHoldUrl;
    public String url;

    public RestaurantItem() {
    }

    public RestaurantItem(String id, String titre, String placeHoldUrl, String url) {
        this.id = id;
        this.titre = titre;
        this.placeHoldUrl = placeHoldUrl;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlaceHoldUrl() {
        return placeHoldUrl;
    }

    public void setPlaceHoldUrl(String placeHoldUrl) {
        this.placeHoldUrl = placeHoldUrl;
    }

    @Override
    public String toString() {
        return "RestaurantItem{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", placeHoldUrl='" + placeHoldUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
