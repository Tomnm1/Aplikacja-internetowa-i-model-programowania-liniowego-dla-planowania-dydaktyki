package pl.poznan.put.planner_endpoints.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    /**
     * Returns all Subjects
     * @return list of Subject
     */
    public List<Subject> getAllSubject(){
        return subjectRepository.findAll();
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
        boolean exists = subjectRepository.existsByNameAndSemester(subject.name, subject.semester);
        if(exists){
            return null;
        } else {
            createSubject(subject);
            return subject;
        }
    }
}
