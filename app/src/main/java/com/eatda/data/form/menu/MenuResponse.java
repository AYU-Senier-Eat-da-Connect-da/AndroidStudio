package com.eatda.data.form.menu;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuResponse implements Parcelable {
    private Long id;
    private String menuName;
    private String menuBody;
    private int price;
    private boolean menuStatus;
    private Long restaurantId;

    public Long getMenuId(){
        return this.id;
    }

    public String getMenuName(){
        return this.menuName;
    }

    public String getMenuBody(){
        return this.menuBody;
    }

    public int getPrice(){
        return this.price;
    }

    public boolean getMenuStatus(){
        return this.menuStatus;
    }


    protected MenuResponse(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        menuName = in.readString();
        menuBody = in.readString();
        price = in.readInt();
        menuStatus = in.readByte() != 0;
    }

    public static final Creator<MenuResponse> CREATOR = new Creator<MenuResponse>() {
        @Override
        public MenuResponse createFromParcel(Parcel in) {
            return new MenuResponse(in);
        }

        @Override
        public MenuResponse[] newArray(int size) {
            return new MenuResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(menuName);
        dest.writeString(menuBody);
        dest.writeInt(price);
        dest.writeByte((byte) (menuStatus ? 1 : 0));
    }
}
