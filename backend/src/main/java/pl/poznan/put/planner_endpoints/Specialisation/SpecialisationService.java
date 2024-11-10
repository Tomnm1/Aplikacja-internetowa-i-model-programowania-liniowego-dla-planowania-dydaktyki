package pl.poznan.put.planner_endpoints.Specialisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for specialisations
 */
@Service
public class SpecialisationService {
    @Autowired
    private SpecialisationRepository specialisationRepository;

    /**
     * return all specialisations
     * @return list of all specialisation objects
     */
    public List<Specialisation> getAllSpecialisations() {return specialisationRepository.findAll(Sort.by(Sort.Direction.ASC, "specialisationId"));}

    /**
     * Finds room by ID
     * @param specialisationId ID of specialisation
     * @return Optional - empty or with Specialisation
     */
    public Optional<Specialisation> getSpecialisationByID(Integer specialisationId) {return specialisationRepository.findById(specialisationId);}

    /**
     * Creates a specialisation
     * @param specialisation specialisation object to be inserted into DB
     * @return saved specialisation
     */
    public Specialisation createSpecialisation(Specialisation specialisation) {return specialisationRepository.save(specialisation);}

    /**
     * Updates a specialisation by given ID and params
     * @param specialisationId id of specialisation to update
     * @param specialisationParams params to update
     * @return Optional - null or with updated specialisation
     */
    public Specialisation updateSpecialisationByID(Integer specialisationId, Specialisation specialisationParams){
        Optional<Specialisation> specialisation = specialisationRepository.findById(specialisationId);
        if (specialisation.isPresent()){
            Specialisation oldSpecialisation = specialisation.get();
            oldSpecialisation.name = specialisationParams.name;
            oldSpecialisation.cycle = specialisationParams.cycle;
            oldSpecialisation.fieldOfStudy = specialisationParams.fieldOfStudy;
            return specialisationRepository.save(oldSpecialisation);
        } else {
            return null;
        }
    }

    /**
     * Deletes specialisation by ID
     * @param specialisationId ID of specialisation
     */
    public void deleteSpecialisationByID(Integer specialisationId){
        specialisationRepository.deleteById(specialisationId);
    }

    /**
     * Deletes all specialisations
     */
    public void deleteAllSpecialisations(){
        specialisationRepository.deleteAll();
    }

    public Specialisation createSpecialisationIfNotExists(Specialisation specialisation){
        boolean exists = specialisationRepository.existsByNameAndCycleAndFieldOfStudy(specialisation.name, specialisation.cycle, specialisation.fieldOfStudy);
        if(exists){
            return null;
        } else {
            createSpecialisation(specialisation);
            return specialisation;
        }
    }
}
