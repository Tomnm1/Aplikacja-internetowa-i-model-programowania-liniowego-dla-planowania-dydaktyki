package pl.poznan.put.planner_endpoints.Slot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Slots
 */
@Service
public class SlotService {
    @Autowired
    private SlotRepository SlotRepository;

    /**
     * return all Slots
     * @return list of all Slot objects
     */
    public List<Slot> getAllSlots() {return SlotRepository.findAll(Sort.by(Sort.Direction.ASC, "slotId"));}

    /**
     * Finds room by ID
     * @param slotId ID of Slot
     * @return Optional - empty or with Slot
     */
    public Optional<Slot> getSlotByID(Integer slotId) {return SlotRepository.findById(slotId);}

    /**
     * Creates a Slot
     * @param slot Slot object to be inserted into DB
     * @return saved Slot
     */
    public Slot createSlot(Slot slot) {return SlotRepository.save(slot);}

    /**
     * Updates a Slot by given ID and params
     * @param slotId id of Slot to update
     * @param slotParams params to update
     * @return Optional - null or with updated Slot
     */
    public Slot updateSlotByID(Integer slotId, Slot slotParams){
        Optional<Slot> Slot = SlotRepository.findById(slotId);
        if (Slot.isPresent()){
            Slot oldSlot = Slot.get();
            oldSlot.startTime = slotParams.startTime;
            oldSlot.endTime = slotParams.endTime;
            return SlotRepository.save(oldSlot);
        } else {
            return null;
        }
    }

    /**
     * Deletes Slot by ID
     * @param slotId ID of Slot
     */
    public void deleteSlotByID(Integer slotId){
        SlotRepository.deleteById(slotId);
    }

    /**
     * Deletes all Slots
     */
    public void deleteAllSlots(){
        SlotRepository.deleteAll();
    }
}
