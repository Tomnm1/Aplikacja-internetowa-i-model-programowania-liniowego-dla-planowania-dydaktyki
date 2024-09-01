package pl.poznan.put.planner_endpoints.Room;

import java.io.Serializable;
import java.util.Objects;

public class RoomCompositeKey implements Serializable {
    private Integer number;
    private String building;

    public RoomCompositeKey(){}

    public RoomCompositeKey(String building, Integer number) {
        this.building = building;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomCompositeKey salaID = (RoomCompositeKey) o;
        return building.equals(salaID.building) &&
                number.equals(salaID.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, building);
    }
}
