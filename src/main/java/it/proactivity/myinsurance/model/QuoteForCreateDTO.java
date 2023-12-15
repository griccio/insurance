package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


public class QuoteForCreateDTO {
    public QuoteForCreateDTO() {
    }

    public QuoteForCreateDTO(Long holderId, String registrationMark, Date registrationDateCar, BigDecimal worth, PolicyType policyType) {
        this.holderId = holderId;
        this.registrationMark = registrationMark;
        this.registrationDateCar = registrationDateCar;
        this.worth = worth;
        this.policyType = policyType;
    }


    @NotNull
    private Long holderId;

    @NotBlank
    @NotNull
    @Size(max = 10)
    private String registrationMark;

    @NotNull
    private Date registrationDateCar;

    @NotNull
    private BigDecimal worth;

    @NotNull
    private PolicyType policyType;

    public Long getHolderId() {
        return holderId;
    }

    public void setHolderId(Long holderId) {
        this.holderId = holderId;
    }

    public String getRegistrationMark() {
        return registrationMark;
    }

    public void setRegistrationMark(String registrationMark) {
        this.registrationMark = registrationMark;
    }

    @Override
    public String toString() {
        return "QuoteForCreateDTO{" +
                "holderId=" + holderId +
                ", registrationMark='" + registrationMark + '\'' +
                ", registrationDateCar=" + registrationDateCar +
                ", worth=" + worth +
                ", policyType=" + policyType +
                '}';
    }

    public Date getRegistrationDateCar() {
        return registrationDateCar;
    }

    public void setRegistrationDateCar(Date registrationDateCar) {
        this.registrationDateCar = registrationDateCar;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }
}
