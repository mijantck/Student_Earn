package com.mrsoftit.studentearn;

public class userModle {

    private String Coins;
    private String Email;
    private String Name;
    private String phone;
    private String userID;
    private String imageURL;
    private String userAddressInput;

    public userModle() {
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String coins) {
        Coins = coins;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserAddressInput() {
        return userAddressInput;
    }

    public void setUserAddressInput(String userAddressInput) {
        this.userAddressInput = userAddressInput;
    }
}
