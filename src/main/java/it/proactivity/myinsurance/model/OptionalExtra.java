package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class OptionalExtra {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @NotNull
    @Size(max = 2)
    @Column(name = "code")
    private String code;


    @NotBlank
    @NotNull
    @Size(max = 100)
    @Column(name = "name")
    private String name;


    @ManyToMany(mappedBy="optionalExtras")
    private List<Quote> quotes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    @Override
    public String toString() {
        return "OptionalExtras{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
