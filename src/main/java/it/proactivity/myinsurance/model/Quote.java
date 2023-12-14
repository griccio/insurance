package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Quote {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(optional=false) //optional=false is because field is NOT NULL
    @JoinColumn(name="holder_id")
    private Holder holder;


    @NotBlank
    @NotNull
    @Size(max = 10)
    @Column(name = "registration_mark")
    private String registrationMark;

    @NotNull
    @Column(name = "registration_date_car")
    private Date registrationDateCar;

    @NotNull
    @NotBlank
    @Column(name = "worth", columnDefinition = "NUMERIC(9,2)")
    private BigDecimal worth;

    @NotNull
    @NotBlank
    @Size(min=4,max=5)
    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type")
    private PolicyType policyType;

    @NotNull
    @NotBlank
    @Column(name="cost", columnDefinition = "NUMERIC(9,2)")
    private BigDecimal cost;

    /**
     * Please note that this value is update by the DB with the trigger updateQuoteNumber
     * that create the number in this way: HolderId-RegistrationMark-QuoteId
     */
    @Column(name="quote_number")
    private String quoteNumber;

    @Column(name="date")
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
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

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", holder=" + (holder != null ? holder : "???") +
                ", registrationMark='" + registrationMark +
                ", registrationDateCar=" + registrationDateCar +
                ", worth=" + worth +
                ", policyType=" + policyType +
                ", cost=" + cost +
                ", quoteNumber='" + quoteNumber +
                ", date='" + date +'}';
    }
}