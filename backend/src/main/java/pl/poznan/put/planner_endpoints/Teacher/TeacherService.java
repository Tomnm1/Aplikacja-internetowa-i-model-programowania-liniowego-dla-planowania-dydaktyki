package pl.poznan.put.planner_endpoints.Teacher;

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
    public List<Teacher> getAllteachers(){
        return teacherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * For pagination - returns teachers from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found teachers objects
     */
    public Page<Teacher> getteacherPage(Integer page, Integer size){
        return teacherRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Subject by id or empty Optional
     * @param id subject id
     * @return Optional - empty or with Subject
     */
    public Optional<Teacher> getteacherByID(Integer id){
        return teacherRepository.findById(id);
    }

    /**
     * Creates an Teacher
     * @param teacher object to be inserted into DB
     * @return saved Teacher
     */
    public Teacher createteacher(Teacher teacher){
        return teacherRepository.save(teacher);
    }

    /**
     * Updates existing Teacher if it exists
     * @param id id
     * @param teacherParams new values in JSON format
     * @return saved Teacher or null
     */
    public Teacher updateteacherByID(Integer id, Teacher teacherParams){
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            Teacher oldteacher = teacher.get();
            oldteacher.firstName = teacherParams.firstName;
            oldteacher.lastName = teacherParams.lastName;
            oldteacher.degree = teacherParams.degree;
            oldteacher.preferences = teacherParams.preferences;
            return teacherRepository.save(oldteacher);
        } else {
            return null;
        }
    }

    /**
     * Deletes Teacher by ID
     * @param id id
     */
    public void deleteteacherByID(Integer id){
        teacherRepository.deleteById(id);
    }

    /**
     * Deletes all teachers
     */
    public void deleteAllteachers(){
        teacherRepository.deleteAll();
    }
}
