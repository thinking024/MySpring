package org.springframework.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonWithCar extends Person {
    @Autowired
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "PersonWithCar{" +
                "car=" + car +
                "} " + super.toString();
    }
}
