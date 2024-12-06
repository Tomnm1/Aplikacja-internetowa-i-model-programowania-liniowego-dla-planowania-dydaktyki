PGDMP  #    -                |            inz    16.4    16.4 x    h           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            i           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            j           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            k           1262    16397    inz    DATABASE     v   CREATE DATABASE inz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE inz;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            l           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            k           1247    16399 
   class_type    TYPE     m   CREATE TYPE public.class_type AS ENUM (
    'wykład',
    'ćwiczenia',
    'laboratoria',
    'projekt'
);
    DROP TYPE public.class_type;
       public          postgres    false    4            n           1247    16408    cycle    TYPE     @   CREATE TYPE public.cycle AS ENUM (
    'first',
    'second'
);
    DROP TYPE public.cycle;
       public          postgres    false    4            q           1247    16414    day    TYPE     �   CREATE TYPE public.day AS ENUM (
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday'
);
    DROP TYPE public.day;
       public          postgres    false    4            t           1247    16430    degree    TYPE     ;  CREATE TYPE public.degree AS ENUM (
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
       public          postgres    false    4            w           1247    16462    language    TYPE     G   CREATE TYPE public.language AS ENUM (
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
            public          postgres    false    215    4            �            1259    16471 
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
            public          postgres    false    219    4            �            1259    16483    generated_plans    TABLE     �   CREATE TABLE public.generated_plans (
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
       public         heap    postgres    false    4    875            �            1259    16490    groups_group_id_seq    SEQUENCE     �   ALTER TABLE public.groups ALTER COLUMN group_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.groups_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    223            �            1259    16491    plans    TABLE     �   CREATE TABLE public.plans (
    name text NOT NULL,
    creation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    plan_id integer NOT NULL
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
       public         heap    postgres    false    881    4            �            1259    16508    slots_days_slots_days_id_seq    SEQUENCE     �   ALTER TABLE public.slots_days ALTER COLUMN slots_days_id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    4    229            �            1259    16510    specialisations    TABLE     �   CREATE TABLE public.specialisations (
    specialisation_id integer NOT NULL,
    name text,
    cycle public.cycle NOT NULL,
    field_of_study_id integer NOT NULL
);
 #   DROP TABLE public.specialisations;
       public         heap    postgres    false    4    878            �            1259    16515 %   specialisations_specialisation_id_seq    SEQUENCE     �   ALTER TABLE public.specialisations ALTER COLUMN specialisation_id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    237    4            �            1259    16524    subject_types    TABLE     �   CREATE TABLE public.subject_types (
    subject_types_id integer NOT NULL,
    subject_id integer NOT NULL,
    type public.class_type NOT NULL,
    max_students integer NOT NULL,
    number_of_hours integer NOT NULL
);
 !   DROP TABLE public.subject_types;
       public         heap    postgres    false    4    875            �            1259    16527    subject_types_groups    TABLE     �   CREATE TABLE public.subject_types_groups (
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
       public         heap    postgres    false    4    887            �            1259    16536    subjects_groups_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types_groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    4    242            �            1259    16538    teachers    TABLE     B  CREATE TABLE public.teachers (
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    preferences json,
    teacher_id integer NOT NULL,
    degree text,
    second_name character varying(50),
    usos_id integer,
    inner_id integer,
    is_admin boolean,
    elogin_id text
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
            public          postgres    false    4    245            D          0    16467 	   buildings 
   TABLE DATA           6   COPY public.buildings (code, building_id) FROM stdin;
    public          postgres    false    215   G�       F          0    16471 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    217   x�       e          0    16719    classrooms_subject_types 
   TABLE DATA           U   COPY public.classrooms_subject_types (id, classroom_id, subject_type_id) FROM stdin;
    public          postgres    false    248   ߞ       H          0    16477    fields_of_study 
   TABLE DATA           G   COPY public.fields_of_study (field_of_study_id, name, typ) FROM stdin;
    public          postgres    false    219   /�       J          0    16483    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    221   ��       L          0    16487    groups 
   TABLE DATA           I   COPY public.groups (group_id, code, semester_id, group_type) FROM stdin;
    public          postgres    false    223   ��       N          0    16491    plans 
   TABLE DATA           =   COPY public.plans (name, creation_date, plan_id) FROM stdin;
    public          postgres    false    225   $�       P          0    16498 	   semesters 
   TABLE DATA           P   COPY public.semesters (semester_id, specialisation_id, number, typ) FROM stdin;
    public          postgres    false    227   A�       R          0    16502    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    229   ��       S          0    16505 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    230   ӹ       V          0    16510    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    233   �       X          0    16516 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    235   κ       Z          0    16520    subject_type_teacher 
   TABLE DATA           o   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id, num_hours) FROM stdin;
    public          postgres    false    237   �       \          0    16524    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    239   <�       ]          0    16527    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    240   z�       _          0    16531    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    242   ��       b          0    16538    teachers 
   TABLE DATA           �   COPY public.teachers (first_name, last_name, preferences, teacher_id, degree, second_name, usos_id, inner_id, is_admin, elogin_id) FROM stdin;
    public          postgres    false    245   ��       m           0    0    buildings_building_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.buildings_building_id_seq', 9, true);
          public          postgres    false    216            n           0    0    classrooms_classroom_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.classrooms_classroom_id_seq', 23, true);
          public          postgres    false    218            o           0    0    classrooms_subject_types_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.classrooms_subject_types_id_seq', 39, true);
          public          postgres    false    247            p           0    0 %   fields_of_study_field_of_study_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.fields_of_study_field_of_study_id_seq', 12, true);
          public          postgres    false    220            q           0    0    generated_plans_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.generated_plans_id_seq', 1, false);
          public          postgres    false    222            r           0    0    groups_group_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.groups_group_id_seq', 1937, true);
          public          postgres    false    224            s           0    0    plans_plan_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.plans_plan_id_seq', 1, false);
          public          postgres    false    226            t           0    0    semesters_semester_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.semesters_semester_id_seq', 95, true);
          public          postgres    false    228            u           0    0    slots_days_slots_days_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.slots_days_slots_days_id_seq', 30, true);
          public          postgres    false    231            v           0    0    slots_slot_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.slots_slot_id_seq', 5, true);
          public          postgres    false    232            w           0    0 %   specialisations_specialisation_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.specialisations_specialisation_id_seq', 31, true);
          public          postgres    false    234            x           0    0    subgroups_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.subgroups_id_seq', 1, false);
          public          postgres    false    236            y           0    0 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE SET     _   SELECT pg_catalog.setval('public.subject_type_teacher_subject_type_teacher_id_seq', 22, true);
          public          postgres    false    238            z           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 757, true);
          public          postgres    false    241            {           0    0    subjects_groups_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 115, true);
          public          postgres    false    243            |           0    0    subjects_subject_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 393, true);
          public          postgres    false    244            }           0    0    teachers_teacher_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.teachers_teacher_id_seq', 16, true);
          public          postgres    false    246            }           2606    16545    buildings buildings_pk 
   CONSTRAINT     ]   ALTER TABLE ONLY public.buildings
    ADD CONSTRAINT buildings_pk PRIMARY KEY (building_id);
 @   ALTER TABLE ONLY public.buildings DROP CONSTRAINT buildings_pk;
       public            postgres    false    215                       2606    16547    classrooms classrooms_pk 
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
       public            postgres    false    245            �           2606    16734     classrooms classroom_building_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classroom_building_fk FOREIGN KEY (building_id) REFERENCES public.buildings(building_id) ON DELETE CASCADE NOT VALID;
 J   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classroom_building_fk;
       public          postgres    false    217    215    4733            �           2606    16764 >   classrooms_subject_types classrooms_subject_types_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id) ON DELETE CASCADE NOT VALID;
 h   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_classroom_fk;
       public          postgres    false    4735    248    217            �           2606    16769 A   classrooms_subject_types classrooms_subject_types_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms_subject_types
    ADD CONSTRAINT classrooms_subject_types_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 k   ALTER TABLE ONLY public.classrooms_subject_types DROP CONSTRAINT classrooms_subject_types_subject_type_fk;
       public          postgres    false    239    4757    248            �           2606    16739 0   specialisations field_of_study_specialisation_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT field_of_study_specialisation_fk FOREIGN KEY (field_of_study_id) REFERENCES public.fields_of_study(field_of_study_id) ON DELETE CASCADE NOT VALID;
 Z   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT field_of_study_specialisation_fk;
       public          postgres    false    4737    219    233            �           2606    16774 ,   generated_plans generated_plans_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id) ON DELETE SET NULL NOT VALID;
 V   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_classroom_fk;
       public          postgres    false    4735    217    221            �           2606    16779 (   generated_plans generated_plans_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE SET NULL NOT VALID;
 R   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_group_fk;
       public          postgres    false    4741    221    223            �           2606    16784 '   generated_plans generated_plans_plan_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_plan_fk FOREIGN KEY (plan_id) REFERENCES public.plans(plan_id) ON DELETE CASCADE NOT VALID;
 Q   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_plan_fk;
       public          postgres    false    225    4743    221            �           2606    16789 +   generated_plans generated_plans_slot_day_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_slot_day_fk FOREIGN KEY (slot_day_id) REFERENCES public.slots_days(slots_days_id) ON DELETE CASCADE NOT VALID;
 U   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_slot_day_fk;
       public          postgres    false    230    4749    221            �           2606    16794 /   generated_plans generated_plans_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 Y   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_subject_type_fk;
       public          postgres    false    221    239    4757            �           2606    16799 *   generated_plans generated_plans_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id) ON DELETE SET NULL NOT VALID;
 T   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_teacher_fk;
       public          postgres    false    245    221    4765            �           2606    16809    subgroups group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 <   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT group_fk;
       public          postgres    false    223    235    4741            �           2606    16804    groups groups_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) ON DELETE CASCADE NOT VALID;
 C   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_semester_fk;
       public          postgres    false    223    227    4745            �           2606    16744 %   semesters semester_specialisations_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_specialisations_fk FOREIGN KEY (specialisation_id) REFERENCES public.specialisations(specialisation_id) ON DELETE CASCADE NOT VALID;
 O   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_specialisations_fk;
       public          postgres    false    233    227    4751            �           2606    16754    slots_days slots_days_slot_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_slot_fk FOREIGN KEY (slot_id) REFERENCES public.slots(slot_id) ON DELETE CASCADE NOT VALID;
 G   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_slot_fk;
       public          postgres    false    230    229    4747            �           2606    16814    subgroups subgroup_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroup_fk FOREIGN KEY (subgroup_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 ?   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroup_fk;
       public          postgres    false    4741    235    223            �           2606    16819 9   subject_type_teacher subject_type_teacher_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 c   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_subject_type_fk;
       public          postgres    false    239    237    4757            �           2606    16824 4   subject_type_teacher subject_type_teacher_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id) ON DELETE CASCADE NOT VALID;
 ^   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_teacher_fk;
       public          postgres    false    245    4765    237            �           2606    16829 9   subject_types_groups subject_types_groups_sbuject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_sbuject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) ON DELETE CASCADE NOT VALID;
 c   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_sbuject_type_fk;
       public          postgres    false    4757    239    240            �           2606    16759 &   subject_types subject_types_subject_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_subject_fk FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id) ON DELETE CASCADE NOT VALID;
 P   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_subject_fk;
       public          postgres    false    4763    242    239            �           2606    16834 -   subject_types_groups subjects_groups_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON DELETE CASCADE NOT VALID;
 W   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_group_fk;
       public          postgres    false    4741    240    223            �           2606    16749    subjects subjects_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) ON DELETE CASCADE NOT VALID;
 G   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_semester_fk;
       public          postgres    false    227    4745    242            D   !   x�s�4�r4�4�r4��r
