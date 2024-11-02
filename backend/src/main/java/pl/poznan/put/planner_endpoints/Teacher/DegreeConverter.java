package pl.poznan.put.planner_endpoints.Teacher;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DegreeConverter implements AttributeConverter<Degree, String> {

    @Override
    public String convertToDatabaseColumn(Degree degree) {
         return degree != null ? degree.toString() : null;
    }

    @Override
    public Degree convertToEntityAttribute(String dbData) {
        return dbData != null ? Degree.valueOf(dbData) : null;
    }
}
