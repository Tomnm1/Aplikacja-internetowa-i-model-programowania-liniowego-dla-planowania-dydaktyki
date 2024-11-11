package pl.poznan.put.constans;

public final class Constans {
    private Constans(){}

    public static final class Weeks {
        public static final String WEEKLY = "weekly";
        public static final String EVEN_WEEKS = "even_weeks";
        public static final String ODD_WEEKS = "odd_weeks";
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