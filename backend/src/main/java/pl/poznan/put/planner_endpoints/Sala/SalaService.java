package pl.poznan.put.planner_endpoints.Sala;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Logika biznesowa dla Sal
 */
@Service
public class SalaService {
    /**
     * repozytorium tabeli Sale
     */
    @Autowired
    private SalaRepository salaRepository;

    /**
     * Zwraca wszystkie sale
     * @return lista Sala
     */
    public List<Sala> getAllSala(){
        return salaRepository.findAll(Sort.by(Sort.Direction.ASC, "numer"));
    }

    /**
     * Do pagincaji - zwraca sale z podanej strony
     * @param page numer strony
     * @param size liczba stron
     * @return Obiekt Page zawierający znalezione sale
     */
    public Page<Sala> getSalaPage(Integer page, Integer size){
        return salaRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Zwraca salę po numerze, lub empty Optional
     * @param salaID numer sali oraz budynek (klucz złożony)
     * @return Optional - pusty lub z wyszukaną salą
     */
    public Optional<Sala> getSalaByID(SalaCompositeKey salaID){
        return salaRepository.findById(salaID);
    }

    /**
     * Tworzy salę
     * @param sala obiekt sali do wstawienia do bazy danych
     * @return zapisana sala
     */
    public Sala createSala(Sala sala){
        return salaRepository.save(sala);
    }

    /**
     * Aktualizuje salę jeśli istnieje
     * @param salaID numer sali oraz budynek (klucz złożony)
     * @param salaParams nowe wartości pól w formacie JSON
     * @return obiekt zaktualizowanej sali lub null
     */
    public Sala updateSalaByID(SalaCompositeKey salaID, Sala salaParams){
        Optional<Sala> sala = salaRepository.findById(salaID);
        if (sala.isPresent()) {
            Sala oldSala = sala.get();
            //oldSala.numer = salaParams.numer;
            //oldSala.budynek = salaParams.budynek;
            oldSala.pietro = salaParams.pietro;
            oldSala.liczba_miejsc = salaParams.liczba_miejsc;
            oldSala.typ = salaParams.typ;
            oldSala.opiekun = salaParams.opiekun;
            return salaRepository.save(oldSala);
        } else {
            return null;
        }
    }

    /**
     * Usuwa salę o podanym numerze
     * @param salaID numer sali oraz budynek (klucz złożony)
     */
    public void deleteSalaByID(SalaCompositeKey salaID){
        salaRepository.deleteById(salaID);
    }

    /**
     * Usuwa wszystkie sale
     */
    public void deleteAllSale(){
        salaRepository.deleteAll();
    }

    // Tu będzie więcej...
}
