package com.example.evaluacionapp.Models;

import com.google.gson.annotations.SerializedName;

public class ListHeroes {

    @SerializedName("id")
    private String idHero;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String detail;
    @SerializedName("path")
    private String image;

    public ListHeroes(String idHero, String name, String detail, String image) {
        this.idHero = idHero;
        this.name = name;
        this.detail = detail;
        this.image = image;
    }

    public String getIdHero() {
        return idHero;
    }

    public void setIdHero(String idHero) {
        this.idHero = idHero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
