package pl.poznan.put.planner_endpoints.Teacher;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business logic for teachers
 */
@Service
public class TeacherService {

    /**
     * Teachers repository
     */
    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Returns all teachers as DTOs
     * @return list of TeacherDTO
     */
    @Transactional
    public List<TeacherDTO> getAllTeachersDTO() {
        List<Teacher> teachers = teacherRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        return teachers.stream()
                .map(Teacher::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns all teachers
     * @return list of teachers
     */
    @Transactional
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Returns all teachers with preferences
     * @return list of teachers
     */
    @Transactional
    public List<Teacher> getAllTeachersWithPreferences() {
        return teacherRepository.findAllTeachersWithPreferences();
    }

    /**
     * For pagination - returns teachers from given page
     * @param page number of page to return
     * @param size size of the page
     * @return Page object containing all found teachers
     */
    public Page<Teacher> getTeacherPage(Integer page, Integer size) {
        return teacherRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Finds Teacher by ID
     * @param id teacher ID
     * @return Optional containing Teacher if found
     */
    public Optional<Teacher> getTeacherByID(Integer id) {
        return teacherRepository.findById(id);
    }

    /**
     * Finds Teacher by ID and converts to DTO
     * @param id teacher ID
     * @return TeacherDTO
     */
    @Transactional
    public TeacherDTO getTeacherDTOByID(Integer id) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(id);
        if (teacherOpt.isPresent()) {
            return teacherOpt.get().convertToDTO();
        } else {
            throw new RuntimeException("Teacher not found");
        }
    }

    @Transactional
    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findTeacherByEmail(email);
    }

    @Transactional
    public TeacherDTO getTeacherDTOByEmail(String email) {
        Optional<Teacher> teacherOpt = teacherRepository.findTeacherByEmail(email);
        if (teacherOpt.isPresent()) {
            return teacherOpt.get().convertToDTO();
        } else {
            throw new RuntimeException("Teacher not found");
        }
    }

    /**
     * Creates a Teacher
     * @param teacher object to be inserted into DB
     * @return saved Teacher
     */
    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    /**
     * Updates existing Teacher if it exists
     * @param id teacher ID
     * @param teacherParams new values in JSON format
     * @return saved Teacher
     */
    @Transactional
    public Teacher updateTeacherByID(Integer id, Teacher teacherParams) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(id);
        if (teacherOpt.isPresent()) {
            Teacher oldTeacher = teacherOpt.get();
            oldTeacher.firstName = teacherParams.firstName;
            oldTeacher.lastName = teacherParams.lastName;
            oldTeacher.degree = teacherParams.degree;
            oldTeacher.preferences = teacherParams.preferences;
            oldTeacher.secondName = teacherParams.secondName;
            oldTeacher.usosId = teacherParams.usosId;
            oldTeacher.innerId = teacherParams.innerId;
            oldTeacher.subjectTypesList = teacherParams.subjectTypesList;
            return teacherRepository.save(oldTeacher);
        } else {
            throw new RuntimeException("Teacher not found");
        }
    }

    /**
     * Updates specified teacher's email
     * @param id teacher ID
     * @param email new email
     * @return updated Teacher
     */
    @Transactional
    public Teacher updateTeacherEmailByID(Integer id, String email) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(id);
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            teacher.email = email;
            return teacherRepository.save(teacher);
        } else {
            throw new RuntimeException("Teacher not found");
        }
    }

    /**
     * Deletes Teacher by ID
     * @param id teacher ID
     */
    @Transactional
    public void deleteTeacherByID(Integer id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        } else {
            throw new RuntimeException("Teacher not found");
        }
    }

    /**
     * Deletes all teachers
     */
    @Transactional
    public void deleteAllTeachers() {
        teacherRepository.deleteAll();
    }

    /**
     * Finds Teacher by first name, last name, and second name
     * @param firstName first name
     * @param lastName last name
     * @param secondName second name
     * @return Teacher
     */
    public Teacher findByFirstNameAndLastNameAndSecondName(String firstName, String lastName, String secondName) {
        return teacherRepository.findByFirstNameAndLastNameAndSecondName(firstName, lastName, secondName);
    }

    /**
     * Finds Teacher by first name and last name
     * @param firstName first name
     * @param lastName last name
     * @return Teacher
     */
    public Teacher findByFirstNameAndLastName(String firstName, String lastName) {
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * Finds Teacher by usosId
     * @param usosId usos ID
     * @return Teacher
     */
    public Teacher findByUsosId(int usosId) {
        return teacherRepository.findByUsosId(usosId);
    }


    /**
     * Finds a random Teacher
     * @return Teacher
     */
    public Teacher findRandomTeacher() {
        return teacherRepository.findRandomTeacher();
    }

    /**
     * Finds Teacher by innerId or returns a random Teacher if not found
     * @param innerId inner ID
     * @return Teacher
     */
    public Teacher findByInnerId(int innerId) {
        Teacher teacher = teacherRepository.findByInnerId(innerId);
        if (teacher == null) {
            System.err.println("Teacher with innerId: " + innerId + " does not exist!");
            teacher = teacherRepository.findRandomTeacher();
        }
        return teacher;
    }
}
