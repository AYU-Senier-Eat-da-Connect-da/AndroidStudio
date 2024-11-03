package com.eatda.kakaomap.goodInfluenceStore;

import java.io.Serializable;

public class GoodInfluenceStore implements Serializable {
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

    public void setCMPNM_NM(String CMPNM_NM) {
        this.CMPNM_NM = CMPNM_NM;
    }

    public String getBSN_TM_NM() {
        return BSN_TM_NM;
    }

    public void setBSN_TM_NM(String BSN_TM_NM) {
        this.BSN_TM_NM = BSN_TM_NM;
    }

    public String getREFINE_ROADNM_ADDR() {
        return REFINE_ROADNM_ADDR;
    }

    public void setREFINE_ROADNM_ADDR(String REFINE_ROADNM_ADDR) {
        this.REFINE_ROADNM_ADDR = REFINE_ROADNM_ADDR;
    }

    public String getDETAIL_ADDR() {
        return DETAIL_ADDR;
    }

    public void setDETAIL_ADDR(String DETAIL_ADDR) {
        this.DETAIL_ADDR = DETAIL_ADDR;
    }

    public String getREFINE_ZIPNO() {
        return REFINE_ZIPNO;
    }

    public void setREFINE_ZIPNO(String REFINE_ZIPNO) {
        this.REFINE_ZIPNO = REFINE_ZIPNO;
    }

    public double getREFINE_WGS84_LAT() {
        return REFINE_WGS84_LAT;
    }

    public void setREFINE_WGS84_LAT(double REFINE_WGS84_LAT) {
        this.REFINE_WGS84_LAT = REFINE_WGS84_LAT;
    }

    public double getREFINE_WGS84_LOGT() {
        return REFINE_WGS84_LOGT;
    }

    public void setREFINE_WGS84_LOGT(double REFINE_WGS84_LOGT) {
        this.REFINE_WGS84_LOGT = REFINE_WGS84_LOGT;
    }
}
