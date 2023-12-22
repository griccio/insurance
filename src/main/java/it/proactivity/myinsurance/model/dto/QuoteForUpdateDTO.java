package it.proactivity.myinsurance.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


public class QuoteForUpdateDTO {
    public QuoteForUpdateDTO() {
    }


    @NotNull
    @Min(1)
    private Long Id;

    private List<String> optionalExtraByCodeList = new ArrayList<>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<String> getOptionalExtraByCodeList() {
        return optionalExtraByCodeList;
    }

    public void setOptionalExtraByCodeList(List<String> optionalExtraByCodeList) {
        this.optionalExtraByCodeList = optionalExtraByCodeList;
    }
}



