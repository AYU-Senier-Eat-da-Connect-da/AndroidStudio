package com.eatda.president.PresidentBusinessNumberCheck;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PresidentBusinessResponse {

    @SerializedName("request_cnt")
    private int requestCnt;

    @SerializedName("match_cnt")
    private int matchCnt;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("data")
    private List<BusinessData> data;

    // Inner class for Business Data
    public static class BusinessData {

        @SerializedName("b_no")
        private String businessNumber;

        @SerializedName("b_stt")
        private String businessStatus;

        @SerializedName("b_stt_cd")
        private String businessStatusCode;

        @SerializedName("tax_type")
        private String taxType;

        @SerializedName("tax_type_cd")
        private String taxTypeCode;

        @SerializedName("end_dt")
        private String endDate;

        @SerializedName("utcc_yn")
        private String utccYn;

        @SerializedName("tax_type_change_dt")
        private String taxTypeChangeDate;

        @SerializedName("invoice_apply_dt")
        private String invoiceApplyDate;

        @SerializedName("rbf_tax_type")
        private String rbfTaxType;

        @SerializedName("rbf_tax_type_cd")
        private String rbfTaxTypeCode;

        // Getters and setters for all fields
        public String getBusinessNumber() { return businessNumber; }
        public void setBusinessNumber(String businessNumber) { this.businessNumber = businessNumber; }

        public String getBusinessStatus() { return businessStatus; }
        public void setBusinessStatus(String businessStatus) { this.businessStatus = businessStatus; }

        public String getBusinessStatusCode() { return businessStatusCode; }
        public void setBusinessStatusCode(String businessStatusCode) { this.businessStatusCode = businessStatusCode; }

        public String getTaxType() { return taxType; }
        public void setTaxType(String taxType) { this.taxType = taxType; }

        public String getTaxTypeCode() { return taxTypeCode; }
        public void setTaxTypeCode(String taxTypeCode) { this.taxTypeCode = taxTypeCode; }

        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }

        public String getUtccYn() { return utccYn; }
        public void setUtccYn(String utccYn) { this.utccYn = utccYn; }

        public String getTaxTypeChangeDate() { return taxTypeChangeDate; }
        public void setTaxTypeChangeDate(String taxTypeChangeDate) { this.taxTypeChangeDate = taxTypeChangeDate; }

        public String getInvoiceApplyDate() { return invoiceApplyDate; }
        public void setInvoiceApplyDate(String invoiceApplyDate) { this.invoiceApplyDate = invoiceApplyDate; }

        public String getRbfTaxType() { return rbfTaxType; }
        public void setRbfTaxType(String rbfTaxType) { this.rbfTaxType = rbfTaxType; }

        public String getRbfTaxTypeCode() { return rbfTaxTypeCode; }
        public void setRbfTaxTypeCode(String rbfTaxTypeCode) { this.rbfTaxTypeCode = rbfTaxTypeCode; }
    }

    // Getters and setters for outer class fields
    public int getRequestCnt() { return requestCnt; }
    public void setRequestCnt(int requestCnt) { this.requestCnt = requestCnt; }

    public int getMatchCnt() { return matchCnt; }
    public void setMatchCnt(int matchCnt) { this.matchCnt = matchCnt; }

    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }

    public List<BusinessData> getData() { return data; }
    public void setData(List<BusinessData> data) { this.data = data; }
}

