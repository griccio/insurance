package it.proactivity.myinsurance.model;

import it.proactivity.myinsurance.model.query.QQuote;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
public class Quote {


    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(optional=false) //optional=false is because field is NOT NULL
    @JoinColumn(name="holder_id")
    private Holder holder;

    @ManyToOne(optional=false) //optional=false is because field is NOT NULL
    @JoinColumn(name="car_id")
    private Car car;

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

    @Column(name="quote_number")
    private String quoteNumber;

    @Column(name="date")
    private Date date;

    @ManyToMany
    @JoinTable(name="quote_optional_extra",
            joinColumns = @JoinColumn(name="quote_id"),
            inverseJoinColumns = @JoinColumn(name="optional_extra_id")
    )
    private List<OptionalExtra> optionalExtras = new ArrayList<>();

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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
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

    public List<OptionalExtra> getOptionalExtras() {
        return optionalExtras;
    }

    public void addOptionalExtra(OptionalExtra optionalExtras){
       this.optionalExtras.add(optionalExtras);
    }

    public void removeOptionalExtra(OptionalExtra optionalExtras){
        this.optionalExtras.remove(optionalExtras);
    }

    /**
     * remove all the optional extras and add only the kasko.
     * Kasko include all the services.
     * @param kasko
     */
    public void addKaskoOptionalExtra(OptionalExtra kasko){
        this.optionalExtras.clear();
        this.optionalExtras.add(kasko);
    }

    public Boolean hasKasko(){
        if(this.optionalExtras== null || optionalExtras.size() == 0)
            return false;
        for(OptionalExtra extra : this.optionalExtras) {
            if (extra.isKasko())
                return (true);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", holder=" + (holder != null ? holder.getId() : "???") +
                ", car=" + car.getRegistrationMark() +
                ", policyType=" + policyType +
                ", cost=" + cost +
                ", quoteNumber=" + quoteNumber +
                ", date='" + date +'}';
    }
}
