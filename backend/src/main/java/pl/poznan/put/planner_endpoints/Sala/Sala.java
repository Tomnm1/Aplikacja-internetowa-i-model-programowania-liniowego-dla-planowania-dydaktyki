package pl.poznan.put.planner_endpoints.Sala;

import jakarta.persistence.*;

/**
 * Model danych dla tabeli Sale
 */
@Entity
@Table(name="sale")
@IdClass(SalaCompositeKey.class)
public class Sala {
    @Id
    public Integer numer;
    @Id
    public String budynek;
    public Short pietro;
    public Integer liczba_miejsc;
    public String typ;
    public Integer opiekun;
}