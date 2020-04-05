package com.developer.enjad.models;

import java.io.Serializable;
import java.util.List;

public class Reports_Model implements Serializable {
    private List<Data> data;
    public List<Data> getData() {
        return data;
    }



    public static class Data implements Serializable {
        private int number;
private int distance;
private String staus;

        public Data(int number, int distance, String staus) {
            this.number = number;
            this.distance = distance;
            this.staus = staus;
        }

        public int getNumber() {
            return number;
        }

        public int getDistance() {
            return distance;
        }

        public String getStaus() {
            return staus;
        }
    }


}