package it.proactivity.myinsurance.model;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class QuoteWithOptionalExtraDTO {

    private String holder;

    private String registrationMark;

    private Date registrationDateCar;

    private BigDecimal worth;

    @Enumerated(EnumType.STRING)
    private PolicyType policyType;

    private BigDecimal cost;

    private String quoteNumber;

    private Date date;

    private List<String> optionalExtras = new ArrayList<>();

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

    public List<String> getOptionalExtras() {
        return optionalExtras;
    }

    public void setOptionalExtras(List<String> optionalExtras) {
        this.optionalExtras = optionalExtras;
    }

    public void map(Quote quote){
        BeanUtils.copyProperties(quote,this);
        this.setHolder(quote.getHolder());
        this.registrationMark = quote.getCar().getRegistrationMark();
        this.setRegistrationDateCar(quote.getCar().getRegistrationDate());
        this.setWorth(quote.getCar().getWorth());
        this.optionalExtras = quote.getOptionalExtras().stream().map(extra -> {
            String name = extra.getName();
            return name;
        }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "QuoteWithOptionalExtraDTO{" +
                "holder='" + holder + '\'' +
                ", registrationMark='" + registrationMark + '\'' +
                ", registrationDateCar=" + registrationDateCar +
                ", worth=" + worth +
                ", policyType=" + policyType +
                ", cost=" + cost +
                ", quoteNumber='" + quoteNumber + '\'' +
                ", date=" + date +
                ", optionalExtras=" + optionalExtras +
                '}';
    }
}
