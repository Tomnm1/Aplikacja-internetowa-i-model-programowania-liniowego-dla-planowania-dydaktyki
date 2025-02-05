PGDMP  0                     }            inz    16.4    16.4 }    n           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            o           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            p           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            q           1262    16397    inz    DATABASE     v   CREATE DATABASE inz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE inz;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            r           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            o           1247    16399 
   class_type    TYPE     m   CREATE TYPE public.class_type AS ENUM (
    'wykład',
    'ćwiczenia',
    'laboratoria',
    'projekt'
);
    DROP TYPE public.class_type;
       public          postgres    false    4            r           1247    16408    cycle    TYPE     @   CREATE TYPE public.cycle AS ENUM (
    'first',
    'second'
);
    DROP TYPE public.cycle;
       public          postgres    false    4            u           1247    16414    day    TYPE     �   CREATE TYPE public.day AS ENUM (
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday'
);
    DROP TYPE public.day;
       public          postgres    false    4            x           1247    16430    degree    TYPE     ;  CREATE TYPE public.degree AS ENUM (
    'brak',
    'lic.',
    'inż.',
    'mgr',
    'mgr inż.',
    'dr',
    'dr inż.',
    'dr hab.',
    'dr hab. inż.',
    'dr, prof. PP',
    'dr inż., prof. PP',
    'dr hab., prof. PP',
    'dr hab. inż., prof. PP',
    'prof. dr hab.',
    'prof. dr hab. inż.'
);
    DROP TYPE public.degree;
       public          postgres    false    4            {           1247    16462    language    TYPE     G   CREATE TYPE public.language AS ENUM (
    'polski',
    'angielski'
);
    DROP TYPE public.language;
       public          postgres    false    4            �            1255    16896    check_classrooms(integer)    FUNCTION     q  CREATE FUNCTION public.check_classrooms(our_subject_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
	total INTEGER := 0;
	i INTEGER;
BEGIN

	FOR i IN 
	(SELECT COUNT(classrooms_subject_types.subject_type_id)
	FROM classrooms_subject_types 
	RIGHT OUTER JOIN (SELECT subject_types_id FROM subject_types WHERE subject_id = our_subject_id) tab
	ON (classrooms_subject_types.subject_type_id = tab.subject_types_id)
	GROUP BY tab.subject_types_id) LOOP

		IF i = 0 THEN
			RETURN FALSE;
		END IF;

		total := total + 1;
		
	END LOOP;

	IF total = 0 THEN
		RETURN FALSE;
	ELSE
		RETURN TRUE;
	END IF;
	
END;
$$;
 ?   DROP FUNCTION public.check_classrooms(our_subject_id integer);
       public          postgres    false    4            �            1255    16900    check_groups(integer)    FUNCTION     a  CREATE FUNCTION public.check_groups(our_subject_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
	total INTEGER := 0;
	i INTEGER;
BEGIN

	FOR i IN 
	(SELECT COUNT(subject_types_groups.subject_type_id)
	FROM subject_types_groups 
	RIGHT OUTER JOIN (SELECT subject_types_id FROM subject_types WHERE subject_id = our_subject_id) tab
	ON (subject_types_groups.subject_type_id = tab.subject_types_id)
	GROUP BY tab.subject_types_id) LOOP

		IF i = 0 THEN
			RETURN FALSE;
		END IF;

		total := total + 1;
		
	END LOOP;

	IF total = 0 THEN
		RETURN FALSE;
	ELSE
		RETURN TRUE;
	END IF;
	
END;
$$;
 ;   DROP FUNCTION public.check_groups(our_subject_id integer);
       public          postgres    false    4            	           1255    16902    check_teachers(integer)    FUNCTION       CREATE FUNCTION public.check_teachers(our_subject_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
	total INTEGER := 0;
	i INTEGER;
	sum_hours INTEGER;
	goal_hours INTEGER;
	tmp BOOLEAN;
	typ TEXT;
BEGIN

	FOR i,sum_hours,goal_hours,typ IN 
	(SELECT (SELECT COUNT(*) FROM subject_types_groups WHERE subject_type_id = tab.subject_types_id),SUM(subject_type_teacher.num_hours),MAX(tab.number_of_hours),MIN(type)
	FROM subject_type_teacher 
	RIGHT OUTER JOIN (SELECT subject_types_id, number_of_hours,type FROM subject_types WHERE subject_id = our_subject_id) tab
	ON (subject_type_teacher.subject_type_id = tab.subject_types_id)
	GROUP BY tab.subject_types_id) LOOP

		IF typ = 'wykład' THEN
			tmp := sum_hours = goal_hours;
		ELSIF typ = 'ćwiczenia' THEN
			tmp := sum_hours = goal_hours*i/2;
		ELSE
			tmp := sum_hours = goal_hours*i;
		END IF;

		IF tmp = FALSE OR sum_hours IS NULL THEN
			RETURN FALSE;
		END IF;
		
		total := total + 1;
		
	END LOOP;

	IF total = 0 THEN
		RETURN FALSE;
	ELSE
		RETURN TRUE;
	END IF;
	
END;
$$;
 =   DROP FUNCTION public.check_teachers(our_subject_id integer);
       public          postgres    false    4            �            1255    16839    definegroup(integer, integer) 	   PROCEDURE     H  CREATE PROCEDURE public.definegroup(IN groupcount integer, IN semesterid integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
    current_count int;
begin
   SELECT count(g.group_id) INTO current_count
        FROM semesters as s 
        LEFT JOIN groups g ON g.semester_id = s.semester_id AND g.group_type = 'laboratoria' 
		where s.semester_id = semesterId
        group by s.semester_id;

	IF groupCount >= current_count THEN
        FOR i IN (current_count+1)..groupCount LOOP
            INSERT INTO groups (code,semester_id,group_type)
            VALUES (CONCAT('l',i),semesterId,'laboratoria');
        END LOOP;
    ELSE
		FOR i IN REVERSE current_count..(groupCount+1) LOOP
            DELETE FROM groups
        	WHERE semester_id=semesterId AND group_type='laboratoria' AND code=CONCAT('l',i);
        END LOOP;
    END IF;
end;$$;
 Q   DROP PROCEDURE public.definegroup(IN groupcount integer, IN semesterid integer);
       public          postgres    false    4            �            1255    16893    one_published_plan()    FUNCTION     �   CREATE FUNCTION public.one_published_plan() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.published = TRUE THEN
        UPDATE plans SET published = FALSE WHERE plan_id != NEW.plan_id;
    END IF;
    RETURN NEW;
END;
$$;
 +   DROP FUNCTION public.one_published_plan();
       public          postgres    false    4            �            1255    16672    truncate_tables()    FUNCTION     6  CREATE FUNCTION public.truncate_tables() RETURNS void
    LANGUAGE sql
    AS $_$
DO $$ DECLARE
    table_name text;
BEGIN
    FOR table_name IN (SELECT tablename FROM pg_tables WHERE schemaname='public') LOOP
        EXECUTE 'TRUNCATE TABLE public."' || table_name || '" CASCADE;';
    END LOOP;
END $$;
$_$;
 (   DROP FUNCTION public.truncate_tables();
       public          postgres    false    4            �            1259    16467 	   buildings    TABLE     m   CREATE TABLE public.buildings (
    code character varying(50) NOT NULL,
    building_id integer NOT NULL
);
    DROP TABLE public.buildings;
       public         heap    postgres    false    4            �            1259    16470    buildings_building_id_seq    SEQUENCE     �   ALTER TABLE public.buildings ALTER COLUMN building_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.buildings_building_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    215            �            1259    16471 
   classrooms    TABLE     �   CREATE TABLE public.classrooms (
    code text NOT NULL,
    floor smallint,
    capacity integer NOT NULL,
    equipment json,
    classroom_id integer NOT NULL,
    building_id integer NOT NULL
);
    DROP TABLE public.classrooms;
       public         heap    postgres    false    4            �            1259    16476    classrooms_classroom_id_seq    SEQUENCE     �   ALTER TABLE public.classrooms ALTER COLUMN classroom_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.classrooms_classroom_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    217    4            �            1259    16719    classrooms_subject_types    TABLE     �   CREATE TABLE public.classrooms_subject_types (
    id integer NOT NULL,
    classroom_id integer NOT NULL,
    subject_type_id integer NOT NULL
);
 ,   DROP TABLE public.classrooms_subject_types;
       public         heap    postgres    false    4            �            1259    16718    classrooms_subject_types_id_seq    SEQUENCE     �   ALTER TABLE public.classrooms_subject_types ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.classrooms_subject_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    248            �            1259    16477    fields_of_study    TABLE     �   CREATE TABLE public.fields_of_study (
    field_of_study_id integer NOT NULL,
    name text NOT NULL,
    typ character varying(20)
);
 #   DROP TABLE public.fields_of_study;
       public         heap    postgres    false    4            �            1259    16482 %   fields_of_study_field_of_study_id_seq    SEQUENCE     �   ALTER TABLE public.fields_of_study ALTER COLUMN field_of_study_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.fields_of_study_field_of_study_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    219            �            1259    16483    generated_plans    TABLE     �   CREATE TABLE public.generated_plans (
    id integer NOT NULL,
    plan_id integer NOT NULL,
    slot_day_id integer,
    group_id integer,
    teacher_id integer,
    classroom_id integer,
    subject_type_id integer,
    even_week boolean
);
 #   DROP TABLE public.generated_plans;
       public         heap    postgres    false    4            �            1259    16486    generated_plans_id_seq    SEQUENCE     �   ALTER TABLE public.generated_plans ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.generated_plans_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    221            �            1259    16487    groups    TABLE     �   CREATE TABLE public.groups (
    group_id integer NOT NULL,
    code character varying(50) NOT NULL,
    semester_id integer NOT NULL,
    group_type public.class_type
);
    DROP TABLE public.groups;
       public         heap    postgres    false    879    4            �            1259    16490    groups_group_id_seq    SEQUENCE     �   ALTER TABLE public.groups ALTER COLUMN group_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.groups_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    223    4            �            1259    16491    plans    TABLE     �   CREATE TABLE public.plans (
    name text NOT NULL,
    creation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    plan_id integer NOT NULL,
    published boolean DEFAULT false NOT NULL
);
    DROP TABLE public.plans;
       public         heap    postgres    false    4            �            1259    16497    plans_plan_id_seq    SEQUENCE     �   ALTER TABLE public.plans ALTER COLUMN plan_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.plans_plan_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    225            �            1259    16498 	   semesters    TABLE     �   CREATE TABLE public.semesters (
    semester_id integer NOT NULL,
    specialisation_id integer NOT NULL,
    number character varying(100) NOT NULL,
    typ character varying(20)
);
    DROP TABLE public.semesters;
       public         heap    postgres    false    4            �            1259    16501    semesters_semester_id_seq    SEQUENCE     �   ALTER TABLE public.semesters ALTER COLUMN semester_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.semesters_semester_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    227    4            �            1259    16502    slots    TABLE     �   CREATE TABLE public.slots (
    slot_id integer NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL
);
    DROP TABLE public.slots;
       public         heap    postgres    false    4            �            1259    16505 
   slots_days    TABLE     �   CREATE TABLE public.slots_days (
    slots_days_id integer NOT NULL,
    slot_id integer NOT NULL,
    day public.day NOT NULL
);
    DROP TABLE public.slots_days;
       public         heap    postgres    false    885    4            �            1259    16508    slots_days_slots_days_id_seq    SEQUENCE     �   ALTER TABLE public.slots_days ALTER COLUMN slots_days_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.slots_days_slots_days_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    230    4            �            1259    16509    slots_slot_id_seq    SEQUENCE     �   ALTER TABLE public.slots ALTER COLUMN slot_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.slots_slot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    229            �            1259    16510    specialisations    TABLE     �   CREATE TABLE public.specialisations (
    specialisation_id integer NOT NULL,
    name text,
    cycle public.cycle NOT NULL,
    field_of_study_id integer NOT NULL
);
 #   DROP TABLE public.specialisations;
       public         heap    postgres    false    882    4            �            1259    16515 %   specialisations_specialisation_id_seq    SEQUENCE     �   ALTER TABLE public.specialisations ALTER COLUMN specialisation_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.specialisations_specialisation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    233            �            1259    16516 	   subgroups    TABLE     |   CREATE TABLE public.subgroups (
    id integer NOT NULL,
    group_id integer NOT NULL,
    subgroup_id integer NOT NULL
);
    DROP TABLE public.subgroups;
       public         heap    postgres    false    4            �            1259    16519    subgroups_id_seq    SEQUENCE     �   ALTER TABLE public.subgroups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subgroups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    235    4            �            1259    16520    subject_type_teacher    TABLE     �   CREATE TABLE public.subject_type_teacher (
    subject_type_teacher_id integer NOT NULL,
    subject_type_id integer NOT NULL,
    teacher_id integer NOT NULL,
    num_hours integer
);
 (   DROP TABLE public.subject_type_teacher;
       public         heap    postgres    false    4            �            1259    16523 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE       ALTER TABLE public.subject_type_teacher ALTER COLUMN subject_type_teacher_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subject_type_teacher_subject_type_teacher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    237            �            1259    16524    subject_types    TABLE     �   CREATE TABLE public.subject_types (
    subject_types_id integer NOT NULL,
    subject_id integer NOT NULL,
    type public.class_type NOT NULL,
    max_students integer NOT NULL,
    number_of_hours integer NOT NULL
);
 !   DROP TABLE public.subject_types;
       public         heap    postgres    false    4    879            �            1259    16527    subject_types_groups    TABLE     �   CREATE TABLE public.subject_types_groups (
    id integer NOT NULL,
    subject_type_id integer NOT NULL,
    group_id integer NOT NULL
);
 (   DROP TABLE public.subject_types_groups;
       public         heap    postgres    false    4            �            1259    16530 "   subject_types_subject_types_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types ALTER COLUMN subject_types_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subject_types_subject_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    239            �            1259    16531    subjects    TABLE     �   CREATE TABLE public.subjects (
    subject_id integer NOT NULL,
    semester_id integer NOT NULL,
    name text NOT NULL,
    exam boolean NOT NULL,
    mandatory boolean NOT NULL,
    planned boolean NOT NULL,
    language public.language NOT NULL
);
    DROP TABLE public.subjects;
       public         heap    postgres    false    891    4            �            1259    16536    subjects_groups_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types_groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subjects_groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    240    4            �            1259    16537    subjects_subject_id_seq    SEQUENCE     �   ALTER TABLE public.subjects ALTER COLUMN subject_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subjects_subject_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    242            �            1259    16538    teachers    TABLE     d  CREATE TABLE public.teachers (
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    preferences json,
    teacher_id integer NOT NULL,
    degree text,
    second_name character varying(50),
    usos_id integer,
    inner_id integer,
    is_admin boolean,
    elogin_id text,
    email character varying(100)
);
    DROP TABLE public.teachers;
       public         heap    postgres    false    4            �            1259    16543    teachers_teacher_id_seq    SEQUENCE     �   ALTER TABLE public.teachers ALTER COLUMN teacher_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.teachers_teacher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    245            J          0    16467 	   buildings 
   TABLE DATA           6   COPY public.buildings (code, building_id) FROM stdin;
    public          postgres    false    215   F�       L          0    16471 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    217   c�       k          0    16719    classrooms_subject_types 
   TABLE DATA           U   COPY public.classrooms_subject_types (id, classroom_id, subject_type_id) FROM stdin;
    public          postgres    false    248   ��       N          0    16477    fields_of_study 
   TABLE DATA           G   COPY public.fields_of_study (field_of_study_id, name, typ) FROM stdin;
    public          postgres    false    219   ��       P          0    16483    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    221   ��       R          0    16487    groups 
   TABLE DATA           I   COPY public.groups (group_id, code, semester_id, group_type) FROM stdin;
    public          postgres    false    223   ׭       T          0    16491    plans 
   TABLE DATA           H   COPY public.plans (name, creation_date, plan_id, published) FROM stdin;
    public          postgres    false    225   ��       V          0    16498 	   semesters 
   TABLE DATA           P   COPY public.semesters (semester_id, specialisation_id, number, typ) FROM stdin;
    public          postgres    false    227   �       X          0    16502    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    229   .�       Y          0    16505 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    230   K�       \          0    16510    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    233   h�       ^          0    16516 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    235   ��       `          0    16520    subject_type_teacher 
   TABLE DATA           o   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id, num_hours) FROM stdin;
    public          postgres    false    237   ��       b          0    16524    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    239   ��       c          0    16527    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    240   ܮ       e          0    16531    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    242   ��       h          0    16538    teachers 
   TABLE DATA           �   COPY public.teachers (first_name, last_name, preferences, teacher_id, degree, second_name, usos_id, inner_id, is_admin, elogin_id, email) FROM stdin;
    public          postgres    false    245   �       s           0    0    buildings_building_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.buildings_building_id_seq', 16, true);
          public          postgres    false    216            t           0    0    classrooms_classroom_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.classrooms_classroom_id_seq', 84, true);
          public          postgres    false    218            u           0    0    classrooms_subject_types_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.classrooms_subject_types_id_seq', 96, true);
          public          postgres    false    247            v           0    0 %   fields_of_study_field_of_study_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.fields_of_study_field_of_study_id_seq', 20, true);
          public          postgres    false    220            w           0    0    generated_plans_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.generated_plans_id_seq', 3, true);
          public          postgres    false    222            x           0    0    groups_group_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.groups_group_id_seq', 2246, true);
          public          postgres    false    224            y           0    0    plans_plan_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.plans_plan_id_seq', 10, true);
          public          postgres    false    226            z           0    0    semesters_semester_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.semesters_semester_id_seq', 179, true);
          public          postgres    false    228            {           0    0    slots_days_slots_days_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.slots_days_slots_days_id_seq', 30, true);
          public          postgres    false    231            |           0    0    slots_slot_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.slots_slot_id_seq', 5, true);
          public          postgres    false    232            }           0    0 %   specialisations_specialisation_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.specialisations_specialisation_id_seq', 57, true);
          public          postgres    false    234            ~           0    0    subgroups_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.subgroups_id_seq', 1, false);
          public          postgres    false    236                       0    0 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE SET     `   SELECT pg_catalog.setval('public.subject_type_teacher_subject_type_teacher_id_seq', 910, true);
          public          postgres    false    238            �           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 1502, true);
          public          postgres    false    241            �           0    0    subjects_groups_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 3365, true);
          public          postgres    false    243            �           0    0    subjects_subject_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 787, true);
          public          postgres    false    244            �           0    0    teachers_teacher_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.teachers_teacher_id_seq', 337, true);
          public          postgres    false    246            �           2606    16545    buildings buildings_pk 
   CONSTRAINT     ]   ALTER TABLE ONLY public.buildings
    ADD CONSTRAINT buildings_pk PRIMARY KEY (building_id);
 @   ALTER TABLE ONLY public.buildings DROP CONSTRAINT buildings_pk;
       public            postgres    false    215            �           2606    16547    classrooms classrooms_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classrooms_pk PRIMARY KEY (classroom_id);
 B   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classrooms_pk;
       public            postgres    false    217            �           2606    16723 4   classrooms_subject_types classrooms_subject_types_pk 
   CONSTRAINT     r   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_pk PRIMARY KEY (id);
 ^   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_pk;
       public            postgres    false    248            �           2606    16549 "   fields_of_study fields_of_study_pk 
   CONSTRAINT     o   ALTER TABLE ONLY public.fields_of_study
    ADD CONSTRAINT fields_of_study_pk PRIMARY KEY (field_of_study_id);
 L   ALTER TABLE ONLY public.fields_of_study DROP CONSTRAINT fields_of_study_pk;
       public            postgres    false    219            �           2606    16551 "   generated_plans generated_plans_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_pk PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_pk;
       public            postgres    false    221            �           2606    16553    groups group_pk 
   CONSTRAINT     S   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT group_pk PRIMARY KEY (group_id);
 9   ALTER TABLE ONLY public.groups DROP CONSTRAINT group_pk;
       public            postgres    false    223            �           2606    16555    plans plans_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.plans
    ADD CONSTRAINT plans_pkey PRIMARY KEY (plan_id);
 :   ALTER TABLE ONLY public.plans DROP CONSTRAINT plans_pkey;
       public            postgres    false    225            �           2606    16557    semesters semester_pk 
   CONSTRAINT     \   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_pk PRIMARY KEY (semester_id);
 ?   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_pk;
       public            postgres    false    227            �           2606    16559    slots_days slots_days_pk 
   CONSTRAINT     a   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_pk PRIMARY KEY (slots_days_id);
 B   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_pk;
       public            postgres    false    230            �           2606    16561    slots slots_pk 
   CONSTRAINT     Q   ALTER TABLE ONLY public.slots
    ADD CONSTRAINT slots_pk PRIMARY KEY (slot_id);
 8   ALTER TABLE ONLY public.slots DROP CONSTRAINT slots_pk;
       public            postgres    false    229            �           2606    16563 !   specialisations specialisation_pk 
   CONSTRAINT     n   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT specialisation_pk PRIMARY KEY (specialisation_id);
 K   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT specialisation_pk;
       public            postgres    false    233            �           2606    16565    subgroups subgroups_pk 
   CONSTRAINT     T   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroups_pk PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroups_pk;
       public            postgres    false    235            �           2606    16567 ,   subject_type_teacher subject_type_teacher_pk 
   CONSTRAINT        ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_pk PRIMARY KEY (subject_type_teacher_id);
 V   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_pk;
       public            postgres    false    237            �           2606    16841 +   subject_types_groups subject_types_groups_u 
   CONSTRAINT     {   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_u UNIQUE (subject_type_id, group_id);
 U   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_u;
       public            postgres    false    240    240            �           2606    16569    subject_types subject_types_pk 
   CONSTRAINT     j   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_pk PRIMARY KEY (subject_types_id);
 H   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_pk;
       public            postgres    false    239            �           2606    16571 '   subject_types_groups subjects_groups_pk 
   CONSTRAINT     e   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_pk PRIMARY KEY (id);
 Q   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_pk;
       public            postgres    false    240            �           2606    16573    subjects subjects_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_pk PRIMARY KEY (subject_id);
 >   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_pk;
       public            postgres    false    242            �           2606    16575    teachers teachers_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pk PRIMARY KEY (teacher_id);
 >   ALTER TABLE ONLY public.teachers DROP CONSTRAINT teachers_pk;
       public            postgres    false    245            �           2620    16894     plans trigger_one_published_plan    TRIGGER     �   CREATE TRIGGER trigger_one_published_plan BEFORE INSERT OR UPDATE ON public.plans FOR EACH ROW EXECUTE FUNCTION public.one_published_plan();
 9   DROP TRIGGER trigger_one_published_plan ON public.plans;
       public          postgres    false    225    250            �           2606    16734     classrooms classroom_building_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classroom_building_fk FOREIGN KEY (building_id) REFERENCES public.buildings(building_id) ON DELETE CASCADE NOT VALID;
 J   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classroom_building_fk;
       public          postgres    false    215    4738    217            �           2606    16764 >   classrooms_subject_types classrooms_subject_types_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id) ON DELETE CASCADE NOT VALID;
 h   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_classroom_fk;
       public          postgres    false    4740    217    248            �           2606    16769 A   classrooms_subject_types classrooms_subject_types_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 k   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_subject_type_fk;
       public          postgres    false    4762    239    248            �           2606    16739 0   specialisations field_of_study_specialisation_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT field_of_study_specialisation_fk FOREIGN KEY (field_of_study_id) REFERENCES public.fields_of_study(field_of_study_id) ON DELETE CASCADE NOT VALID;
 Z   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT field_of_study_specialisation_fk;
       public          postgres    false    219    4742    233            �           2606    16774 ,   generated_plans generated_plans_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id) ON DELETE SET NULL NOT VALID;
 V   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_classroom_fk;
       public          postgres    false    4740    221    217            �           2606    16779 (   generated_plans generated_plans_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE SET NULL NOT VALID;
 R   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_group_fk;
       public          postgres    false    223    221    4746            �           2606    16784 '   generated_plans generated_plans_plan_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_plan_fk FOREIGN KEY (plan_id) REFERENCES public.plans(plan_id) ON DELETE CASCADE NOT VALID;
 Q   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_plan_fk;
       public          postgres    false    4748    221    225            �           2606    16789 +   generated_plans generated_plans_slot_day_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_slot_day_fk FOREIGN KEY (slot_day_id) REFERENCES public.slots_days(slots_days_id) ON DELETE CASCADE NOT VALID;
 U   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_slot_day_fk;
       public          postgres    false    230    221    4754            �           2606    16794 /   generated_plans generated_plans_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 Y   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_subject_type_fk;
       public          postgres    false    4762    221    239            �           2606    16799 *   generated_plans generated_plans_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id) ON DELETE SET NULL NOT VALID;
 T   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_teacher_fk;
       public          postgres    false    245    221    4770            �           2606    16809    subgroups group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 <   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT group_fk;
       public          postgres    false    4746    235    223            �           2606    16804    groups groups_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) ON DELETE CASCADE NOT VALID;
 C   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_semester_fk;
       public          postgres    false    227    4750    223            �           2606    16744 %   semesters semester_specialisations_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_specialisations_fk FOREIGN KEY (specialisation_id) REFERENCES public.specialisations(specialisation_id) ON DELETE CASCADE NOT VALID;
 O   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_specialisations_fk;
       public          postgres    false    233    4756    227            �           2606    16754    slots_days slots_days_slot_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_slot_fk FOREIGN KEY (slot_id) REFERENCES public.slots(slot_id) ON DELETE CASCADE NOT VALID;
 G   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_slot_fk;
       public          postgres    false    4752    230    229            �           2606    16814    subgroups subgroup_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroup_fk FOREIGN KEY (subgroup_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 ?   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroup_fk;
       public          postgres    false    223    235    4746            �           2606    16819 9   subject_type_teacher subject_type_teacher_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 c   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_subject_type_fk;
       public          postgres    false    4762    237    239            �           2606    16824 4   subject_type_teacher subject_type_teacher_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id) ON DELETE CASCADE NOT VALID;
 ^   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_teacher_fk;
       public          postgres    false    245    237    4770            �           2606    16829 9   subject_types_groups subject_types_groups_sbuject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_sbuject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 c   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_sbuject_type_fk;
       public          postgres    false    240    239    4762            �           2606    16759 &   subject_types subject_types_subject_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_subject_fk FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id) ON DELETE CASCADE NOT VALID;
 P   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_subject_fk;
       public          postgres    false    242    239    4768            �           2606    16834 -   subject_types_groups subjects_groups_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 W   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_group_fk;
       public          postgres    false    4746    223    240            �           2606    16749    subjects subjects_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) ON DELETE CASCADE NOT VALID;
 G   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_semester_fk;
       public          postgres    false    227    4750    242            J      x������ � �      L      x������ � �      k      x������ � �      N      x������ � �      P      x������ � �      R      x������ � �      T      x������ � �      V      x������ � �      X      x������ � �      Y      x������ � �      \      x������ � �      ^      x������ � �      `      x������ � �      b      x������ � �      c      x������ � �      e      x������ � �      h      x������ � �     