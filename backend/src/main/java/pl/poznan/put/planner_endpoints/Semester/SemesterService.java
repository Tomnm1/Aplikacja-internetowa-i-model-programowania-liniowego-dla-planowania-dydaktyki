package pl.poznan.put.planner_endpoints.Semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
import pl.poznan.put.planner_endpoints.Specialisation.SpecialisationRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
import pl.poznan.put.planner_endpoints.Specialisation.SpecialisationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for semesters
 */
@Service
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SpecialisationRepository specialisationRepository;


//    public void checkTupleResults() {
//        List<Tuple> results = semesterRepository.findSemesterDetails();
//        for (Tuple tuple : results) {
//            System.out.println("semesterId type: " + tuple.get(0).getClass().getName());
//            System.out.println("number type: " + tuple.get(1).getClass().getName());
//            System.out.println("specialisation type: " + tuple.get(2).getClass().getName());
//            System.out.println("groupCount type: " + tuple.get(3).getClass().getName());
//        }
//    }
    /**
     * return all semesters
     * @return list of all semester objects
     */
    public List<Semester> getAllSemesters() {return semesterRepository.findAll(Sort.by(Sort.Direction.ASC, "semesterId"));}

    public List<SemesterDTO> getAllSemestersDTO() {
        List<Object[]> results = semesterRepository.findAllSemestersDTO();
        List<SemesterDTO> semesterDTOs = new ArrayList<>();

        for (Object[] result : results) {
            Integer semesterId = (Integer) result[0];
            String number = (String) result[1];
            String type = (String) result[4];
            Specialisation specialisation =  specialisationRepository.findById((Integer) result[2]).get();
            Long groupCount = (Long) result[3];
            SemesterDTO dto = new SemesterDTO(semesterId, number,type ,specialisation, groupCount);
            semesterDTOs.add(dto);
        }

        return semesterDTOs;
        }

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

    @Transactional
    public Semester createSemesterDTO(SemesterDTO semester) {
        Semester savedSemester = semesterRepository.save(semester.toSemester());
        semesterRepository.defineGroup(semester.groupCount.intValue(),savedSemester.semesterId);
        return savedSemester;}
    /**
     * Updates a semester by given ID and params
     * @param semesterId id of semester to update
     * @param semesterParams params to update
     * @return Optional - null or with updated semester
     */
    @Transactional
    public Semester updateSemesterByID(Integer semesterId ,SemesterDTO semesterParams){
        System.out.println("--- "+semesterParams.groupCount);
        Optional<Semester> semester = semesterRepository.findById(semesterId);
        if (semester.isPresent()){
            Semester oldSemester = semester.get();
            oldSemester.number = semesterParams.number;
            oldSemester.specialisation = semesterParams.specialisation;
            oldSemester.typ = semesterParams.typ;
            semesterRepository.defineGroup(semesterParams.groupCount.intValue(),semesterId);
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
