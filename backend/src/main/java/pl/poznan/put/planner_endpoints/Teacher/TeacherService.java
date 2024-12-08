package pl.poznan.put.planner_endpoints.Teacher;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for teachers
 */
@Service
public class TeacherService {
    /**
     * teachers repository
     */
    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Returns all teachers
     * @return list of teachers
     */
    @Transactional
    public List<TeacherDTO> getAllTeachersDTO(){
        return teacherRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(Teacher::convertToDTO).toList();
    }

    @Transactional
    public List<Teacher> getAllTeachers(){
        return teacherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }



    /**
     * For pagination - returns teachers from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found teachers objects
     */
    public Page<Teacher> getTeacherPage(Integer page, Integer size){
        return teacherRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Subject by id or empty Optional
     * @param id subject id
     * @return Optional - empty or with Subject
     */

    public Optional<Teacher> getTeacherByID(Integer id){
        return teacherRepository.findById(id);
    }
    @Transactional
    public TeacherDTO getTeacherDTOByID(Integer id){
        Teacher teacher = teacherRepository.findById(id).orElseThrow();
        return teacher.convertToDTO();
    }

    /**
     * Creates a Teacher
     * @param teacher object to be inserted into DB
     * @return saved Teacher
     */
    public Teacher createTeacher(Teacher teacher){
        System.out.print(teacher);
        return teacherRepository.save(teacher);
    }

    /**
     * Updates existing Teacher if it exists
     * @param id id
     * @param teacherParams new values in JSON format
     * @return saved Teacher or null
     */
    @Transactional
    public Teacher updateTeacherByID(Integer id, Teacher teacherParams){
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            Teacher oldteacher = teacher.get();
            oldteacher.firstName = teacherParams.firstName;
            oldteacher.lastName = teacherParams.lastName;
            oldteacher.degree = teacherParams.degree;
            oldteacher.preferences = teacherParams.preferences;
            oldteacher.secondName = teacherParams.secondName;
            oldteacher.usosId = teacherParams.usosId;
            oldteacher.innerId = teacherParams.innerId;
            //oldteacher.eloginId = teacherParams.eloginId; // Scary!!
            //oldteacher.isAdmin = teacherParams.isAdmin; // Maybe separate endpoint, that can only be accessed by admin?
            oldteacher.subjectTypesList = teacherParams.subjectTypesList;
            return teacherRepository.save(oldteacher);
        } else {
            return null;
        }
    }

    /**
     * Deletes Teacher by ID
     * @param id id
     */
    public void deleteTeacherByID(Integer id){
        teacherRepository.deleteById(id);
    }

    /**
     * Deletes all teachers
     */
    public void deleteAllTeachers(){
        teacherRepository.deleteAll();
    }

    public Teacher findByFirstNameAndLastNameAndSecondName(String firstName, String lastName, String secondName){
        return teacherRepository.findByFirstNameAndLastNameAndSecondName(firstName, lastName, secondName);
    }

    public Teacher findByFirstNameAndLastName(String firstName, String lastName){
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Teacher findByUsosId(int usosId){
        return teacherRepository.findByUsosId(usosId);
    }

    /**
     * Finds Teacher by EloginId
     * @param eloginId id from elogin token
     * @return Teacher or null
     */
    public Optional<Teacher> findByEloginId(String eloginId){ return teacherRepository.findByEloginId(eloginId); }

    /**
     * Finds user by EloginId and checks if user is admin
     * @param eloginId id from elogin token
     * @return Boolean if user exists or null if user does not exist
     */
    public Boolean checkIfAdminByEloginId(String eloginId) {
        Optional<Teacher> teacher = teacherRepository.findByEloginId(eloginId);
        if (teacher.isPresent()) {
            Teacher challenger = teacher.get();
            return challenger.isAdmin;
        } else {
            return null;
        }
    }

    public Teacher findRandomTeacher(){ return teacherRepository.findRandomTeacher(); }

    public Teacher findByInnerId(int innerId){
        Teacher teacher = teacherRepository.findByInnerId(innerId);
        if(teacher == null){
            System.err.println("Teacher with innerId: " + innerId + " does not exist!");
            teacher = teacherRepository.findRandomTeacher();
        }
        return teacher;
    }
}
