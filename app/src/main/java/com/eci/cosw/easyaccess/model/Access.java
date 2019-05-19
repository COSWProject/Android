package com.eci.cosw.easyaccess.model;

public class Access {

    private String owner;
    private String qr;
    private String invitedBy;
    private String institution;
    private String time;
    private String date;
    private String expirationTime;

    public Access() {

    }

    public Access(String owner, String qr, String invitedBy, String institution,
                  String time, String date, String expirationTime) {
        this.owner = owner;
        this.qr = qr;
        this.invitedBy = invitedBy;
        this.institution = institution;
        this.time = time;
        this.date = date;
        this.expirationTime = expirationTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        institution = institution;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

}
