package it.proactivity.myinsurance.model;

import java.util.ArrayList;
import java.util.List;

public class QuoteByHolderAndCarDTO {
    private Long holder_id;
    private String  registrationMark;


    public QuoteByHolderAndCarDTO() {
    }

    public QuoteByHolderAndCarDTO(Long holder_id) {
        this.holder_id = holder_id;
    }

    private List<QuoteDTO> quotes = new ArrayList<>();


    public Long getHolder_id() {
        return holder_id;
    }

    public void setHolder_id(Long holder_id) {
        this.holder_id = holder_id;
    }

    public String getRegistrationMark() {
        return registrationMark;
    }

    public void setRegistrationMark(String registrationMark) {
        this.registrationMark = registrationMark;
    }

    public List<QuoteDTO> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuoteDTO> quotes) {
        this.quotes = quotes;
    }

    @Override
    public String toString() {
        return "QuoteByCarDTO{" +
                "holder_id=" + holder_id +
                ", registrationMark='" + registrationMark + '\'' +
                ", quotes=" + quotes +
                '}';
    }
}
