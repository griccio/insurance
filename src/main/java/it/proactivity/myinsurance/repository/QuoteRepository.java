package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import io.ebean.SqlRow;
import it.proactivity.myinsurance.model.Quote;
import it.proactivity.myinsurance.model.query.QQuote;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuoteRepository {

    public List<Quote> findAll() {
        return new QQuote().orderBy("quoteNumber").findList();
    }

    /**
     * return all the Quote with the info of holder
     * @return
     */
    public List<Quote> findAllWithHolderAndCarData() {
       return new QQuote().orderBy("quoteNumber")
               .fetch("holder").fetch("car").findList();
    }


    public Quote findById(Long id) {
        return DB.find(Quote.class, id);
    }


    public Quote findByIdWithHolderData(Long id) {
        return new QQuote().id.eq(id).holder.fetch().findOne();
    }


    public List<Quote> findByRegistrationMark(String registrationMark){

        return new QQuote()
                .orderBy("quoteNumber")
                .holder.fetch()
                .car.fetch().car.registrationMark.eq(registrationMark)
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
                .car.fetch().car.registrationMark.eq(registrationMark)
                .findList();
    }


    /**
     * return the list of registration marks of the cars owned by holder
     * @param holderId
     * @return
     */
    public List<String> getCarsListOwnedByHolder(Long holderId) {
        String sql = "select distinct(registration_mark) as registrationMark from quote where holder_id = :holderId";

        List<SqlRow> registrationsMarkByHolder = DB.sqlQuery(sql)
                .setParameter("holderId", holderId)
                .findList();

        List<String> registrationsMark = registrationsMarkByHolder.stream()
                                            .map(row -> {
                                                String registration = row.getString("registrationMark");
                                                return registration;})
                                            .toList();
        return (registrationsMark);
    }


    public Quote save(Quote quote) {
        DB.save(quote);
        return quote;
    }


    public Quote update(Quote Quote) {
        DB.update(Quote);
        return Quote;
    }

    public Boolean delete(Quote quote) {
        return DB.delete(quote);
    }


    /**
     * check if there are less than two plates into the quotes,
     * return true if there are none or if only one is found
     * @param registrationMark
     * @return
     */
    public Boolean verifyRegistrationMarkNumber(String registrationMark) {

        int count = new QQuote().car.registrationMark.eq(registrationMark).findCount();

        return(count < 2);

    }

    public Boolean verifyRegistrationMarkNumberExist(Long id, String registrationMark) {

        int count = new QQuote().id.eq(id).car.registrationMark.eq(registrationMark).findCount();

        return(count == 1);

    }

    public int getNextValidIndexByHolder(Long holderId) {
        int count = new QQuote().holder.id.eq(holderId).findCount();
        return(count+1);

    }

    public Boolean hasKasko(Long id,String registrationMark){
        int count = new QQuote().optionalExtras.code.eq("K").car.registrationMark.eq(registrationMark).findCount();
        return count > 0;
    }
}
