package pl.poznan.put.planner_endpoints.SubjectType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for subjectTypes
 */
@Service
public class SubjectTypeService {
    /**
     * subjectTypes repository
     */
    @Autowired
    private SubjectTypeRepository subjectTypeRepository;

    /**
     * Returns all subjectTypes
     * @return list of SubjectType
     */
    public List<SubjectType> getAllsubjectType(){
        return subjectTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * For pagination - returns subjectTypes from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found SubjectType objects
     */
    public Page<SubjectType> getsubjectTypePage(Integer page, Integer size){
        return subjectTypeRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds SubjectType by id or empty Optional
     * @param id SubjectType id
     * @return Optional - empty or with SubjectType
     */
    public Optional<SubjectType> getsubjectTypeByID(Integer id){
        return subjectTypeRepository.findById(id);
    }

    /**
     * Creates a SubjectType
     * @param subjectType object to be inserted into DB
     * @return saved SubjectType
     */
    public SubjectType createsubjectType(SubjectType subjectType){
        return subjectTypeRepository.save(subjectType);
    }

    /**
     * Updates existing SubjectType if it exists
     * @param id id
     * @param subjectTypeParams new values in JSON format
     * @return saved SubjectType or null
     */
    public SubjectType updatesubjectTypeByID(Integer id, SubjectType subjectTypeParams){
        Optional<SubjectType> subjectType = subjectTypeRepository.findById(id);
        if (subjectType.isPresent()) {
            SubjectType oldsubjectType = subjectType.get();
            oldsubjectType.subject = subjectTypeParams.subject;
            oldsubjectType.type = subjectTypeParams.type;
            oldsubjectType.numOfHours = subjectTypeParams.numOfHours;
            oldsubjectType.maxStudentsPerGroup = subjectTypeParams.maxStudentsPerGroup;
            return subjectTypeRepository.save(oldsubjectType);
        } else {
            return null;
        }
    }

    /**
     * Deletes SubjectType by ID
     * @param id id
     */
    public void deletesubjectTypeByID(Integer id){
        subjectTypeRepository.deleteById(id);
    }

    /**
     * Deletes all subjectTypes
     */
    public void deleteAllsubjectTypes(){
        subjectTypeRepository.deleteAll();
    }
}
