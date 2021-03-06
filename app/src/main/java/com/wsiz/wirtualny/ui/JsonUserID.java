package com.wsiz.wirtualny.ui;

public class JsonUserID {
    int studentid;
    int album;
    String imie;
    String nazwisko;
    String dataRejestracji;
    boolean active;
    boolean star;
    int finid;
    String email;
    long phone;
    String comment;

    public JsonUserID(int studentid, int album, String imie, String nazwisko, String dataRejestracji, boolean active, boolean star, int finid, String email, long phone, String comment) {
        this.studentid = studentid;
        this.album = album;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.dataRejestracji = dataRejestracji;
        this.active = active;
        this.star = star;
        this.finid = finid;
        this.email = email;
        this.phone = phone;
        this.comment = comment;
    }

    public int getStudentid() {
        return studentid;
    }

    public int getAlbum() {
        return album;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getDataRejestracji() {
        return dataRejestracji;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isStar() {
        return star;
    }

    public int getFinid() {
        return finid;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }
}