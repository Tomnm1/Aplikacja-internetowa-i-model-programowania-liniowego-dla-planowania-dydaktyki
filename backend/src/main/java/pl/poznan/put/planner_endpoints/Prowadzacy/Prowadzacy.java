package pl.poznan.put.planner_endpoints.Prowadzacy;

import jakarta.persistence.*;

/**
 * Model danych dla tabeli Prowadzacy
 */
@Entity
@Table(name="prowadzacy")
public class Prowadzacy {
    @Id
    public Integer id;
    public String imie;
    public String nazwisko;
    public String stopien;
    //public Json preferencje; // jak będzie uspecifikowane dokładnie: @Convert(converter = prefConverter.class)
}