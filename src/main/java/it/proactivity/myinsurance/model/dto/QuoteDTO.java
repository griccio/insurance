package it.proactivity.myinsurance.model.dto;

import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.PolicyType;
import it.proactivity.myinsurance.model.Quote;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


public class QuoteDTO {

    private String holder;

    private String registrationMark;

    private Date registrationDateCar;

    private BigDecimal worth;

    @Enumerated(EnumType.STRING)
    private PolicyType policyType;

    private BigDecimal cost;

    private String quoteNumber;

    private Date date;

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public void setHolder(Holder holder){
        this.holder = holder.getName() + " "+ holder.getSurname()
                + " (" + holder.getFiscalCode() + ")";
    }
    public String getRegistrationMark() {
        return registrationMark;
    }

    public void setRegistrationMark(String registrationMark) {
        this.registrationMark = registrationMark;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void map(Quote quote){
        BeanUtils.copyProperties(quote,this);
        this.setHolder(quote.getHolder());
        this.registrationMark = quote.getCar().getRegistrationMark();
        this.registrationDateCar = quote.getCar().getRegistrationDate();
        this.worth = quote.getCar().getWorth();
    }
}
