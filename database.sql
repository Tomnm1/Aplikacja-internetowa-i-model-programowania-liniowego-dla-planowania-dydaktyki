PGDMP      #            
    |            inz    16.4    16.4 x    h           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
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
            public          postgres    false    4    242            �            1259    16538    teachers    TABLE       CREATE TABLE public.teachers (
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    preferences json,
    teacher_id integer NOT NULL,
    degree text,
    second_name character varying(50),
    usos_id integer,
    inner_id integer
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
    public          postgres    false    215   �       F          0    16471 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    217   $�       e          0    16719    classrooms_subject_types 
   TABLE DATA           U   COPY public.classrooms_subject_types (id, classroom_id, subject_type_id) FROM stdin;
    public          postgres    false    248   A�       H          0    16477    fields_of_study 
   TABLE DATA           G   COPY public.fields_of_study (field_of_study_id, name, typ) FROM stdin;
    public          postgres    false    219   ^�       J          0    16483    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    221   ��       L          0    16487    groups 
   TABLE DATA           I   COPY public.groups (group_id, code, semester_id, group_type) FROM stdin;
    public          postgres    false    223   ў       N          0    16491    plans 
   TABLE DATA           =   COPY public.plans (name, creation_date, plan_id) FROM stdin;
    public          postgres    false    225   C�       P          0    16498 	   semesters 
   TABLE DATA           P   COPY public.semesters (semester_id, specialisation_id, number, typ) FROM stdin;
    public          postgres    false    227   `�       R          0    16502    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    229   ��       S          0    16505 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    230   �       V          0    16510    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    233   $�       X          0    16516 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    235   ��       Z          0    16520    subject_type_teacher 
   TABLE DATA           o   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id, num_hours) FROM stdin;
    public          postgres    false    237   
�       \          0    16524    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    239   R�       ]          0    16527    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    240   {�       _          0    16531    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    242   ��       b          0    16538    teachers 
   TABLE DATA           z   COPY public.teachers (first_name, last_name, preferences, teacher_id, degree, second_name, usos_id, inner_id) FROM stdin;
    public          postgres    false    245   H�       m           0    0    buildings_building_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.buildings_building_id_seq', 5, true);
          public          postgres    false    216            n           0    0    classrooms_classroom_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.classrooms_classroom_id_seq', 14, true);
          public          postgres    false    218            o           0    0    classrooms_subject_types_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.classrooms_subject_types_id_seq', 1, false);
          public          postgres    false    247            p           0    0 %   fields_of_study_field_of_study_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.fields_of_study_field_of_study_id_seq', 10, true);
          public          postgres    false    220            q           0    0    generated_plans_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.generated_plans_id_seq', 1, false);
          public          postgres    false    222            r           0    0    groups_group_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.groups_group_id_seq', 1925, true);
          public          postgres    false    224            s           0    0    plans_plan_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.plans_plan_id_seq', 1, false);
          public          postgres    false    226            t           0    0    semesters_semester_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.semesters_semester_id_seq', 91, true);
          public          postgres    false    228            u           0    0    slots_days_slots_days_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.slots_days_slots_days_id_seq', 30, true);
          public          postgres    false    231            v           0    0    slots_slot_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.slots_slot_id_seq', 5, true);
          public          postgres    false    232            w           0    0 %   specialisations_specialisation_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.specialisations_specialisation_id_seq', 31, true);
          public          postgres    false    234            x           0    0    subgroups_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.subgroups_id_seq', 1, false);
          public          postgres    false    236            y           0    0 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE SET     _   SELECT pg_catalog.setval('public.subject_type_teacher_subject_type_teacher_id_seq', 20, true);
          public          postgres    false    238            z           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 753, true);
          public          postgres    false    241            {           0    0    subjects_groups_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 69, true);
          public          postgres    false    243            |           0    0    subjects_subject_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 390, true);
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
       public          postgres    false    227    4745    242            D      x������ � �      F      x������ � �      e      x������ � �      H   F   x�3�t�����2����L9=�� L3��6�k�陗aZ�����`Q0�� �V��,.���qqq ��7      J      x������ � �      L      x�m�Mn$�u��1�b����Mx�\�!��1/�U�F���Hz��l�kV>E�H�_��?��|���?�����c�����X^~+j���'���P]~;���?Y7�ݩ��v��U���v�j��Q�ן?��v����Q�W�ɾ���ᷣί��c�����V�ε}����o�����|}՟���nU�~;���?�V����K������t��=\�����ǦS����ҿ���ǦS���ҿ�}y�d�H��$ׯ�?�}������g���Ē+�d��������H���C�N��������D>��+&uń�����c�2WtrEWtpEW$�\q%sE'WtpEWtpE��72Wt�Su��g�^�O��R��g�>-��K���;�x���P��gy�Y>�w}�_ߺm}�������������_�k߮��o���޷%�.�ܷ?r�㏜|^�ȝ�?r��ܹ�#w����>r�nM��ޗu�c綺������:�������{���:�����S����S���U���U��<����G�O]�}���+ޟO��s�}H}ޅzޏɟ�މ�C�g��B��z76ݰ�ן��}����ŧ�\}z�ͧ��}z�ç�<uz�K��uz�G�G�~?u����.�~��ԥ����9v�����\'��8=���N�������G�E�Q�̣ęG	Y�g%�<J�<J<J<J�N�q���N����x����sz�E��X9=���vN�upz���s	���U��)�Γ�<J>=���s^>=���s>>=R��E��Xuz�M���uz�C��8uz�K��uz��ӣ���Z8=���6Nϥ�<������M�����������ɩ��+o���a���O�����O�}�'u?���������~J�S�u�0��qq����t=&u�8L��a��|9�}�9���b�駱��S��r�0�+���8���a&3��bBwń�	���	��+&tWL讘஘⮘⮘⮘��ZuWL���|W<�wŤ�I����I���+&7�s�a:���a:.���a:&��`~�oz��O�=�g�O��a�v�up����t]�K_�$��?�u��c%�[��a:/���a:&��LW���t��rBO�z��8t��S��t��[��xt�ě��a:ӵr����t����0]z���sE��s�c#�\1��I?WL��b���~��||��*�(!�(!�(!�(!� �� (@) JPJ %�#_n|ɑ/��(i��(i��(i��(!�� J�� J�� J�� J P
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
h���������������*b�,b�,b�1s���C��!f�3�b�>C̘!f�3f�3Čb�1}>!f�&f�&f�&f�&f�&��Ĝ}��1C̘!f�3f�3Čb�1c61m�_&f�&f�&f�&��+�\}��>o3f�3Čb�1c��1C̘M��M��ML�Oȸ�c��1CF�/�3d�2�c��1���������������?/�x�c��1CƘ!c̐��2�c��1����������������.���v���QX�QT�����_�n�6      N      x������ � �      P   H  x�]�=nD1�k�[n���ؗ�	�I�b��4������I��o�yjT��n�'Vhݠ%t����B�zR_s�@���1G:成�
�����Ыޠ�2��2����Рz���~}ܟ��
�ϗ�������bX�	�E�jYY�9D��F�!��p�<:"P+T램�9Y`IX�n��d�-Y��,<�5��n����z���{ۓv!�d�[��&�������eý��pܲ�X�U�X��C,��!i�������0�uC��	Ƃ�s%�!v�?[�uX=�a�t�Ճ�G�t��YV�9`���z:�� ^��       R   *   x�3�4��20 "NK+c���41�Z�\1z\\\ ��      S   "   x�3��4����KI��26 �KJS�A�=... yk�      V   �   x�U��
�0E�3��d�j�m-D(m� �|���c��N���y��P���,_����ܯ��-�����n[T��C�`�D7�A�0�0F�&���=�,&^�2���Abϰ��0��$�XB9'CʱyN���I0yb�,��BrP���H>���-��K�D
�-	Z�Ɋ����$\v���gu(      X      x������ � �      Z   8   x�%�� 0��0L�8v�.����9�8�#�VR=_�F��G��r��q�B		�      \     x�m�K�%KR���/�xg��z�4⢫�Z0d��`_�����O�Jn�a��c�Y�i�����������o�����t�������ퟞ�>�F�nz����_~�����������m݊���E�W���>�+��O�<+������⊭+�.��s���y�V��N�E�E�G�JW�
�xj�u�V�7�i]������XI��l_��04�i���D�K<7��~���y�]���߱�b�V�'��X�n1汉����󷤎��UN�%���<���l�s���*'sύ���DejeY�����"h�"~����S!-2���V�ޢ� �ۊ�e��cϻ	P��ύ�yT�[�xj�?�ة��X���'.{O�|���"���Zۇxn,�U��%�k��s4�j��F<]��_��� �7n��Y���3�M�����*��O�x?Ϭ�6�qf��wO�x�5h��]ϴSG�M8_B��L�/��X�vEВB�O׸"X�Sw��|V���"��gYN��!�G�>�rB��C\���c�Ӟ�?��χ�g<�Z@�3��X������e�>/�5�bl�W:�W�6��VR�齫�5�����tT�j15��m55���ZNMg����t���Dk:X�Ɍ��sWgZ�Y�=�:w^j�ĭ�����L۶ؚN�]�TG����L��*l:��T�.s�1m��:=N�S��s�#�9��<���O��6���Q��6������_���\��)3��x������r�i�=�贝�2C�:�gH{���]���մ�2CgH��N�DH�ٛ�����)6�Z��Ӟ
�F�������ټe����m�튽������I_)Z�oq.5������
����9E���rzk]^��Z~���Zhק�֓������ەǭ���epM 4-�[(��	�Ǩ&��]I[gg��Ԏk� ve1,��������w+e�4��(�f��U�Hʦ&Q�����΍OD��H����D��G3�S��E��W�9��eFD�1+���
�:�Ӓ׹�	�kU�k��{��0�����?-� ��9D��ui���1�B��;D�ۚ-�.V�퇸����#ē��pv��7�a�یp���:g:p?�7(W��X?�It�w%@�����ǜ	^8@��0��A��ڋ��q��H�ݹ��:�r����t� �5�)��F�~���1��Q�B=�@X�%#�P>_��9��C�M[K7��!L�Z޵%��I;E�jj�s2�c��|�8W y��N����	�8�p�y��@r�r�#��H҂t��5������X+���s�~�2��q���}�H�!�֐v$'N���!��:�ti�t���H�X����	�:���#��@��i���Y ��e���:@��f=��y8�.��sɏ�@����$wx!]��o�:��\]i���FtVV ��R�@|-�4�k��F�-�4
ly�AZ yH(0�c���:@��4V ���F�-_Ө�U�iTX�e��@��/�^!-�_O��h�h�@��_7���(0wl���:�p��T��6	V�6��@����@��q3a�Z����@��jO����2���k%��OU���]P`�_#��v6R@���$��	v�~C������t�@}�3P_n��bD�T�s��̝�����9�/��&�@|O/�˭�I�H��5�_n��b�\t���@���@��4�`o�#k��^_�H��|��
{=�H���3*��<#��=b�੼&����@z ���BF���i$�[�F���f�`o�2���`$�[�eT��F���K�H�KH�ү�07��t�*�-�����4�",�Vl"(07�@a�kAG�E4C���I0!i_@]_���[N
��5�e �n9)�_ח?��2:���O
�Wi���5�~ �n6*����y��!���@l8T�B�'?��i���'��A�
s����C�t��2ɤ/��,��u�t���bC�t�,��'��~~�lI�t聤���=~~f vq.����w/ߙe/x��ĆD���S�ƹ�G�Kt7��|��H��a�u{�s��/��zE�l�-�L��u�nYk�?��v�V+�H�C������i?�R]Xn�D7Q^��9��I�X��w�ӧD�\���D4��e6ٽ�	���1�'��뺟�Ƀp-^�Nj��-�I-��`�R�^�Ĩ���P�ޢ *�V�Qe��z�aϗj�l[�X<ݶ�'���:[���Sa20�K�IYTQ�C�HX�ڨқ�H�75���b�� l^�
2������ŵt����o+m��e�R��� mՋ��u��̈́���:,g�>��Ha\�Ns�@r��r':-� �J��3ɫ��؎'V�Y��Y}v�3%��2*�e�Δ��^�3��zi#M���6Ҕ�ꥍ4%���CH�[IO + �t�_�y�<�h���g"��6h���g"���g���D"��+��h���g"���h�e�iU�L���HiU��,���HmU�Hm�M�.�����BW���BZ����U����Vn--tUi--��[Ki��R���@�ϙ���z����'�EW�uA]�A������rW)ڿ��eĵ�Z�o��l!���h����(*yG �� ��bh����t��P��@l4��6��7�|_�ձ�@�ձ�(��Q� -�� -���qU�&-���&-�ձ�#��qu��V����Vn?-�Ui?-���O�U�O����Bd���'�4{�X^ӴXoq,,�ǛM����N����؊�s��ʭ���*ò���*�M�+���B^e�I�V�7�I�g�����L~�g��J�+��[J��@��8{���b�VcB^%�(�0�^-�U��"e��*S��U�ꩢ��SD��U������7�<җYV�Y�q$)�L@ߝAF �a>�]���
Ć��.���@���{��b��9=�����^������;ɹ��~=���.�=�X�w}�ވ��[9ŷ_ -� �n��ֳ�>���7`�abD�����H�����L�Cxn �Ҟe��u��2ݻ�@������V vHK|����s�c�vI�fw
K�D�H����p�)�����-�5�f�ݲnkKk���Z�붂��u_w��0>�|u�H|��s|�o�÷������HW�;� ��M�o 6$H�韟@r)��uZ��@��9G�*�;�|\$��i�xv y:Jn��HkKr/�ڒ\�t�6R(�F�e�W$'a��2����XHW[���?7�6��[�z|�7LVo>�+k�6� �O�4">,>Zӭ��ݳ�g��2�|
X�g�t�l�9��g�����A��=�|��ς��wl��|��Yt�X�������<0.W�9�X����@�@}�d\��l��w�\g�ǃ����h���|��[���+U��7.u�w@�H$?!�WQT�{����j��ʢ���*��@v v)�.ꤸ��u�k�j�dض�v�`nq��-��fV 3� �nf�v ��UJ2l[J,YV�"��c�@Z �2H�]��+�(��]I�݈
d���A���@��\|�&��(�|�\�vl�<H0��H:,�N\�e��a�w"i_����:@:�4X�}"�j4ر�����)��ua7�فXv�	�h��qD��'4��YF�%��h��5����� �^{�  �������
��/�N��˯6�?� �n�Lđ>���������|.��      ]   '   x�33�475�4�233̸�,�c��%�a����� ��      _      x��<�r#Ǒg�+��a��~�FΌ���CJ�=
_@,���c��q��>�W�<�/gV����Po�+!3++ߙ��`Ɲ٧]���A�J�e��*Q�c�n��\+6{���%�l��	gܝ�-��X��.���d�,���/���}4�Mp�B��]^n��:D�爭��\�Q����G}�������oK,پ��Bl�����9��i�˓�I,�Xw�6�/�\©�N�Ѣ|�)���W*�$+�m���b�1��;���>�ò\ߒE� [Fq�x"��_E������??��S��Z�2K��.=�]���t��w��ݥ�*���\��^�h"\n������=!Z�g[T&���C�9=���@pF.��Z=r��_~+�F
��;v(4G�+���eyZ��"��x�@��X��x�xN�@��� �[�����a�J.�׮�e:#����Fi1D ?�Y+�f�$��$K����]�$��Fk#(x�;Z2�O6ň���)ʕ�e>�<<���볿��#
��p��;��t�~�"�D�7�錈@�}��O�h��3ϒͣè-�8��v��g��zb��V��=�%D	��V�p��1�����[��r( Aϴ!�@�ml�t(Jh@!B]��� "��X�u��qM>i����6�b� ����*_~M!��U�.p�H�e��8M��`�$�Jc%��,lY��Թ�U䈊��>� V'����b��-�T[�,w�d;�r��gw`3�/�.��-1Tg`>Z�#��xH�գ�Nx��#�<�!0��UI��]��zT���Or�Qp�dN���T�He��;��_j���ȵƎ�]�[�ɺ��BG�!0G`�ݍz��?�*V��,Y�P�}FP�~P�4JKȘK��$����…����#�%���E�udQ�xR�\��<B�x�����8<��k��f��e�W�iQy��q�U
�����J�(��Q���S �pg�7�*�Ͼ`׆h@�P pCD��?��e<����(�g��=a�*�����'�`o�
1!~�-x
�M0A�P3%�PD�2d�R��t7`�A�V��kX֔c�n�w��H(�"�<�iY�����%�����>�}>|DG�1��zУ�\0�YeU !��g��M0�[p]�L�G��	�F��l��զ�5�h�$�����YĐT��цP`�|�4}����e�fOjG��3���Jw ���.�CM4e��&�2���p�Q�#F�R���H����U�j
�l(Q�[�P�Ȕ���LeTJA#��ܷ��_G!@��vK�PJ#�Ѱ�F$���d�,DH�l��>:���n���q����x�h%z���^��aw@D�#��-�2�$6���tׇI1���s�V��4�Mo��)��~��Y��࢓&A����k�	nb�W��|ĩ�6�)���R��*�b�<�|&�"�'�T����"�A�V����5����2v3kB!�B<�l�7*�v*+$iJ��$X��> 3���ZC�@7��S

�v2:�����:��������F4C]L�;`�����f{�V��C�C��&|�0A���@�����k�4��A֡���}ZҺ,�Ё*�N_Ȭ����͇�]�Gr�mYH��g�AB4�B&�aR�j���6�Qi�-�Y*,K#6j�v���<
� �^�zM��b���T>BRI�d�v���4�U:bp��!��p���2�Nx�D�~S`��Ѿd�IE���9o@�u�,�5�I:xpz���`���\��[��X)�0�!K��6Z5uo30�<��';������G�n���~ib�d�=�u�R��:zlIA��|�v+�A��-p�koظpٛwr�{R1<���J�Gj��a��f#5�jڶ�h��~�h�����<�#��6b�?F���/���8��� ���c��m�w�Q���y���aGM����!�wi���#@��u jʍ�!��A�z��-��2�R��-h��U���M57j�jK\��v�P�ǲ�ؖ,.��5�3����K��`5P�Bw��*['���  ��a��Ę��R�p;	��xxQ>�4󙂻�wx7�Pl������/8��mȹ��W�<dz:�}{ʹ�C��pF`�ޅ�*�}EX��BڍZ�IgM�v�A��S=[���C�nt:��q�P��(����^*E�JM��7���v�8B�D���/��Wi~؅��nfS8W�վiLp�U=V?n������M\z��4eΎ偁=�c�?s��ʓ"�#�68B�5o@:�mt�ë���DJ*DRcrU?�T�LРju�I��+�����}�u����A6�1?��<h��D[��� �}L�i��X��xǪI��@G�6�"䢖�~^,�A����v.�K(]�1��E�s�vl���l�X+8b��˗d^���>E�5o���9D7��z� �`����ֺ��m�Y�9��	'it$��������>�*%�i��-=*0(�}����S$p��EΉ�;�&�Q�\~��,\v���<�v�IR�����Z���F,���X������\�;���5Em���Q�Tf�b���خ��!ʒ9F[��*�4��'O��A�4Դ)�<p�G�2�5Z�����t?��}� ��n1d�;1��Ӷ����F2j^kם(Rd2�z�B�)�O&�C2��<��pv%;���j� 1o�r{<�=*(��ZT���{v�)XU�G��y����	�c/딆�4hO���pV��ͻWQ��O��d���p�I���D���Fb�FE�#��'�f��!�;��U��6�n��\A�G+]?¾Ew����X�dP��6b<tՒ�Q ���;�j�<Uؾ@��r�RW�=���D�̣)o����ƳvO�k��K4���f�L�u���'�xߦ��?@����l�C�6�VY��7V�#��&Y�Oܪ�J���E�`��yp� I|R)��Y������Ա`
��(������&C}�� ^7Y=��V��6��[���%����]�\� �ǖ�zB��� �b���=;p���Gܛ��M���{o1�������E_��:`&�Y2v��X7�����G���4���ύv�<AaB�i��2��"�Q50�f������-%�A���>s��^(��`n_��,]{��.5������z
�&	\��#t��TF!�.>�$���h]�Hu�{��m��Wsb�-ȇP,d[�aI�{G,.+�Tbw��!��YQuZ��;N��w�����S�G��$�ð��n�[`h�A�[6��$8ܰb�X�|���PA���M|_�|�e������)�м�:�k럶Ř#p�AD#��K��t��fv5lw_̢�0J�rK	yH��D��E�4D�>�v���
�[BNW��C1��[��5	�α�D�B,2/CMuo��3�:Po��X�F�tU(���T�@���j�T7�csn;��E��;��W����M=%�H����.h�7�-�����?�=D�\�vD�������rln�3Ϩ/�=[�)>,��p;衎���μ3޽8���B˨s5G��O��Ѧ+Q�O��pn#��y��*�]1���vV1ꇒ3����\? �VG#�*�]<#�r�i��*l9e�����sйcR��S"����) a!��Q�	�N�p}��|_OӚé��+a�G� ��fi6{�m��f"#[���p�"	�aDˉ��Ok�x7�b����-���n�έ��+��t���9N�w�˗&�SR`��c�O@J)%�9��ć(˓�����	���o���X�wF�����ɲ�;6��]�b�Ɵ̜�Hc1��.��S��4mHv���;)�Vo��D�5$�h�@qDl�;�$��p]�̑w�J\���3E��v���G({�}� �ԋ�>��c��1�\s'���^&�b�7�/�˰B5��p�k;�o�6��d��νQ�Qn �  ��.(	��z�on�t-G��yw�Z �t�<���e^���o���7�f��tܚ��������?��`���S�-_~��a�p�JR�R�4XЁ����.�6����c�9-����f��g�����	���l��{pr��*O�Z��.��s��c�9α���{���A^(y�?��bZG�퐬zY�Q(OO�}�+ae�CSK��ǪФx���~ԛ������-��Ƌ�LXc���It\;Tc}J�3Y6���K�}�>u�z�س��2M�c���1��fx���J�P]�`T\�D=�[�!<�mF�[����k��w,i�ښ�ȴ��H���-��w�I�_]��i�%�wW�ӗɯD��3(�Q~k"��*;��ژ&z7��O[�wP�⊌��t�����hwٲ �7��N����R�j�[	&GN��d?D�3j��B�����"�=�TS�]h�SH৚ـ�~e�:4�!$�0v;�Ѐ:ME�(��)ʰ���e�ʃ��YU=E�D�G�{�i>k��'�:�5����}��������ۿ�~����{
��M�����<��G��xh!�-;���F�ӛt<�x�&��ܤ㡃p�ޤ�;���+q��&��ࡇ�#_ 0_�Y���p���؊�右��x� ���&,�>��%,[�Y��|�p�j���yz�OX�|baMX`
L�#k[7�����k[�����h���侗�wb�KX!����c�b�v�Jؠxqz/J��N�EQ$�H��B��(��9	�E����S�7����:g�Bؠq�})a9��I�vr�Lp�N�_���G��Hw��Bn����'��sp�ʉ�bv��.B��#��@]�2�yh�"�?I�u���h����1���4����� `q��;��g@؈|���#������JL��}!�=�QZ��9����5V˫����c!�B��o��t���w^!��'����B�cS!o#����t��7�H��85�@��	������o'a�����=o����ǝA�;�Q����iE��s]����g�_�oVh$p�
E��?�NXqH%XYtg.��G�¹A42���I@�F����K�nW}<�a��z�_�.G��Ov)Q�1�,maPgd��!(`ab��d�f��| v]�w�b�B\�נ,Os�^~�Zﮚ�"`ZNu�C����U��d�P<z� �c��|s��Dq�wn�8�����G�/sm�1��5��X���"I�g��b1�7�(ͻV�������S�b�1�c���1:��C�
�j���w��P��i�X��9���r�s�J��pY+��I�OS���4��b��W':�w% ���������߷��;_z�V"S9�#�+�)���z����$z���/�92m�����.��ƫa�#�5حM��Զ��r-�� ��W�]����B��u�m��6ȿP�����P� ��2+@�.� <K�F�5����LZ��Q������X<����A��4f���{�I��؈Яrz���:�k��/�5M����̚g$�!�e����A���J��?AC������Ϟ�
�#���d�a� �r ��2%k6��Y�{���o��ނ��      b   �   x�s,*)-��/O䬮�44�t	��w���� .W�lhvbqf6X�)'XP���S��_B�cJb.�OiR��QUjqUv&X�'D1B����U�i��7m*.M)�*4��uB�rJ,*9ڔ��������W	Vd2��=... G�<�     