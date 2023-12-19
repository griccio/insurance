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


    public List<Car> findByHolder(Long holderId) {
        return new QCar().holder.id.eq(holderId).findList();
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
