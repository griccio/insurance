package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


public class HolderDTO {
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String surname;

    @NotNull
    @Column(name = "birth_date")
    private Date birthDate;

    @NotBlank
    @NotNull
    @Size(max = 25)
    private String fiscalCode;

    @NotBlank
    @NotNull
    @Size(max = 250)
    private String residence;

    @NotBlank
    @NotNull
    @Size(max = 250)
    private String domicile;

    @NotBlank
    @NotNull
    @Size(max = 20)
    private String tel;

    @NotBlank
    @NotNull
    @Size(min = 10, max = 50)
    private String email;

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

    @Override
    public String toString() {
        return "HolderDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", residence='" + residence + '\'' +
                ", domicile='" + domicile + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void map(Holder holder){
        this.name = holder.getName();
        this.surname = holder.getSurname();
        this.birthDate = holder.getBirthDate();
        this.fiscalCode = holder.getFiscalCode();
        this.residence = holder.getResidence();
        this.domicile = holder.getDomicile();
        this.tel = holder.getTel();
        this.email = holder.getEmail();
    }

}
