package pl.poznan.put.planner_endpoints.Semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for semesters
 */
@Service
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    /**
     * return all semesters
     * @return list of all semester objects
     */
    public List<Semester> getAllSemesters() {return semesterRepository.findAll(Sort.by(Sort.Direction.ASC, "semesterId"));}

    /**
     * Finds room by ID
     * @param semesterId ID of semester
     * @return Optional - empty or with Semester
     */
    public Optional<Semester> getSemesterByID(Integer semesterId) {return semesterRepository.findById(semesterId);}

    /**
     * Creates a semester
     * @param semester semester object to be inserted into DB
     * @return saved semester
     */
    public Semester createSemester(Semester semester) {return semesterRepository.save(semester);}

    /**
     * Updates a semester by given ID and params
     * @param semesterId id of semester to update
     * @param semesterParams params to update
     * @return Optional - null or with updated semester
     */
    public Semester updateSemesterByID(Integer semesterId, Semester semesterParams){
        Optional<Semester> semester = semesterRepository.findById(semesterId);
        if (semester.isPresent()){
            Semester oldSemester = semester.get();
            oldSemester.number = semesterParams.number;
            oldSemester.specialisation = semesterParams.specialisation;
            return semesterRepository.save(oldSemester);
        } else {
            return null;
        }
    }

    /**
     * Deletes semester by ID
     * @param semesterId ID of semester
     */
    public void deleteSemesterByID(Integer semesterId){
        semesterRepository.deleteById(semesterId);
    }

    /**
     * Deletes all semesters
     */
    public void deleteAllSemesters(){
        semesterRepository.deleteAll();
    }

    public Semester createSemesterIfNotExists(Semester semester){
        Semester existingSemester = semesterRepository.findByNumberAndSpecialisation(semester.number, semester.specialisation);
        if(existingSemester != null){
            return existingSemester;
        } else {
            createSemester(semester);
            return semester;
        }
    }
}
