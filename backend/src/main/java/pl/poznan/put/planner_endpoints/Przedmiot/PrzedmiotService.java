package pl.poznan.put.planner_endpoints.Przedmiot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Logika biznesowa dla Przedmiotów
 */
@Service
public class PrzedmiotService {
    /**
     * repozytorium tabeli Przedmioty
     */
    @Autowired
    private PrzedmiotRepository przedmiotRepository;

    /**
     * Zwraca wszystkie Przedmioty
     * @return lista Przedmiot
     */
    public List<Przedmiot> getAllPrzedmiot(){
        return przedmiotRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Do pagincaji - zwraca przedmioty z podanej strony
     * @param page numer strony
     * @param size liczba stron
     * @return Obiekt Page zawierający znalezione przedmioty
     */
    public Page<Przedmiot> getPrzedmiotPage(Integer page, Integer size){
        return przedmiotRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Zwraca Przedmiot po numerze, lub empty Optional
     * @param id id przedmiotu
     * @return Optional - pusty lub z wyszukanym przedmiotem
     */
    public Optional<Przedmiot> getPrzedmiotByID(Integer id){
        return przedmiotRepository.findById(id);
    }

    /**
     * Tworzy przedmiot
     * @param przedmiot obiekt przedmiotu do wstawienia do bazy danych
     * @return zapisany przedmiot
     */
    public Przedmiot createPrzedmiot(Przedmiot przedmiot){
        return przedmiotRepository.save(przedmiot);
    }

    /**
     * Aktualizuje Przedmiot jeśli istnieje
     * @param id numer przedmiotu
     * @param przedmiotParams nowe wartości pól w formacie JSON
     * @return obiekt zaktualizowanego Przedmiotu lub null
     */
    public Przedmiot updatePrzedmiotByID(Integer id, Przedmiot przedmiotParams){
        Optional<Przedmiot> przedmiot = przedmiotRepository.findById(id);
        if (przedmiot.isPresent()) {
            Przedmiot oldPrzedmiot = przedmiot.get();
            oldPrzedmiot.nazwa = przedmiotParams.nazwa;
            oldPrzedmiot.liczba_godzin = przedmiotParams.liczba_godzin;
            oldPrzedmiot.semestr = przedmiotParams.semestr;
            oldPrzedmiot.kierunek = przedmiotParams.kierunek;
            oldPrzedmiot.jezyk = przedmiotParams.jezyk;
            oldPrzedmiot.specjalnosc = przedmiotParams.specjalnosc;
            oldPrzedmiot.typ = przedmiotParams.typ;
            oldPrzedmiot.egzamin = przedmiotParams.egzamin;
            return przedmiotRepository.save(oldPrzedmiot);
        } else {
            return null;
        }
    }

    /**
     * Usuwa Przedmiot o podanym numerze
     * @param id id Przedmiotu
     */
    public void deletePrzedmiotByID(Integer id){
        przedmiotRepository.deleteById(id);
    }

    /**
     * Usuwa wszystkie Przedmioty
     */
    public void deleteAllPrzedmioty(){
        przedmiotRepository.deleteAll();
    }

    // Tu będzie więcej...
}
