package pl.poznan.put.planner_endpoints.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherDTO {
    public Integer id;
    public String firstName;
    public String secondName;
    public String lastName;
    public String email;
    public Degree degree;
    public Boolean isAdmin;
    public Map<String, String> preferences = new HashMap<>();
    public List<Integer> subjectTypesList;
    public Integer usosId;
    public Integer innerId;
}
