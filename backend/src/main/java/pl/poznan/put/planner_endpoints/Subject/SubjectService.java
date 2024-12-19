package pl.poznan.put.planner_endpoints.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Semester.SemesterDTO;
import pl.poznan.put.planner_endpoints.Semester.SemesterRepository;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for subjects
 */
@Service
public class SubjectService {
    /**
     * Subjects repository
     */
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    /**
     * Returns all Subjects
     * @return list of Subject
     */
    public List<Subject> getAllSubject(){
        return subjectRepository.findAll();
    }

    public List<SubjectDTO> getAllSubjectsWithChecks(){
        List<Object[]> results = subjectRepository.getAllWithChecks();
        List<SubjectDTO> subjectDTOS = new ArrayList<>();

        for (Object[] result : results) {
            Integer subjectId = (Integer) result[0];
            Semester semester = semesterRepository.findById((Integer) result[1]).get();
            String name = (String) result[2];
            Boolean exam = (Boolean) result[3];
            Boolean mandatory = (Boolean) result[4];
            Boolean planned = (Boolean) result[5];
            Language language = Language.valueOf((String) result[6]);
            Boolean checkClassrooms = (Boolean) result[7];
            Boolean checkGroups = (Boolean) result[8];
            Boolean checkTeachers = (Boolean) result[9];

            SubjectDTO dto = new SubjectDTO(subjectId, semester, name, exam, mandatory, planned, language, checkClassrooms, checkGroups, checkTeachers);
            subjectDTOS.add(dto);
        }


        return subjectDTOS;
    }

    /**
     * For pagination - returns subjects from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found subject objects
     */
    public Page<Subject> getSubjectPage(Integer page, Integer size){
        return subjectRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Subject by id or empty Optional
     * @param id subject id
     * @return Optional - empty or with Subject
     */
    public Optional<Subject> getSubjectByID(Integer id){
        return subjectRepository.findById(id);
    }

    /**
     * Creates a subject
     * @param subject object to be inserted into DB
     * @return saved subject
     */
    public Subject createSubject(Subject subject){
        return subjectRepository.save(subject);
    }

    /**
     * Updates existing Subject if it exists
     * @param id id
     * @param subjectParams new values in JSON format
     * @return saved subject or null
     */
    public Subject updateSubjectByID(Integer id, Subject subjectParams){
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject oldSubject = subject.get();
            oldSubject.name = subjectParams.name;
            oldSubject.language = subjectParams.language;
            oldSubject.exam = subjectParams.exam;
            oldSubject.mandatory = subjectParams.mandatory;
            oldSubject.planned = subjectParams.planned;
            oldSubject.semester = subjectParams.semester;
            return subjectRepository.save(oldSubject);
        } else {
            return null;
        }
    }

    /**
     * Deletes subject by ID
     * @param id id
     */
    public void deleteSubjectByID(Integer id){
        subjectRepository.deleteById(id);
    }

    /**
     * Deletes all subjects
     */
    public void deleteAllSubjects(){
        subjectRepository.deleteAll();
    }

    public Subject createSubjectIfNotExists(Subject subject){
        Subject existingSubject = subjectRepository.findByNameAndSemester(subject.name, subject.semester);
        if(existingSubject != null){
            return existingSubject;
        } else {
            createSubject(subject);
            return subject;
        }
    }
}
