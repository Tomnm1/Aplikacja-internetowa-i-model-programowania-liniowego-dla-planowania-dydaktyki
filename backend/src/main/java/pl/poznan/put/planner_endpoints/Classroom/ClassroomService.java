package pl.poznan.put.planner_endpoints.Classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Classrooms
 */
@Service
public class ClassroomService {
    /**
     * repository for Classrooms
     */
    @Autowired
    private ClassroomRepository classroomRepository;

    /**
     * Returns all classrooms
     * @return list of Classroom objects
     */
    public List<Classroom> getAllClassrooms(){
        return classroomRepository.findAll(Sort.by(Sort.Direction.ASC, "classroomID"));
    }

    /**
     * For pagination - returns classrooms from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found Classroom objects
     */
    public Page<Classroom> getRoomPage(Integer page, Integer size){
        return classroomRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Classroom by id or empty Optional
     * @param roomID RoomCompositeKey
     * @return Optional - empty or with Classroom
     */
    public Optional<Classroom> getRoomByID(Integer roomID){
        return classroomRepository.findById(roomID);
    }

    /**
     * Creates a classroom
     * @param classroom classroom object to be inserted into DB
     * @return saved classroom
     */
    public Classroom createRoom(Classroom classroom){
        return classroomRepository.save(classroom);
    }

    /**
     * Updates existing Classroom if it exists
     * @param roomID RoomCompositeKey
     * @param classroomParams new values in JSON format
     * @return saved room or null
     */
    public Classroom updateRoomByID(Integer roomID, Classroom classroomParams){
        Optional<Classroom> room = classroomRepository.findById(roomID);
        if (room.isPresent()) {
            Classroom oldClassroom = room.get();
            oldClassroom.floor = classroomParams.floor;
            oldClassroom.code = classroomParams.code;
            oldClassroom.capacity = classroomParams.capacity;
            oldClassroom.building = classroomParams.building;
            oldClassroom.equipment = classroomParams.equipment;
            return classroomRepository.save(oldClassroom);
        } else {
            return null;
        }
    }

    /**
     * Deletes room by ID
     * @param roomID RoomCompositeKey
     */
    public void deleteRoomByID(Integer roomID){
        classroomRepository.deleteById(roomID);
    }

    /**
     * Deletes all classrooms
     */
    public void deleteAllClassrooms(){
        classroomRepository.deleteAll();
    }

    public Classroom createClassroomIfNotExists(Classroom classroom){
        boolean exists = classroomRepository.existsByCodeAndBuilding(classroom.code, classroom.building);
        if(exists){
            return null;
        } else {
            createRoom(classroom);
            return classroom;
        }
    }
}
