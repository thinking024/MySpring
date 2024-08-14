package org.springframework.bean;

import java.time.LocalDate;

public class CarWithConvert extends Car {
    private int price;

    private LocalDate produceDate;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(LocalDate produceDate) {
        this.produceDate = produceDate;
    }

    @Override
    public String toString() {
        return "CarWithConvert{" +
                "price=" + price +
                ", produceDate=" + produceDate +
                "} " + super.toString();
    }
}
