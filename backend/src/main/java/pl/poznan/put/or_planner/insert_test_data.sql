-- Skrypt do testowania solvera
INSERT INTO fields_of_study (field_of_study_id, name) OVERRIDING SYSTEM VALUE VALUES
(1, 'Informatyka');

INSERT INTO specialisations (specialisation_id, name, cycle, field_of_study_id) OVERRIDING SYSTEM VALUE VALUES
(1, 'Informatyka', 'first', 1);

INSERT INTO semesters (semester_id, number, specialisation_id) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1);

-- Insert groups
INSERT INTO groups (group_id, code, semester_id) OVERRIDING SYSTEM VALUE VALUES
(1, 'w1', 1), (2, 'ćw1', 1), (3, 'ćw2', 1), (4, 'l1', 1), (5, 'l2', 1), (6, 'l3', 1), (7, 'l4', 1);

-- Insert teachers
INSERT INTO teachers (teacher_id, first_name, last_name) OVERRIDING SYSTEM VALUE VALUES
(1, 'AD', ''), (2, 'RW', ''), (3, 'RK', '');

INSERT INTO buildings (building_id, code) OVERRIDING SYSTEM VALUE VALUES
(1, 'CW');

-- Insert classrooms (rooms)
INSERT INTO classrooms (classroom_id, code, capacity, building_id) OVERRIDING SYSTEM VALUE VALUES
(1, '142', 15, 1), (2, '143', 15, 1), (3, 'CW8', 15, 1), (4, '144', 15, 1);

-- Insert time slots
INSERT INTO slots (slot_id, start_time, end_time) OVERRIDING SYSTEM VALUE VALUES
(1, '08:00', '09:30'), (2, '09:45', '11:15'), (3, '11:45', '13:15'), 
(4, '13:30', '15:00');

-- Day and Time association (assuming 5 days, using Pn, Wt, Śr, Cz, Pt)
INSERT INTO slots_days (slots_days_id, slot_id, day) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 'monday'), (2, 2, 'monday'), (3, 3, 'monday'), (4, 4, 'monday'),
(5, 1, 'tuesday'), (6, 2, 'tuesday'), (7, 3, 'tuesday'), (8, 4, 'tuesday'),
(9, 1, 'wednesday'), (10, 2, 'wednesday'), (11, 3, 'wednesday'), (12, 4, 'wednesday'),
(13, 1, 'thursday'), (14, 2, 'thursday'), (15, 3, 'thursday'), (16, 4, 'thursday'),
(17, 1, 'friday'), (18, 2, 'friday'), (19, 3, 'friday'), (20, 4, 'friday');

-- Insert subjects
INSERT INTO subjects (subject_id, name, semester_id, exam, mandatory, planned, language) OVERRIDING SYSTEM VALUE VALUES
(1, 'PTC', 1, false, false, false, 'polski'), 
(2, 'Prr', 1, false, false, false, 'polski'), 
(3, 'Prz', 1, false, false, false, 'polski'), 
(4, 'ASK', 1, false, false, false, 'polski');

-- Insert subject types
INSERT INTO subject_types (subject_types_id, type, max_students, number_of_hours, subject_id) OVERRIDING SYSTEM VALUE VALUES
(1, 'wykład', 10, 30, 1), (2, 'ćwiczenia', 10, 15, 1), (3, 'laboratoria', 10, 15, 1),
(4, 'wykład', 10, 30, 2), (5, 'ćwiczenia', 10, 30, 2), (6, 'laboratoria', 10, 30, 2),
(7, 'wykład', 10, 30, 3), (8, 'ćwiczenia', 10, 30, 3), (9, 'laboratoria', 10, 30, 3),
(10, 'wykład', 10, 30, 4), (11, 'ćwiczenia', 10, 30, 4), (12, 'laboratoria', 10, 30, 4);
