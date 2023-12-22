package it.proactivity.myinsurance.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class QuoteForDeleteDTO {
    public QuoteForDeleteDTO() {
    }


    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    @NotNull
    @Size(max = 10)
    private String registrationMark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationMark() {
        return registrationMark;
    }

    public void setRegistrationMark(String registrationMark) {
        this.registrationMark = registrationMark;
    }


    public Boolean isCorrect(){
        return
                (this.getId() != null && this.getId() >0)  &&
                (this.getRegistrationMark() != null && this.getRegistrationMark().length() >0) ;
    }

    @Override
    public String toString() {
        return "QuoteForDeleteDTO{" +
                "Id=" + id +
                ", registrationMark='" + registrationMark + '\'' +
                '}';
    }
}



