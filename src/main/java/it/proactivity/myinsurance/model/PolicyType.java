package it.proactivity.myinsurance.model;

import java.math.BigDecimal;

public enum PolicyType {
    RCA6,RCA12,RCA50;

    public BigDecimal getPercentage(){
        if(this == RCA6)
            return new BigDecimal(0.1);
        else if(this == RCA12)
            return new BigDecimal(0.12);
        else if (this == RCA50)
            return new BigDecimal(0.25);
        else
            return new BigDecimal(0);
    }
}
