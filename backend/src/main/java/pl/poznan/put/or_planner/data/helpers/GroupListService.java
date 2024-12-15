package pl.poznan.put.or_planner.data.helpers;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class GroupListService {
    public List<String> getGroupsFromGroupMapping(Map<String, List<String>> groupMappings){
        return groupMappings.entrySet()
            .stream()
            .flatMap(entry -> Stream.concat(
                    Stream.of(entry.getKey()),
                    entry.getValue().stream()
            )).toList();
    }
}
