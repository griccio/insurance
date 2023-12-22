package it.proactivity.myinsurance.model.dto;

import it.proactivity.myinsurance.model.PolicyType;
import it.proactivity.myinsurance.model.Quote;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

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
    @Min(1)
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




    public void map(Quote quote){
        BeanUtils.copyProperties(quote,this);
        this.setHolderId(quote.getHolder().getId());
        this.registrationMark = quote.getCar().getRegistrationMark();
        this.registrationDateCar = quote.getCar().getRegistrationDate();
        this.worth = quote.getCar().getWorth();
    }



    public boolean isCorrect (){

        if(this.holderId == null) return false;
        if(registrationMark == null || registrationMark.length()==0) return false;
        if(registrationDateCar == null) return false;
        if(this.worth == null) return false;

        return true;
    }
}

