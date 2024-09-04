package pl.poznan.put.planner_endpoints.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Rooms
 */
@Service
public class RoomService {
    /**
     * repository for Rooms
     */
    @Autowired
    private RoomRepository roomRepository;

    /**
     * Returns all rooms
     * @return list of Room objects
     */
    public List<Room> getAllRooms(){
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "number"));
    }

    /**
     * For pagination - returns rooms from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found Room objects
     */
    public Page<Room> getRoomPage(Integer page, Integer size){
        return roomRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Room by id or empty Optional
     * @param roomID RoomCompositeKey
     * @return Optional - empty or with Room
     */
    public Optional<Room> getRoomByID(RoomCompositeKey roomID){
        return roomRepository.findById(roomID);
    }

    /**
     * Creates a room
     * @param room room object to be inserted into DB
     * @return saved room
     */
    public Room createRoom(Room room){
        return roomRepository.save(room);
    }

    /**
     * Updates existing Room if it exists
     * @param roomID RoomCompositeKey
     * @param roomParams new values in JSON format
     * @return saved room or null
     */
    public Room updateRoomByID(RoomCompositeKey roomID, Room roomParams){
        Optional<Room> room = roomRepository.findById(roomID);
        if (room.isPresent()) {
            Room oldRoom = room.get();
            oldRoom.floor = roomParams.floor;
            oldRoom.numOfSeats = roomParams.numOfSeats;
            oldRoom.type = roomParams.type;
            oldRoom.caretaker = roomParams.caretaker;
            oldRoom.equipment = roomParams.equipment;
            return roomRepository.save(oldRoom);
        } else {
            return null;
        }
    }

    /**
     * Deletes room by ID
     * @param roomID RoomCompositeKey
     */
    public void deleteRoomByID(RoomCompositeKey roomID){
        roomRepository.deleteById(roomID);
    }

    /**
     * Deletes all rooms
     */
    public void deleteAllRooms(){
        roomRepository.deleteAll();
    }
}
