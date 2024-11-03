package com.eatda.kakaomap.goodInfluenceStore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoodInfluenceResponse {
    private List<GoodInfluenceStore> row;

    public List<GoodInfluenceStore> getGoodInfluenceStores() {
        return row;
    }

    public void setGoodInfluenceStores(List<GoodInfluenceStore> row) {
        this.row = row;
    }
}