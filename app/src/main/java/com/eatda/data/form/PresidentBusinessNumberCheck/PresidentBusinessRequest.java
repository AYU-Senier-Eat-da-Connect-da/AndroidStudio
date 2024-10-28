package com.eatda.president.PresidentBusinessNumberCheck;

import java.util.Collections;
import java.util.List;

public class PresidentBusinessRequest {
    private List<String> b_no;

    public PresidentBusinessRequest(List<String> b_no){
        this.b_no = b_no;
    }

    public List<String> getB_no() {
        return b_no;
    }

    public void setB_no(List<String> b_no) {
        this.b_no = b_no;
    }
}

