package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import io.ebean.SqlRow;
import it.proactivity.myinsurance.model.Car;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.query.QCar;
import it.proactivity.myinsurance.model.query.QHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HolderRepository {

    public List<Holder> findAll() {
        return new QHolder().orderBy("name").findList();
    }

    public Holder findById(Long id) {
        return new QHolder().id.eq(id).fetch("carList").findOne();
    }

    public List<Holder> findBySurname(String surname) {
        return new QHolder().surname.contains(surname).findList();
    }

    public List<Holder> findByName(String name) {
        return new QHolder().name.contains(name).findList();
    }


    public Holder findByFiscalCode(String fiscalCode) {
        return new QHolder().fiscalCode.eq(fiscalCode).findOne();
    }

    public List<String> getRegistrationMarks(Long holderId){
        return new QCar().holder.id.eq(holderId).select("registrationMark").findSingleAttributeList();
    }


    public Car getCar(Long holderId, String registrationMark){
        Car car = new QCar().holder.id.eq(holderId).registrationMark.eq(registrationMark).findOne();
        return car;
    }

    public Holder save(Holder holder) {
        DB.save(holder);
        return holder;
    }


    public Holder update(Holder holder) {
        DB.update(holder);
        return holder;
    }

    public Boolean delete(Holder holder) {
        return DB.delete(holder);
    }


    public Boolean verifyEmailExistence(String email) {
        String sql = "select count(id) as tot from holder where email = :email";

        SqlRow row = DB.sqlQuery(sql)
                .setParameter("email", email)
                .findOne();

       Long count = row.getLong("tot");

       return (count != null && count>0);
    }


    /**
     * verify if the holder's email has been assigned to another holder
     * return true if exist
     * @param email the mail to verify
     * @param id the email's owner id
     * @return
     */
    public Boolean verifyEmailForExistingHolder(String email, Long id) {
        String sql = "select count(id) as tot from holder where email = :email and id <> :id";

        SqlRow row = DB.sqlQuery(sql)
                .setParameter("email", email)
                .setParameter("id", id)
                .findOne();

        Long count = row.getLong("tot");

        return (count != null && count>0);
    }


    public Boolean verifyFiscalCodeExistence(String fiscalCode) {
        String sql = "select count(id) as tot from holder where fiscal_code = ?";

        SqlRow row = DB.sqlQuery(sql)
                .setParameter(1, fiscalCode)
                .findOne();
        Long count = row.getLong("tot");
        return (count != null && count>0);
    }


    public Boolean verifyFiscalCodeForExistingHolder(String fiscalCode, Long id) {
        String sql = "select count(id) as tot from holder where fiscal_code = :fiscalCode and id <> :id";

        SqlRow row = DB.sqlQuery(sql)
                .setParameter("fiscalCode", fiscalCode)
                .setParameter("id", id)
                .findOne();

        Long count = row.getLong("tot");

        return (count != null && count>0);
    }


}