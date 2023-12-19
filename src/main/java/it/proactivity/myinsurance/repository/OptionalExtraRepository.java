package it.proactivity.myinsurance.repository;

import it.proactivity.myinsurance.model.MyInsuranceConstants;
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


    /**
     * return tot of optional extras included into a Kasko
     * @return
     */
    public int getTotOptionalExtrasIntoKasko(){

        return  new  QOptionalExtra().code.ne(MyInsuranceConstants.KASKO_CODE).findCount();
    }

}
