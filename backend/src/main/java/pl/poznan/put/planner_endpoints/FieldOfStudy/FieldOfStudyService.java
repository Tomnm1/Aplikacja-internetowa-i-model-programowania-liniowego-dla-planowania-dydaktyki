package pl.poznan.put.planner_endpoints.FieldOfStudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for fieldOfStudys
 */
@Service
public class FieldOfStudyService {
    @Autowired
    private FieldOfStudyRepository fieldOfStudyRepository;

    /**
     * return all fieldOfStudys
     * @return list of all fieldOfStudy objects
     */
    public List<FieldOfStudy> getAllFieldOfStudys() {return fieldOfStudyRepository.findAll(Sort.by(Sort.Direction.ASC, "fieldOfStudyId"));}

    /**
     * Finds room by ID
     * @param fieldOfStudyId ID of fieldOfStudy
     * @return Optional - empty or with FieldOfStudy
     */
    public Optional<FieldOfStudy> getFieldOfStudyByID(Integer fieldOfStudyId) {return fieldOfStudyRepository.findById(fieldOfStudyId);}

    /**
     * Creates a fieldOfStudy
     * @param fieldOfStudy fieldOfStudy object to be inserted into DB
     * @return saved fieldOfStudy
     */
    public FieldOfStudy createFieldOfStudy(FieldOfStudy fieldOfStudy) {return fieldOfStudyRepository.save(fieldOfStudy);}

    /**
     * Updates a fieldOfStudy by given ID and params
     * @param fieldOfStudyId id of fieldOfStudy to update
     * @param fieldOfStudyParams params to update
     * @return Optional - null or with updated fieldOfStudy
     */
    public FieldOfStudy updateFieldOfStudyByID(Integer fieldOfStudyId, FieldOfStudy fieldOfStudyParams){
        Optional<FieldOfStudy> fieldOfStudy = fieldOfStudyRepository.findById(fieldOfStudyId);
        if (fieldOfStudy.isPresent()){
            FieldOfStudy oldFieldOfStudy = fieldOfStudy.get();
            oldFieldOfStudy.name = fieldOfStudyParams.name;
            oldFieldOfStudy.typ = fieldOfStudyParams.typ;
            return fieldOfStudyRepository.save(oldFieldOfStudy);
        } else {
            return null;
        }
    }

    /**
     * Deletes fieldOfStudy by ID
     * @param fieldOfStudyId ID of fieldOfStudy
     */
    public void deleteFieldOfStudyByID(Integer fieldOfStudyId){
        fieldOfStudyRepository.deleteById(fieldOfStudyId);
    }

    /**
     * Deletes all fieldOfStudys
     */
    public void deleteAllFieldOfStudys(){
        fieldOfStudyRepository.deleteAll();
    }

    public FieldOfStudy createFieldOfStudyIfNotExists(FieldOfStudy fieldOfStudy){
        FieldOfStudy existingFieldOfStudy = fieldOfStudyRepository.findByName(fieldOfStudy.name);
        if(existingFieldOfStudy != null){
            return existingFieldOfStudy;
        } else {
            createFieldOfStudy(fieldOfStudy);
            return fieldOfStudy;
        }
    }
}
