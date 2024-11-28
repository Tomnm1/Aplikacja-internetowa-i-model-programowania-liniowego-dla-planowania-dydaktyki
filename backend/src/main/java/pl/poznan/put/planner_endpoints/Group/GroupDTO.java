package pl.poznan.put.planner_endpoints.Group;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;

public class GroupDTO {
    public Integer id;
    public String code;
    public ClassTypeOwn group_type;

}
