PGDMP      0                |            inz    16.4    16.4 z    k           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            l           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            m           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            n           1262    16397    inz    DATABASE     v   CREATE DATABASE inz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE inz;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            o           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            l           1247    16399 
   class_type    TYPE     m   CREATE TYPE public.class_type AS ENUM (
    'wykład',
    'ćwiczenia',
    'laboratoria',
    'projekt'
);
    DROP TYPE public.class_type;
       public          postgres    false    4            o           1247    16408    cycle    TYPE     @   CREATE TYPE public.cycle AS ENUM (
    'first',
    'second'
);
    DROP TYPE public.cycle;
       public          postgres    false    4            r           1247    16414    day    TYPE     �   CREATE TYPE public.day AS ENUM (
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday'
);
    DROP TYPE public.day;
       public          postgres    false    4            u           1247    16430    degree    TYPE     ;  CREATE TYPE public.degree AS ENUM (
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
       public          postgres    false    4            x           1247    16462    language    TYPE     G   CREATE TYPE public.language AS ENUM (
    'polski',
    'angielski'
);
    DROP TYPE public.language;
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
            public          postgres    false    4    217            �            1259    16719    classrooms_subject_types    TABLE     �   CREATE TABLE public.classrooms_subject_types (
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
            public          postgres    false    221    4            �            1259    16487    groups    TABLE     �   CREATE TABLE public.groups (
    group_id integer NOT NULL,
    code character varying(50) NOT NULL,
    semester_id integer NOT NULL,
    group_type public.class_type
);
    DROP TABLE public.groups;
       public         heap    postgres    false    4    876            �            1259    16490    groups_group_id_seq    SEQUENCE     �   ALTER TABLE public.groups ALTER COLUMN group_id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    225    4            �            1259    16498 	   semesters    TABLE     �   CREATE TABLE public.semesters (
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
            public          postgres    false    4    227            �            1259    16502    slots    TABLE     �   CREATE TABLE public.slots (
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
       public         heap    postgres    false    882    4            �            1259    16508    slots_days_slots_days_id_seq    SEQUENCE     �   ALTER TABLE public.slots_days ALTER COLUMN slots_days_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.slots_days_slots_days_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    230            �            1259    16509    slots_slot_id_seq    SEQUENCE     �   ALTER TABLE public.slots ALTER COLUMN slot_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.slots_slot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    229    4            �            1259    16510    specialisations    TABLE     �   CREATE TABLE public.specialisations (
    specialisation_id integer NOT NULL,
    name text,
    cycle public.cycle NOT NULL,
    field_of_study_id integer NOT NULL
);
 #   DROP TABLE public.specialisations;
       public         heap    postgres    false    4    879            �            1259    16515 %   specialisations_specialisation_id_seq    SEQUENCE     �   ALTER TABLE public.specialisations ALTER COLUMN specialisation_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.specialisations_specialisation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    233    4            �            1259    16516 	   subgroups    TABLE     |   CREATE TABLE public.subgroups (
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
            public          postgres    false    4    235            �            1259    16520    subject_type_teacher    TABLE     �   CREATE TABLE public.subject_type_teacher (
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
       public         heap    postgres    false    876    4            �            1259    16527    subject_types_groups    TABLE     �   CREATE TABLE public.subject_types_groups (
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
            public          postgres    false    239    4            �            1259    16531    subjects    TABLE     �   CREATE TABLE public.subjects (
    subject_id integer NOT NULL,
    semester_id integer NOT NULL,
    name text NOT NULL,
    exam boolean NOT NULL,
    mandatory boolean NOT NULL,
    planned boolean NOT NULL,
    language public.language NOT NULL
);
    DROP TABLE public.subjects;
       public         heap    postgres    false    4    888            �            1259    16536    subjects_groups_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types_groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    242    4            �            1259    16538    teachers    TABLE     d  CREATE TABLE public.teachers (
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
            public          postgres    false    245    4            G          0    16467 	   buildings 
   TABLE DATA           6   COPY public.buildings (code, building_id) FROM stdin;
    public          postgres    false    215   ҡ       I          0    16471 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    217   �       h          0    16719    classrooms_subject_types 
   TABLE DATA           U   COPY public.classrooms_subject_types (id, classroom_id, subject_type_id) FROM stdin;
    public          postgres    false    248   �       K          0    16477    fields_of_study 
   TABLE DATA           G   COPY public.fields_of_study (field_of_study_id, name, typ) FROM stdin;
    public          postgres    false    219   0�       M          0    16483    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    221   ��       O          0    16487    groups 
   TABLE DATA           I   COPY public.groups (group_id, code, semester_id, group_type) FROM stdin;
    public          postgres    false    223   ��       Q          0    16491    plans 
   TABLE DATA           H   COPY public.plans (name, creation_date, plan_id, published) FROM stdin;
    public          postgres    false    225   q�       S          0    16498 	   semesters 
   TABLE DATA           P   COPY public.semesters (semester_id, specialisation_id, number, typ) FROM stdin;
    public          postgres    false    227   ��       U          0    16502    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    229   :�       V          0    16505 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    230   ��       Y          0    16510    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    233   Y�       [          0    16516 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    235    �       ]          0    16520    subject_type_teacher 
   TABLE DATA           o   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id, num_hours) FROM stdin;
    public          postgres    false    237   =�       _          0    16524    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    239   ��       `          0    16527    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    240   ��       b          0    16531    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    242   �      e          0    16538    teachers 
   TABLE DATA           �   COPY public.teachers (first_name, last_name, preferences, teacher_id, degree, second_name, usos_id, inner_id, is_admin, elogin_id, email) FROM stdin;
    public          postgres    false    245   �      p           0    0    buildings_building_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.buildings_building_id_seq', 16, true);
          public          postgres    false    216            q           0    0    classrooms_classroom_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.classrooms_classroom_id_seq', 84, true);
          public          postgres    false    218            r           0    0    classrooms_subject_types_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.classrooms_subject_types_id_seq', 59, true);
          public          postgres    false    247            s           0    0 %   fields_of_study_field_of_study_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.fields_of_study_field_of_study_id_seq', 20, true);
          public          postgres    false    220            t           0    0    generated_plans_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.generated_plans_id_seq', 3, true);
          public          postgres    false    222            u           0    0    groups_group_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.groups_group_id_seq', 2246, true);
          public          postgres    false    224            v           0    0    plans_plan_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.plans_plan_id_seq', 10, true);
          public          postgres    false    226            w           0    0    semesters_semester_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.semesters_semester_id_seq', 179, true);
          public          postgres    false    228            x           0    0    slots_days_slots_days_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.slots_days_slots_days_id_seq', 30, true);
          public          postgres    false    231            y           0    0    slots_slot_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.slots_slot_id_seq', 5, true);
          public          postgres    false    232            z           0    0 %   specialisations_specialisation_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.specialisations_specialisation_id_seq', 57, true);
          public          postgres    false    234            {           0    0    subgroups_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.subgroups_id_seq', 1, false);
          public          postgres    false    236            |           0    0 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE SET     `   SELECT pg_catalog.setval('public.subject_type_teacher_subject_type_teacher_id_seq', 889, true);
          public          postgres    false    238            }           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 1499, true);
          public          postgres    false    241            ~           0    0    subjects_groups_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 3133, true);
          public          postgres    false    243                       0    0    subjects_subject_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 786, true);
          public          postgres    false    244            �           0    0    teachers_teacher_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.teachers_teacher_id_seq', 337, true);
          public          postgres    false    246                       2606    16545    buildings buildings_pk 
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
       public          postgres    false    4735    217    215            �           2606    16764 >   classrooms_subject_types classrooms_subject_types_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id) ON DELETE CASCADE NOT VALID;
 h   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_classroom_fk;
       public          postgres    false    248    217    4737            �           2606    16769 A   classrooms_subject_types classrooms_subject_types_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 k   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_subject_type_fk;
       public          postgres    false    4759    248    239            �           2606    16739 0   specialisations field_of_study_specialisation_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT field_of_study_specialisation_fk FOREIGN KEY (field_of_study_id) REFERENCES public.fields_of_study(field_of_study_id) ON DELETE CASCADE NOT VALID;
 Z   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT field_of_study_specialisation_fk;
       public          postgres    false    219    4739    233            �           2606    16774 ,   generated_plans generated_plans_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id) ON DELETE SET NULL NOT VALID;
 V   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_classroom_fk;
       public          postgres    false    217    221    4737            �           2606    16779 (   generated_plans generated_plans_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE SET NULL NOT VALID;
 R   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_group_fk;
       public          postgres    false    223    4743    221            �           2606    16784 '   generated_plans generated_plans_plan_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_plan_fk FOREIGN KEY (plan_id) REFERENCES public.plans(plan_id) ON DELETE CASCADE NOT VALID;
 Q   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_plan_fk;
       public          postgres    false    4745    225    221            �           2606    16789 +   generated_plans generated_plans_slot_day_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_slot_day_fk FOREIGN KEY (slot_day_id) REFERENCES public.slots_days(slots_days_id) ON DELETE CASCADE NOT VALID;
 U   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_slot_day_fk;
       public          postgres    false    230    4751    221            �           2606    16794 /   generated_plans generated_plans_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 Y   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_subject_type_fk;
       public          postgres    false    239    4759    221            �           2606    16799 *   generated_plans generated_plans_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id) ON DELETE SET NULL NOT VALID;
 T   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_teacher_fk;
       public          postgres    false    221    4767    245            �           2606    16809    subgroups group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 <   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT group_fk;
       public          postgres    false    4743    235    223            �           2606    16804    groups groups_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) ON DELETE CASCADE NOT VALID;
 C   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_semester_fk;
       public          postgres    false    223    227    4747            �           2606    16744 %   semesters semester_specialisations_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_specialisations_fk FOREIGN KEY (specialisation_id) REFERENCES public.specialisations(specialisation_id) ON DELETE CASCADE NOT VALID;
 O   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_specialisations_fk;
       public          postgres    false    227    233    4753            �           2606    16754    slots_days slots_days_slot_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_slot_fk FOREIGN KEY (slot_id) REFERENCES public.slots(slot_id) ON DELETE CASCADE NOT VALID;
 G   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_slot_fk;
       public          postgres    false    229    4749    230            �           2606    16814    subgroups subgroup_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroup_fk FOREIGN KEY (subgroup_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 ?   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroup_fk;
       public          postgres    false    223    235    4743            �           2606    16819 9   subject_type_teacher subject_type_teacher_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 c   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_subject_type_fk;
       public          postgres    false    237    239    4759            �           2606    16824 4   subject_type_teacher subject_type_teacher_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id) ON DELETE CASCADE NOT VALID;
 ^   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_teacher_fk;
       public          postgres    false    245    237    4767            �           2606    16829 9   subject_types_groups subject_types_groups_sbuject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_sbuject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 c   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_sbuject_type_fk;
       public          postgres    false    239    240    4759            �           2606    16759 &   subject_types subject_types_subject_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_subject_fk FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id) ON DELETE CASCADE NOT VALID;
 P   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_subject_fk;
       public          postgres    false    239    242    4765            �           2606    16834 -   subject_types_groups subjects_groups_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 W   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_group_fk;
       public          postgres    false    4743    223    240            �           2606    16749    subjects subjects_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) ON DELETE CASCADE NOT VALID;
 G   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_semester_fk;
       public          postgres    false    4747    227    242            G   5   x�ȱ  ��� Z�A��K��OzG>h�q 5�M:�ի)Z��$?�F      I   �  x�m��n�0 ���+���TV��Yn�TU!\ܬnv�(�*� .H|�g�	���x�iQi�R^��ؙ�u.�f��j��շ�+Q�x�����ZrTuҵZ��<�����?{ow�5ޱ;���&75��Bj�d�~U޲�qk��XM�c/�p��!��aJ��٫�;8XƤe�Z��e�������Y�21��0��c7nb�a�����mk}o�R2_&+<ܨ�k� �Xؐ�r�2r��t�0v�18Ǆ����5��5�Y\��K����X�ȉK�|����� N��8�O�K�U(rtYM� n��8�]��7�A$l�g�"��;�s�姝($~�Kb"YAHF�9v��v6؛��,����x_y��]s�����O��랄� K3�}�4G���B߲�o/�����{h�:����0:ֆ�㏇���\��޻��s����,�Y?@�C6�cJ�*�pކ}�_M�c�[��ܺ�5՝��L3�����w���}����,����n�\\v��unCs�`����_�ͦ�@d&R/���T"MHG��⒘(<'����L�H m"qN*�)5^"�GR�!�D��&&ѤY�.L&?�a*�L;Jw�֑���"283m1��U&��J�b~v5����v���Cxz;*ܧu���3כ��$k���e/���(�	�#��/I�)d����:�LqF&=n4
�ncP�uh�,xAn�ID"�y"�Ĵe��t����OAF�\�~�&���:˲�ۢ��      h      x������ � �      K   W   x�34�t���,.IL���K,�K�24��D3���sC4���"a�陗�.h63/3E��C�� $���Y\�)���� s�<�      M      x������ � �      O   �  x�m�K�1D��a��_�K6�] rĭ�7��}48SERb+����X�������������3�Z�|���*��z�}����<wn�s;n��=��8���e'Y��N�v�2]��-���~��/��y����<��Y�:/��y���r��9���}*?�R~��R~���0�����ޔߛdt��>�|)��ϼ�<)?xV~p�7y�M՗\�%W?'�é~&��.���vI/��������*�^�{����{�����B/��7y.ͯ�|�@9��H���N�-���[�8��?>O�։��{��`*~�
�$�=�N��&I'�)�<�G���Lo2��T�!S��L_2�y�d*x
S�a*%LE�����Jӹ.r\�:�ȗ�s^.y��x�w�Eށ����!��;Fx����D N�e�����Nk�"S��Lo2��T�!S�g���
S=�7���{�J'����N��o�|9�E�+�\+�ئ��m*�ڦ��+�<�M%�2��"S�2��&S�2��]99��E��<�.�$S��L/2��T�&S�{���S�a*+L���n���$����/���;�&�����|�;��y�^��I�}���~�4���<�<�<�<�<�<���� �'��f>Ì�g��͜�s���1��y�2�.�l������"�%E%Σ��w:���Dȵb��\+6x��˹Ƙ\�fIεm�듢d��� �'E�~��� ��������^�z�C�׷����XT_rշx�J�����.���R��Jo��T=	T���I�\z��_��\z��_����g���u���y�^/�n��[�^M�L�~�^Ǧ~&W}��1��ȥ�y����\����ۼ���Ko�~��K�����?�����^�C�m�'C�%��'#�^�z�C/x���j&_�j�׬q�i_����v�^p���5�\��z ���\̨�����'r=��G}}�≋<�����k��6��ҾA�}�\���瞜�|&�JI���\�U�ε_��S	<�S�\�Q/�u�뉚\O�亏zu���\��^zȣ�v������k~{w��%W?��\�L���O纏�5�}9����~5�?c�"����V��\z��O�^r���_�K���ƣ��zţ��yx�b�"���W�W����������_���?�L�      Q      x������ � �      S   �  x�m�9nC1Dk�S��C�JwI��@�&@��>�G7~�8�fOE�\s���~�� ��������	&@}$g��TH�Nw(����$� �%�7�^��b �2 �	B.��]jW�X⭵$��]qy+������S�P(���%�x�2 a08�f����TA(� �����#��=�� [����x�Zv\r�5��jp���z��S� �S��Ը�Q�F�S�3���L6����c���Q�G��®o��q��G6�®J��(lz�ӷ���JX��w7*��b�MCa��=Y�;iB� �pm�Ѕ��/\�p]���2H����*v���u﷭İ���
`��`ow}�����U�Ŷ���wk����� X\&����z��7��      U   M   x�-���@C�3� �PK��#1���ד�$;E���4�L5���f�Y*�R�fz�DF��Q���-��3Wkx      V   �   x�M�K�0��aPm��ޅR�`A�
q{�!v����F�B����ΧOd��<��^��oI/����9oW���h��Ǔ^lL����؜l{HPқ2�
%$/T�FȮ0��
<�Z�)d?����Nz�B�Y���
���&�YAX+�ja��@zA�޼�a�p<���W��      Y   �   x�]�M�0���_)ȶ4�zts�al��x��(8�?�
#��}�4}*���?��rB�?�o����#V)���S
#yR	Cc�~��ԕ�6���9�ٌ�N����(�d�N��v23�sC_K�a�Tj,sH?P�9Ӌ�$OcW�Y�D&����6�N�c�z�E��`�x8��<o��v�      [      x������ � �      ]      x�]�[��(D�+3��c/��uL��2�ѧ+��C$!�f_?y֟�>5	�ҊQ6jݠ�̑~rT��O��g�@�u����P5�o��j?E�����2�O.B��>�ڟ&Fc���aM����ck� �4Z���Z5*� J�����w�)�e�K�f~Dnb47~&Fj/5^'F��{��:gW_�]TD*��;}��L��'&s�'*s������J?5�w����S$�^�El����h.�%�@EĖ���5;s�_��F�\�5�i�܍j��m$��X��mt#F��O�l=<`kF�J�s'O�����%Q�NJ��hd qQ{Q�T&)'�k�)}���E��l�\^4�yn�/)������ߍ"�%���FMDB��E��Xh-yj
V*Fz�^��6���P{&ES��_�.�:-����d�����%���73�p�$���ʕ�OO�TiԘ��r��jT�!1�b5�D]Zpe���r��DR����3�G�����������E�d�C�+姜N��O�B�U�^����v���*,�q��_��ld�sa<�7o�2�v���,�-ٷ/��>���$Dl��b�f�g��(�2rgodih��EJf�*��$-��<ދ7KOKv���ĬAk5���f@Qc$f�}�.-veiŬ��ff���75Y�R<�Y�Y2�62�V�v�;�I�'�#�T�z9�'�:�S�d�:������ih���*������H7�u(�,|T<O�Y�[�!�4��;P3*�v���dK���4���V��"�x���"�+K�Y��!�ei_�Ѝ��d/���K��evPђ�,�z�خ�hW��c2홳���Ss����l��-m-���܍��4��c����0]��/�9b�H]�wd�B����rԢA�u�MZ��KF�i�^�ժ���ֵ����� ���������h2�����"^ɓ���D�%��,w�g��_��,�U��%��$zx�C���A���cSщ�؞}#��п�B)����͋�$3๋�e�CT-= ��+l��R.5.�1�f�>6�:�Z
٩�Pٲ���mL�-l4����>�edQ�=cR�ά�kaI=[�u_\����#}��>	�SEo$8ƧH=;#9|l��$V[�6��%�,!q��=�Y���cE�k@=�mC���[,��R	�^Q�S��/��.a
��M�H-�{�\���z�K��[V�Ū~Cjdޕݔ��bE�}ƊA�*N�;�E��S�/%��!���+�p���}5� ������^K_$v��q�\�M�_��J0�����c$�ޭ{f�K�6�UY5���S�țŒ��a�߻�H�NW������/E������-�XTP����Dޒ���τ�Z���)/�,C�j���c��c8J���h����`����+0J�[���?_$��u���W���!���%�bA�h@(<�E�+����m��M�G
�ui��>m��RqBç�����F�`:mx���k����XQԶC�Oxc�r��6�6Ԏ� ��%:���k�Ɵ9�R�3��ix{�W�\�ױ��cl~�,�T�Y�%���@h��kt�����G(^q�ǵ�q��x�@�t�2^Co�Bq嘙�'}�| /Z����)�9�K<����Ѹ
{����
�� ]�,V7\����~���@�ً�d�C<\D�Cp�,���xu�Y>�䀠�G���q~��N������
l	�-���
}oI��A�-=��8g ��[���BI�Y-���zIS�\�FP�apf��� p�>��x�$1X�\#"R��+M� �L0� ���5�
'���IRD{�ֱZ1��j��R�c�}p�,]��f���^�����w3�p֝�A8�N�ʹ.1�D��Ǎ&�)a=9���ߧ�Sw�nF=��u5#*����� ��=A��u3A �:n&�U?_q�	u"*��4��h(g���RBr?�.qr��k�����kl�63��6e�Ɩjf���@�S{zp�{f���i�`�&k�t�d?��n �e��ܐ�C��c��LDJ�7*���|����i��4@�9��lO����<�qv�\G�U�觡;�����<��������&��������x��f�Ϛ��c�	î���+��x�q�	`;Oε;�~"���:�n%��Z��[�!R������8��9�����D��-N�j�T'���%D��a��q�c��#.����#�j�A�b:|�D�৻t:T�d���E�^�@���X�Ի3�Yӻ���6}�(�!�q�@v+��R
LU�p~���`�3~��ڧ��[jWoz��ȳ�[2��x���l�K����ڨ��`B�k�4�v"�2��S����Kο���$`J���NN�U��s��C�]y�f�7fD�
����-�:���{��|��?�|�~��d`�x�ٳ�����?�ڙ�S@ر��%�}���j�F�گV�Ʋ�Zr>��١9H�n���K�=���)���
Gy0|�Toa�ԯǛ)��k�4����D�S��tl�P{)p�[���o���I�K(�NJV�:��
�Q��]�Cb��e�py.y�W~�ʎ��R��q�p��".�QG�7�s���]؎���)�/ǵ?��g�A����Ӽ!�����$�i�f�a�F���ӹc����55{�Ը>٦�NH���8�CLO���<�'��]��$ ��$���Z�s��`І�#�o+�+��@�ܦ�u�r�����#=u�y�&����9C�UM�<����������9�d͒5��:�L��N�U�Sh�P�Q�hx4
�a?���p�����P�	zv�r�^���x(��f/֣D�%$���Г	:"m���BN��H�B�����q^�n����!
�+;��}�"E�`���[�I��hʁK�1:ދ�W9EN���ƶ/���{2��'C�]{S7Y��xde����j�U/S��m٩-�=�0o�	�:��ơ��.g�$�y��/ǁQT(�:;���SL�+�L;?��k/W�wElu�x�S��3�j�r?g�W3�e�|�y5#@�Q�|5#:��&��Fѡ�Kk�5���N��Qκ	�Vg+�nEF|53=�#��0\M�Ll�?P��`D���{,R���" i����cp���Q���Rg%����6���>w�B�b\O`U�r�u�8�l�w-|j����jf�d!��h���� a}L��e�:�B,��߁5u�nޑ�d��=����ӄK`,�A�>t1�)���e����0��<vx�[]fW�z�����w�j:�îd�#�t��ۏ��蛓��O����2`��͐����5�v�X�{��g���ݞs������r%Ի��l�U:���8�{��E�����8g�t�]�Rnyb��If�����Q>|:W����Ff������>��(D�Dý���7	:��sɡ{�o%�;���.��q<�ˑk��P�f-:�Qc@4/���+*�:������t�y*�B�
K,a��y��\��{G;\�iGD�q�8�qF���25<����]�~8�nz%�D�)�X��9�.�=��.�p"����4"�V�{8#�����a�P���r����ȊuO��'o���6��ev�}_E֯��6�}"�a�����is�%za�K@��u#^vB�h�ǣ!�"��pI"���nz�Ͱ�nX����y���X��Ӡ��=Z�ٲ�'x����� ��C'�~����[�zp�x8 ���ak�3=7��^( �x�o��s��ÏI���Q������7�Y���zD�e���Q�kWȁ���>*�sg����΢��;z�m��Уb���õ@vM� ٹ�YP�-���T��x&^�m���R6\�r�U�8����%jg��\����*�?q�p�E��]w�Ȑ�K��g�F�I�p]D�82nW3������adK��f��'����1|���w����Ke\�w3"b�u�s���0��7�F�ҜQ�x5#�^��îN�r���4���%"�璻 �  ���'��>+��1inb��.��}�4�?�Ёz���7.A��f�<��N��t��ҧF;Ta����.�i�z+
"�/_D�|�(]d���`�P��d��N|������p"sD�t��`St{�m�b�0�8Kg~�88R��*Y9��V��N�>���>�	wA�L5q��zT�'��J�K�np:i�E���u/ϙLz���y��6�pvb���d:#Q�i�8��{	�!�su.DL�QT��y�N�l�����bM��q��X/0C]y�1��5j1~��q��-^�JS��b��r��[�3�u��z&֒j�}�i�|]t�Α�����z]�eQ�������v̀s���@�ku%��/A��W3��/���Mȓ������2�(�S'�<���U�̗kT�nv�4���g��?�ɤ�ru��'sG��\�ˉ�v����=\�j��<k�ԫ�oӳ���B��g����d�\ʺӄ��V�	F�	�2��nE���yƥ��U���&e��5�;�D�B�==!�P&_L����/���,�~�ĳ{k�,&گs~�c7�����~p���y,q�'\w�F���4����?jp��/�6V�+]�	�_4T�W�k����3`v��G����ʍw��x_�&��׽�Ysi�j5������S8�?v���g�;r�<썽������.��������z��_�5�i?���*�?Ɵï���h��o��cG�V7&��x��K��� v���o��E�0>j���ؗ6ΔJ̾kl�ʥ��	j�?\ U6�;�'�/'�c�����X���H����Q�6�q��z��@�P�e�Km����!pp��.���s�Ӈ�j�b�Cl�@�^Y-��j���CvU�y����L����U�ː�Cvm�^O�7%}��J�>�?�hB���>\9��Z�`�Yz��%^y}�I5:pC��������MW��YCs���54�y_f�Æ�|s�F����!����I�~�yC;���_j�\Ѡ�{z�5�y��ƀ�;�s�������|Ԑ)ȝ-�g�����aX�#vg8�Z�Rr͍&�拌����7�jmc�o(�a��+����fk����w��W�f�:{P1���8���{ͷ�E�>�������(�/�̈́��'߼���`��	�a"���&�BZ����v�#�Ѿ�lҞ~؍�-�7r%�;�U^�� �j��?�W3�I�݃aN�!�2��z�$�g9�'7E?$��ls>���[�\V����hްó�_j��`�r1Ǧ�i���ѯ?��L`�������w��B
���r}\÷9Jjw3l�0[}�Y.V��6�ˎ}��N|/r�N��%������kՎg�g-���~L����gD7$�q;d����u��}>��Ķw�      _   �  x�m[K�-7����-i/Lx`L`����1����sժ�f}��7��������������o��V��֦������������@e�}�ӽ'���O����o��
�Ԗ%�ҝ�WXl�O(�s�I�;�鲛�(��V�+�LX�ٮ]BY,��_MY,����2�s�B������B���Ҝ���n?�3.��� ]s��;Z���N�߄F��o���	���7%�?��|��$�K|&�A�'���	ʩ��^�F�rjο��I�;�!4�'e;������E���?�,4�'����P?p�����w�Ǯ�tsr�Ch����Ɓ%B4�=C�_��A���lsu�Uh�O���w��?��ݟ���f"_;��%4���;������;�*0r�r��]h@��eۥw�� �e;h��	��e �RN�,0��R��  �{Rȳ��]`�M�����,m_��$:�,5	���9O�"�7Y��^AЬ�M8jL?N�Z��O�uuG�`Z���.��ji [�ٝ���i�[��B#��NZ�F���iB=�F��5;��v>���Ih0�Q�]ο��A�}�b��j�등`۲s7���kuSH����B
�VO��VC�mο��I�;�&4�'_s�&4�'������p�@!��9�Q@��SG���Nկ�7�Q�M.uTO��,`۷34�,&4���8�)4���������RG�����Q�ͮ,�ۿ���_���Ͽ}����V�ШU�.�N�e
�Y&E�]�����h�&E�%�_�F��j�\M
(��jRDa_�YM*(��O*({}�=еׄ��ʶL_2��ɢ=5)�̜�����sڄ�������u���� wd ّv�D�۶Ѣ+���Z��H��M#��&�Өο���t4�߄~�r���8���f��NÜ���r��
e��������RGNe(B�6�TN3���҄��if�߄F�Au�
�)�f9'��i+xܭr�=}��k����fw(y��ۅF�#�����c&(��:��K�t�9Vn���z�\F\���~�e)��>�2 \��W?�\�m�~$�
肣 ����%�!4�l���:�u�|$uXȸ��,���ɸ����
>`_;)�肣���Y�N��\���M��J۪��F��U5���%�|YC�N�Qä�@z+w����y�o�2���!t
��܌$8�A�~nF���n^�	>�W��2D��y�LCp0v8f�V
����\-D��j!O8!	o�{�Ӯ��r\7��˻'���s��{��=�� rmg��u]����D�[-{�g�~`"��M��*<!u7����c$�&���~yOu���<�/�`(a��6�!V��q[��2��V �����/�a�4ޅ�����
_�F��V�cJ��G�I�!Yx�J&yB���AH�B�3��V@�^��=�x4-�߶^��1�͗^�
f�m���{|�T�a�o��zS���� �o{"���	�os��n�KxRI~��rq�£~U�?+4���0�܇[ŵ�L��5�vOI��pj��J�"(ֶRGKCB���=O�����`;�5�	Է��gqQ��%��L�����EѶ�r>����,�܍S���,�h;�9O�o�a�SH�v�t���@�vu��ߎ���&<!��k
�B�߶��'�
���{��m�9!Tp=�.jN�����&8����'��"%\?�J�Y�\����[k�)����8	~���ܽw5KwVz�MS�ɵA���/`�A��GL���:]�qv��8	>��2���?v.�#v�s�g�z�= ng��	��S��Y� <����t'
:��DMgn���Yaz"��]�脛��ITt6|�	#�������^Qύ���,`>��(��0n���wx��{�����j-����)8j�b�� '�a���\��<���ITr���xT��D!7^��$�6���O40M��S$Q��o�IpD�
n�C!Q����'��_ og-� �}&8� ����|_���ݦ_�m3^�n��WL�l�
�D��G4�aY� ��0��ϧ���(��m���l_��ql�b����`�0���8"���/?�:�W�L&w$����p�\0�)���+$�H�|��#H�������]p0�d��q2����?�,�d�i`���FHVG�q����\�ݗ �	���݉L�1nH���ϐ$K��z�����TOus�BAw��}���M\+�|�Q���=͂��/畂<�w�<ǞC2��׀=d:]��%8���ڷ��΂�%�0�!ٷ�΃ ��k�z{7�G�Z@8��V�+�N��kV>0H��+?o���8o�/���W��,���q��v������$���il����m��D`��>Sp�]Է����^饸.a�����4��Bx?mXoŭO�&8�D��7a�Cdm>b
�"2Y�Iz;���Q΂_�nX�}+^}"��0̷��'�#�|+]!"��0�*W���E���U���X�n;��VF���d�}�*�ȿLdm�u�#���oc#��k��Br��k3�\��^
΂�ՙ1x}�?}�T����1x}+?�t�]p�[����\���Vfz� ��L�D�۴>$�}�"8LD��#��0���!Tcq)S���;�[�聩�|�h���;嘩���@O��bQ���E�j��/�L5�KB�d2�+S��+E+?Y;٘)��Aw"J��E�ÁR�;٘)��JQ��%�L)��2�X��e�;_=m�����L1f�Q�3���|�g�������E���dV}DF��^�yz5��"��>����m�(�e"s����}�g�2����l�Ò�,.�d���c���t��̆��Be�n
��(>"�X�:n����&�P��\��l�Z(Ά;Z��pGk�0�%�(��2�75�QNɄ���j(�e[9e>Up�s��e�U����l���S�R������VOQ#�DYv>P��4ۊ*_!YxB�竉D��Z�2�œe"{�&8� ��F���Od�:ۊ'�x*��
��o��N0�jJ��;`�Pe�n0�=Ղ��0�`�>b#&L�u��|Ue�LյQ�਍J��GT��' ���kؓ�󓵽"�h~�*����h�ࠪ^YO�G��f[��Q�J�3�
$�.8�XoϤ�w�C�+b�|] �l{'�D�Ұ��f�C)F���Yv�1��&�n��*����̻���27߫!8��׆]�O�=#��#���'L�- ��_Z��T�a ��_�&���8��e?�v�f��)�g��9���B��o�˩;.z۞N�~�E���'v���+BM7�a�w߆	>�_e"q{Ep��|F�4�|����d>]�f�
�m�OD1��f[��a��6�L�y�W�HV�w�� �ӰmO��j������c���Kh'\��k���&8� ��[qu���m>b
�"����$8	#��u�k@R���d�^�S|G��J�\�^�&7T8�,���L ^������:V����b���2�x�����4&���>T+�o`��>�jg%*��dn>b#�|�"�t��D��Gd�a�o����
�]i�[���ƿRZ��������e�����b�w��Օ� 
�V� ����CK���.-iw�r��|�i�H�oKj6_qZ���5�φ�+NZ�l��������Q����2�}����W��<u[����W��	췪�}t�zvV����o�C�DL��=����Vy��Ip����N�Q�m���w͂�|�D]p���_'����'��Ϳ:����xu�G��:R���@��ՑL n��n��ey�'(u�/@�2��&u�/@i)���N�7��h��ܽ:��,xu�6���N��wmj�d=��	{�[�*?�����Dᶕ��
��l�D��V��D�gYS�me�OD��[}�a�?��x�u�0Y���<�������pG      `      x�E�[��J�O/fLP����1�D��5�nY	�Hr@}$=��[c�{N���3���_x�B�p����L�zֿ5����������7���z�k�=����	�үgԳ���(A�z��p{�c�=�5ܞ��=ݠ�aP�4�G	�{�A=�p{��p{�L���օf���\P��iP�2�gԣ��9?��9�����p{��t�z�A=Ӡ��`�ܳ�9����~�s�&Xz����kx.4�{�&X����M��)A�mвc��%�߲�1�e�k�2%^�Z6Z��˖A˶A˔`}�ޟ�.{�]�*���fвnв�`���ӠeˠeJp����3�e�&ؿoY{Z�Zvl�u�0��4�Jмl���~��-��.�A˔`xY7h�0h�L/[-�-S��-��29��.����fвn�2%8^6Z�Zv�p����U��Un�������2������(�p�nEN<�ȉ�(�r�TQ���0��ǕePe��?�����V��k����>s?�r�Ϩ�{]iU����0�2/tW�A%�í%�_�<U�`���(�veTQ���2�r<?W����{0������M𼆛�i���醛��������Y����%P��\�	��7�~�M𾆛�m���톛��������%�{�&x��&x��&h�n���M�^�M��s���7A놛�y�7�n�鍲�0�mn�N���n����0.��y�&�{�A=à�iP�M�_�l�z�������t%h_O��7�z�A=J��3�Y�l�z����������p����`�'�P��nP�0�g~@�,�l�z������J����n�l���Q��iP�2�g@�M0~_�u���:��s�X����=�t�z�A=�zn��g�s�GN�#'���ȉ�GN,PO��%���,�z��(��r���@N~ȉ��9q� �W��Xp_Yrb�}&ʉ��('��mP�1��.'�J���ˉ�fPO7�G	�{�A=ˠ�mP�M�M�z�Ă�#'�9���ȉܤ����L�znnR�����n���M�z�Ă�#'��ԣ<���4�˰.lþ���n��Xp{�Ă�#'r�BO7�g�3�Q��mP�1�9���ȉܤ�GN,�=rb�z�A=Jp�3�Y�l�znnR�#'�9���ȉ�GN䎅�nP�0�g�sp�B�6��n��Xp{�D�e�#'�9�@=ݠ%�>��wa�JN,��*9���ȉ�z�Ă�#'�9�[z���z�A=J�ܳ?��n��Xp{�Dn��#'����Q���=ˠ�mP�M���z�D@=rb��n���=�z�A=Ӡ���(z����7�Ă����r"p{��X��nP�t���Y�l�z��{9qσ!'��y0���=���M�n��_�qA	��-��m��)���ɉw��Xp�ɉ�t������&��eӠeˠe7�c,;��LN,���D�ʹLN,вfв��5���M��)A��mвc���Dn�LN,����-S?
r�� '?
r�� '?
r�� 'r���ߐօǰ/(��fвnв��pȲiвeв}��c�ˮ?�ˮw�Zv����5����ˆA˦A˔�{�6h�1�eK	Ʒl=��l�-S��eݠeàeJ��l�l�L	�(��.ۏ�.�J�Ga7��u���܅��ھ/˂s_�����}mߗ����e�-�/����8ܹj�}�����&���e��M��)A���ˎ���?%�l����{Z����,-S��e��m��)���=���yw٣���,�-�	�?�ihnn˩l�*7������]r�a��T�	�{���(�peTQ���6��뫴��V�lW^�*Jp\�Un��cp���*7o�~aƅ��w	T�N��V��Py���e@�TQ���4��ӕmPE	�W?í%خ�U�������@eT�	�����}���c 'v9��ȉ��@N����ݰ.(�peTQ���6���ȉ�"'r[N�5��ǕnP�&���4�rp#MeT�	�]VEN,�9����/���j�/���}5ʉ��('�g��Xp��rb�=�rb�=�rb�M '��1��5��	���8�sȉ�^*��X��Tj9�@=���-9��]x��k�a^�ua��i8��^?'������M��Ă�#'^ ��n��XpSˉ7��XpSˉ7��XpSˉ7�s7��3��(��J-'r���rb�zP����,�M-'��rb�M-'��rb�M-'�ԭn��7As�nN��r"7�J-'�� �ZN,�=rb�M-'��rb�M-'��rb�M-'��}n��7Aw�N��r"o3(��Xp{���ZN,P�0��rb�M-'��rb�M-'��rb�M=�M0_�M0�@��(���J-'�g�ZN,P�1��rb�M-'��rb�M-'��rb�M���&X�p,'P��J-'�V�Rˉ�GN�P��Ă�#'��rb�M-'��rb�M-'��rb�M���&8?�Mp�@��(��ț=J-'�g �'�/5N\_j����8qU�W��8qU�W�޿f����o����?'���W�3�W���zt^����s��"'�﹣?�|p�&'�﹣?�|�����<r���;�s�w9q|���烛@N�sG�)�<r����s�7��8�'������#'��I�?�|p�ȉ�{R��=�8r����s�7Nső���x_��K|�~�Ă�9�@�����~�D���ȉw�rb��TN,��ʉw���&�N�n���&�N�n9q�/��Xp{�Ă�#'�9�খ��nj9�খnj9��N���	�z:�RO'Pj9q�/��X��aP�4�gnj9Pj9�খnj9�খn��J��@��(�r����Rˉ�9��#'�9�খ��nj9�খnj9���N���	�z;�R'Pj9q���rb�������ZN�ZN,���Ă�ZN,hz����7S>z����7F>Xz����79�~*����@=ˠ�mP�1�?��PpS덄�?�]����7>���Ă��q�~�@�'P��	�ZNܟCt�������Qp{���9D��J-'��!�;�খ���pS�N���	��9�R7'Pj9q�]���L�z�ᦖ���t~pSˉ�s�n-?�����9D7���	��;�Rw'Pj���������:q��N�_j���������:q��N�_j��������N���	�z8�Rs�x��\'�/(׉��u���r�x��\'�/(׉��u���r�x��\'�/�r]N���	'����Ɖ�'�o�8�p��F���'�o�8�p��F���'�o��a;�F���ˆח'�/N\_69��f�e�n69��f�n69��f;N�l�	�MN\����I�E[�'��DM�N;G�?�?�0=�izE��D��E�4.�Gh��� HHYg�?��=�2��q��,��e���3�������rxL9<��Wπ,�g���3���^ga��Y��ufh��8sz�9=֜�mNπ7�g@��3`��P���sz�9=Cwf����Y��;3`��P��rh}�D�1ii�H3ȣE�A"-�2i�f�J�4�pf���Y�a83ȧ{{	b�H}Rj����"� �iY�H3H�E�A^-�k�f����Y�a93,ga�u� �B� ��7M�-�f�b�4�[�$�"� �ii�H3lga��,̰����0�\{~�A���A�-R�t[�>��H3H�E�A�-�Rn�f�s�4��[���0�qf8΢���h�G�=�7�#�B��w���&��������i��i�����ȻE[�,��y�E3<��0��,� ��3Ȼ3ȻE�m}Ǥ��"� �iy�H3ȻE�A�-���0��,��:3�����i�Aޅ�A�-R��[�>y�H3ȻE�A�-��n�f�w�4��[���0Cwf����Y�A�=>    O?���y��w��ӏ�{|�~�����#���y��<���|�����>`�f���Y�a83gay��<���1��{|��m>R��{|��Gm>����yZ��H3Ȼ��i}��#�0����0�tfX�����yZ�)by��<���|D�0iy��<�O�|�����>{�f�w������Gʲ����0�vf�w����N3�]������w}�~�����y���>O?x�������]�a��3�w}�}���ޏ���Ż>þx�g�����]�2M�Mle���4��eb+۴E�t'z�h�D����W�,SYg���8�T��Y���O�����П�?��ժ���^���C����I�g�5���^�,����FE�PӼI�8oR1ϛTԒ����'joP����AzGP��Dm5Q�F&j'���/��zR1QO*&�I�D=����jz�>��� �;H�	j��j��5�x�L4ZP��D#��h$��b��TL4H�<���;��z�T�lAM4{P��Ds�h��&�;��fR1�J*&ZI�D+��h�j{�Ճ� �3H�
j����h��&�?#�'����D;��h'��b��TL�I�����{~A��'����D�5��F&:#���j��TLt���NRi"}��8�J��u�,��[���w�p�p�p5�>Yd��E�W�T�H�/2*ՓTL�$=����9##�+H��{���?v]��]�p�c�5���u�?v]{���ޤb�7���M*&��]�p�c�5���u�?v]��]�p�c�5���u�?v]��]�p�c׵�TLԓ��zR1QO*&��]�p�c�5���u�?v]��]�p�c�5���u�?v]��]�p�c׵�TL4���FR1�H*&��]�p�c�5���u�?v]��]�p�c�5���u�?v]��]�p�c׵�TL4���fR1�J*&*�?����x�r����'*�?����x�r����ovt��Q�������wTn��r����_���|���͇���|���͇���|���͇���|����;*�w��޽�r{����=;ZAv���GЏQ���^n�c���~�z�ݏQ/��1��v?F���|�r��M���ob��b����r��^n�#���~{�ݏ`/���Qn���G�����r��^n�#���~{�ݏQ/��1����M� ��A6E*N���j�r;���¥���,��JUn���*U�T�r;�T-��R��ZJՒj)n�j1n/dq32n/���P#��B���5n/���P#��b��T�0��p��x�^��d�42n/���P#��B���5n/���P#̤b��T�0��p��z�^��d�12n/���P#��B���5n/���P#��b��T����p��<n/�b�^�Ÿd�^�p{�F���j�^�p{�F�I�'��$#���{�^��d�02n/���P#��B����u:��P_����0�4��%�F������a��B� ��Q#�^��[�~A}�n/�;�ޠF���j�'��I*Fx��p�;=n/�b�^�Ÿd�^�p{�F����EH3�p{�Fx��ޤb��T�����p{!�[������P#��B���5n/���P#�ۗG�I�=��'#�۷G(�o�Pn��ܾ=B�}{�r��������#�۷G(�o�0��FR1�H*F(���<��>;�r��Σ���(���<��>;�r��Σ���(���<��>;��T�0��fR1no>;��|v����<p{��y�������g�ۛ���7��no>;��|v;�a'#�b��|v����<p{��y�������g�ۛ���7��no>;��|v����<NR1�I*F8I�����<p{��y�������g�ۛ���7��'no>;O��|v�����<q{��y��J#�_Ri��K*�0q;��w+n/T*�^�T��P�p{�R��B��m�
�*n/T*�^�TOR�z��(՛T���#������=�����P#��B���5n/���P#��B�В�ZR1BK*F����fF��,^A� _�w�|5�/�p{�F���j�^�zR1BO*F�I�������P�q{���B���5n/���zP#��B���5n/�#�a$#̤b�Χ����Y܃�j�^�p{�F���j�^�VR1�J*FXI����^1n/d��x������P#��B���5n/���P#�b��T����p;��b�^���b�^�p{�F��|�cj�^�p{�F���$#��b��T�/�T�n�.��-Յۻ��p{�Tn�3�#H�һ��*U�
n�V����*X��[�w�`������}gS#Ȧf�M� �"����w?�n�~>/���|^�v?�V�����w�%��H�w?�n�~"-���DZ�����p{�i���'����O��ۻ�H���1p{�b��B����8A��텊���*n/T�ޛ�$n9���P�p{!_vJ���c������7��no>�-��|j[���Զp{�����M� �:Am
�7�b�����VR��|��:�����|5�:�������[�ϸ�����[�ϸ�[�|���/n�*�L�����{^��絀�{^���P��͇��ۛ]]�7��no>tu��|�꺽y��no��ۛG�����=Oºn�NU��ݩ꺽;U]�w�����Tu�ޝ��ۻS�u{w��n�NU��yz�u�/>W]���s�u�/>w]��2o�u���d1������Y<�,^�&�A�1����_P� \��6Ү�v+h�u����
�u�n��n��v]�������~�ݏ���v?���n�r����V�.��lj��2֦v�M��6�����р��P��B��5 n/��GP�p{�R�v��S*�^�T��P��/�T�	*n��d��T{P�q{�2��Be��|��*3nVf�^�̸�P��T�тJ����q{!�Tf�^�̸�P�q{�2��Be���ʌ���*��A��#�T���Ԑ�R�Ae��ʌ����'�̸�P�q��2�u�q�n?μfP��
*n�?d�텪��Be��ʌ����u�=�̸]X�q{�2��Be�;�T��
��%2��B�oP�q{�2��Be��ʌ�����ae��ʌ�_}	�/؄O�Ieo�PR��!A�^}���}�|=Ӈ��tܾ��n��G���y�J���R��eo�GR�Ae������>�K�O������/{�o�P�q��7�~���A���|��q{!�Tf�^������r�po�}���>��kpz�r������y����ۧ�-�O�[n����>=o�}y������ۗ�[n_�o�}y������ �=A�������(|�:�]C��-H�Gp�:�Ty^�TW�*ϫ��	����q�RU��K��*�~RA�J��TW��R��	���w�n�9V�}�X���c��7G�/n/����Ju��j�z����}\��T���RmA�Ju�TG��R�7���T��z�����]��TI5RmA�����َ��G�v�^�g;n/Գ����c|����P?����������X���AzIuһ�ջ����z�v}������?T���!����r�0V�һ���|zʽ�H����}��}I���6c�� �#H/�zz��zw��To#�po{���7�τ� ������AzW�^R��#��To��@	�x�4av!�N�#HU��!\�)��%T���b��C-��?�b�]�W��dq�X����3��d1�z���_P�'���7��d1�f� �g�ŤZY��,>A-^��^����7�bR�,�A� ����#(���d�R�y���Xn�P��v}��~;Gn�POQ�]��su��J����ᬠ���T#���rH5]=O�*�V�-H�T;��J���
RU*������~PGnק�*��*����U[��R�-��:�T���TW�*�F�'�*no>Vno3UR�T[�*�v�#H�T'���T�����M?e�T�q�RU���ڂT���TG�� �  R���
R%�H�U��}�����j�ڂTI�SA��:�� U��.<��wR����*�xSmA�J5Z�#HU�FOu��j�z����1]��TI�RmA��ک� UR�TW��R�+�>s�p�̱��3�
��+�>s�p�̱��3�
�O����j�z���vn����B��Z�� UR�TG�*�N�+HU�x���'�*n������Rq_���T�,Wu�*�ʱ��+�
��+ܾr�p;7q�>�#$�J���j�:�TI����+�^(_�vn���~M���S���T������ U��&��#HU�����
R%�H�U����5��n箭�T;H�T*n�
��;�B��V�P���jM��]�T�[���S*n�
����B�▧P���iS؃KH���R��^�D���T�?>0,����~A:���n��o�j���]�A��0vu��j���TI5]�����T+�7H�T;��J���RU*.Ի~mOn���z�	��}Jŵ7U��C�J��vU[��Rq�Z���T\�Vu�*עU=AU'����	R%�H���j�:�TI�R]A������?OPO~��増���!U�⢷�-HU����RU*.z���TI5S=AU7����	RU*.\�ڂT��e^��C��Z�� UR�TOPU��墷�/����y�z�R�,�A� ���K�Z��,�A+W�Z��R?��'����Y܂,�A�je��xYL���'���/���x���Rqu]���T\]Wu�*W�U�A�J��u���/����_������j�ڃTI5S�A��Z�� URmWq{!?J�����T\]W��*W�U�A�J��uUw��R�+�>r�p;W��s�oP�D�۹��jR%�Lu��j���TI�]���P*�N�o��Rqu]���T\]�����P�g���uUw��Rqu]?����۹����J��jR%�Lu��j���TI�]���t,�N�o��Rqu]���T+�
��+��յ~��ϥ~��>n��*n/T�su]�7H�T#��J���R%�Ju��j���U��\]W�RU*���ڃT���k�)�K�PF���ԫ��U�۹�V/�K���z�\ꇏ�T-�=H�0V��K�����������\�Ӌ�Ջ����AzI5�;��.c�� ��Z������ ���P�������Az��zg�^R��� ��H/n/T/n�e��7��V����T��*75S?����)���\B�▇^�^�^�^�^�^�^���Q�� �#H��K*n+�^�c�۹����U���j-I�^�#�۹ժjR%�Iu��T�[��� �����r�����[���A��_��jR��.Wg�*�F�;H�T�����H�c�� URqp��Z=�z��:�T��;��� U��>e�s࿠�r��zn���!��G_n�P���^�5����&ڤ������~�/���'���7���fd����C������W��� �=��H���1j�|.��GH���ϥ~��@���~�7�K���w��1����~R��y���ӂ��Ӄ��3���3��ﳌ��d�JŽ3���?�䟠�+�����j�r��گ�^�~���_��qw\��A�{��o���������֌�����H�z�m�߶��w��	j���~�cd��j��T���=����~�j�}����cd���~ϫ������*Gc� U�WI5fP�p��T��I*��$n�
��[���2n/Ԗq{��������e�y�;؄'�yq{����J���
��[���2n/d�3ȖW�-� [&���q{�����e�^�-��Bm�j˸��sj�3ȖW�-� [>Am�j˸����2n/Ԗq{�����e�^Ȗg�-�jd�;ȖO�n�ϥ~8�Op
��*�_i�|.��&A�<�ly���e��/����e�^�-��Bm�j˸�P[���ٵ�p�p�O�����
�*n�1����e��e�^�-��B�<�l�T=[�A�|�l�j˸�P[���2n�=7����e�֖g�-� [�A�L��-��Bm��l�j˸�P[���2n�ݼ����e�-� [>Am�j˸����2n/Ԗq;Ȗq{�������eR�ly��1�e�^�-��Bm�j˸���2n/Ԗq;X[�A���lyٲR�v%[���2n�2n/Ԗq{�����e�����=�A�����=�A�����=�A�����=�A�����=�A�����=�A�����=�A�����=�A�����=�A��[�G6�텲n/��p{�l���
��~lm���\�S���n!�η)>��!�A65�ljٔR�Nom��)�^�M��Bm
�jS�����n/dS#Ȧf�M� �"՛M��6���)�^�M��Bm
��3���lj����dS����	jS��P����n/Ԧp;oe�)�^ȦF�M� �ZA6E��M��6���)�^�M��Bm
�����=�p{O*�ޓ
�����=�p{O*�ޓ
�����=�p;oݟ#����p��B��N����=A���B��v�������ۃ�*����׻��� �Jş��������������A�z��_��;�����w����zq{�zq;������ۃ��*�/n�y|q����g_�>��������<��}�������z��_��;���j�w������s��BR�Ti�7؅-8�=8�J�~�AzW���W���^�^�^�^�^�^�^ܾ��� �#H��K�����Ջ�Ջ�Ww/n/T/n/���%�J�һ��� ���������������+�/n_y|q����W_ܾ��������<��}����;�/n�y|q����w_ܾFzg���w��XM���B���B���B����Se	{pG��Q���ϥ�Oy��AzOP������^�^�^�^Ho7V/�Fzg���w��Tӽ��P���P�������w���U��K��{���� ���<�������vc��j�w�]Az��z9Vǽ��P���P�����<�������'�/n?y|q����O_�~����B��T�]�z�\�[؂�vc����w�]Az��zI�S�q�\ꇯ�	6��BR��� �#H��K�����Ջ�Ջ��q/n/T/n/��齩�wl�� �+H��{��e�_����z���+��^�]!׿�����6�z�Zzw��To���I���ߠz{�ۃ��j�w�]Azw�^RM��_P��	�w�A�R��� �#H��K������;A�NR��7��ق�� �J��TѫLn/ԧ9�\�z����w
^|�,��?�b�]��A��W��4����\�A�������R?Ԁ�T#��J����UݤZ��A��ک� UR�Tg��R��Tw��R���r���������A�J��T{��R�=�r��z��88z���<��Um|.��#$�J�R%�N��*U�� UR�Tw��R���r������'��A�J�z�=H�TO�3HU�ڛ�R%�N�]|�GH*��
�`�j�ڃTIuR�A�J�7B��RU*����~��R�ɯ��*��T��=HU����W�A���� URMWq{����sp��.!�v�=H�T'���T��RU���U������*��� U�o�3H�T-��J*��u���s
I���:A�⍔B�⽑B��m�B�⎾P�����T�7R�T���vޅhz-�v�뵀�y��뵀�������Wu�*��U]A�J�]yUOPU��}7U�^HU����jRU*:�TI5R]A���������;k����*�88z���B�Rp;w�UA�J��oUW��Rq�[��U>�ڹ�T�ϥ~HU��[�jRU*�˪:�TI�R]A��꩞�����E����*�88C؂SH���RU*�骺�T����������������Rq'R���T'�q�If�ΝHUW�*�V�'�*n箍*n/�J��jο�������V      b      x��\�r�ȑ>k��nk�C@��M�n{�i�4#y;��H��I���!���g����unn���  PP;|�03++++�Jș�g}v�f��e���d�4�ҳ��&Z%K��3�@�iG�`~0�5j)>D1sZ���k��F�$v{E|�8Xsh����M�TK��lVM�te����Q��c�
�䛜��������,�h'h�� ��E����'i�]��x��A��=J�!�U�6�FI����`��͒,���&�C��&�N��8`�٤�u���`��e���0HIQ2A �p�Q�NmԮ��a�����?�70֋ll�7Cu�Oȡ�*ژ��P܁�K����:Z��v��������.���������P�����28t�p�����1����>���&>��6iq���7#Uw��=l�Yr�
��������ِ�o��?�Ar؇X����a2Z'5��.�]��*|�}Orb2[T�U��c�˪r�џIyi�f�����W�p9{��6R?��`�k��|� s�\ug����6���-6���.0�$ k��Қ'r5:ZI>a��hnV��i�3���azx�E�Gf�e�2�h�m��ѐ骆�BP���r�͍��/q� Õ[��N^��yt�� ��$ؑ�Eǆz.�_wjT�Y�:�'������Gssm�~͞�8ɑnj�6i4��-�<����C3�	�TR�	s\U�w��|?�7�����o98z��C	ǭ����]�"O#x4w�8�w�^o�|��P�j��m�J>�̠����bJ;�,��t�E�|;��.�y�<G��8?�� g���������˫�#st.�U�8��)�~h��g�;�������*Z7��������o��!p�	�m�9z�S9�Q�����n�;`���pM����ɾ6
U��8��])�ɬV��j�Bw���l2�j
Q?}D�eg��-�i]&	��B�v[fuZ�=&�����`�#�,B���F%3�]$�Y3�;����p~,��lb�϶���mi���t/���6�*�g���;�\S�2oV�Y�2̄�p�C<���i�,MZ�0�
/����O8��1Yf_[WȀ���ޯHa�rx��*F����K�o�L7�'�:�)�a�w��i��B?���T�C�U��^]�ѹz{,�K�߮�&A}����,z�:�ǄJ�M�}�<�)�G
Bˆ�c&��;N���P�b�	SUv����	�DE8�����um������\A�+�犉?�$ymB���|g̦Kq�0�Uk)�R���0yk�ˤG�s��%j0E�kp/d~�^�B�qqbk>+���I��s�k�!7�8y����U��S��$�4�C���j�)����$1��:�+���Zh:h*�?�X�*�EQ\$M���?j8�c���q��Ol"-��<�sʢ�,Ppİ.�v9�,ܐ2�K
�'f�Ӄ�0s�N�����#�V�pI�1�I�h�̸Ǎ�Q��p���"N��$7\ft�v?5qb("xC�LR��	�׀����LY�$�A��������
v6%E�YiY�:��:^7�)(\4�+/�C��66� ��.W�V�������%jl�4�J|Dz� ?p�%Q�m�i�2M� ��������?E��\�رW,�C�y�z�c�d�����y�S�~Ɛ��yT���$�/*���QM�뫫{)'��GM{��6�}"-�������m/�2&�ѕ�3�UvYX4'��c$҆,�b�F$�T�������tS�;��D�����&�ccU��\�^7�bQ
}T!E�Ŭ���:�I)�e!?��ne�d��?�q0kI��)�6��[�ʁj�68�G5�I"��N��%W�p��hf��,�#��M�_O#2�Z�m�:�0}5�7M�&�.~�(���:��Wy��wo-J��E�����w4�eoT;N�޼3�ͣ�����vO>JV��*a�/�����w�U�+Y�=v$i]���^���fʏA|����@�����ϴ(F.O���`̄�F��|�����:	�w1������[����8X�`�\j�,M��A�d����cP�{u$t�����f2�y #Y5���\t��2�����#I��ݫpKW���$���j���,WH��]㌀L6�C�,�Mt�����>�u��=o�8�ʤ4SZ ǳ���'I?��k˗��U�d��=��{Cy �C��zt��K�{ŋ�ಡX�3����� �o�;l��6�P���vG�c5T"��_d�>#I�� "� j�B���Ϛ��� ���|���ɨ��7wґdv��E�����O�a��k��D6`p5����0��,��]����A( ؓ� `͎MQW�g�8�9(r���۩�߭M�m�G�0`�n��x0� ��h���;��尬6����+�cK>���m(�����f�J�y,#i=̈́G��e~�Hg�����i�1�?eD�Il��ݕE�f����= �P$?��t;�p��������TG(��ÄY��y ;���h���5;<�vt��6�<�L�C���M(Wg�� �9����GG���=ҭr�4�{��%��Q��'�~�Y���Na��W_$�w������C��2�v��R�R���s@��lw/:�%k���9��;���@�Y_�E$�X�)q�N�c>/y ���i��K��^�65AM�+�ھ��y@f������ ���-�p9�ɧ��Z�yݞ����7�@�`N�"�,KKpy$f��������Ox�������������<��>:P�HQ�L�K���6(�@BT������%� �9�v��/�\9���񓃉ɽ���@���?[�$��i^
����$���(,^¶����~I��'�:�f%z*I!#�{`��0�f�~���LU�og�K�g�l� 9�Я3�D]n,�ޖ�])�����F��X3�ܗE�8m"�R�9���&f?j�Nx����٩�؄
àP�@U���Q����M�<fOV�ΰ��S�˅'�]��Qf己ɠ�{j�H� M\�X�%,uW`��z�ԗ�h��=���v2U�aJ�PA��.<��O6��:I@���C@]�)��� ��ˌZ��WI�2�U��Ӱ�`��^��&�(n�6�'�_�����L�6yP)9l��o�{@�Rח�<�ͣ��r�%�����8��
/�xS�_�Z�JyY>ʣƬT�o�h@����U�������$"�&*���}��%�@���v@�G�z7D�/]�����XS�ǵ�"I���M6�j0���SwB�2C5��Up����A0�%l2>)�VJ�㜄�bj�NVp���A�eJ�v�Y���t��5J�TR$k������h&���Gu�A� �I�'�y�K��&	�m)`�$�vo�]v�H�ؚ[Y��Q�#� �&ʻ���C��4�.����!���XΝ>���4ZZ�Pf�JU y�L=YM����z�z+ \;�j��T|. �d�;�����~���V��J��_�%i �$��F�T+D?i�y��%��YOj`�D�]OVt���@��9����D׫V;#	��DN��E;��S�!|]�	��C5�i�rY�o�(����2��/枼�р��q�n��[_���rNXB�[p��H�\�� ��
ܚ�/��HҸa�����\;���:��@�����2a�a;��ґ\~��B�[���l�Ji�J��yCj�_�UYӾ�pр������<�����]�*��6�1��*��DMDW��s<�L�&�	Y���W������'o�8�3��(+ںR�Ut�I�2�-��	�D;��Й����w9JW�/�C�� ��I��y�[ΆS�t`��@�
�р�IF�鄄yG穁:y��j����L�r�W�a(�ێ�U-*ES`����Kߥ,r��f���T�(9����� I�$K������}�� B��}���"�2h�b���6�� �c��UUWLe�U�=u�p�[8%�F���Z���`o��X�vS��
�   8���Z2
Rx�s�JUd=��~��"x ]�͌B�2T �waҮG"Y� �v�X>�dͤ1߭d��ͷ˴x�:9o��@s$���=��h����L���"H��T*C��g����k�F�dLd��u/MK�Lz�ި#E	S���tL�u��_���w'\_A��3A�8;�s鬿����t�ҫ����V�h���E�q
�"�v�!ys�u�k�}�@r]!�#w�GRk,�> ��pٲFg������e��WxO�B�FY�������
���o��C�%��gH�:?!n�7j�]�=��D��{5𯏰^�\�1/�_�H�^�� �e��Ż6�Xe��ڇ�6��p��Y�5#�a=S�n��T��ߺc������C��!e8,�e����s�#��[qs���T�yѽD�,#�^�[@���6�Ul��Y^d��`���z���x$w�f�1����:e�~[����-Ɏܶ�&s���b~�it� x�ɉ7׊U$���Q��-�쳀{�rJ�?:��L��!��%E�2�{�AW[[�����T�c:�?�y�Ѐ}+JV� 5ߒ{�_	-u���[��R���"|Z~�)] �7�D�N��z�^e!�\J5;Ok�Ѭk��Q�ѥ�P���y	 �[g��u,����ᰌ��UfT�R��"�|FN��2k��r"��`Vm5h�s{<�a���������y��к��B��)?^����x�כ�?}|/i�"� e�f�����"g�������~��j�>����o�R��zLZj@��7LZj@�^6i�NNL�)`��[r��g+��?J]&e{��1��Y��C�>�Xo�{���Z����z��!%����w��������6�;�} �>��b.�>���}.��J���7���}>Wo��D�d�{i�S����r>�T�6�8�8L��؛�r��5f>pS��/��Ku���KCUR��g~���{����W��e|�.��1)�O��m�:_�g�S��洑�0�^����S?'�|@������h&�9��n�=��*�Z����v�ҩ�׌�5�O���z�:�@n��8�c�uS�կp*����b����^˒��t�!A> [��+����I�ek(����?}����j����>|P�{���j�uh�dV�'17�0b�3ҍ���u`3"avl>[�^�e�p�|��C�朗ό�  �W�{>v��o>`b=�V��f�'��;�=fn�\�-#|+��z����IF����o-}@�,�Uc1`H�� ���5> fȠt��PRD�o�}��h~| ���O���F�r�BN�c_�R$�����!_�G�|#Yc��qҀ�I�M��Fܯ" ��L��vGO�d�/m5��9���Q"l#,���4`�"Onm������lVAX���9�2!�9����kZ���&�Q���w>I)��eh�� Dnh�i�����u�¨@�(߫p�q����*Dy�ӭ��e�f�*��T� ��J�4�����JR��>s[�Ө���$쯊 ���O*}Sf�����|�׾n����}���>�41�JtrM�Y_��+���^�����Zx�@N�d�"�U��\U��(,���H>
�qFw3���Z��^���֪��ӈ4]�Zt�~սS�,�5�3�o����6@o���4��&қ�#Ŭ�k�Ek�8̂-ڗ���pL��v�H�^n�ͅ���&m�Z��7*>m��* ���<��O�}���ݹq       e      x�u[͎�H�>�O������c��m�dم�w
k�Ȓ�*�(�@������>�g�`Ͼm�ͥ���?ff��1��̌���/��E�����Xܕ�nS����˛�BY���c�����xp��r��oI�x3�8�~</�{��Ͷi�}�O�ߓ����,�#������X�e�����ဏ�#�ȿۿ�B��}!�O�:�j�D���w>�5��oUl:^��6xw���v�R.K����\���Ŀ�u�����VEY�_g`���^̆}B����N�8���`y<��ӷM#���(K��j��﫲揻C'ְ�#��|,9�\ZOބoFB�2W�m:8�>Xjec��u�eQ�x]y{�f��A��s�����~��W��5i<m��^�%Aʈ�|�'>��ž\��a4�;���]���ٳ�i>��o~�h�R�-����n�ǻl�����(�I�Qt��l*,7M�?�e��'��E�	�;$r�,�7���.�Pn��1�R��6q��=���ŕ�{EYp}��������7`�:X����vq~f1�ox	�ST��WU�� ������B<���T��kƑ�OQ�oh��k����Cs�].�}� �]��i�%`����b�Y���������'�R��o��_�Gv�3�νA�sQ�P[Kߊ���Y��u���P[����v�ϩ�R¹�˄�6�s���5���	2�\�,���ŒW�V$�Gu�#��׵?�@IR?�b���U @q0�N��~R�����W����%��)s�%~d|.�-��n�
y�4P>�}���8�;@���Z�"y�J���X 8���7`�F���)JH����Y�`�C���į=�����s����)zO�&S�,�}����Q��ϡ����U�k�껇�CQ����b��������U^��k���,^(B����~+����MŹg˫�5o��W͑�\������"��V9J2׊x �;���bw���'�*+����0F�sɷ%�	�`���C&�Q2�A�`�3fG
|��P����hC wܔE�G�O,`�7-Tx��_pU �� Ҁ��ߓi�	b��$���ᆳ��v�[@���_we�D��3�=���(��[8T7��4����n��Z�xg
C?N/Z�D/ ڬOB����d���{���W�^4F~ix[t�H��Z$:cw���E��������d@*n���W�YՂT���v�]��r��	�`z�IB��>7_J(]*^�E�
�r��z?�11��C:o����]�U�$�k^�˶�5
!������&=xxÐ�s�8�����I\�*���{�#��P�Q��/n�34J=G��P?�2��p����|�Ws`���Y����"����-��NQ��!T@�a�&�������6�إ#�6ꔘ/�t�6�ae�LH'� HPb��Ԭ���� ���~�kY�̛�^އ�����N�0:o6��&l�����L�8�q9����S��&���hw�@�}��ȏg��%��}�l��8�U���_&|���6�e��ٷ�%>e'9�4�p;S�B�:a%c�LPFF�ܵ�5Z�Q�9���]u	��x��>s�ŋ���Tp����k`0�ѥ �P�O4��^\���5�m�~ ��m�Ǻ���
"0v��0�%�UJ2I� C����5�J��6sn�@�7`�!O*���5e����d0i�؏"��"v�y�Q$��	r��B
�=��Z����/!�׀�N�;�����"B�|'r�T�2��}������ "���w��k�Gg���u#^�h�u�kL��>�0�7qD?�-Pµ:�#",F#ß�l�Z7�::H�����oY:Q��^*u����	��\��
�(s5(Ts��"�̚�^k���2;����f�˲�;`cZ.s�ĺ�ĉ�ӫF��pȂ�f���D�mN�7 �)q�4S��{H���0/~)�Bg�	��uYt@'��/d�!�9��X�Q�I47a�`mn�Ai?�+�����I<%�}ޗ"J�+�<�w8�Й*K��􌋤�V�ӟ\�S4F�8KFkx0߂w�׊�C�Ն����ڹZ��SX������V�l�LT��
�V�Z���q�=�|�B埌�O"����Q��X
׺l�;��A� �ư� �=�D���e����}����*�33��>k��E?���3ν��6��>�J׸�O�*2��[��Ix����U�3�7�2d��f-�%�/&��ri��*�K�-���$��QCGhaBix6iM�cS$��[� T�F���8s�C���h��<\,jA�'�r�WFi$��hc��������|n�,����u�	3�0�Y��y���}J��c�W�*
�m9�M$�yF��e�6@� ��z踑U�	��� oyW���K�sL��f�o�w�n-�������_Wߜ�=�qg�	b{2B� #ٰ9�u>v$)㖅_=�����=v<)�i{ٷ�?*ܐ���A�t�'$}�!q�T��U~͑ ��˞o�ӷ��*=yb%����h�j(·���$h�5��
�s+C�	���y�K�.��ռ:�'�C�n�r�
Q�!�~�w���٤�8z�ʊ�4D&Cζ<T{���'W����G�i\9us��?�%>�����M;UJ�	5%IQ3Q6�g-��M���x?���%�fƻ���5j������X�2��%�_CP� g�`��cSwι���X�%:���7��P��� �^�n�Ae֫���&c�EP_�����l��X6í��v������t�H��� H���K�2ewl�����0�ڦ�+��QG�O�ݐ׬~��6f�"����`���1ܮ�9�㎔D��E0!Qy���c�����-gA>�}�de�,SŮ8`��01@R��� �R���h0�$ϱ9s��)8/�D���s��W��pF�	��}_�3��($�pF�|"ӛV$������R�6���� (IΘ�A]ET�N@�k��D��Z�	�n�,�tZ��;�;�i
����ND�m)΁h%�i�j4���o�o���� ���STJ�_z��| -���:?jg)�:t,k�	��C�M"��	(�u�^	P'��Q�+]�����,����Qvp@A�ˌ�$(J�h���w���V=����������2�ba��#8�'��<�(ὺ��Y�<B$&�]<�� �W�R��]�`�J�U!�ʣ�sp��9��GϠP����%p�IK\y�5E����hB�)�tv�8-޶�ֈ���W�ϻn����o�H��l6�<v<�V�6���'0!g�F
L~m�-w���9�l)��ps���) �rx��/���͈����%dJ0�l��J�&�B`�N�T�[����^܀��q��s1,<�K�!�c[&��c�g�^ �J=��dC!��.�΄E�� "H��+� �^��a��N��n�D�n�_�i�R4T`*X|���ё��*���S�AY�������ŭ��Ô�'��GH5��b0��Ae���x����aB��P+���[���B�ldT��}�|��!��9.�?��(r�*&X��]�|���A�<���&x�H��ۢ캢6�'*3���р� �ba��F���* �/��y��m�~��C-P�\����s��jë�A��ԛ��f�����S@��|�&U���z����?��_
H���]��e��'�s;o�^�gh|�IAw׭�y[�*�c/n�Q\c���C��N�b�`��c�K�Y��Z����f���P��$��.�FɝqL�/w�?M�����|k�� �@�N��yY��uY7_՚8�E�⯔���A�F�Ԕ)�BՏ�����4�e�''���ߩ�/M#��A'U���������+���Ǧsd%�(A���\s5�G����v��#m�12q��IӞ2aj��9HDcަC��q���%��d����H�
Y�.5o���D6����24E %  ��X��ޡ��a���~q�[](�E9�sC~۸]�ak�бZ���n�ܔw���`�Q쟊�@1Lї����s���x:r�v+�*�|�T���:�d�QBlra��NA��fE�k��������>;l��V�D@�.�}����1��C��T�i�b+�k�jb�Ǚ�chZk���[��t͈�1iN&/�Apz��?����ݪ����*��ߍ ��1ۍP����r�<j���N��-���`��ƀ[p�\	I�}�!FCa3��lD��lF�~��&D,�Q��)��E�\���V��r�'�(w�_�ș�����NF��6�'ύg��4� ����|��dk�4���̐��L�T����͐-2�G���Z�Ҫџ�Q�M�k���!W�e�h��1f��Q���#��/
B,�O}eD�Jg�T`!B���- X��ؕ���C��H���ɏo��VF�g~%qJ�!��g	ǵ�@���R�����?��c�t�����G٨Ps��5S�v�Q�Bn�: ᭌ�K�P~
N%�%O��ZF�/�p�Ll5�v܀!!*�;�J�a�ד����!��kg��؄&$`����O����#��&G��#��iV�m�m���c��I��3jq��<��?}L]P��%�L��5�/��FPt�Yd�I���% ��*���98o%H���Q�CC���	�`�.�N��UH8A^��mKEC�Xd'Y��#��E#F ex�i�08��z��Y9�x�C�4�ޖ�Mȸ|��=�u{$z<Z�Bv��N�Q�N��T��1�4�q��ziĦ�8p3��yS�\�~��\?3$�|"���=�;{��m�q����r������V��m�ʵ����B��Lj��:��jv��X�� PN�k��������J�͈�/�h{�\o*Ҍ�E(*w �P����rϵ�1#�'6&�ԪOS�(�"���n��EQw_�J�f�����oi��'��o����)��,ko�m����i�g��/Qn����J� �L���k�y_8�l�n1�i�;h҃���`l9� �c�kx�n�5�R��-W��a��m)l�\RZ��I��F>d������L<����m��漏�_��p��D+��M5����R;��B�����,��W�\�0���X�/ڇ��N\+όy(���$M�nR_IG#V��~:=j���~)ު/R��g�nj�{�~;$F)B4&�DdW��d��{<�� �jg
�֮��)���H���	�Ý��&_l<đB�u����ۋ/�5��     