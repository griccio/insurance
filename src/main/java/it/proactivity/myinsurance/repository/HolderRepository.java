package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.query.QHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HolderRepository {

    public List<Holder> findAll() {
        return new QHolder().orderBy("name").findList();
    }

    //hello123
    public Holder findById(Long id) {
        return DB.find(Holder.class, id);
    }

    public List<Holder> findBySurname(String surname) {
        return new QHolder().surname.contains(surname).findList();
    }

    public List<Holder> findByName(String name) {
        return new QHolder().name.contains(name).findList();
    }


    public List<Holder> findByFiscalCode(String fiscalCode) {
        return new QHolder().fiscalCode.contains(fiscalCode).findList();
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


}