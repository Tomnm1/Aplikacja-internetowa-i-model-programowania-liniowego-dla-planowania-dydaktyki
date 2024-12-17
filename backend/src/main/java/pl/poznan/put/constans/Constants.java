package pl.poznan.put.constans;

import pl.poznan.put.planner_endpoints.SlotsDay.Day;
import pl.poznan.put.planner_endpoints.Teacher.Degree;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static pl.poznan.put.planner_endpoints.Teacher.Degree.*;


public final class Constants {
    private Constants(){}

    public static final class PreferenceWeight {
        public static final Map<Degree, Integer> degreeToWeight = Map.ofEntries(
            entry(BRAK, 1),
            entry(LIC, 2),
            entry(INZ, 3),
            entry(MGR, 4),
            entry(MGR_INZ, 5),
            entry(DR, 6),
            entry(DR_INZ, 7),
            entry(DR_HAB, 8),
            entry(DR_HAB_INZ, 9),
            entry(DR_PROF_PP, 10),
            entry(DR_INZ_PROF_PP, 11),
            entry(DR_HAB_PROF_PP, 12),
            entry(DR_HAB_INZ_PROF_PP, 13),
            entry(PROF_DR_HAB, 14),
            entry(PROF_DR_HAB_INZ, 15),
            entry(PROF_ZW_DR_HAB_INZ, 16)
        );
    }

    public static final class Weeks {
        public static final String WEEKLY = "weekly";
        public static final String EVEN_WEEKS = "even_weeks";
        public static final String ODD_WEEKS = "odd_weeks";
    }

    public static final class FieldsOfStudyTypes{
        public static final String FULL_TIME = "stacjonarne";
        public static final String PART_TIME = "niestacjonarne";
        public static final List<Day> fullTimeDays = Arrays.asList(Day.monday, Day.tuesday, Day.wednesday, Day.thursday,
                Day.friday);
        public static final List<Day> partTimeDays = Arrays.asList(Day.saturday, Day.sunday);
    }

    public static final class SemesterTypes{
        public static final String SUMMER = "letni";
        public static final String WINTER = "zimowy";
        public static final List<String> summerSemesters = Arrays.asList("2.0", "4.0", "6.0", "8.0", "1 (letni)", "3 (letni)");
        public static final List<String> winterSemesters = Arrays.asList("1.0", "3.0", "5.0", "7.0", "2 (zimowy)");
    }

    public static final class ExcelToDb{
        public static final class HeaderHelper{
            public static final String NUM_HOURS = "Ilość godz.";
            public static final String NUM_GROUPS = "Ilość grup";
            public static final String LECTURER = "wyk.";
            public static final String TEACHER = "Prow.";

            public static final class Prefixes{
                public static final String HOURS = "hours_";
                public static final String GROUPS = "groups_";
                public static final String LEC_TEACHER_PR = "lectureTeacher_";
                public static final String TEACHER_PR = "teacher_";
            }
        }

        public static final class ColumnNames{
            public static final String FIELD_OF_STUDY = "Kierunek, specjal.";
            public static final String TYPE_FACULTY = "Rodzaj stud./ Wydz.";
            public static final String TERM = "Sem";
            public static final String SUBJECT = "Nazwa przedmiotu";
            public static final String EXAM_LETTER = "E";
            public static final String LECTURE_LETTER = "w";
            public static final String EXERCISE_LETTER = "ćw";
            public static final String EXERCISE_DOT_LETTER = "ćw.";
            public static final String LAB_LETTER = "l";
            public static final String PROJECT_LETTER = "p";
            public static final String SUMMARY = "bilans";
        }

        public static final class subjectTypeStudentQuantity{
            public static final int MAX_LECTURE = 250;
            public static final int MAX_EXERCISE = 32;
            public static final int MAX_LABORATORY= 16;
            public static final int MAX_PROJECT= 32;
        }
    }

    public static class EnumUtils {
        public interface DisplayName {
           String getDisplayName();
        }
        public static <E extends Enum<E> & DisplayName> E fromString(Class<E> enumType, String label) {
            for (E enumConstant : enumType.getEnumConstants()) {
                if (enumConstant.getDisplayName().equalsIgnoreCase(label)) {
                    return enumConstant;
                }
            }
            throw new IllegalArgumentException("Unknown enum type: " + label);
        }
    }

    public static class HelperMethods{
        public static <T> T assignIfNotNull(T newValue, T currentValue) {
            return newValue != null ? newValue : currentValue;
        }
    }
}