package pl.poznan.put.planner_endpoints.Prowadzacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Logika biznesowa dla Prowadzacych
 */
@Service
public class ProwadzacyService {
    /**
     * repozytorium tabeli Prowadzacy
     */
    @Autowired
    private ProwadzacyRepository prowadzacyRepository;

    /**
     * Zwraca wszystkich prowadzących
     * @return lista Prowadzacy
     */
    public List<Prowadzacy> getAllProwadzacy(){
        return prowadzacyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Do pagincaji - zwraca przedmioty z podanej strony
     * @param page numer strony
     * @param size liczba stron
     * @return Obiekt Page zawierający znalezionych prowadzących
     */
    public Page<Prowadzacy> getProwadzacyPage(Integer page, Integer size){
        return prowadzacyRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Zwraca Prowadzacego po numerze, lub empty Optional
     * @param id id Prowadzacego
     * @return Optional - pusty lub z wyszukanym przedmiotem
     */
    public Optional<Prowadzacy> getProwadzacyByID(Integer id){
        return prowadzacyRepository.findById(id);
    }

    /**
     * Tworzy Prowadzacego
     * @param prowadzacy obiekt przedmiotu do wstawienia do bazy danych
     * @return zapisany prowadzacy
     */
    public Prowadzacy createProwadzacy(Prowadzacy prowadzacy){
        return prowadzacyRepository.save(prowadzacy);
    }

    /**
     * Aktualizuje Prowadzacego jeśli istnieje
     * @param id numer Prowadzacego
     * @param prowadzacyParams nowe wartości pól w formacie JSON
     * @return obiekt zaktualizowanego Prowadzacego lub null
     */
    public Prowadzacy updateProwadzacyByID(Integer id, Prowadzacy prowadzacyParams){
        Optional<Prowadzacy> przedmiot = prowadzacyRepository.findById(id);
        if (przedmiot.isPresent()) {
            Prowadzacy oldProwadzacy = przedmiot.get();
            oldProwadzacy.imie = prowadzacyParams.imie;
            oldProwadzacy.nazwisko = prowadzacyParams.nazwisko;
            oldProwadzacy.stopien = prowadzacyParams.stopien;
            //oldProwadzacy.preferencje = prowadzacyParams.preferencje // później
            return prowadzacyRepository.save(oldProwadzacy);
        } else {
            return null;
        }
    }

    /**
     * Usuwa Prowadzacego o podanym numerze
     * @param id id Prowadzacego
     */
    public void deleteProwadzacyByID(Integer id){
        prowadzacyRepository.deleteById(id);
    }

    /**
     * Usuwa wszystkich Prowadzacych
     */
    public void deleteAllProwadzacy(){
        prowadzacyRepository.deleteAll();
    }
    // Tu będzie więcej...
}
