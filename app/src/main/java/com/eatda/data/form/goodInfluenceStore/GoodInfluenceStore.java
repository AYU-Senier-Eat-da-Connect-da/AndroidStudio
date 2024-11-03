package com.eatda.data.form.goodInfluenceStore;

public class GoodInfluenceStore {
    private String CMPNM_NM;    // 상호명
    private String INDUTYPE_NM; // 업종명
    private String SIGUN_NM;    // 시군명
    private String REFINE_ROADNM_ADDR;  // 정제도로명주소
    private String REFINE_LOTNO_ADDR;   // 정제지번주소
    private String DETAIL_ADDR; // 상세주소
    private String BSN_TM_NM;   // 영업시간
    private Double REFINE_WGS84_LAT;    // 위도
    private Double REFINE_WGS84_LOGT;   // 경도

    public String getCMPNM_NM() {
        return CMPNM_NM;
    }

    public void setCMPNM_NM(String CMPNM_NM) {
        this.CMPNM_NM = CMPNM_NM;
    }

    public String getINDUTYPE_NM() {
        return INDUTYPE_NM;
    }

    public void setINDUTYPE_NM(String INDUTYPE_NM) {
        this.INDUTYPE_NM = INDUTYPE_NM;
    }

    public String getSIGUN_NM() {
        return SIGUN_NM;
    }

    public void setSIGUN_NM(String SIGUN_NM) {
        this.SIGUN_NM = SIGUN_NM;
    }

    public String getREFINE_ROADNM_ADDR() {
        return REFINE_ROADNM_ADDR;
    }

    public void setREFINE_ROADNM_ADDR(String REFINE_ROADNM_ADDR) {
        this.REFINE_ROADNM_ADDR = REFINE_ROADNM_ADDR;
    }

    public String getREFINE_LOTNO_ADDR() {
        return REFINE_LOTNO_ADDR;
    }

    public void setREFINE_LOTNO_ADDR(String REFINE_LOTNO_ADDR) {
        this.REFINE_LOTNO_ADDR = REFINE_LOTNO_ADDR;
    }

    public String getDETAIL_ADDR() {
        return DETAIL_ADDR;
    }

    public void setDETAIL_ADDR(String DETAIL_ADDR) {
        this.DETAIL_ADDR = DETAIL_ADDR;
    }

    public String getBSN_TM_NM() {
        return BSN_TM_NM;
    }

    public void setBSN_TM_NM(String BSN_TM_NM) {
        this.BSN_TM_NM = BSN_TM_NM;
    }

    public Double getREFINE_WGS84_LAT() {
        return REFINE_WGS84_LAT;
    }

    public void setREFINE_WGS84_LAT(Double REFINE_WGS84_LAT) {
        this.REFINE_WGS84_LAT = REFINE_WGS84_LAT;
    }

    public Double getREFINE_WGS84_LOGT() {
        return REFINE_WGS84_LOGT;
    }

    public void setREFINE_WGS84_LOGT(Double REFINE_WGS84_LOGT) {
        this.REFINE_WGS84_LOGT = REFINE_WGS84_LOGT;
    }
}
