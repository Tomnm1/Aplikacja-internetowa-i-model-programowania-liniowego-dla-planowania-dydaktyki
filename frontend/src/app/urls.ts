const API_BASE = "http://localhost:8080";

export const API_ENDPOINTS = {
    BUILDINGS: `${API_BASE}/buildings`,
    CLASSROOMS: `${API_BASE}/classrooms`,
    TEACHERS: `${API_BASE}/teachers`,
    FIELD_OF_STUDIES: `${API_BASE}/fieldOfStudys`,
    SPECIALISATIONS: `${API_BASE}/specialisations`,
    SLOTS: `${API_BASE}/slots`,
    SLOTS_DAYS: `${API_BASE}/SlotsDays`,
    SEMESTERS: `${API_BASE}/semesters`,
    SUBJECT: `${API_BASE}/subjects`,
    SUBJECT_TYPE: `${API_BASE}/subjectTypes`,
    GROUPS: `${API_BASE}/groups`,
    GENERATED_PLAN_TEACHERS: (id: number) => `${API_BASE}/generatedPlans/teacher/${id}`,
    GENERATED_PLAN_CLASSROOM: (id: number) => `${API_BASE}/generatedPlans/classroom/${id}`,
    GENERATED_PLAN_SEMESTER: (id: number) => `${API_BASE}/generatedPlans/semester/${id}`,
};

export default API_ENDPOINTS;