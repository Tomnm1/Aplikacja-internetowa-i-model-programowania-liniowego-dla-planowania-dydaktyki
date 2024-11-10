package pl.poznan.put.planner_endpoints.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for groups
 */
@Service
public class GroupService {
    /**
     * Groups repository
     */
    @Autowired
    private GroupRepository groupRepository;

    /**
     * Returns all Groups
     * @return list of Group
     */
    public List<Group> getAllGroup(){
        return groupRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * For pagination - returns groups from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found group objects
     */
    public Page<Group> getGroupPage(Integer page, Integer size){
        return groupRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Group by id or empty Optional
     * @param id group id
     * @return Optional - empty or with Group
     */
    public Optional<Group> getGroupByID(Integer id){
        return groupRepository.findById(id);
    }

    /**
     * Creates a group
     * @param group object to be inserted into DB
     * @return saved group
     */
    public Group createGroup(Group group){
        return groupRepository.save(group);
    }

    /**
     * Updates existing Group if it exists
     * @param id id
     * @param groupParams new values in JSON format
     * @return saved group or null
     */
    public Group updateGroupByID(Integer id, Group groupParams){
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            Group oldGroup = group.get();
            oldGroup.code = groupParams.code;
            return groupRepository.save(oldGroup);
        } else {
            return null;
        }
    }

    /**
     * Deletes group by ID
     * @param id id
     */
    public void deleteGroupByID(Integer id){
        groupRepository.deleteById(id);
    }

    /**
     * Deletes all groups
     */
    public void deleteAllGroups(){
        groupRepository.deleteAll();
    }
}
