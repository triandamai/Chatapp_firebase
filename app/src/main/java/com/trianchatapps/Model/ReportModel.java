package com.trianchatapps.Model;

public class ReportModel {
   public String Uid;
   public String error_report;
   public String manufacturer ;
   public String model ;
   public int version ;
   public String versionRelease ;

    public ReportModel(String uid, String[] strings, String manufacturer, String model, int version, String versionRelease) {


    }

    public ReportModel(String uid, String error_report, String manufacturer, String model, int version, String versionRelease) {
        Uid = uid;
        this.error_report = error_report;
        this.manufacturer = manufacturer;
        this.model = model;
        this.version = version;
        this.versionRelease = versionRelease;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getError_report() {
        return error_report;
    }

    public void setError_report(String error_report) {
        this.error_report = error_report;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionRelease() {
        return versionRelease;
    }

    public void setVersionRelease(String versionRelease) {
        this.versionRelease = versionRelease;
    }
}
