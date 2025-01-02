const API_BASE = "/api";

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
    GENERATED_PLAN: `${API_BASE}/generatedPlans`,
    PLANS: `${API_BASE}/plans`,
    START_PLANNING: `${API_BASE}/api/planner/startPlanningBasedOnDb`,
    GENERATED_PLAN_ALL: (planId: number) => `${API_BASE}/generatedPlans/plans/${planId}`,
    GENERATED_PLAN_TEACHERS: (id: number, planId: number) => `${API_BASE}/generatedPlans/${planId}/teacher/${id}`,
    GENERATED_PLAN_CLASSROOM: (id: number, planId: number) => `${API_BASE}/generatedPlans/${planId}/classroom/${id}`,
    GENERATED_PLAN_SEMESTER: (id: number, planId: number) => `${API_BASE}/generatedPlans/${planId}/semester/${id}`,
    PLANNING_PROGRESS: (id: string) => `${API_BASE}/planningProgress/${id}`,
};

export default API_ENDPOINTS;