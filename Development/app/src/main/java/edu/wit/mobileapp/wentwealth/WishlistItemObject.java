package edu.wit.mobileapp.wentwealth;

public class WishlistItemObject {
    public String image;
    public String itemName;
    public int value;
    public int rBalance=0;

    public WishlistItemObject(String image, String itemName, int value){
        this.image = image;
        this.itemName = itemName;
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getrBalance() {
        return rBalance;
    }

    public void setrBalance(int rBalance) {
        this.rBalance = rBalance;
    }
}
