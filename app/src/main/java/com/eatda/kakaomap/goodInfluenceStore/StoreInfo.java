package com.eatda.kakaomap.goodInfluenceStore;

import com.kakao.vectormap.label.LabelTextBuilder;

public class StoreInfo {
    private String CMPNM_NM; // 가게 이름
    private String BSN_TM_NM; // 영업 시간
    private String REFINE_ROADNM_ADDR; // 도로명 주소
    private String DETAIL_ADDR; // 상세 주소
    private String REFINE_ZIPNO; // 우편번호
    private double REFINE_WGS84_LAT; // 위도
    private double REFINE_WGS84_LOGT; // 경도

    public String getCMPNM_NM() {
        return CMPNM_NM;
    }

    public String getBSN_TM_NM() {
        return BSN_TM_NM;
    }

    public String getREFINE_ROADNM_ADDR() {
        return REFINE_ROADNM_ADDR;
    }

    public String getDETAIL_ADDR() {
        return DETAIL_ADDR;
    }

    public String getREFINE_ZIPNO() {
        return REFINE_ZIPNO;
    }

    public double getREFINE_WGS84_LAT() {
        return REFINE_WGS84_LAT;
    }

    public double getREFINE_WGS84_LOGT() {
        return REFINE_WGS84_LOGT;
    }

    //라벨
    public String getLabelCMPNM_NM() {
        return CMPNM_NM;
    }

}
