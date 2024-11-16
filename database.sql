PGDMP  !                
    |            inz    16.4    16.4 o    Z           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            [           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            \           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            ]           1262    16397    inz    DATABASE     v   CREATE DATABASE inz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE inz;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            ^           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            h           1247    16399 
   class_type    TYPE     m   CREATE TYPE public.class_type AS ENUM (
    'wykład',
    'ćwiczenia',
    'laboratoria',
    'projekt'
);
    DROP TYPE public.class_type;
       public          postgres    false    4            k           1247    16408    cycle    TYPE     @   CREATE TYPE public.cycle AS ENUM (
    'first',
    'second'
);
    DROP TYPE public.cycle;
       public          postgres    false    4            n           1247    16414    day    TYPE     �   CREATE TYPE public.day AS ENUM (
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday'
);
    DROP TYPE public.day;
       public          postgres    false    4            q           1247    16430    degree    TYPE     ;  CREATE TYPE public.degree AS ENUM (
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
       public          postgres    false    4            t           1247    16462    language    TYPE     G   CREATE TYPE public.language AS ENUM (
    'polski',
    'angielski'
);
    DROP TYPE public.language;
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
            public          postgres    false    4    217            �            1259    16477    fields_of_study    TABLE     h   CREATE TABLE public.fields_of_study (
    field_of_study_id integer NOT NULL,
    name text NOT NULL
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
            public          postgres    false    4    219            �            1259    16483    generated_plans    TABLE     !  CREATE TABLE public.generated_plans (
    id integer NOT NULL,
    plan_id integer NOT NULL,
    slot_day_id integer NOT NULL,
    group_id integer NOT NULL,
    teacher_id integer NOT NULL,
    classroom_id integer NOT NULL,
    subject_type_id integer NOT NULL,
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
    semester_id integer NOT NULL
);
    DROP TABLE public.groups;
       public         heap    postgres    false    4            �            1259    16490    groups_group_id_seq    SEQUENCE     �   ALTER TABLE public.groups ALTER COLUMN group_id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    225    4            �            1259    16498 	   semesters    TABLE     �   CREATE TABLE public.semesters (
    semester_id integer NOT NULL,
    specialisation_id integer NOT NULL,
    number character varying(100) NOT NULL
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
       public         heap    postgres    false    4    878            �            1259    16508    slots_days_slots_days_id_seq    SEQUENCE     �   ALTER TABLE public.slots_days ALTER COLUMN slots_days_id ADD GENERATED ALWAYS AS IDENTITY (
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
       public         heap    postgres    false    875    4            �            1259    16515 %   specialisations_specialisation_id_seq    SEQUENCE     �   ALTER TABLE public.specialisations ALTER COLUMN specialisation_id ADD GENERATED ALWAYS AS IDENTITY (
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
       public         heap    postgres    false    872    4            �            1259    16527    subject_types_groups    TABLE     �   CREATE TABLE public.subject_types_groups (
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
       public         heap    postgres    false    4    884            �            1259    16536    subjects_groups_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types_groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    245    4            8          0    16467 	   buildings 
   TABLE DATA           6   COPY public.buildings (code, building_id) FROM stdin;
    public          postgres    false    215   o�       :          0    16471 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    217   ��       <          0    16477    fields_of_study 
   TABLE DATA           B   COPY public.fields_of_study (field_of_study_id, name) FROM stdin;
    public          postgres    false    219   ��       >          0    16483    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    221   ��       @          0    16487    groups 
   TABLE DATA           =   COPY public.groups (group_id, code, semester_id) FROM stdin;
    public          postgres    false    223   �       B          0    16491    plans 
   TABLE DATA           =   COPY public.plans (name, creation_date, plan_id) FROM stdin;
    public          postgres    false    225   �       D          0    16498 	   semesters 
   TABLE DATA           K   COPY public.semesters (semester_id, specialisation_id, number) FROM stdin;
    public          postgres    false    227   6�       F          0    16502    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    229   u�       G          0    16505 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    230   ��       J          0    16510    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    233   �       L          0    16516 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    235   ��       N          0    16520    subject_type_teacher 
   TABLE DATA           o   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id, num_hours) FROM stdin;
    public          postgres    false    237   Ǥ       P          0    16524    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    239   �       Q          0    16527    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    240   �       S          0    16531    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    242   �       V          0    16538    teachers 
   TABLE DATA           z   COPY public.teachers (first_name, last_name, preferences, teacher_id, degree, second_name, usos_id, inner_id) FROM stdin;
    public          postgres    false    245   ��       _           0    0    buildings_building_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.buildings_building_id_seq', 4, true);
          public          postgres    false    216            `           0    0    classrooms_classroom_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.classrooms_classroom_id_seq', 13, true);
          public          postgres    false    218            a           0    0 %   fields_of_study_field_of_study_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.fields_of_study_field_of_study_id_seq', 10, true);
          public          postgres    false    220            b           0    0    generated_plans_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.generated_plans_id_seq', 1, false);
          public          postgres    false    222            c           0    0    groups_group_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.groups_group_id_seq', 1917, true);
          public          postgres    false    224            d           0    0    plans_plan_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.plans_plan_id_seq', 1, false);
          public          postgres    false    226            e           0    0    semesters_semester_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.semesters_semester_id_seq', 91, true);
          public          postgres    false    228            f           0    0    slots_days_slots_days_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.slots_days_slots_days_id_seq', 30, true);
          public          postgres    false    231            g           0    0    slots_slot_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.slots_slot_id_seq', 5, true);
          public          postgres    false    232            h           0    0 %   specialisations_specialisation_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.specialisations_specialisation_id_seq', 31, true);
          public          postgres    false    234            i           0    0    subgroups_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.subgroups_id_seq', 1, false);
          public          postgres    false    236            j           0    0 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE SET     _   SELECT pg_catalog.setval('public.subject_type_teacher_subject_type_teacher_id_seq', 1, false);
          public          postgres    false    238            k           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 744, true);
          public          postgres    false    241            l           0    0    subjects_groups_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 1, false);
          public          postgres    false    243            m           0    0    subjects_subject_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 388, true);
          public          postgres    false    244            n           0    0    teachers_teacher_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.teachers_teacher_id_seq', 11, true);
          public          postgres    false    246            w           2606    16545    buildings buildings_pk 
   CONSTRAINT     ]   ALTER TABLE ONLY public.buildings
    ADD CONSTRAINT buildings_pk PRIMARY KEY (building_id);
 @   ALTER TABLE ONLY public.buildings DROP CONSTRAINT buildings_pk;
       public            postgres    false    215            y           2606    16547    classrooms classrooms_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classrooms_pk PRIMARY KEY (classroom_id);
 B   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classrooms_pk;
       public            postgres    false    217            {           2606    16549 "   fields_of_study fields_of_study_pk 
   CONSTRAINT     o   ALTER TABLE ONLY public.fields_of_study
    ADD CONSTRAINT fields_of_study_pk PRIMARY KEY (field_of_study_id);
 L   ALTER TABLE ONLY public.fields_of_study DROP CONSTRAINT fields_of_study_pk;
       public            postgres    false    219            }           2606    16551 "   generated_plans generated_plans_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_pk PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_pk;
       public            postgres    false    221                       2606    16553    groups group_pk 
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
       public            postgres    false    237            �           2606    16569    subject_types subject_types_pk 
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
       public            postgres    false    245            �           2606    16576     classrooms classroom_building_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classroom_building_fk FOREIGN KEY (building_id) REFERENCES public.buildings(building_id) NOT VALID;
 J   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classroom_building_fk;
       public          postgres    false    4727    217    215            �           2606    16581 0   specialisations field_of_study_specialisation_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT field_of_study_specialisation_fk FOREIGN KEY (field_of_study_id) REFERENCES public.fields_of_study(field_of_study_id);
 Z   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT field_of_study_specialisation_fk;
       public          postgres    false    4731    219    233            �           2606    16586 ,   generated_plans generated_plans_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id);
 V   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_classroom_fk;
       public          postgres    false    221    217    4729            �           2606    16591 (   generated_plans generated_plans_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 R   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_group_fk;
       public          postgres    false    4735    223    221            �           2606    16596 '   generated_plans generated_plans_plan_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_plan_fk FOREIGN KEY (plan_id) REFERENCES public.plans(plan_id);
 Q   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_plan_fk;
       public          postgres    false    4737    221    225            �           2606    16601 +   generated_plans generated_plans_slot_day_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_slot_day_fk FOREIGN KEY (slot_day_id) REFERENCES public.slots_days(slots_days_id);
 U   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_slot_day_fk;
       public          postgres    false    230    4743    221            �           2606    16606 /   generated_plans generated_plans_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id);
 Y   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_subject_type_fk;
       public          postgres    false    239    4751    221            �           2606    16611 *   generated_plans generated_plans_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id);
 T   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_teacher_fk;
       public          postgres    false    221    4757    245            �           2606    16616    subgroups group_fk    FK CONSTRAINT     y   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 <   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT group_fk;
       public          postgres    false    235    223    4735            �           2606    16621    groups groups_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) NOT VALID;
 C   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_semester_fk;
       public          postgres    false    223    4739    227            �           2606    16626 %   semesters semester_specialisations_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_specialisations_fk FOREIGN KEY (specialisation_id) REFERENCES public.specialisations(specialisation_id);
 O   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_specialisations_fk;
       public          postgres    false    233    227    4745            �           2606    16631    slots_days slots_days_slot_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_slot_fk FOREIGN KEY (slot_id) REFERENCES public.slots(slot_id);
 G   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_slot_fk;
       public          postgres    false    230    229    4741            �           2606    16636    subgroups subgroup_fk    FK CONSTRAINT        ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroup_fk FOREIGN KEY (subgroup_id) REFERENCES public.groups(group_id);
 ?   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroup_fk;
       public          postgres    false    223    4735    235            �           2606    16641 9   subject_type_teacher subject_type_teacher_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id);
 c   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_subject_type_fk;
       public          postgres    false    239    237    4751            �           2606    16646 4   subject_type_teacher subject_type_teacher_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id);
 ^   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_teacher_fk;
       public          postgres    false    245    237    4757            �           2606    16651 9   subject_types_groups subject_types_groups_sbuject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_sbuject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) NOT VALID;
 c   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_sbuject_type_fk;
       public          postgres    false    240    239    4751            �           2606    16656 &   subject_types subject_types_subject_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_subject_fk FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id);
 P   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_subject_fk;
       public          postgres    false    242    4755    239            �           2606    16661 -   subject_types_groups subjects_groups_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 W   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_group_fk;
       public          postgres    false    223    4735    240            �           2606    16666    subjects subjects_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id);
 G   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_semester_fk;
       public          postgres    false    4739    242    227            8      x������ � �      :      x������ � �      <   A   x�3�t���2��R���~n@�$�a�sz�i���%���eh b(xz� y1z\\\ ���      >      x������ � �      @      x�=��u$�z,�1 WF�[�P̎T��q����o׽��Fd�[Q������������?������~����Ͽ=����;�o���w��^�~�k_��~��N^a��K���ݗX|~�k//q�~?����K,|y���/q�~������_�x����Xxx�����~�O_��ݷ��������/�p�/�p�%����K,�?����%6^b��K,���їx��/�x���n^b��%^^��=?����K����K��O��Kg^"���������{�}������k��K,|y����X8x���/���������_{x�������^ba�%�}�����2���xk�7ޜAߝ�������xw�;ޝ�Π����}��yw�;^ޝ�΀wg�w�~��}w��;ޝ�΀wg��sa������}���_u���?�����J�e"����u��N�u"�y���D^'�:�׉���w��.�ub�y���D^'�:�׉�N�u����Y�u0�A\q�u�A\q�u�c��-�ub�y���D^'�:�׉�N�u�ι�wy�{���D^'�:�׉�N�u"��t�u��N�o�]�>4o�S�{��������=|���׸t�xA�����7�A�C�E�G�����yx�<��z
�{�z�{U�a��x�<6/�'��u�,ؼ�<�5[�8�O�-��o����%��m�����;ߖ����|["������^���������� E_�-�η%:��D'ߖ����~[��oK��mY�>~[��oK�G��q����g�-��oK|��?���/ߖ���oK��m������s�W�s��Yω?_e='���}�Z��K���WYO���ˬ���uxNx��	E�O�wF�B�B�z�K� ��d�_d:�/2!�yމ��N��w"�����xG򧓇�މ��;���;���ŝ�n�D�p'��;���p'��;}��Ν��Dtz'��;�މ��N��w"�"�Q���|TD>*"����GE�"�Q�@�Q�Q�Q�Q�Q�QΣ�^�GE<��x������Qϣ"�Q���|TDw"���Ý�^������ȝ�6�D��N��w"<���/�DHС��?��IEM��"D��;܉��NDw"����_�D��7�D��N���c��|<�g�D|�N�w�D��N�o��2O�q'�/w"ڹ����N�Dtq'��;=܉��N,�D�y'¯w"ܽ!]�Ο�6*"�>r�#�~)��"�c�_�zH��\��~������")
������������U�.OU�S�TE<UOU�S1w��w%z�+K�a��+��ş����zX����a��+��ş���+��]	�J�zW��w%ܼ+!����Y��`��`�a��D/we)�2�"�1�/2�3�"�ݻޕ�����w%��+a?�~�[>�̱�ͻ����{W��]��w���@�b?���@�b?����~@������G�e?����~ ����@�b?���r?����~@�v?�m���������������w%:�+��]�n�J�pW�������z`��z��ݻޕ�����w%��+!�����ً?3A��	�OMl��JtrW����ܕ��D�we1��/2��/���:)�~�w����.�Oh�ջ߽+�ӻ��+��qĭ�ޕ��q��q��q��Dw%��+��]�^���z`�D�w%�zW�ݻޕ����|�@|�@|�@|�X:���������������������������������s_������?p?�~������ޕ�/k�X{��#�����G�=b�k�\�pw��k]{��C�����G�=b�k�=b�k�X{��#���е��=t������X���=���=���=��c������#�����G�=b�k�\{��C�>�]{��C�����G�=b�k�X{��Gk�X{��#���е��=t����<k����ǳ��}<k��ǳ��k���k�����q�#�>r�#�>r���}ص��a�>��/n�}��G�}��G�}��G�}��G�}��/}]��kv�î}ص�����<k��ǳ����}<k��ǳ��k���k���k��K�k���ku�î}ص��a�>t�#�~�t�#�>r�#�>r�#�>r�#�>r�����յ��a�>�ڇ����}<k��ǳ����=k��Ǯ}��G�}��G�}��G�}��/=�}Ե��a�>�ڇ]�е�\�ȵ�\��׵�\�ȵ�\�ȵ�\�ȵ���a�>��߿��kv��Y�gy�>���g��Y�x�>���g�7�>r�#�>r�#�>r�#�>r�#�>��/~��a�>�ڇ]�е�\�ȵ�\�ȵ�\��ݵ�\�ȵ�\�ȵ���a�>�ڇ]��k�8���]���g��Y�x�>���g��Y�ص�\��ӵ�\�ȵ�\�ȵ�\�ȵ���a�>��/^]��k���k���k���k���k�t���k��Q�>�ڇ]��kv�î��Y���O�w�q�w�q�w�1k�X{��#�>zY{��#�����G�=t��k]{��/>�����G�=b�k�X{��#������#���е��=t�a��ږ���k����k����k�����G�=b�k�X{��G;k�X{��#���е��=t�!k�X�ȟ�@�=b�k�X{��#�����G�}x��е��=t�a���.w�q�w�q�w��k�����G�=b�k�X{��#������#���е��=t�!k�X{��#�>zX{��#�����G�=b�k]{�ڇ�k]{ص���=���=���=������a�k�X{��#�����G�=b�kn�=t��k]{��#�����G�=b��/k�X{��#���ȵ��=t��k]�p~��:����k����k����k�����G�}t����G�=b�k�X{��#���еO�����G�=b�k�X{��#������#���ȵ��=t��k]{8k.��/߳��}<k��ǳ��}��G�}��G�����G�}��G�}��G]��kv�î}ص_|]�ȵ�\�ȵ�\�ȵ�\�ȵ�\�ȵ?�W�=���>���N>���}~�kV���>����~��f�9��sp������C Z ���6 4`+ 7p; 7pK ��wc Z��� 4�E Mh@� �
�Y���a�[xҀ��6�0q�a�����~*o����H�V4�� h)���l+��n-��n/`�Z�d�6?fh4@���n����� ���[�4c���ē��I�&"8LEp���0��kH@K�Ж���&�9�	hP�E~��Ӥ�m
ܨ��
lV@�в���-�qP�Zм��40`71p72p+��:��xs���0��aJ�ä�i�68���w�Z��64:�������򈏦n{`�Z����4@�Mh����h��vn���n��n�ܟZ#�戇�������&��6��DR%�,�t	�	I�H�D�&�8�։6O�}�m���D�4�$R$�"�I�HBER*�T��
|ii�h[E�*�VѶ�n��[mmm��������������ZE�*�V����U$�7ZE�*�V����U���mm�hZE�*�8+iI�HZE�*�V����U$�"m��*�VѶ��Ut[����V��V��V��V��V�p�U<�U<�*�V����U$�"iI�HZ����U���mm�h[E�*�V����U�E�HZE�*�V����U$�"mm�h[߶��Ut[��sh�xh�xh�xh�xh�xh�xh�p��JZE�*�V����U$�"iI�H[����U���mM�HZE�*�V����U���뿳�U$�"iI�H[E�*�VѶ��U��ՙ;�m������������������ZE�*�K�HZE�*�V����U$�"mm�h[ﶊ�U4�"iI�HZE�*�V����U�A�HZE�*�VѶ��U���m=�r�0���9��aZ�ô
�i�*�U8�*�V��
�UB �  �^�*�V��
�U`[�Up[�Up[%�륭mh�@[�*�V��
�U���#�����*����*����U��U8L�p�V�a~��ô
�i�*lh�@[�*�V��ʕ�#f�@[�*����*����*���mh����mh�@[�*�V��
�U���*񷭂�*����*xZ��aZ�ô
�i�*�UrاU8L�p�U��mh�@[�*�V��Jx�*����*����*���mh�@[�*�i�@[�*�V��
�U���*����*��V�m<���0��aZ�ô
�i�*�U8L��p�*�V��
�U��mh�@[�*��?m�V�m�V��
�U��mh�@[%|mh�@[�*�V�m�V�m�V�m�'�tZ��aZ�ô
�i�*�U8L�p�V�`�@[%�lh�@[�*�V��
�U`[�Up[%��Up[�*�V��
�U��mh�@[%�mh�@[�Up[�Up[�Up[����V�p�U<�U<�U<�U<�U<�U<�*�V����U�I�HZE�*�V����U���mm�h[��#����U$�"iI�HZE�*�V��
�ii�h[E�*�VѶ�n�<���w[�C[�C[���V��V��V��V�@�HZE�*�V����U�K�HZE�*�VѶ��U���mM�HZ%��i��V����U$�"iI�HZE�*�V����mm�h[E�U������⡭⡭⡭⡭���V��V�@�HZE�*�V����U$�"i��*�VѶ��U���mM�HZE�*�V��"iI�HZE�*�V����U���m|�*�V�m���<A[�C[�C[�C[�C[�C[�C[�?)X�*�V����U$�"iI�HZE�*��U���mm�hZE�*�V����U$�ZE�*�V����U���mm�h[E�*�m�<�O�mmmmmmm���U����^�*�V����U$�"ii�h[E�*x�U���iI�HZE�*�V����U$�����U$�"mm�h[E�*�V��*�o;}�UrاU8L�p�V�0��aZ�ô
[�*�V��Jx�*�V��
�U���*����*����*�i�@[�*�V��
�U��mh�@[%�l�V�m�V�m�V��*W�*�U8L��pO�p�V�0��aZ���mh�@[�*�V	[�*�V�m�V�m�V�ml�@[%|mh�@[�*�V��
�U��m�V��߭i��
n��
�V�s�V�0��aZ�ô
�i��i�*lh�@[�*�V��
�U��~m�V�m�V�m�V��
�U��m�p�U��mh�@[�*�V�m�V�m��h��
�Vyr�V�0��aZ�ô
�i�*�Ur8mh�@[�*�V��
�U��m�V���
n��
n�`[�*�V��
�U��޶
�U��mh���
n��
n��J�L��9L�p�V�0��aZ�ô
�i�*lh����mh�@[�*�V��
l��
n����[�Up[�*�V��
�U��mh�@[%�lh�@[�Up[�Up[�U����������9\�      B      x������ � �      D   /  x�U�;n1Dk�)\:�!~$JwI��@>M� 9}f����=y���J��&�~��0 F� '``v�9e�) �L�2kqf����d& |�- �l �m5�\�߾?�/�T/׿����/\������2�&=�|�X��>}�����ћH������!�:$�C��]}7��g��%Li*Oʃ�Z�5��N����t0yP&���)�xPx��E'q�!$�>��Շ���pW����$-
ϻh:oP�c%-t��XBb�R;޼�9'en�27I��5�3�H��$�v'1�z;��y��3      F   *   x�3�4��20 "NK+c���41�Z�\1z\\\ ��      G   "   x�3��4����KI��26 �KJS�A�=... yk�      J   �   x�U��
�0E�3��d�j�m-D(m� �|���c��N���y��P���,_����ܯ��-�����n[T��C�`�D7�A�0�0F�&���=�,&^�2���Abϰ��0��$�XB9'CʱyN���I0yb�,��BrP���H>���-��K�D
�-	Z�Ɋ����$\v���gu(      L      x������ � �      N      x������ � �      P   �  x�m�K�-KRD�y�v�3�B��(
��SI%hV��������n޹��-"3,=>�-=�Ӟ��������O���ߞџ���
��?����_��>�F�nz�����~�����������m݊���E�W���>�+��O�<+������⊭+�.��s���ys��-���s�y��h���h��U/�Ը��Yo���XW�⹱rU=��ah�ӂG�)���xn|A�v�����*��Xc��,��sc��Ř�&��G�ߒ:ާW9�xz����*'�ύ�ӫ��A<7��rX4^���g�"~��+����%��_M���n�s[�{���pn+z�)��E<o2@U�C<7֞R�o]���0�bk�sc�˟��A<���?�xn��Vkm⹱V�ۗ���������v�t�#~�R;�x��ůZjg�W�Ϭ6�s����3��K<��<�J�ۈ����U�A<㝏֠��w=�#�7�|	ѫ2���sc�+V��ꔺ�x���✺�p>g糪Eu��x?�r��<��Y�◸��:܇������9^?�x>t?�Y�j�	`��R<|6�]],K�y��x�`CƸ��A�����P_���N�]���ߖ���zW���϶��t��j95����Sӡ����`�&3���]�iMg����P�y�E|��b����3m�bk:�v�RU�S�3i�ZQ������eN5��Q��)s���xNu$�2�����T���cӦ~|=j�ֆ������_���k�>e�#Ϝ�^��bY.:m�����S��`�Ų:��v�c�W�~x�	?�!mr:i!mfo�����\�k��O{*����.���f�iо�z�}��+��_3�_�GT$}�hٿŹԴ�����*(����-�s��uy}zk��rzk�]��ZO:�Rj�j�oWZ�:����5дn� �&��ߨ���w%Ml��ytbhS;��ؕŰ�ښ�ק��ޭ�Y���~�h�qW�g )��8D���3;7>�{�#ْ��ٶ�N��u$_\��ϗ��p�4Z�+� OK^�'��UE�e������W{S��@����F �A��wh[�
�l
��#n�ok�кX5���7�����6�O2H�:��M��`l�Io3�������ޠ\�c��'�q~ܕ 	��7�s&xx� y:� �Q 3 k/��Zĝw 6 Qv�"�7� �ݶ�����֠��=�U�F��nD���a�7���C�|�[��D�.�7m-}܈�0�jyז�g$�]�����$�}b���\���K:5�;b�'� �r���G�-�<��2�kb mK�ǱV _4����e�'� iW� 7��aC�3�!�HN�4UsC$��u���W�m���t-�q��ui7G���=,��2�$�@���L��u����z�g��p ]�����t���H��B�����u@���`�]�3��*�:�@�r�|/��Z�i��*3� [�i��L��@�P`n�D��u���i:�@���5�
[��Qa�\Ө�l�쟁
s_��BZ ��]��4Ёs�&n0�/%P`��D��u�ҳ�07m�tm*�m��+}��
s�f �v��:̽���՞>���(e 	���J𵟪�
�������FB��l�6�@u�H:��|��:KQ�=-鈁�r�f����	��:��~�䗙;#�Ǖ��7r�_��)L���:�^��[?�;��#k ���	��:@�2�*����+��
sh ���G�@�����`o�"�z��`o�gT��yF��{�V�SyM�$��G��@��˅�
{=�H��|�
{}�"���e0*����H��\˨��3�{��=�<��`�_5PanX$X�XT�[V	v��i�DX��DP`n\� ��ׂ�h�h&� ���2`BҾ�����_���k�@|�rR���/ėdt@]����"�k�@|�lT��;dC�CW߁�p�,('��gO~	�,��):�O ��&�Nա������Ed�I_H�Y�;� �2��Ć��Y��O ����ْ<��I#�8W{���@��\iS��^�35�^�����"�ɧ.�s�B��n�����S���5����Z��+�_.��٠[֙�7� ݲ֤���V��=�|��7���{�~6����~�n��)s(���DWﶧO�.��t��h�9(�:l�{�5?\=�c$Or��u?c�9�Z�$��Bu[��Z(��&�P�\�Q�+1ʡ|�EAT~�6��;�PÞ/�Pٶ��x�mN 6-�u��ȯ��d`�����6��=�$�&�Q�75)�rojR��8sA�&��.dJ7Wy���k�2��	$�Vڪ��ĥv$�Aڪ%8 3�@��	ӹ�uX��}��¸:�澁���NtZ �ƕ�g�WO���ɳ���oJ�9eT|ˊ�)qսfgJY��F�W�m�)e�KiJ\�凐�U��� V v��2'�jy��VeU�D^-?l�Ve]�D^ya�D[��D.�W^�3�Veq�D^-��V��1Ҫ,��H+7�&Ҫ,�Yh+7�ڪ4��j� ]h���ㅮ*����rki��Jki!��ZZ��ZZH+���ʭ�(am�|�3]��Ba�O拮*�����e�H]�R��ˈkY1��U�~�BVy!�BSy%QT�@ҳAVy��BW��*w�9��h��m��n ����cs��c?QO�,AZ,�AZ���7�,MZ�+�MZ��cYG\;���-`!�J�i���~Zh��~Z�+�����"�����*�'j�O i�����i!���XX<�7����*C7���?��[QiU4�e��/�U�ZW��k߅��~�:���o
����B[]{�O�� �(��u��V 6"��(�9�Xq�R)��ح"Ƅ�JVQ�a\�ZȫdE�.�5&�U.�"e���SE]e��0��W�7�Սo y�/��L��H.R����;��@��|��Hq����]��=��އ�V��KszdI�/��(�b \�Zw�ss!\�zZwb \.�{����ֽM��r8�o�@Z �A��>�g?�}D�o���
�:�p�u!'����������@��=� -� id�w��!5HWw��@��.��n' k�j�����������-�|	��S #����[:k�o�h+�e�֖�^��%���m�7� ��@�a|�ꠑ������ߞ�o��1����6�=v ��՛�=�@lH�.�??��R\���|gs��U�wX��Hn/Ҋ��@�t��e�֖�^��%�F�vm�P�<�0��4�HNse��5�������n�m�����4��o���|�W�hm�̟�iD|X|��[���g�� �=db��\�|����s@w�`�7��=��H�{��*��3�����4��2�ݳ�0�0.������y`\��s�0.W����g����ɸ�k�*�e�h�� ���߱�~`���ӷZ=;�W.�",��7n\����r?,�H~B询�J�FyQ�F�EU�UTO��@�R"]�Iq�7��׮�j;Ȱm�������2l[F�ͬ@f �A��̊;�@����dض�4X��E���l��@�e�`�HVQ 3��(��$ͱ�+,':���D��|M*,�Q����L*��&y�`�;�6tX����˴�ò�DҾ���u�t.J?h�\�D��*<h�c[�A�y�S 3� �n>��"����`��8:���O h�׳�K�Qk���g�A���~a���?����Br��      Q      x������ � �      S      x��<�v�Ʊk�+zw���@7��I3�\ţ�l)�s|���"	��14����>��l���R�x5
 I9�9vfTU]]�.�ϸ�}ڥ�>Zd�$[&LŏI���r�f���]������w�2ˣ}� k�F[@�U4�M@����s�o�˗b��\�7*���K��s,�Xތ;�;�dɒ���<�H!/7�$-�m	D�<-�y��l��� 8��\%�0X#�qw�C�x*b�iD�e�K����?�|Odb[���m�Q�h��u���8ʁG�yԧoۈ�ݠ,پ�ނ\���ׯ���l��;��),�Xw�6�/�\©�N�c����R�&Y��m��\�(��A���>�ò\ߒE�@��>���W�a\K��?~���X���e���]z��"�� ��-g}�٫Cm@��M�����'$@���o��$�Qv(c<��4���e�Uk�Gn�O���J���������9B_�LE���D��C ���c��I<�T<�r�����a�J.�׮�e:#��[�P-��'7k%���@��C��d)7�����dQ�h-`�o�v�AK&�ɦ1: �����b��yxX�^�gI�GVwሉwĻ�4��Ep�o"{�0&l��j�'Y����g���aԖG�Vu�d��xV}�	1�c+�d��م����j%ю�#�ym e�(���L��ƖL���T"�e/�"�����X��m�䓆)<�k#,�r�J+�!������^e�׋$^�ӄ��	��O�*�4V�),lY��j����}�A�N$}%�1ĸ�[��*�lY�6�v��(�cҖ�/�.��-1Tg`>Z�#��xH�գ�Nx��#�<�!0��UI��]��zT�����f���ɜQߩl��#w�$�_j���ȵƎ�]�[���_�H2�l��Q��}�'�X�*W�%�
���@�j�FP�D����L���4�]e�-��$�.���� ��œ�
���bǛ��,7G��!]� �6Ӝ.���HM���G�[�*P`��e�T�E�� ��wf��;{�VQ~��6Dj����"���p�/�Y���G�l8���c�T���=>�{�Vp�	�[m�S�m�	ʇ�)y�"�!㕊%���µ_�����t�D�FB�6��!L˪�`���2�3�<�V>˟������@�C�A��s�4�g�Ul� �˟��r7��o!�u3!�B]hB�'|۳�V��H�S�ݓ���zgUCR%?GB]T�q�9��%r��E�=�wz���"*݁0f�5����(�dƮ/�UG͏�Jf#��&�WŪ)��D}�ny@IP"S�^/3�Q)=��rߪr~� 5��-�C)��Gö�4R�>�u �������	R�f�1��F�O��!���{�v:xa�ևa�\0�p����4��0��8�]b$�H.�^�5[Y�o�87��ۧ����g->���N���#:�u'�!�%^��N��G��.����.�c�gB�!�]Ap�J5���-��mU��ѺZ3�ߪ.!c7#�&��)ģͦ|��l�R�B��tyL�e��0C@��5tSl��:�� o'�c�h�: ��{Љ���ziD3��D��ͮo�`��j�?$;�n l��ā�.�)�Ha*�VL��4a�/�ѧ ��"O ء2 �a�d�����؝�?�|��e$'ږ�Dn�x��$D3(d�&��ָ�n������²4b�&o[`J̣0�������T;A/�H�#$��MVn�	\KSZ�#g[��	w(J-�wO�0��݀��nYfkH�����q�^���ö���1A������  ����ç7l|��޼��ݓ���iDvW��RI�V�I���M&�m#ڑ�)���:�m�#��hs�u��m���������49��mA?@\�om�E�Wam�э�vt�Jn�ѐû4Z׳A� �p9�5�F��� }=#��]E���T��S6�*� y�T���i�f������"�1����%�0n��L����"�r���Y�B7��*['��t�w�0֣a\yK��I����f��(q��L�]�;�(��v�����u�6��C�jf0=A�>��f"΃!W����GU���*��I!���N�Κ(?����;g=�����n�8��q�P���(����^*E�jF��7���v�8B��(���/��Wi~8b��(f�̦p���}Әr��z�~  oW� �噸�K�iʜ��{ �f(���
�'E�G�mp�,k�It�����WWm��T��ƦȪ~Z����Ae�ݺνM����^؆Z��G���� ܏�~^��R��b�� �>&�_9!�֧lޱ�CҢ<�1����忟�j�-{����ʻf�C�w���d��%�"�
�����%ك��i�OQ|��!/t�M����: +����.���ĬȜ?Y����4:M�|Vl%񲏪J	}Z�5EK��� �b�G0���.�"�}x9'fԔ�8F�r�9��p�5���3�u&I�Z;8;hk}֞��jpb�J��{�Zs������}�;F�S����b�b�{�(K�m�j��lן<���CӦT���5���s�;i�H�S9Oч��K�Cƺ�.|=m��m�^)$���v݉"1@&���/������ a";$����gW򰃘x��#�v�ۇ�f�أ"�r��Eu��+�g��UU}�����뜠9��Li�H�����Iܼ�Z��JyG�D�L$Q?c���vmTD>"�~���`�T���Z��pe3�v�1��t�q���3!�[�p׼���R%�jķ㡫���d�����N��d��g0η}���=�2��z��{�̣)o����ƳvOՋ��K4��G͘��"r3cN*�Me5p~�{=�ؾ�HmЭ��o���G�M�X/�"�U{����(��
��������R���!1}ף��c�x[Q�M�yM���,�n�z7�H�mH]�DuK`#�'U����@ 9�=���2��A6Ā��o�v� ��׏�7E�Ļ���bo��k)�-N浣:`&�Y2v/�X7��D��#��v�C����y��<�e��7��j`��B��M�[JZ-�d#ҟ!|�ൽPB!�ܾ4v�Y(
��/]
j�cl7Q[��M�j�G�#��B�]`I
}�Ѻ,t���dU�2��C���H[��Xȶ*Ò\��X\V4t����>бC�#%�>�괖鷎j��|)H��f�<�mc�a�7��>,����3��l��Ip�a�N�~�������ې|����y�V �J4F§�B�F넮��c��-�8�3,-"��F��հ9�1���(��-%�!�fd}X�a����'+Dn9]!�"n=��$4;Ƕ�l�ȼ59Խm#h��@�5�r`�6.�U�|�jԪ?Pa��ޯ3��R�P��]�� ��i��,;_ퟢg7�P���"�FЧ����D�ڧ_�UF�����r�Y���Jfʱ���<�V�4�X�lE���2�g����:RfrzX8��x��B��-,�����i<��MW���Z���Fb`��[U��b<,X9�=4(���b�%/fމw,���~@*��F�U��xF����$_U�r���)�sǤ�էD|!^'S �B�;4���6���0�����5�S#��ݺ���Af��3.�lv��C�D8F6]_5b��EFÈ�]#�ր�n��nՅ/�ݚ*�[q�Wr�/X/�$s��/M������d���2RJ�s���Q�'Y�qꡗ�ʡ�	����;�㝓e?wl�8.���?�9���&�]�çh�iڐ�'wRꭧ6��}dHl�h��8��wfH ]-ຌ=�#�<�����g��!��"%��P���8AȩZ}8~�]7ƞ+�c6�K�N0%J=�L��:o^_(�a�j���0�"v��\m6W�*�ͽt����o] i  P���,��D�Z��W�� ��y.EY˼��o���7�f��tܚ��������?��`���S�-_~��a�p�JR�R�4XЁ����-�6:,�]��ǶsZ&:^Aw�*��\�����7�ټ����>�U�T�P�]R�J?a���1�d�X�E��=��y� /�<ҟq�W1����vHV���(��'�>0��y�ա��%��cUhR<m��t?�MF�p�j䉖{n��]&����n�$:���>%�,���%
�A�����_����~�&�1�[��i3�o�r�O�.\�*.~�慭��6#����mz�5U�;��|mMzdZ���`S��Q���$կ����4���+��������p�G�xo��F}mL��������;�WqEF�q�c�{�c��lY��og�`'��x�}MG5�#�cm�"�5N`!`Od�^h�۞C������)$�S�l�	N����I�Ph@����l��e�AL��n�������\"ڣ�=�4�5}ՓM�ߚX[��>��x������_o?^���=K	���Ԋ��r�ӈ��y<�����}��6ޤ�!G�s6�x(��&�{�&��U\�cG7���=D�J������be$Ѐ����V.�����p���4aY=��-a��ȚE�C�sWۄ%���;|�r��k�S�`
Yۺ�m�_�����E���'���h��^�
��V_ö��T�ŋ�{Q��wj/�"	Dz���D9w�I�.�O,��Z������9���;��K	;@ȑMNB0D��Kd�[wj���>B�F�õrΧ�?�m%8���{WNw{�;G�p��%������C���I6���f�h@;����܈<��!.&�����`q��;��g@؈|���#������JL��}!�=�QZ��9����5V˫����c!�B��o��t���w^!��'����B�cS!o#����t��7�H��85�@��	������o'a�����=o����ǝA�;�Q����iE��s]����g�_�oVh$p�
E��?�NXqH%XYtg.��G�¹A42���I@�F����K�nW}2;�a��z�oe.G��Ov)Q�1�,maPgd�/�!(`ab��d�f��| v]�w�b�B\�נ,Os�^~�Zﮚ�"`ZNu�C����U��d�P<z� �c��|s��Dq��`�8�����G�/sm�1��.��X���"I����b1�7�(ͻV����w��S�b�1�c���1:��C�
�j���w��P��i�X��9���r�s�J�1mY+��I�l�y�U�������wV��͍|�B��[��/�w@+�������υ_�{��R@k�XT���6��L|�L	�F��0􁑀���&�{j�R@��p�^٫ʮ�O��c{����v�6Uh�_(���̍Vrg(] ��^� oO���F��`p�@&�Hݨj�os`Z,����� �h3y��@�=�$A@lD�W9=x|h��A��ښ&�xlxf�3ҬZ�oQ��n3�%&v�-�+���F.W���	:��py���o�(��      V      x������ � �     