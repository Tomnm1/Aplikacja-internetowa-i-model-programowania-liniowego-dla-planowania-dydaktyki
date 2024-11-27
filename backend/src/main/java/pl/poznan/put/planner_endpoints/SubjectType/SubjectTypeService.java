package pl.poznan.put.planner_endpoints.SubjectType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Group.GroupDTO;
import pl.poznan.put.planner_endpoints.Group.GroupRepository;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectTypeTeacherDTO;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_Teacher;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_TeacherRepository;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SubjectType_TeacherRepository subjectTypeTeacherRepository;


    /**
     * Returns all subjectTypes
     * @return list of SubjectType
     */
    @Transactional
    public List<SubjectTypeDTO> getAllsubjectType(){
        return subjectTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "subjectTypeId")).stream().map(SubjectType::convertToDTO).toList();
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
     * @param SubjectTypeDTO object to be inserted into DB
     * @return saved SubjectType
     */
    @Transactional
    public SubjectTypeDTO createsubjectType(SubjectTypeDTO subjectTypeDTO){
        SubjectType st = toSubjectType(subjectTypeDTO,false);
        SubjectType saved = subjectTypeRepository.save(st);
        List<SubjectType_Teacher> teachersList = subjectTypeDTO.teachersList.stream().map(this::toSubjectType_Teacher).toList();
        for(SubjectType_Teacher stt : teachersList ){
            stt.subjectType = saved;
            subjectTypeTeacherRepository.save(stt);
        }
        saved.teachersList = teachersList;

        return saved.convertToDTO();
    }

    /**
     * Updates existing SubjectType if it exists
     * @param id id
     * @param subjectTypeParams new values in JSON format
     * @return saved SubjectType or null
     */
    @Transactional
    public SubjectTypeDTO updatesubjectTypeByID(Integer id, SubjectTypeDTO subjectTypeParams){
        Optional<SubjectType> subjectType = subjectTypeRepository.findById(id);
        if (subjectType.isPresent()) {
            SubjectType oldsubjectType = subjectType.get();
            oldsubjectType.subject = subjectTypeParams.subject;
            oldsubjectType.type = subjectTypeParams.type;
            oldsubjectType.numOfHours = subjectTypeParams.numOfHours;
            oldsubjectType.maxStudentsPerGroup = subjectTypeParams.maxStudentsPerGroup;
            List<SubjectType_Teacher> teachersList = subjectTypeParams.teachersList.stream().map(this::toSubjectType_Teacher).collect(Collectors.toList());
            for(SubjectType_Teacher stt : oldsubjectType.teachersList ){
                if (!teachersList.contains(stt)){
                    subjectTypeTeacherRepository.delete(stt);
                }
            }
            for(SubjectType_Teacher stt : teachersList ){
                subjectTypeTeacherRepository.save(stt);
            }
            oldsubjectType.teachersList = teachersList;
            oldsubjectType.groupsList = subjectTypeParams.groupsList.stream().map(this::toGroup).collect(Collectors.toList());
            return subjectTypeRepository.save(oldsubjectType).convertToDTO();
        } else {
            return null;
        }
    }

    @Transactional
    private Group toGroup(GroupDTO dto){
        Group g = new Group();
        Optional<Group> og = groupRepository.findById(dto.id);
        if(og.isPresent()){
            g = og.get();
        }
        return g;
    }

    public SubjectType_Teacher toSubjectType_Teacher(SubjectTypeTeacherDTO dto){
        SubjectType_Teacher stt = new SubjectType_Teacher();
        if (dto.id != 0) stt.id = dto.id;
        stt.teacher = teacherRepository.findById(dto.teacherId).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(subjectTypeRepository.findById(dto.subjectTypeId).isPresent()) stt.subjectType =subjectTypeRepository.findById(dto.subjectTypeId).get() ;
        stt.numHours = dto.numHours;
        return stt;
    }

    public SubjectType toSubjectType(SubjectTypeDTO dto,boolean withTeacherList){
        SubjectType st = new SubjectType();
        st.subjectTypeId = dto.subjectTypeId;
        st.subject = dto.subject;
        st.numOfHours = dto.numOfHours;
        st.type = dto.type;
        st.maxStudentsPerGroup = dto.maxStudentsPerGroup;
        if(withTeacherList) st.teachersList = dto.teachersList.stream().map(this::toSubjectType_Teacher).collect(Collectors.toList());
        st.groupsList = dto.groupsList.stream().map(this::toGroup).collect(Collectors.toList());
        return st;
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
