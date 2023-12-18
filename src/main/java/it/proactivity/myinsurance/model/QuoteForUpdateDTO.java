package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class QuoteForUpdateDTO {
    public QuoteForUpdateDTO() {
    }


    @NotNull
    private Long Id;

    private List<String> OptionalExtraByCodeList = new ArrayList<>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<String> getOptionalExtraByCodeList() {
        return OptionalExtraByCodeList;
    }

    public void setOptionalExtraByCodeList(List<String> optionalExtraByCodeList) {
        OptionalExtraByCodeList = optionalExtraByCodeList;
    }
}



