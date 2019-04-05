package com.ritika.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

    private String name;
    private String office;
    private String party;

    private String address;
    private String phone;
    private String websiteUrl;
    private String email;

    private String photoURL;
    private String googlePlusId;
    private String facebookId;
    private String twitterId;
    private String youtubeId;

    public Official() {
        this.name = "No Data Provided";
        this.office = "No Data Provided";
        this.party = "Unknown";
        this.address = "No Data Provided";
        this.phone = "No Data Provided";
        this.websiteUrl = "No Data Provided";
        this.email = "No Data Provided";
        this.photoURL = "No Data Provided";
        this.googlePlusId = "No Data Provided";
        this.facebookId = "No Data Provided";
        this.twitterId = "No Data Provided";
        this.youtubeId = "No Data Provided";
    }

    public Official(String name, String office, String party, String address, String phone, String websiteUrl, String email, String photoURL, String googlePlusId, String facebookId, String twitterId, String youtubeId) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.email = email;
        this.photoURL = photoURL;
        this.googlePlusId = googlePlusId;
        this.facebookId = facebookId;
        this.twitterId = twitterId;
        this.youtubeId = youtubeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public void setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", office='" + office + '\'' +
                ", party='" + party + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", googlePlusId='" + googlePlusId + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", twitterId='" + twitterId + '\'' +
                ", youtubeId='" + youtubeId + '\'' +
                '}';
    }
}
