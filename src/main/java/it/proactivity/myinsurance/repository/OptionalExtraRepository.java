package it.proactivity.myinsurance.repository;

import it.proactivity.myinsurance.model.OptionalExtra;
import it.proactivity.myinsurance.model.query.QOptionalExtra;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionalExtraRepository {
    public List<OptionalExtra> findAll(){
        return  new QOptionalExtra().findList();
    }


    public OptionalExtra findByCode(String code){
        return  new  QOptionalExtra().code.eq(code).findOne();
    }


}
