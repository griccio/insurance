package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


public class QuoteForDeleteDTO {
    public QuoteForDeleteDTO() {
    }


    @NotNull
    private Long Id;

    @NotBlank
    @NotNull
    @Size(max = 10)
    private String registrationMark;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getRegistrationMark() {
        return registrationMark;
    }

    public void setRegistrationMark(String registrationMark) {
        this.registrationMark = registrationMark;
    }

    @Override
    public String toString() {
        return "QuoteForDeleteDTO{" +
                "Id=" + Id +
                ", registrationMark='" + registrationMark + '\'' +
                '}';
    }
}



