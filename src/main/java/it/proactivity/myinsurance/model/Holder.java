package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
public class Holder {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @NotNull
    @Size(max = 100)
    @Column(name = "name")
    private String name;

    @NotBlank
    @NotNull
    @Size(max = 100)
    @Column(name = "surname")
    private String surname;


    @NotNull
    @Column(name = "birth_date")
    private Date birthDate;

    @NotBlank
    @NotNull
    @Size(max = 25)
    @Column(name = "fiscal_code")
    private String fiscalCode;

    @NotBlank
    @NotNull
    @Size(max = 250)
    @Column(name = "residence")
    private String residence;

    @NotBlank
    @NotNull
    @Size(max = 250)
    @Column(name = "domicile")
    private String domicile;

    @NotBlank
    @NotNull
    @Size(max = 20)
    @Column(name = "tel")
    private String tel;

    @NotBlank
    @NotNull
    @Size(min = 10, max = 50)
    @Column(name = "email")
    private String email;


    @Size(max = 20)
    @Column(name = "login")
    private String login;


    @Size(max = 20)
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "holder")
    private List<Quote> quoteList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<Quote> quoteList) {
        this.quoteList = quoteList;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "id=" + id +
                ", name='" + name +
                ", surname='" + surname +
                ", birthDate=" + birthDate +
                ", fiscalCode='" + fiscalCode +
                ", residence='" + residence +
                ", domicile='" + domicile +
                ", tel='" + tel + '\'' +
                ", email='" + email +
                ", login='" + login +
                ", password='" + password +
                '}';
    }
}
