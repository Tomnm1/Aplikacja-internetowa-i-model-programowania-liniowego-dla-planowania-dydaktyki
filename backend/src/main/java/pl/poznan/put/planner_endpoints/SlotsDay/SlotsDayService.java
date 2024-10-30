package pl.poznan.put.planner_endpoints.SlotsDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for SlotsDays
 */
@Service
public class SlotsDayService {
    @Autowired
    private SlotsDayRepository SlotsDayRepository;

    /**
     * return all SlotsDays
     * @return list of all SlotsDay objects
     */
    public List<SlotsDay> getAllSlotsDays() {return SlotsDayRepository.findAll(Sort.by(Sort.Direction.ASC, "slots_days_id"));}

    /**
     * Finds room by ID
     * @param SlotsDayId ID of SlotsDay
     * @return Optional - empty or with SlotsDay
     */
    public Optional<SlotsDay> getSlotsDayByID(Integer SlotsDayId) {return SlotsDayRepository.findById(SlotsDayId);}

    /**
     * Creates a SlotsDay
     * @param SlotsDay SlotsDay object to be inserted into DB
     * @return saved SlotsDay
     */
    public SlotsDay createSlotsDay(SlotsDay SlotsDay) {return SlotsDayRepository.save(SlotsDay);}

    /**
     * Updates a SlotsDay by given ID and params
     * @param SlotsDayId id of SlotsDay to update
     * @param SlotsDayParams params to update
     * @return Optional - null or with updated SlotsDay
     */
    public SlotsDay updateSlotsDayByID(Integer SlotsDayId, SlotsDay SlotsDayParams){
        Optional<SlotsDay> SlotsDay = SlotsDayRepository.findById(SlotsDayId);
        if (SlotsDay.isPresent()){
            SlotsDay oldSlotsDay = SlotsDay.get();
            oldSlotsDay.code = SlotsDayParams.code; // TODO
            return SlotsDayRepository.save(oldSlotsDay);
        } else {
            return null;
        }
    }

    /**
     * Deletes SlotsDay by ID
     * @param SlotsDayId ID of SlotsDay
     */
    public void deleteSlotsDayByID(Integer SlotsDayId){
        SlotsDayRepository.deleteById(SlotsDayId);
    }

    /**
     * Deletes all SlotsDays
     */
    public void deleteAllSlotsDays(){
        SlotsDayRepository.deleteAll();
    }
}
