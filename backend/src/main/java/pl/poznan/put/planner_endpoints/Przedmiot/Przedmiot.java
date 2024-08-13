package pl.poznan.put.planner_endpoints.Przedmiot;

import jakarta.persistence.*;

/**
 * Model danych dla tabeli Przedmipty
 */
@Entity
@Table(name="przedmioty")
public class Przedmiot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Konrad
    public Integer id;
    public String nazwa;
    public Short liczba_godzin;
    public Short semestr;
    public String kierunek;
    public String jezyk;
    public String specjalnosc;
    public String typ;
    public Boolean egzamin;
}