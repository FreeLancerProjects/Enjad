package com.developer.enjad.models;


public class ReportModel {

    private String id;
    private String number;
    private String link;
    private String distance="";



    public ReportModel() {


    }

    public ReportModel(String id, String number, String link) {
        this.id = id;
        this.number = number;
        this.link = link;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;

    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
