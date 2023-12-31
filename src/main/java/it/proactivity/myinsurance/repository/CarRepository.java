package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import it.proactivity.myinsurance.model.Car;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.query.QCar;
import it.proactivity.myinsurance.model.query.QHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarRepository {

    public List<Car> findAll() {
        return new QCar().findList();
    }

    public Boolean existCar(String registrationMark){
        return (new QCar().registrationMark.eq(registrationMark).findCount()  > 0 );
    }

    public Boolean carBelongTo(String registrationMark, Long holderId){
        return (new QCar().registrationMark.eq(registrationMark).holder.id.eq(holderId).findCount() >0);
    }

    public List<Car> findByHolder(Long holderId) {
        return new QCar().holder.id.eq(holderId).findList();
    }

    public Car findByRegistrationMark(String registrationMark) {
        return  new QCar().registrationMark.eq(registrationMark).findOne();
    }

    public Car save(Car car) {
        DB.save(car);
        return car;
    }


    public Car update(Car car) {
        DB.update(car);
        return car;
    }

    public Boolean delete(Car car) {
        return DB.delete(car);
    }

}
