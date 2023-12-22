package it.proactivity.myinsurance.model.dto;

import it.proactivity.myinsurance.model.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class QuoteWithOptionalExtraDTO extends QuoteDTO{

    private List<String> optionalExtras = new ArrayList<>();

    public List<String> getOptionalExtras() {
        return optionalExtras;
    }

    public void setOptionalExtras(List<String> optionalExtras) {
        this.optionalExtras = optionalExtras;
    }

    public void map(Quote quote){
        super.map(quote);
        this.optionalExtras = quote.getOptionalExtras().stream().map(extra -> {
            String name = extra.getName();
            return name;
        }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString() +
                ", optionalExtras=" + optionalExtras;
    }
}
