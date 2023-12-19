package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Car {


    public Car() {
    }

    public Car(Holder holder, String registrationMark, Date registrationDate, BigDecimal worth) {
        this.holder = holder;
        this.registrationMark = registrationMark;
        this.registrationDate = registrationDate;
        this.worth = worth;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional=false) //optional=false is because field is NOT NULL
    @JoinColumn(name="holder_id")
    private Holder holder;

    @OneToMany(mappedBy = "car")
    List<Quote> quoteList = new ArrayList<>();


    @NotBlank
    @NotNull
    @Size(max = 10)
    @Column(name = "registration_mark")
    private String registrationMark;

    @NotNull
    @Column(name = "registration_date")
    private Date registrationDate;

    @NotNull
    @NotBlank
    @Column(name = "worth", columnDefinition = "NUMERIC(9,2)")
    private BigDecimal worth;

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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<Quote> quoteList) {
        this.quoteList = quoteList;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", holderId=" + holder.getId() +
                ", registrationMark='" + registrationMark + '\'' +
                ", registrationDate=" + registrationDate +
                ", worth=" + worth +
                '}';
    }
}
