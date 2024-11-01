const BASE_URL = 'WPISZ-TU-URL';

const endpoints = {
    building: {
        getAll: `${BASE_URL}/buildings`,
        getById: (id) => `${BASE_URL}/buildings/${id}`,
        create: `${BASE_URL}/buildings`,
        update: (id) => `${BASE_URL}/buildings/${id}`,
        delete: (id) => `${BASE_URL}/buildings/${id}`,
    },
    course: {
        getAll: `${BASE_URL}/courses`,
        getById: (id) => `${BASE_URL}/courses/${id}`,
        create: `${BASE_URL}/courses`,
        update: (id) => `${BASE_URL}/courses/${id}`,
        delete: (id) => `${BASE_URL}/courses/${id}`,
    },
    employee: {
        getAll: `${BASE_URL}/employees`,
        getById: (id) => `${BASE_URL}/employees/${id}`,
        create: `${BASE_URL}/employees`,
        update: (id) => `${BASE_URL}/employees/${id}`,
        delete: (id) => `${BASE_URL}/employees/${id}`,
    },
    group: {
        getAll: `${BASE_URL}/groups`,
        getById: (id) => `${BASE_URL}/groups/${id}`,
        create: `${BASE_URL}/groups`,
        update: (id) => `${BASE_URL}/groups/${id}`,
        delete: (id) => `${BASE_URL}/groups/${id}`,
    },
    room: {
        getAll: `${BASE_URL}/rooms`,
        getById: (id) => `${BASE_URL}/rooms/${id}`,
        create: `${BASE_URL}/rooms`,
        update: (id) => `${BASE_URL}/rooms/${id}`,
        delete: (id) => `${BASE_URL}/rooms/${id}`,
    },
    subject: {
        getAll: `${BASE_URL}/subjects`,
        getById: (id) => `${BASE_URL}/subjects/${id}`,
        create: `${BASE_URL}/subjects`,
        update: (id) => `${BASE_URL}/subjects/${id}`,
        delete: (id) => `${BASE_URL}/subjects/${id}`,
    },
    subjectType: {
        getAll: `${BASE_URL}/subjectTypes`,
        getById: (id) => `${BASE_URL}/subjectTypes/${id}`,
        create: `${BASE_URL}/subjectTypes`,
        update: (id) => `${BASE_URL}/subjectTypes/${id}`,
        delete: (id) => `${BASE_URL}/subjectTypes/${id}`,
    },
};

export default endpoints;