package pl.poznan.put.planner_endpoints.Subgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for subgroups
 */
@Service
public class SubgroupService {
    /**
     * Subgroups repository
     */
    @Autowired
    private SubgroupRepository subgroupRepository;

    /**
     * Returns all Subgroups
     * @return list of Subgroup
     */
    public List<Subgroup> getAllSubgroup(){
        return subgroupRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * For pagination - returns subgroups from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found subgroup objects
     */
    public Page<Subgroup> getSubgroupPage(Integer page, Integer size){
        return subgroupRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Subgroup by id or empty Optional
     * @param id subgroup id
     * @return Optional - empty or with Subgroup
     */
    public Optional<Subgroup> getSubgroupByID(Integer id){
        return subgroupRepository.findById(id);
    }

    /**
     * Creates a subgroup
     * @param subgroup object to be inserted into DB
     * @return saved subgroup
     */
    public Subgroup createSubgroup(Subgroup subgroup){
        return subgroupRepository.save(subgroup);
    }

    /**
     * Updates existing Subgroup if it exists
     * @param id id
     * @param subgroupParams new values in JSON format
     * @return saved subgroup or null
     */
    public Subgroup updateSubgroupByID(Integer id, Subgroup subgroupParams){
        Optional<Subgroup> subgroup = subgroupRepository.findById(id);
        if (subgroup.isPresent()) {
            Subgroup oldSubgroup = subgroup.get();
            oldSubgroup.group = subgroupParams.group;
            oldSubgroup.subgroup = subgroupParams.subgroup;
            return subgroupRepository.save(oldSubgroup);
        } else {
            return null;
        }
    }

    /**
     * Deletes subgroup by ID
     * @param id id
     */
    public void deleteSubgroupByID(Integer id){
        subgroupRepository.deleteById(id);
    }

    /**
     * Deletes all subgroups
     */
    public void deleteAllSubgroups(){
        subgroupRepository.deleteAll();
    }
}
