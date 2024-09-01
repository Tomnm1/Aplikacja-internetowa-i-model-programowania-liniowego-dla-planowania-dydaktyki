package pl.poznan.put.planner_endpoints.Sala;

import java.io.Serializable;
import java.util.Objects;

public class SalaCompositeKey implements Serializable {
    private Integer numer;
    private String budynek;

    public SalaCompositeKey(){}

    public SalaCompositeKey(String budynek, Integer numer) {
        this.budynek = budynek;
        this.numer = numer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalaCompositeKey salaID = (SalaCompositeKey) o;
        return budynek.equals(salaID.budynek) &&
                numer.equals(salaID.numer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numer, budynek);
    }
}
