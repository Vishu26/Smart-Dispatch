package com.example.myapplication;

public class Request {

    private String usrname, drivername, contactno, vehicleno, usrage, usrsex;

    public Request(String usrname, String usrage, String usrsex, String drivername, String contactno, String vehicleno){

        this.usrname = usrname;
        this.usrage = usrage;
        this.usrsex = usrsex;
        this.drivername = drivername;
        this.contactno = contactno;
        this.vehicleno = vehicleno;
    }

    public String getDrivername() {
        return drivername;
    }

    public String getUsrname() {
        return usrname;
    }

    public String getContactno() {
        return contactno;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public String getUsrage() {
        return usrage;
    }

    public String getUsrsex() {
        return usrsex;
    }
}
