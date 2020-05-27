package com.example.android.mynews;

public class MyNews {
    private String Name;
    private String headline;
    private String date;
    private String url;
    private String firstName;
    private String lastName;

    public MyNews(String Name,String headline,String date, String url, String firstName, String lastName){
        this.Name = Name;
        this.headline = headline;
        this.date  = date;
        this.url = url;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return Name;
    }

    public String getheadline() {
        return headline;
    }

    public String getUrl() {
        return url;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
