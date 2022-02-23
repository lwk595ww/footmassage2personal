package com.seetech.footmassage2.core.configration.jacksonConfig;


public class LongWithStringConvert {

    private final Long val;

    public Long getVal() {
        return val;
    }

    public LongWithStringConvert(Long val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}