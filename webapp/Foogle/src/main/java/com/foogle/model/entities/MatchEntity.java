package com.foogle.model.entities;

public class MatchEntity {
    
    public String country1;
    public String country2;
    public int score1;
    public int score2;
    public int year;
    public String date;
    public String stage;
    
    public MatchEntity(String country1, String country2, int score1,
            int score2, int year, String date, String stage) {
        super();
        this.country1 = country1;
        this.country2 = country2;
        this.score1 = score1;
        this.score2 = score2;
        this.year = year;
        this.date = date;
        this.stage = stage;
    }

    
}