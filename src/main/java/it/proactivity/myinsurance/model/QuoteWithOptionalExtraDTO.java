package it.proactivity.myinsurance.model;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
