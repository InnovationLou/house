package xyz.nadev.house.VO;

import xyz.nadev.house.entity.House;

import javax.persistence.Entity;

@Entity
public class HouseDTO extends House {
    Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


}
