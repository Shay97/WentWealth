package edu.wit.mobileapp.wentwealth;

import android.os.Parcel;
import android.os.Parcelable;

public class WishlistItemObject implements Parcelable {

    public String image;
    public String itemName;
    public int value;
    public int rBalance=0;

    public WishlistItemObject(String image, String itemName, int value, int rBalance){
        this.image = image ;
        this.itemName = itemName;
        this.value = value;
        this.rBalance = rBalance;
    }

    public WishlistItemObject(Parcel in) {
        image = in.readString();
        itemName = in.readString();
        value = in.readInt();
        rBalance = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(itemName);
        dest.writeInt(value);
        dest.writeInt(rBalance);
    }

    public static final Parcelable.Creator<WishlistItemObject> CREATOR = new Parcelable.Creator<WishlistItemObject>() {
        public WishlistItemObject createFromParcel(Parcel source) {
            return new WishlistItemObject(source);
        }

        @Override
        public WishlistItemObject[] newArray(int size) {
            return new WishlistItemObject[size];
        }
    };
}