������ 7(�      F   W   x�M�A�0D��p��zob��c��������%�^�䀡�'R�xO7:5*r-:���N�L1a����K�d���/��"��a      e   @   x�%���@�7�2h%ysq�q�Ћ.fA���N�	����U�]�B�����f��� � ���      H   V   x�3�t���,.IL���K,�K�2��D2���sC3i��7���KC34 	*xz���q�e�"KZ�5`[�������� >p;[      J      x������ � �      L      x�m�Mn$�u��1�b����Mx�\�!��1/�U�F���Hz��l�kV>E�H�_��?��|���?�����c�����X^~+j���'���P]~;���?Y7�ݩ��v��U���v�j��Q�ן?��v����Q�W�ɾ���ᷣί��c�����V�ε}����o�����|}՟���nU�~;���?�V����K������t��=\�����ǦS����ҿ���ǦS���ҿ�}y�d�H��$ׯ�?�}������g���Ē+�d��������H���C�N��������D>��+&uń�����c�2WtrEWtpEW$�\q%sE'WtpEWtpE��72Wt�Su��g�^�O��R��g�>-��K���;�x���P��gy�Y>�w}�_ߺm}�������������_�k߮��o���޷%�.�ܷ?r�㏜|^�ȝ�?r��ܹ�#w����>r�nM��ޗu�c綺������:�������{���:�����S����S���U���U��<����G�O]�}���+ޟO��s�}H}ޅzޏɟ�މ�C�g��B��z76ݰ�ן��}����ŧ�\}z�ͧ��}z�ç�<uz�K��uz�G�G�~?u����.�~��ԥ����9v�����\'��8=���N�������G�E�Q�̣ęG	Y�g%�<J�<J<J<J�N�q���N����x����sz�E��X9=���vN�upz���s	���U��)�Γ�<J>=���s^>=���s>>=R��E��Xuz�M���uz�C��8uz�K��uz��ӣ���Z8=���6Nϥ�<������M�����������ɩ��+o���a���O�����O�}�'u?���������~J�S�u�0��qq����t=&u�8L��a��|9�}�9���b�駱��S��r�0�+���8���a&3��bBwń�	���	��+&tWL讘஘⮘⮘⮘��ZuWL���|W<�wŤ�I����I���+&7�s�a:���a:.���a:&��`~�oz��O�=�g�O��a�v�up����t]�K_�$��?�u��c%�[��a:/���a:&��LW���t��rBO�z��8t��S��t��[��xt�ě��a:ӵr����t����0]z���sE��s�c#�\1��I?WL��b���~��||��*�(!�(!�(!�(!� �� (@) JPJ %�#_n|ɑ/��(i��(i��(i��(!�� J�� J�� J�� J P
�R � (@)�@G�*��_�m_�Gͯ/��������Xz��|�3`9�3`9�3`9�X�r ��|���� ,`9��`���`�� ���5`�d�r,g�"���X΀�XN�r ���� ,`;`9 �X��2X.��2X.��,`g��Ȁ�X΀�X΀E^����� ,`9 �X�r �����e�\�e�\�X�R\��u�����������\ �X�r ���� ,`9 �a���`���`����E�q<2`9�3`9�3`9�3`9�X�X�r ���� ,`9��`�u,��r���� �/� 9o�4@I�4@I�4@I�@	�@�G %PB %PB % (@) JP
���%�ȑo�i��(i���J��J�� J�� J�� J �&�(!� �� � (@)�@�� �+i��(i��(i���J�� J�� J�� J�� J �%� �� � (@)�@�� �i��(i��(i��(i���%�(!�(!�(!�(!� �Z (@) JPJ %�#�D;w� %P� %P� %P� %PB 9v�@	�@	�@	� � � �u P
�R(!�g �۷g "� �@� �@� �@N r �� D� �   9 �@�2@.�2@.D= �  ^��zE�|�;�cP:��(�C�8�Di$JAQ
�R`�B��V<JR
�R&)i��F)i��f)�K)`r�g^�ұ)���)����Pi�JAT
�R �B�L�p*T��R�*i���*i���*�W)�r��v��t�J�t�J��t�J.��\)�JaW
�R蕂�~� ,�`)���lX҈%�XҌ%q,dyMJ${ܡ,���,���,��-�h)Hsm�����`-�k)`K![ʴ%m[Ҹ9w�4oI|K�+/��^n�>m\����r�f���U�jIW%�d]��K]ҮJ�Uɻ*�W%� ��*A��*a/y˽*��zߵ�_��6|��e��_��6|Ղ�J�U	�*�W%��_j|U��J�U_%�U_%�U_��|���]���U�j�Wm��_z7|Ն�Z�U	�*�W%��_��|U�/u�*��J��J�|U�/�WN^���U�j�Wm��_��6|i}��O	�*�W%��_��|U��J�U_��J��J�|U�ϵ嵚��6|Ն���U�j�Wm��_��|�U�U	�*�W%��_��|U�W	|���܀��*_��K��p>��ҁ/�ҁ/�ҁ/|)�K_
�\�^�
�R����_��%_��%��W�U�+�]�Nw�K�t�s��kՁ/�ҁ/|)�K_
�R��>��B���O_
�R�/i���/i���/	|)�s���r�t�K�t�K>��ҁ/|)�K_
�R����ϵ_��%_��%_��%�/|�3�����|���|���|��羀/|)�K_
�R����/e����K���K�$𥀏:�c��|���|���|���|i�K�k��/|)�K_
�R�/i���Ϲ���K_
�\��_n×�_��6|Ն���U�j�W%��_��K��J�U	�*�W%�����*���*�/�OU	��e�n���e��_��K߆���U�j�W-��_��|U��J��W%��_�U_%�U_%�U
�*��:�w�i�Wm��_��6|���U�j�W%��_��|U��J�U	��&�����*���*��R�U	��a������6|Ն���U�j�Wm��z���|U��J�U	�*�W%��_����|��W	|��W)��_�1|���1|Ն���U�j�Wm��_��|��%��_��|U��J�U	�*��J�K.�W	|���J���矚]�{|���|���|�����/|��R����/e���/i���/i����u���ҁ/��W�K�t�K�4�/|)�K_
�\7�/|)×4|I×4|I×��Q�+�]���|���|���^_:�/|)�K_
�R����V�K���K���K�$�ϵ���ҁ/�ҁ/�ҁ/���/|)�K_
�R������4|I×4|I×������_:�_:�_:�_�R��z�/|)�K_
�R�����K���#y���'_�R�矱�j�_��'�۸�G��x�g����ǯ��(�^Ǹ�m\u�6����{�t�6޺f]���&�[���׸�m<t�6�������~�ůq�5۸�m�t�6^���{�u�6>����}��\�������_��f��5�x}������[�l�׸�m�t�6�fo]s�=>�������s�6.�f��	�����	��_O�G�~�6����~�>���>�����>�s~��I��s�����>�۸��h�!�W�+��+�m��u�rݾrݾrݶ��ߞ�rݾrݾrݶn���j��;V]w��n_w�������������k[�n_�n_�n[/��߶qI�����ʀ����c�����������~ﱾ�|?s��z^s�|p�?7����� ܘ���>?�3�>ݚ��y���6Wݜƺ�����������z���z���zqp}�9�>?\���煃k����|�t��n�c�x�������9�E��U�� ]  M�ם����������|qp}�9�6��ڟ�P�+վ�P�+վ�P�+վ�P�O�x����>���g?T��j��Pm���i�����E:�U<�U>�u���u��������������������ppm�<�^��yƽ̏��{��1��
:}���N[?OI��.:���:��n:���:��\�O���盃�����Y���_��{����	�X�{��������g��\\_W\_7��;�烃���������0�D���߫�qc�=n��ǍU����7V��ƪ{\_7����=n�����=n�����=�ϧ��<���+ϱ�Kϱ�kϱ�:��:���:��^:���:��>\�?_�-��|u��ONΏ��2?@��k[�<����ƪ�qc���y�X�<n�z7V=�+�����1�<��~7f����������a��a�¡��p�p�p�p�p+8��c�1�C�/�p�mcccc}���X��X��X�a��0fp38��|�?VG[�����Wp�+8��
}�����W��g��g��g��g���ζ�C_�����Wp�+8��
}���6�ơ�ơ�ơ�ơ�78\m��>��Wp�+8��
}���C_�C��C��������e||�o�/����펻���Wp�+8�u���C_�����Wp�q�q�q�q�qh�	O[�����Wp�+8��z�C_�����W��g��g��g��g��{�_��|�U8�U8�U8�U8�U8�U8�U8�uc�1�Ø�a��0fp�3߲:��
��
��
��
��
��
��
��
���C�p38��}��ù�U8��ccccccc�1�Ø���8����sk�p�p�p��*�*�*�*�*�
c�1�Ø�a���g�k{�mcccc}=��X��X��X�a��0fp38����C���}���C_�����Wp�+8�^���l�n�n �n!�n"��aD�^�cF�1�Ęqb�@1f�3T�+�l,��[�����ۋ���w���P��c�s�1�Ƙac̸1f�3r��t��v��x��X�����R^�p���"�9#Ș!����Dƌ"c��1�Ș��-��M��m�؍I�w4y�'cƓ1ʘe̐��S�*cF�1���ە���[������;�����f�2g�2g�2g�2g3g	3g3�G��b�1s���C��!�﫾a��}m1s1s1s1s1s1s1s1s���o3w��;��bƮ�S~~#C�E̘O3g3g3g3g3g3g3g��;��b�~A��!f�����,b�,b�,b����;�>��Y��Y��Y��Y��b�1s���C��!f����*�Y��Y��Y��Y��YČy1s1s1s���C��!f�3w�;����>C̘!f�3f�3Čb�1}�@s61c71c71c71c71m��9�1c��1C̘!f�3f�3Čb�lb���������������s�b�|@̘!f�3f�3Čb�1c61c71c71}?M��ML�o���1c��1CL��3Čb�1c��1���������������+�<}��1C̘!f�3f���1c��1C̘M��M��M��M��ML��}��o(h���������������������%b�1s���C����2v������wI?����"f�"f�"f�"f�"f�"f�"f�"f�3�b�1s���C���=��oh���.b�,b�,b�,b�,b�,b�,b�1s���C����;Č]�A���6��9��9��1�"f�"f�"f�"f�"f�3w��;��b�1}�����o
h���������������*b�,b�,b�1s���C��!f�3�b�>C̘!f�3f�3Čb�1}>!f�&f�&f�&f�&f�&��Ĝ}��1C̘!f�3f�3Čb�1c61m�_&f�&f�&f�&��+�\}��>o3f�3Čb�1c��1C̘M��M��ML�Oȸ�c��1CF�/�3d�2�c��1���������������?/�x�c��1CƘ!c̐��2�c��1����������������.���v���QX�QT�����_�n�6      N      x������ � �      P   H  x�m��NC1Fgߧ�K��N��C%~$O�gت;�#����tI�~n��uyy��<Xh�������
����	�@�����f�,�c�����,B�{�z0�N`�5`���T�����hQ?������|��]ѿeX�.�)�*����p��:�#��s���iu;G�Ѩ��w@q�PR-�V5����BY�Pu�jW��[���-k��������sL!�j�m�K�z�.3p�z�x�.����UYs����ga�ga�ga�ga�9�¦�
F���)�{2�[���ou�ȷ�9�-��~uN��9�O�Կ�o(�o���9���q|�_(
      R   *   x�3�4��20 "NK+c���41�Z�\1z\\\ ��      S   "   x�3��4����KI��26 �KJS�A�=... yk�      V   �   x�U��
�0E�3��d�j�m-D(m� �|���c��N���y��P���,_����ܯ��-�����n[T��C�`�D7�A�0�0F�&���=�,&^�2���Abϰ��0��$�XB9'CʱyN���I0yb�,��BrP���H>���-��K�D
�-	Z�Ɋ����$\v���gu(      X      x������ � �      Z   A   x�%���0���0=L�iw��s�ϒ,r��Ŏ쭤윰�����~��@m�&̣|���T��      \   .  x�m�K�%KR���/�xg��z�4�VK-����������O�Jn�a��c�Y�i���_�������o�����t�������OO_B�j7��z��/������/��Y�&�M�3������}�W����yVM�i���[W8]���TM��ڭ�s�x;�拾�ƕ��.�Ը��Bo����UO�⹱�R=پ��ah�ӂG�)���xn|A�v��?�$�����c���O⹱��b�cύϣ�oI�ӫ��K<=��yz��و����UN� ��G9,/���ʲ\?��E�rE�O���&BZd
7¹���E�A8�=��"�w���!�k��.��xd�S5⹱��O\� �����E<7�M�����X���K�����h��R;�x���j��A<o��W-����+�gV��9�s���U�%���~�Y%�m�s���*�� ����Gk�f�i��p���U�z_⹱�+튠%�x#��qE�8�� ����jQ�E<7�ϲ�p�C<��}���%n��N�����=+?~��O'���xV���g�=űT	�`W�R)|^ k.��>��+�,tЯj'l:ԗ����{Wik:������bj�k�jj:w�����m��P�Չ�t�n�M��δ��r{Ru(��">�[k1��Sי�m�5�^�L����)ՙ��U�t�lϩ�]�Tc�.uz�2�:)��TG�)s���xN�џj96m��ףvom����������=r�ҧ̜v���֫s _C,�E�m����vz�i�<�!흧:��v�c�W�~x�	?�!mr:i!mfo�9��v�ڦشk��O{*����.���f�iо�z�}��+��_3�_�GT$}�hٿŹԴ�����*(����-�s��uy}zk��rzk�]��ZO:�Rj�j�oWZ�:����5дn� �&������w%Ml��ytbhS;��ؕŰ�ښ�ק��ޭ�Y���~�h�qW�g )��8D���3;7>�{�#ْ��ٶ�N��u$_\��ϗ��p�4Z�+� OK^�'��UE�e������W{S��@����F �A��wh[�
�l
��#��ok�кX5���7�����6�O2H�:��ݟ�`l�Io3�������ޠ\�c��'�q~ܕ 	��7�s&xx� y:� �Q 3 k/��Zĝw 6 Qv�"�7� �ݶ�����֠��=�U�F��nD���a�7���C�|�[��D�.�7m-}܈�0�jyז�g$�]�����$�}b���\���K:5�;b�'� �r���}�0[ yH�e���:@ږ2�c�@�:h�-H�q�`O �Ү�An �Æ�gZC�-��8i��H #� ҥ)�%���"}c�Z��R'� �n�r1{X��e�;Hf�XH��>#� i?����
$��@����	$?��r/?7���t���-�ru�����g�UX-tX�X�J�^ �<ӈ�Uf�<�(��i��!��܎�#� i_�tX�XH�k�|M��V��Qaٖ�?�L{��@~=1��=�i��~M�`�_J��ܱ�;� ¥g3Pan�$X��T��6	V�6���@��j�t�{7��=}��P� l���k?U���7tA�m����Hm =��J#�t�$���u���{Z�����@}����u�S���/3wF��+�o�<�H�S���u<�H/�~&v �G�@~����u�re�T��?	V@��@��Տ��
{}!#���E"*��<#��2Ϩ���s������8H0���XH��z��`o�"���E����`T�믃�`o��Qa�g	��/�{ y.!�J�j��ܰH�ұ�0�����l/���[����ܸA�ݯ��Lv+'e ���}�u}�#�n9)�_�<�����@]_��/7����>)_�E6�_�����٨h/wȆ�yw�,����PYPN�Ϟ�0�Y��St��@rM*̝�C$Iӡ���$���.�|w ��e���	҅�<?� R����%yҡ�F4?p�������Ĺ4Ҧ�߽|gj��,���7E$��O]���.��H��=��"=�k�����ΝW �\�
�A�A��3io ��e�I����[���{ ��o�W�����lJua���Dy%R�P�$=b���mO�]�s���LsP�u�d��k~$�z2�H��V��~�
$r��xI:���D'�Pn�MJ�z���Wb�C�z����ZmF9�=vꡆ=_���mEc�t�*:�@l$"Z��l!7�_;L����/&eQEm�{ I`Mj�JojR ��Ԥ>ʋq悰Mx�+\Ȕn��`��e��H���Uw��K�H���U/Jp@f ցr7�s�발��b#�qu:�}��˝�@��+2�$��c;�XEg�g���ϔ�sʨ��;S�{�Δ�ꥍ4%���HSʪ�6Ҕ���!��n%=� ��-~eN�����ʪ���Z~ؠ�ʺ������r�\"���g���➉�Z�#����c�UY�3�Vn"M�UY���Vn"-�Ui"-��6��V;o�]UZKi���BW���BZ����U����Vn--��[KQ���>g����j��]U�-t�-���/���]�h�b�ײbh����&����B����J�����g��򊡅�:6�U�Br�ш��P��@�}W���W�~�,�FY��X^��X'�o,�UY���W^��PWǲ��:v,,���[�BX���B[����V����Wn?-Vi?-D��O�U�O�v�@��AcyM�B`�ű�xo6%+;U4�n�c+J�-H+��Ҫh�j�_���7��04�׾y��&u@[��H$��������2�@�QH��*��@lDoQ(r���R���[E�	y���øz��W�*�0�]@kLȫ\LE�V������Na�W��oī�@�H_fY�f-Ƒ\�3}w�X��w���+���{�;�ﭾ�%�����_{Q6H�:@�z��$��B�����
�:@�\��b ����{#�o�p�~��@���}$�[�~d��"�߀E��u��?�BN )���3������;4H{�AZ ��.�t�6�Cj��*�v[�� ],��N ��Վ��$���),)�[ �Z�=�@F _�7�t��ߚ�Vt˺�-�5�nkKh��
o �A|݁�����A#�-���=#�2�c��? ]m�{�@���7�{��ؐ ]�~�����i���������q��^��ف��(�5�"�-�5�HkKr����H��ya�i^=������kb ]m囏���|�7l��i���0Y�������|�?Yӈ���hM��;@w�6��{��:�)`����ݳ�����o�{�v�l�U��g<��߱i
�e��g�ab`\��������\��b`\��s�����q�ײU ����r�@�˿c��
���ɧo�zvh�\TEX��oܸ�$���~X -����_EQ���򢪍�*��6����فإD�����o ց�]��v�a�V�A��ť;dض�4��Y��@��H��w؁XHW)=Ȱm)=h�dY)���i��� �v!�
�0�@f v%Qv#*�H�c	VXNtx�;�r���TXv��is�6TرM� ��w"m��;qH�iC�e߉0�}%r�7� �\�~�`���0��Ux�`Ƕ΃�ڧ@f �A��|
dbD��'����qta7�@�`�g�,&�=�֘���փ {�� ��O��'�+���<;_�k,��4;���4��0qGn�pT�wa��߸F����z"�K[��w???�!�E      ]   W   x�-���0C�3��$�K�������eS�B,A;
����1�!tG��]��d#`ꥐ�b\�"/������bl]�^e��x PW      _      x��\�r#Ǒ>COQ������wn���Kk8�D����"�nl��8NX�Ϡ�c����������n ���J�2+++3+3+���;�O�4�G˃��d˄�U���$�Fy�Vl����K6�Z}θ;�!Z<�\�]��.Y&s%_����h��p�B��./��F��s���v��(O�rq��>�F���
~ͷ%K����#�_��r@ɳuZ��d�J �ݭ���,��j��i�(�c�ty��M�R�&�.6Q�m�3���sX,��.Yk�e��'�~�����>�� O��m��,Y'�� wE��#� �w��ݥ�*��p�x�{u(��p���g���	8e��Ee�(;�1���[�5r�l՚�#������x��,����9��L�"��i�΋�&�����^�=�z��#@��� �[������a�J.�׮�e:P#�3f���j�@r�V��
N��=�$K����]�4��Fk#,�dwZ2�O6ň���T�J�2qV�����_����m8b��ng�~�"�D�;�錈�mD_���,�m`�ųd��0j˂#M{t�d��xV�sb �V��=�%��3�_}�E�#�ǈl���[��r� �gڐf��6�d:%4��Q��`2��=:R]o�E\�O�p�>�F,���JZ�B��˯)������I�,�	7^q�a��U@i�D1�%�-k����:W��9�b�O:��I���R��-��*�lY�6�v��>�݁�<���0Ԯ�h��
$��G<b>��U=*P�w:>"ϐ	A���_ШJb�'�2�գ7~����u�M�H�Ne�T���'	��Yߊ\kl���A�U��k��:��������O��X��d�B��!H�A��(-��\��'q�L��ՐN���<[RyI�.���A�E��I�rVT���b#?��ux�C�(@�ʹ��(�,R������
TXmeY��xQ�)���� F���o@Tԟ��]��c����"���h�7�Y�z�ϣL6���l�G�L�CyON���m38������)(6��Ç�)y�$C�+K��;�kO|˚J�"�|����m�R�CL+�x�����8�X��Y�ԗ�/���8Zrzܝ7LC?��
`#���;)w���	a��B3">��l�v߰��0Gb`���k>��wV1$U�s�!�E��ϑ�/Q��,��I���	�k����10�E8�Ds8��DY&3v�\u�����p���c�����U�j�l�Qѭl$�(����LeTKA�"|wߪr~� 5��-�E)���ö�4'�&|`!�@;g�'����\	Z���#�F�Op�ÍV�G�!�����[/�qTDh�<���r.�LB`���~� ��>��#`�`{9�be��I��tn���gH��� .:mB�O�a�;�A-�j���4��M~
�T�ʻ8���	�Hv�	2��2��v��U�6F�><�f��UUB�nFbMh#�Iģͦ�Pq�S)X!��tzL�e�����8;�P ���>���
�������u �P�3��/*uziD3<��|��]?���d�v���к؄�"�w]�s����Z	�*wЌu(�l#D�W��.�<r�ꀠ�������;c��p���HV�-��`�,��H�f��$;�Ԫ\���(�<��/�
�҈���m�pH1�b�9�+�O��v�^$r��G�T�����ؖ�JGζ<���P�ZP	��bPo
,�<ڗ�ߩȵ�4� �����e��{�6���"X�>+1�^�+d	d�XF�&�m���-�~g��u>h����:i���&�L��S�[g,u���ǖ$T:����[�.�W��v�ƕ�.����IQ��4!�+���VGI��.Ԡ�i�6�Q��-�5�۶��7�l��Nۈm~����_�ؓ���A���c��m��;Ϩzt�<�.�/�Q�!;��d(�]��'��b�g�B͹9{��kо�~ˮ�B��T�w�mnUrAbpS��ڂ���W�����;�%�0nM�L����9�R�7��������Xe�d��n��>�՟3�Rn`n���i/�G�f>S���Żf�b�l���|�>�oC�=$���!��q�#�5�~������G�;�aU<�]Pi7j�&�5Qy؁7��
��R�$l"v�ӡ'��c�zp}�b��r<��t�&�ۛ�{j�B�t�	"*Gkj����N~؅��nfS��q�o\�UO����vu�qʹ�A-ج��ޱ<0�pl��b���PyRd��Gز�HG���u�іHY��j�C��g�ʘ	d��щ�yESW�}�η!�r����Dqp�G�~^����D[��� �}L�i��T��xǪI��@[�6�"����~^,�F����v.�KH]�6��E�s�vl���K�E�8b��˗d^~�l���Z�C^�;D�:{�X����u���oE����'��ѱh.�g�n�QUWB��kM�ҭ�R�G(�zn�]>�{]��Sn�������eW@_N��lיde�ѧ�M����g�����-���>��G�5��N��ꓢ��z�8|*�C�VU�Ul��e����M��f����+@�� �jڔj��#R���M��I{G���y�>| �_��2֝t�C�iۿlco�#5���N)�n2�z�B�)��'�!d_�m8�����C5H�o�r{<H=�H��\T_Sy��� S����8@R����v�<�^�)yК"�a�a�4��w��~l��W�@"����(�!�D��v#�j�*���ӓ�3�`m����*�G�a��HqW5��⎣�Ο	c�"����}^,U2�F|)�lI���8�|���ƝL���*l_ �ܳ�T�UgG7��71si*�3�p����S�Z��-���i!�e]$n��I�޷����P`�m"�����խ��+���$���)�]�[���������:v �O*�\9���w�V�ZL����ןPd���``!���)EJ,C�%��[�?�ڥ��"0�	eVӃ�<AO�!؁�TF]?�ޔlB���{�1�}毵�/�����3q̔�{mǼѸ�៨�|�}�vs���ܜn}OPL���1<-c�-b�u���Yh!�_Ԁ����"D6��g^�%	������BeP0��Gd鮠&?��v�YO��KǨ���c,�����,I��:���.r���*[�9x�Ւ�܇�,d[�aJ�kGL.+:Ubwh�!��Q����w�jR�|-ȱ�f�<5Id�aE7z�},����3��l��$8��X�|���PA�H�u|_�|�e��(�R�q�SR�e�yBW�?m�1G����F����� �#
��հ9�}1���H��-e�!��ed&}��e�H��'3Dn����j
����kZ�c���T�_�'9<{�Fh��@�5�r`�6�U�|Pj�G�ʰ��_g�å���p�A��i��,;_���{7uS���"�FЧ����D8��_�UF������r��Y���jfʱ���<#W�4gttoE���42C���G:�fr�X8��x���B���V���j�������M����a��Ff�y��2�]1&���$vV1�e/fމ7:��\? ���F�U��<����,_��r�-���_����C�����N� �B��Q�	q'0\/f<#��ݴfqjDw�JX�<�l8Yǅ@��\v{�P3���Z,\���(��r�j���'`�m�Z���)�J�n�����+��T�oX/�$s��/M������d���2�J�}���Q�'Y�q���ʡ�	����;���ɴ�;6��6�C1c�OfvC�1�Jk��)�b�7\v���;9�Fo��D�5\l�h��8H�6wfH��Vp��̖w�J���3%��v���GH{�}� r�ņf�_I׵��
��f��L�Rw/�e�Λ�*eX��iV8�Z��������*YE�9sod �  p���������s�7Q��#����yw�Z x�l�KQ�2�jj�7r��L3�S>n�������}�|����Nٶ|�u#����*MAdK1�`B���,�l��u�ܻ~c�m�Lt����U����ܯFV�. ܰg�f݃��$VyR�BE�uIu�+1<m��q�UX�����̞��B�#��xӺ8�m�d�˚�Byz��#R	�(��\ʘZ�M=V�&��fht��^gd+�F�h��6^�݄5s��O��ڡj�S�)�1�`>_�����k�}�uh����i��%�!�6���.Wz�j�����'�a^���Q�o�wӛ��jޱK��֤[���Fb65(u��MR��0�}Nc/Y��"��L~%���A�b�[X���V�9Q_�D�f���ik��U�џ��������.[p��wADD����w_
ғ��V�ɖӱ2���'��SټZ�球l*�+m~
��b6��������j�,��H4 O�@S�1�zp�
� ��eY7�`~cVeC�.Q���=�5��M}�5	����}��������ۿ�~����{
K	���Ԉ��p�ӄ��y<���Ȕ9��F��I:r�3I�C�Г�t<t��I:�3��8ǎN�ѹzH8�2�����`$����b*�?��a8����	4aY=�����l�;2&G	�����6a�!�����!|b`MX`
L�#c[7�����c[�����h���众��;1�%�aǧ�������6�8=%l��SsQ�H �k���� ɹsN�v>1�vj����^�[N�9g^J�"G&9	�a'���wj���>��F�ñ�Χ�?1m%8���{GNw�z0;G�pu���^�>:�e���<Ep����^3w4�L��gN�N�o&���_ ��Uܑ9�� �F��9���;'Gw(/05��u,�p���Gi���l��{�_WX*���o_�� N�]�������y�gP���c#i�u��F����t��7FH�q�$���H�`Ɔ
����'��;.{^7���L�;�
�wv���!���(J�#1&�:['�I��[ݾ�߬�H��`��'��b�L�j���\�0�j�}�h��!\K����+j/	ĺ]�9�.X�9qB�����R6�fc�Y�� ���?�CH���Di=���Y[�}����"����R��PyZ�v���zwUw,% �r����퍊�:#���}���曋�&���s����n�D?~i�k�����,���5�XpI�?+��A�YGiޕ��nWǞZ��÷��1hz��U����D#Twr��/�1�����*���ee��s�'�?M����_<�Z�a������ʜ<����[��}Q���P{4���Ȋ)_�\H���+^(�&AЋEUבi�̋���"��H0^����kحM��Դ��t-�� ����]����B��u�m��6�!��3'Zɞ!u�3xܬ��]<Ax2��Zk@����"u������b�^N�W���0SF��܃O�FD�����C����D���c�3k��ȇ𖍟�w�F.W���	
���L~�~v�W�o�v���!����eJ�L(��>F\b`.��Ś����8x{�ǟ���� b�ܷ      b   �   x�s,*)-��/O䬮�44�t	��w����CF\�@5�ىř�`���`u@��N�~Q��Ss9}J�2u���R���3���8!ZЕ{�\���px�Ѧ�Ҕb�rcN_� t�N�E%G��s3S�8��s�*�J�@&cqH� C�     