PGDMP  2                
    |            inz    16.3 (Debian 16.3-1.pgdg120+1)    16.3 n    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16388    inz    DATABASE     n   CREATE DATABASE inz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE inz;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            �           1247    16697 
   class_type    TYPE     m   CREATE TYPE public.class_type AS ENUM (
    'wykład',
    'ćwiczenia',
    'laboratoria',
    'projekt'
);
    DROP TYPE public.class_type;
       public          postgres    false    4            j           1247    16510    cycle    TYPE     @   CREATE TYPE public.cycle AS ENUM (
    'first',
    'second'
);
    DROP TYPE public.cycle;
       public          postgres    false    4            �           1247    16732    day    TYPE     �   CREATE TYPE public.day AS ENUM (
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday '
);
    DROP TYPE public.day;
       public          postgres    false    4            �           1247    16806    degree    TYPE     ;  CREATE TYPE public.degree AS ENUM (
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
       public          postgres    false    4            �           1247    16623    language    TYPE     G   CREATE TYPE public.language AS ENUM (
    'polski',
    'angielski'
);
    DROP TYPE public.language;
       public          postgres    false    4            �            1259    16585 	   buildings    TABLE     m   CREATE TABLE public.buildings (
    code character varying(50) NOT NULL,
    building_id integer NOT NULL
);
    DROP TABLE public.buildings;
       public         heap    postgres    false    4            �            1259    16658    buildings_building_id_seq    SEQUENCE     �   ALTER TABLE public.buildings ALTER COLUMN building_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.buildings_building_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    226            �            1259    16590 
   classrooms    TABLE     �   CREATE TABLE public.classrooms (
    code text NOT NULL,
    floor smallint,
    capacity integer NOT NULL,
    equipment json,
    classroom_id integer NOT NULL,
    building_id integer NOT NULL
);
    DROP TABLE public.classrooms;
       public         heap    postgres    false    4            �            1259    16649    classrooms_classroom_id_seq    SEQUENCE     �   ALTER TABLE public.classrooms ALTER COLUMN classroom_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.classrooms_classroom_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    227            �            1259    16502    fields_of_study    TABLE     h   CREATE TABLE public.fields_of_study (
    field_of_study_id integer NOT NULL,
    name text NOT NULL
);
 #   DROP TABLE public.fields_of_study;
       public         heap    postgres    false    4            �            1259    16501 %   fields_of_study_field_of_study_id_seq    SEQUENCE     �   ALTER TABLE public.fields_of_study ALTER COLUMN field_of_study_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.fields_of_study_field_of_study_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    216            �            1259    16765    generated_plans    TABLE     !  CREATE TABLE public.generated_plans (
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
       public         heap    postgres    false    4            �            1259    16764    generated_plans_id_seq    SEQUENCE     �   ALTER TABLE public.generated_plans ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.generated_plans_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    246    4            �            1259    16529    groups    TABLE     �   CREATE TABLE public.groups (
    group_id integer NOT NULL,
    code character varying(50) NOT NULL,
    semester_id integer NOT NULL
);
    DROP TABLE public.groups;
       public         heap    postgres    false    4            �            1259    16528    groups_group_id_seq    SEQUENCE     �   ALTER TABLE public.groups ALTER COLUMN group_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.groups_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    220            �            1259    16576    plans    TABLE     �   CREATE TABLE public.plans (
    name text NOT NULL,
    creation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    plan_id integer NOT NULL
);
    DROP TABLE public.plans;
       public         heap    postgres    false    4            �            1259    16641    plans_plan_id_seq    SEQUENCE     �   ALTER TABLE public.plans ALTER COLUMN plan_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.plans_plan_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    225            �            1259    16551 	   semesters    TABLE     �   CREATE TABLE public.semesters (
    semester_id integer NOT NULL,
    number smallint NOT NULL,
    specialisation_id integer NOT NULL
);
    DROP TABLE public.semesters;
       public         heap    postgres    false    4            �            1259    16550    semesters_semester_id_seq    SEQUENCE     �   ALTER TABLE public.semesters ALTER COLUMN semester_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.semesters_semester_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    224            �            1259    16748    slots    TABLE     �   CREATE TABLE public.slots (
    slot_id integer NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL
);
    DROP TABLE public.slots;
       public         heap    postgres    false    4            �            1259    16754 
   slots_days    TABLE     �   CREATE TABLE public.slots_days (
    slots_days_id integer NOT NULL,
    slot_id integer NOT NULL,
    day public.day NOT NULL
);
    DROP TABLE public.slots_days;
       public         heap    postgres    false    4    919            �            1259    16753    slots_days_slots_days_id_seq    SEQUENCE     �   ALTER TABLE public.slots_days ALTER COLUMN slots_days_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.slots_days_slots_days_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    244    4            �            1259    16747    slots_slot_id_seq    SEQUENCE     �   ALTER TABLE public.slots ALTER COLUMN slot_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.slots_slot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    242            �            1259    16516    specialisations    TABLE     �   CREATE TABLE public.specialisations (
    specialisation_id integer NOT NULL,
    name text,
    cycle public.cycle NOT NULL,
    field_of_study_id integer NOT NULL
);
 #   DROP TABLE public.specialisations;
       public         heap    postgres    false    874    4            �            1259    16515 %   specialisations_specialisation_id_seq    SEQUENCE     �   ALTER TABLE public.specialisations ALTER COLUMN specialisation_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.specialisations_specialisation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    218            �            1259    16535 	   subgroups    TABLE     |   CREATE TABLE public.subgroups (
    id integer NOT NULL,
    group_id integer NOT NULL,
    subgroup_id integer NOT NULL
);
    DROP TABLE public.subgroups;
       public         heap    postgres    false    4            �            1259    16534    subgroups_id_seq    SEQUENCE     �   ALTER TABLE public.subgroups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subgroups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    222    4            �            1259    16711    subject_type_teacher    TABLE     �   CREATE TABLE public.subject_type_teacher (
    subject_type_teacher_id integer NOT NULL,
    subject_type_id integer NOT NULL,
    teacher_id integer NOT NULL
);
 (   DROP TABLE public.subject_type_teacher;
       public         heap    postgres    false    4            �            1259    16710 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE       ALTER TABLE public.subject_type_teacher ALTER COLUMN subject_type_teacher_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subject_type_teacher_subject_type_teacher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    240    4            �            1259    16686    subject_types    TABLE     �   CREATE TABLE public.subject_types (
    subject_types_id integer NOT NULL,
    subject_id integer NOT NULL,
    type public.class_type NOT NULL,
    max_students integer NOT NULL,
    number_of_hours integer NOT NULL
);
 !   DROP TABLE public.subject_types;
       public         heap    postgres    false    4    910            �            1259    16670    subject_types_groups    TABLE     �   CREATE TABLE public.subject_types_groups (
    id integer NOT NULL,
    subject_type_id integer NOT NULL,
    group_id integer NOT NULL
);
 (   DROP TABLE public.subject_types_groups;
       public         heap    postgres    false    4            �            1259    16685 "   subject_types_subject_types_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types ALTER COLUMN subject_types_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subject_types_subject_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    238            �            1259    16610    subjects    TABLE     �   CREATE TABLE public.subjects (
    subject_id integer NOT NULL,
    semester_id integer NOT NULL,
    name text NOT NULL,
    exam boolean NOT NULL,
    mandatory boolean NOT NULL,
    planned boolean NOT NULL,
    language public.language NOT NULL
);
    DROP TABLE public.subjects;
       public         heap    postgres    false    4    904            �            1259    16669    subjects_groups_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types_groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subjects_groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    236            �            1259    16609    subjects_subject_id_seq    SEQUENCE     �   ALTER TABLE public.subjects ALTER COLUMN subject_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subjects_subject_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    230            �            1259    16602    teachers    TABLE     �   CREATE TABLE public.teachers (
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    preferences json,
    teacher_id integer NOT NULL,
    degree text
);
    DROP TABLE public.teachers;
       public         heap    postgres    false    4            �            1259    16627    teachers_teacher_id_seq    SEQUENCE     �   ALTER TABLE public.teachers ALTER COLUMN teacher_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.teachers_teacher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    228            �          0    16585 	   buildings 
   TABLE DATA           6   COPY public.buildings (code, building_id) FROM stdin;
    public          postgres    false    226   �       �          0    16590 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    227   �       �          0    16502    fields_of_study 
   TABLE DATA           B   COPY public.fields_of_study (field_of_study_id, name) FROM stdin;
    public          postgres    false    216   L�       �          0    16765    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    246   i�       �          0    16529    groups 
   TABLE DATA           =   COPY public.groups (group_id, code, semester_id) FROM stdin;
    public          postgres    false    220   ��       �          0    16576    plans 
   TABLE DATA           =   COPY public.plans (name, creation_date, plan_id) FROM stdin;
    public          postgres    false    225   ��       �          0    16551 	   semesters 
   TABLE DATA           K   COPY public.semesters (semester_id, number, specialisation_id) FROM stdin;
    public          postgres    false    224   ��       �          0    16748    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    242   ݉       �          0    16754 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    244   �       �          0    16516    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    218   ?�       �          0    16535 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    222   \�       �          0    16711    subject_type_teacher 
   TABLE DATA           d   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id) FROM stdin;
    public          postgres    false    240   y�       �          0    16686    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    238   ��       �          0    16670    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    236   ��       �          0    16610    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    230   Њ       �          0    16602    teachers 
   TABLE DATA           Z   COPY public.teachers (first_name, last_name, preferences, teacher_id, degree) FROM stdin;
    public          postgres    false    228   �       �           0    0    buildings_building_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.buildings_building_id_seq', 4, true);
          public          postgres    false    234            �           0    0    classrooms_classroom_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.classrooms_classroom_id_seq', 10, true);
          public          postgres    false    233            �           0    0 %   fields_of_study_field_of_study_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.fields_of_study_field_of_study_id_seq', 1, false);
          public          postgres    false    215            �           0    0    generated_plans_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.generated_plans_id_seq', 1, false);
          public          postgres    false    245            �           0    0    groups_group_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.groups_group_id_seq', 1, false);
          public          postgres    false    219            �           0    0    plans_plan_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.plans_plan_id_seq', 1, false);
          public          postgres    false    232            �           0    0    semesters_semester_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.semesters_semester_id_seq', 1, false);
          public          postgres    false    223            �           0    0    slots_days_slots_days_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.slots_days_slots_days_id_seq', 15, true);
          public          postgres    false    243            �           0    0    slots_slot_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.slots_slot_id_seq', 2, true);
          public          postgres    false    241            �           0    0 %   specialisations_specialisation_id_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.specialisations_specialisation_id_seq', 1, false);
          public          postgres    false    217            �           0    0    subgroups_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.subgroups_id_seq', 1, false);
          public          postgres    false    221            �           0    0 0   subject_type_teacher_subject_type_teacher_id_seq    SEQUENCE SET     _   SELECT pg_catalog.setval('public.subject_type_teacher_subject_type_teacher_id_seq', 1, false);
          public          postgres    false    239            �           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 1, false);
          public          postgres    false    237            �           0    0    subjects_groups_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 1, false);
          public          postgres    false    235            �           0    0    subjects_subject_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 1, false);
          public          postgres    false    229            �           0    0    teachers_teacher_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.teachers_teacher_id_seq', 5, true);
          public          postgres    false    231            �           2606    16663    buildings buildings_pk 
   CONSTRAINT     ]   ALTER TABLE ONLY public.buildings
    ADD CONSTRAINT buildings_pk PRIMARY KEY (building_id);
 @   ALTER TABLE ONLY public.buildings DROP CONSTRAINT buildings_pk;
       public            postgres    false    226            �           2606    16656    classrooms classrooms_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classrooms_pk PRIMARY KEY (classroom_id);
 B   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classrooms_pk;
       public            postgres    false    227            �           2606    16508 "   fields_of_study fields_of_study_pk 
   CONSTRAINT     o   ALTER TABLE ONLY public.fields_of_study
    ADD CONSTRAINT fields_of_study_pk PRIMARY KEY (field_of_study_id);
 L   ALTER TABLE ONLY public.fields_of_study DROP CONSTRAINT fields_of_study_pk;
       public            postgres    false    216            �           2606    16769 "   generated_plans generated_plans_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_pk PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_pk;
       public            postgres    false    246            �           2606    16533    groups group_pk 
   CONSTRAINT     S   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT group_pk PRIMARY KEY (group_id);
 9   ALTER TABLE ONLY public.groups DROP CONSTRAINT group_pk;
       public            postgres    false    220            �           2606    16648    plans plans_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.plans
    ADD CONSTRAINT plans_pkey PRIMARY KEY (plan_id);
 :   ALTER TABLE ONLY public.plans DROP CONSTRAINT plans_pkey;
       public            postgres    false    225            �           2606    16555    semesters semester_pk 
   CONSTRAINT     \   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_pk PRIMARY KEY (semester_id);
 ?   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_pk;
       public            postgres    false    224            �           2606    16758    slots_days slots_days_pk 
   CONSTRAINT     a   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_pk PRIMARY KEY (slots_days_id);
 B   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_pk;
       public            postgres    false    244            �           2606    16752    slots slots_pk 
   CONSTRAINT     Q   ALTER TABLE ONLY public.slots
    ADD CONSTRAINT slots_pk PRIMARY KEY (slot_id);
 8   ALTER TABLE ONLY public.slots DROP CONSTRAINT slots_pk;
       public            postgres    false    242            �           2606    16522 !   specialisations specialisation_pk 
   CONSTRAINT     n   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT specialisation_pk PRIMARY KEY (specialisation_id);
 K   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT specialisation_pk;
       public            postgres    false    218            �           2606    16539    subgroups subgroups_pk 
   CONSTRAINT     T   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroups_pk PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroups_pk;
       public            postgres    false    222            �           2606    16715 ,   subject_type_teacher subject_type_teacher_pk 
   CONSTRAINT        ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_pk PRIMARY KEY (subject_type_teacher_id);
 V   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_pk;
       public            postgres    false    240            �           2606    16690    subject_types subject_types_pk 
   CONSTRAINT     j   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_pk PRIMARY KEY (subject_types_id);
 H   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_pk;
       public            postgres    false    238            �           2606    16674 '   subject_types_groups subjects_groups_pk 
   CONSTRAINT     e   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_pk PRIMARY KEY (id);
 Q   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_pk;
       public            postgres    false    236            �           2606    16616    subjects subjects_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_pk PRIMARY KEY (subject_id);
 >   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_pk;
       public            postgres    false    230            �           2606    16634    teachers teachers_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pk PRIMARY KEY (teacher_id);
 >   ALTER TABLE ONLY public.teachers DROP CONSTRAINT teachers_pk;
       public            postgres    false    228                       2606    16664     classrooms classroom_building_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classroom_building_fk FOREIGN KEY (building_id) REFERENCES public.buildings(building_id) NOT VALID;
 J   ALTER TABLE ONLY public.classrooms DROP CONSTRAINT classroom_building_fk;
       public          postgres    false    227    3307    226            �           2606    16523 0   specialisations field_of_study_specialisation_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT field_of_study_specialisation_fk FOREIGN KEY (field_of_study_id) REFERENCES public.fields_of_study(field_of_study_id);
 Z   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT field_of_study_specialisation_fk;
       public          postgres    false    216    218    3295                       2606    16790 ,   generated_plans generated_plans_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id);
 V   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_classroom_fk;
       public          postgres    false    246    3309    227                       2606    16780 (   generated_plans generated_plans_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 R   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_group_fk;
       public          postgres    false    3299    246    220                       2606    16770 '   generated_plans generated_plans_plan_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_plan_fk FOREIGN KEY (plan_id) REFERENCES public.plans(plan_id);
 Q   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_plan_fk;
       public          postgres    false    225    246    3305                       2606    16775 +   generated_plans generated_plans_slot_day_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_slot_day_fk FOREIGN KEY (slot_day_id) REFERENCES public.slots_days(slots_days_id);
 U   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_slot_day_fk;
       public          postgres    false    244    246    3323                       2606    16795 /   generated_plans generated_plans_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id);
 Y   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_subject_type_fk;
       public          postgres    false    246    3317    238                       2606    16785 *   generated_plans generated_plans_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id);
 T   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_teacher_fk;
       public          postgres    false    3311    228    246                        2606    16540    subgroups group_fk    FK CONSTRAINT     y   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 <   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT group_fk;
       public          postgres    false    3299    222    220            �           2606    16837    groups groups_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) NOT VALID;
 C   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_semester_fk;
       public          postgres    false    220    224    3303                       2606    16556 %   semesters semester_specialisations_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_specialisations_fk FOREIGN KEY (specialisation_id) REFERENCES public.specialisations(specialisation_id);
 O   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_specialisations_fk;
       public          postgres    false    3297    218    224            
           2606    16759    slots_days slots_days_slot_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_slot_fk FOREIGN KEY (slot_id) REFERENCES public.slots(slot_id);
 G   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_slot_fk;
       public          postgres    false    3321    244    242                       2606    16545    subgroups subgroup_fk    FK CONSTRAINT        ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroup_fk FOREIGN KEY (subgroup_id) REFERENCES public.groups(group_id);
 ?   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroup_fk;
       public          postgres    false    3299    222    220                       2606    16716 9   subject_type_teacher subject_type_teacher_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id);
 c   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_subject_type_fk;
       public          postgres    false    240    238    3317            	           2606    16721 4   subject_type_teacher subject_type_teacher_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id);
 ^   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_teacher_fk;
       public          postgres    false    240    3311    228                       2606    16705 9   subject_types_groups subject_types_groups_sbuject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_sbuject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) NOT VALID;
 c   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_sbuject_type_fk;
       public          postgres    false    3317    238    236                       2606    16691 &   subject_types subject_types_subject_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_subject_fk FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id);
 P   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_subject_fk;
       public          postgres    false    230    238    3313                       2606    16680 -   subject_types_groups subjects_groups_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 W   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_group_fk;
       public          postgres    false    236    3299    220                       2606    16617    subjects subjects_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id);
 G   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_semester_fk;
       public          postgres    false    230    224    3303            �      x�s�4�r42�4�r�4����� (�S      �   &   x�3�4�460ମ��4�2r!\CNC�=... r�      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   *   x�3�4��20 "NK+c���41�Z�\1z\\\ �E�      �      x�3�4����KI������ "�      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   Y   x���M,��I�..�/�䬮�4�t	�����J�.M��LM��/J�ə��KR��8!T~yqv&Hڔ3 ��-���щ+F��� 'WP     