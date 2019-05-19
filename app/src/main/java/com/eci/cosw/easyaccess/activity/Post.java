package com.eci.cosw.easyaccess.activity;

/**
 * Created by martin on 19/03/18.
 */


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    
    private String email;
    private String name;
    private Bitmap imageUri;
    private String identification;
    private String country;
    private String phone;
    private String password;

    public Post(String email,String password, String name,String identification, String country, String phone, Bitmap imageUri){
        this.email=email;
        this.imageUri=imageUri;
        this.name=name;
        this.identification = identification;
        this.country = country;
        this.phone = phone;
        this.password= password;
    }

    public Post(){}

    public String getEmail(){return this.email;}
    public Bitmap getImageUri(){return this.imageUri;}
    public void setEmail(String m){this.email=m;}
    public void setImageUri(Bitmap i){this.imageUri=i;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected Post(Parcel in) {
        email = in.readString();
        name = in.readString();
        country = in.readString();
        identification =in.readString();
        imageUri = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(identification);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(country);
        dest.writeParcelable(imageUri, flags);
    }


}
