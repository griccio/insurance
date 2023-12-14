package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import io.ebean.SqlRow;
import it.proactivity.myinsurance.exception.QuoteException;
import it.proactivity.myinsurance.model.Quote;
import it.proactivity.myinsurance.model.query.QQuote;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class QuoteRepository {

    public List<Quote> findAll() {
        return new QQuote().orderBy("quoteNumber").findList();
    }

    public List<Quote> findAllWithHolderData() {

        return new QQuote().orderBy("quoteNumber").holder.fetch().findList();
    }


    public Quote findById(Long id) {
        return DB.find(Quote.class, id);
    }

    public List<Quote> findByRegistrationMark(String registrationMark){

        return new QQuote()
                .orderBy("quoteNumber")
                .holder.fetch()
                .registrationMark.eq(registrationMark)
                .findList();

    }


    public List<Quote> findByHolderId(Long id){
        return new QQuote()
                .orderBy("quoteNumber")
                .holder.fetch()
                .holder.id.eq(id)
                .findList();
    }


    public List<Quote> findByHolderIdAndRegistrationMark(Long holderId, String registrationMark){
        return new QQuote()
                .orderBy("quoteNumber")
                .holder.fetch()
                .holder.id.eq(holderId)
                .registrationMark.eq(registrationMark)
                .findList();
    }



    public Quote save(Quote quote) {
        DB.save(quote);
        // Note that the quoteNumber is updated with a trigger
        return quote;
    }


    public Quote update(Quote Quote) {
        DB.update(Quote);
        return Quote;
    }

    public Boolean delete(Quote quote) {
        return DB.delete(quote);
    }


    public Boolean verifyRegistrationMark(String registrationMark) throws QuoteException {
        Pattern pattern = Pattern.compile("[a-zA-Z1-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(registrationMark);
        if(!matcher.find())
            throw new QuoteException("Registration Mark is invalid");

        String sql = "select count(id) as tot from quote where registration_mark = :registrationMark";

        SqlRow row = DB.sqlQuery(sql)
                .setParameter("registrationMark", registrationMark)
                .findOne();

        Long count = row.getLong("tot");

        if(count != null && count == 2)
            throw new QuoteException("You cannot create more than two quotes for the same registration mark ");

        return true;
    }
    
}
