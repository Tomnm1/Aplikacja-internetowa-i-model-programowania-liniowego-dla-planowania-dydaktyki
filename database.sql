PGDMP       "            
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
    semester_id integer NOT NULL,
    group_type public.class_type
);
    DROP TABLE public.groups;
       public         heap    postgres    false    4    872            �            1259    16490    groups_group_id_seq    SEQUENCE     �   ALTER TABLE public.groups ALTER COLUMN group_id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    4    225            �            1259    16498 	   semesters    TABLE     �   CREATE TABLE public.semesters (
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
       public         heap    postgres    false    4    878            �            1259    16508    slots_days_slots_days_id_seq    SEQUENCE     �   ALTER TABLE public.slots_days ALTER COLUMN slots_days_id ADD GENERATED ALWAYS AS IDENTITY (
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
       public         heap    postgres    false    4    875            �            1259    16515 %   specialisations_specialisation_id_seq    SEQUENCE     �   ALTER TABLE public.specialisations ALTER COLUMN specialisation_id ADD GENERATED ALWAYS AS IDENTITY (
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
       public         heap    postgres    false    884    4            �            1259    16536    subjects_groups_id_seq    SEQUENCE     �   ALTER TABLE public.subject_types_groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subjects_groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    240            �            1259    16537    subjects_subject_id_seq    SEQUENCE     �   ALTER TABLE public.subjects ALTER COLUMN subject_id ADD GENERATED ALWAYS AS IDENTITY (
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
            public          postgres    false    4    245            8          0    16467 	   buildings 
   TABLE DATA           6   COPY public.buildings (code, building_id) FROM stdin;
    public          postgres    false    215   ��       :          0    16471 
   classrooms 
   TABLE DATA           a   COPY public.classrooms (code, floor, capacity, equipment, classroom_id, building_id) FROM stdin;
    public          postgres    false    217          <          0    16477    fields_of_study 
   TABLE DATA           B   COPY public.fields_of_study (field_of_study_id, name) FROM stdin;
    public          postgres    false    219   ߋ       >          0    16483    generated_plans 
   TABLE DATA           �   COPY public.generated_plans (id, plan_id, slot_day_id, group_id, teacher_id, classroom_id, subject_type_id, even_week) FROM stdin;
    public          postgres    false    221   0�       @          0    16487    groups 
   TABLE DATA           I   COPY public.groups (group_id, code, semester_id, group_type) FROM stdin;
    public          postgres    false    223   M�       B          0    16491    plans 
   TABLE DATA           =   COPY public.plans (name, creation_date, plan_id) FROM stdin;
    public          postgres    false    225   9�       D          0    16498 	   semesters 
   TABLE DATA           K   COPY public.semesters (semester_id, specialisation_id, number) FROM stdin;
    public          postgres    false    227   V�       F          0    16502    slots 
   TABLE DATA           >   COPY public.slots (slot_id, start_time, end_time) FROM stdin;
    public          postgres    false    229   ��       G          0    16505 
   slots_days 
   TABLE DATA           A   COPY public.slots_days (slots_days_id, slot_id, day) FROM stdin;
    public          postgres    false    230   ϣ       J          0    16510    specialisations 
   TABLE DATA           \   COPY public.specialisations (specialisation_id, name, cycle, field_of_study_id) FROM stdin;
    public          postgres    false    233   �       L          0    16516 	   subgroups 
   TABLE DATA           >   COPY public.subgroups (id, group_id, subgroup_id) FROM stdin;
    public          postgres    false    235   ʤ       N          0    16520    subject_type_teacher 
   TABLE DATA           o   COPY public.subject_type_teacher (subject_type_teacher_id, subject_type_id, teacher_id, num_hours) FROM stdin;
    public          postgres    false    237   �       P          0    16524    subject_types 
   TABLE DATA           j   COPY public.subject_types (subject_types_id, subject_id, type, max_students, number_of_hours) FROM stdin;
    public          postgres    false    239   �       Q          0    16527    subject_types_groups 
   TABLE DATA           M   COPY public.subject_types_groups (id, subject_type_id, group_id) FROM stdin;
    public          postgres    false    240   �       S          0    16531    subjects 
   TABLE DATA           e   COPY public.subjects (subject_id, semester_id, name, exam, mandatory, planned, language) FROM stdin;
    public          postgres    false    242   ,�       V          0    16538    teachers 
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
          public          postgres    false    238            k           0    0 "   subject_types_subject_types_id_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.subject_types_subject_types_id_seq', 745, true);
          public          postgres    false    241            l           0    0    subjects_groups_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.subjects_groups_id_seq', 1, false);
          public          postgres    false    243            m           0    0    subjects_subject_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.subjects_subject_id_seq', 389, true);
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
       public          postgres    false    215    217    4727            �           2606    16581 0   specialisations field_of_study_specialisation_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.specialisations
    ADD CONSTRAINT field_of_study_specialisation_fk FOREIGN KEY (field_of_study_id) REFERENCES public.fields_of_study(field_of_study_id);
 Z   ALTER TABLE ONLY public.specialisations DROP CONSTRAINT field_of_study_specialisation_fk;
       public          postgres    false    4731    219    233            �           2606    16586 ,   generated_plans generated_plans_classroom_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_classroom_fk FOREIGN KEY (classroom_id) REFERENCES public.classrooms(classroom_id);
 V   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_classroom_fk;
       public          postgres    false    217    221    4729            �           2606    16591 (   generated_plans generated_plans_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 R   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_group_fk;
       public          postgres    false    4735    221    223            �           2606    16596 '   generated_plans generated_plans_plan_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_plan_fk FOREIGN KEY (plan_id) REFERENCES public.plans(plan_id);
 Q   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_plan_fk;
       public          postgres    false    225    221    4737            �           2606    16601 +   generated_plans generated_plans_slot_day_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_slot_day_fk FOREIGN KEY (slot_day_id) REFERENCES public.slots_days(slots_days_id);
 U   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_slot_day_fk;
       public          postgres    false    230    4743    221            �           2606    16606 /   generated_plans generated_plans_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id);
 Y   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_subject_type_fk;
       public          postgres    false    4751    221    239            �           2606    16611 *   generated_plans generated_plans_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.generated_plans
    ADD CONSTRAINT generated_plans_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id);
 T   ALTER TABLE ONLY public.generated_plans DROP CONSTRAINT generated_plans_teacher_fk;
       public          postgres    false    4757    245    221            �           2606    16616    subgroups group_fk    FK CONSTRAINT     y   ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 <   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT group_fk;
       public          postgres    false    4735    235    223            �           2606    16621    groups groups_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id) NOT VALID;
 C   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_semester_fk;
       public          postgres    false    227    223    4739            �           2606    16626 %   semesters semester_specialisations_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semester_specialisations_fk FOREIGN KEY (specialisation_id) REFERENCES public.specialisations(specialisation_id);
 O   ALTER TABLE ONLY public.semesters DROP CONSTRAINT semester_specialisations_fk;
       public          postgres    false    4745    227    233            �           2606    16631    slots_days slots_days_slot_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.slots_days
    ADD CONSTRAINT slots_days_slot_fk FOREIGN KEY (slot_id) REFERENCES public.slots(slot_id);
 G   ALTER TABLE ONLY public.slots_days DROP CONSTRAINT slots_days_slot_fk;
       public          postgres    false    4741    229    230            �           2606    16636    subgroups subgroup_fk    FK CONSTRAINT        ALTER TABLE ONLY public.subgroups
    ADD CONSTRAINT subgroup_fk FOREIGN KEY (subgroup_id) REFERENCES public.groups(group_id);
 ?   ALTER TABLE ONLY public.subgroups DROP CONSTRAINT subgroup_fk;
       public          postgres    false    4735    235    223            �           2606    16641 9   subject_type_teacher subject_type_teacher_subject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_subject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id);
 c   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_subject_type_fk;
       public          postgres    false    237    4751    239            �           2606    16646 4   subject_type_teacher subject_type_teacher_teacher_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_type_teacher
    ADD CONSTRAINT subject_type_teacher_teacher_fk FOREIGN KEY (teacher_id) REFERENCES public.teachers(teacher_id);
 ^   ALTER TABLE ONLY public.subject_type_teacher DROP CONSTRAINT subject_type_teacher_teacher_fk;
       public          postgres    false    245    237    4757            �           2606    16651 9   subject_types_groups subject_types_groups_sbuject_type_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subject_types_groups_sbuject_type_fk FOREIGN KEY (subject_type_id) REFERENCES public.subject_types(subject_types_id) NOT VALID;
 c   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subject_types_groups_sbuject_type_fk;
       public          postgres    false    4751    239    240            �           2606    16656 &   subject_types subject_types_subject_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types
    ADD CONSTRAINT subject_types_subject_fk FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id);
 P   ALTER TABLE ONLY public.subject_types DROP CONSTRAINT subject_types_subject_fk;
       public          postgres    false    242    239    4755            �           2606    16661 -   subject_types_groups subjects_groups_group_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subject_types_groups
    ADD CONSTRAINT subjects_groups_group_fk FOREIGN KEY (group_id) REFERENCES public.groups(group_id);
 W   ALTER TABLE ONLY public.subject_types_groups DROP CONSTRAINT subjects_groups_group_fk;
       public          postgres    false    223    240    4735            �           2606    16666    subjects subjects_semester_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_semester_fk FOREIGN KEY (semester_id) REFERENCES public.semesters(semester_id);
 G   ALTER TABLE ONLY public.subjects DROP CONSTRAINT subjects_semester_fk;
       public          postgres    false    4739    242    227            8      x������ � �      :      x������ � �      <   A   x�3�t���2��R���~n@�$�a�sz�i���%���eh b(xz� y1z\\\ ���      >      x������ � �      @      x�E��y�H�m�1˘���ׂ6�4kڱ6�ɽ��8�xK�P�Q������/�u{�������
�����������z��ޯ;Ç�_,o�X-��W���-�x�Q;>r؎O�{Dǭ���+O�����q;^r܎������q����ʙ�v|��9n�O�����W�p�j}�����9n�G����v|q�;�9n���+�;��x�q;^s܎����>��q����ʕ�v|�?9n��;��x�q?�㫵�E�����{\�������q_��2�9�=7:߯�ʹ��9n�[����v|p�5:9nu�~_�s܎�7���v�����wʭ�8���l�s�1��Ƙ�m�]rs���o�s�1���8���o��߶�%7����8�[�O�7ƹ��~c����.y��P�7ƹ��~c���q�77�v��qr}������~_�_۠3 ��@':�bqw�,`g:�Y��t�� ��,��Y<�����,@g:�Y��t��(��w�,df!2�Y��Bd"����,��,��Y��t�� ��,@g:�Y��XKg;�Y��t�� ��,@gQ��b+���s���ܪ�0��9�vq�� p��'/���Q��n5�ڕ�Jd�ٰDv,�9#3��~9��n�3�ώ�����3���x��S�9|��e&��5���w�\f�{�ŭSޜd�p����I�'��d��$�='9� ?��������0�b���O1�cb�I[N2�s����Μdp�$��'==����IFKO2���N���T�dxp���I�'ޜd��$Ӎ�d��$��<�໧��{�����͡?{��=��1��s�|��Na���U����gS�[f�Sg[�J����"�!�"�!�,|��yQ߼����yQ�����]xtt��مGW�]xD��:�#�,<�Xx����`�ᙅW�Yx�d�y���%�,<ز�`����.<�����£��ۓn���*�
���n���*�
���n���*o����UĶ��V�*b[En�W�
�V��j9�*t[�n���*�
���3�,<���������g[�.<X��`�£���.<:����£�A���U2,�t�P)u�e����G�Yxpe��݅GO����.<Z��h��#�1�}��[ty���d����Û��_fg,Yx�f����{Yxpf����w<]�jw��҅Gkm]x�w��x���
������������ϖ�o^�gC�7/곝���}�>/�����R�3�o��^ԷU/���5����*�V��
�Uh�Bk�*��w���:(>��|����7�ﳱ��w�l��>��|����7�糱~�p�:@w���:��w���:@y�$���tek����G,��u �\�������[�yQ���󢞭��{���:{����Q��.y�S�}߿�^r�u �^r�u �^2 R " R `I��H��H��j��j� ZS " R " R " R " R " ��h��j��j(
�XJ
@R ��; ) Iȍ� ���� ����� ܹ��������N�����>x[���:@G�t�:@W��'u�y�Nי��"�E��<"{�p�: g�p�: w���:���J/�������3�o��^�ţ�c-yV-O�xq��� >\e�^Å� �)��S��4��u �\��u �\��uP|6��u �^h�u��^h�u��^(� D��<yP} �@���3 �g "� D��>P}�� �@��������g �g �g �g �g �g ��:(�t�H'�t�H'�t�H'�t�H'�t�h'����ډ���ډ*�(҉"�(҉"�(҉`O'�t�H'�t�H'�v�j'�v�j'�v"��걗t��%�(�DI'J:Q҉2�(҉�L'�t�H'�t�H'�t�H'�v�j'�v"�ډ���҉"�(҉"�(҉"�(҉"��t�H'�t�h'�v�j'�v�j'*;�(������Dh'B;ډ��ډ��ډ��n'�v"h'�v"h':щ�NDt"����ډ��ډ��ډ��ډ��ډ��Nt"����DD'";�,�Dh'B;���Dh'B;ډ��ډ��ډ��ډ��N�D�Nt"����DD'�v"h'G;�A;�A;�A;�A;Љ�I'":щ�NDv�Uډ�N�v"���X^v"�a;�A;�A;�A;�����DD'":щ�ND�D�N�D�N,�v"h'�v"h'�v"h'�v"������zӉ�NDv�]ډ�N�v"����Dh'�K;�A;�A;�A;�A;Љ�J'":щ�ND�D�N�D�N�D�N,�v"h'�v"h'�v"����DD'":��7������Dh'B;ډ�N��D�N,�v"h'�v"h'�v"h'�v"��������ډ��ډ��ډ��ډ��X\�D�N�D@'":щ�NDt"��wI'N�t��%�(�DI'�t�H'�t�H'�'�(҉"�(҉"�(ډ���ډ���ډ��N'�t�H'�t�H'�t�H'�t�H'�t"X҉���ډ���ډ�����s)�DI'J:�t��%�(�D�N�D�N�D�N�D��E:Q�E;Q�U;Q�U;Q�E:��ME:Q�E:Q�E:Q�E:Q�E;�D�NT�D�NTt⹖t��%�(�DI'N�t�L'�t�H'�t�H'�t�H'�t"�҉���ډ���ډ���҉"�(҉"��t�H'�t�H'�t�H'�t�h'�v�j'����ډ�N<�+��(�DI'J:Q҉�N�tb��Ӊ"�(҉"�(҉"�(҉"�(҉����v�j'�v�j'�t�H'�t�H'�t�H'�5�(҉"�(҉"�(ډ���ډ���ډ��[=��N�t��%�(�DI'J:Q�E:��D�N�D�N�D�N�D�NT�D�NDG;Q�U:Q�E:Q�E:Q�E:Q���N�D�N�D�NT�D�NT�De'��O;���Dh'B;ډ�N�v"l'�v"h'�vbq�A;�A;����DD'":щ��N�D�N�D�N�D�N�D�N��~���n(*JQ���V�Ģ������Em0j�/&���Ѩ�F�lT�F�pT-G�tTmG�6U�Q5�(	HIAJRҐ��Z�hkF�v�jH���jJ���jL�֤jN*z���(%I)iJiT�)�R��ڮԆ��,���i�mKݸT�KռT�K��T-L��TmL?+��TT�$3%�)	MIiʦ�jk�Ʀjm�y��jo��jq�&�js�F��:%�)�N?1DxJ�S��7�=��Om~j�S���OT�AU#T�BU3T�CUCT�DUSTѢ|��M�JjT����R�"UMR�&U�R�*�3\�R�.US�2UMSE�J�TR��<����"3Pl�jUۨ�H�V�6S����������j��ƪj���j����X%�*iV���j��V�nUW�rUMW�vU�W�zEG�U�_UVQ����4�$b%+����X}�cJvLʎi�11;�f���Qzv���hՕ��iG��Q�v���k��a�vش�m坸�nG��Q�v���pGI�Qw���rՓ��r���a+w���r�T�`*wL厩\����1�;�r�T�8�;J�R��T�(�;J�%�;J�R��V;l�[��V�0�;J�|��T�(�;J�R��T�(�;J�R��V��Z��V;l���k�T��S�c*wL���S��T�(�;J�R��T�(�;J�R��H�Z��V;l�[��T�(�;J�R��L�R��T�(�;J�R��T;l�[��j�[�C*��0�;�r�T��S�c*wL��|r�T�(�;J�R��T�(�;J�R��V�|Z��V;l�S��T�(�;J�R��T.�~6���S*w���rG��Q+w���r���a+W��.�F�KaL厩�1�;�r�T���rG�\��rG��Q*w���rG��Q*w���r��\��r���a*w���rG��Q*w���rG�\��rG��Q*w���r���a+w��Z��r�a�j+W[����V��ru+W�rU+W�rQ?2< �  j�V�j�V��r%�+�\I�J*��ãV�j�V�j�V�j�V�j�V.�ǈGT��r%�+�\I�J+��V��r����@����V��ru+W�rU+W�rU+W�r������Z�����\I�J*WR��ʕ�\��EK+W�rU+W�rU+W�rU+W�rU+WQ�p�r%�+�\I�J+��V��r����\m�����V�n�V�j�V�j�V�j�V.�[��ʕT��r%�+�\��U�\��U�\t�rU+W�rU+W�rU+W�r�+�\I�ʕT��rol�j+W[����V��r����V�j�V�j�V�j�V�j�V��r�M�J*WR��ʕ�\��U�\��U�\��EO+W�rU+W�rU+WQ��ʕT��r%�[�ٲ��V��r����\m�j+W[����Z�hi�V�j�V�j�V�j�**WR��ʅ+�+�\��U�\��U�\��U�\��U�\��rU+W�r�+�\I�J*WR��ʽߘ��;�;�r�T��S�c*w���rG��Q*W��Q*w���rG��Q+w���r���a+W�OD�R��T�(�;J�R��T�(�;J�R��J�Z��V;l�[�C*��o�]T��S���r�T��S��T�(�;J�R��T�(���T�(�;J�Z��V;l�[��T�(���|��T�(�;J�R��T�(�;J�R��V�\Z��V;l��ܛ��P�c*wL厩�1��W*wL�S��T�(�;J�R��T�(�;J�-�;j�[��V;l�S��T�(�;J�=�;J�R��T�(�;J�R��V;l�ʣ�;l���{�T��S�c*wL厩�1��󩕟R��T�(�;J�R��T�(�;J�Z��j�[��V;L�R��T�(�;J�R��N�R��T�(�;J�Z��V;l�[��r�S�c*wL厩�1�;�r�T�8�;J��G���R��T�(�;J�R��T;l�[�ri�[��T�(�;J�R��T�(�;J�R�jM�R��T;l�[��V;�rl�����V��r����\m��V�j�V�j墽��Z����Z�����\I�J*WR��ʅG+W�rU+W�rU+W�rU+W�rU+W�r���UT��r%�+�\I�J+��V��r���/+W[����V�n�V�j�V�j�V.�[����Z��ʕT��r%�+�\��U�\��rU+W�rU+W�rU+W�rU+W�r�[�o*WR��ʕT��r/l�j+W[����V.^�\m��V�j�V�j�V�j�V�j墵���\I�J*WR��ʕ�\��U�\��E[+W�rU+W�rU+W�rU+WQ��ʕT.ܩ\I�J+��V��r����\m�j+W[��h�V�j�V�j�V�j�V�j�**�T��r%�+�\��U�\��U�\��U�\t�rU+W�rU+W�r�+�\I�J*WR��rl�j+W[����V��r���[�����V�j�V�j�V�j�V��r%�+�ܲ�y�~�re+W�rU+W�rU+W�rU+W�r���U�\��UT��r%�+�\I��������a#�      B      x������ � �      D   /  x�U�;n1Dk�)\:�!~$JwI��@>M� 9}f����=y���J��&�~��0 F� '``v�9e�) �L�2kqf����d& |�- �l �m5�\�߾?�/�T/׿����/\������2�&=�|�X��>}�����ћH������!�:$�C��]}7��g��%Li*Oʃ�Z�5��N����t0yP&���)�xPx��E'q�!$�>��Շ���pW����$-
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
�:�p�u!'����������@��=� -� id�w��!5HWw��@��.��n' k�j�����������-�|	��S #����[:k�o�h+�e�֖�^��%���m�7� ��@�a|�ꠑ������ߞ�o��1����6�=v ��՛�=�@lH�.�??��R\���|gs��U�wX��Hn/Ҋ��@�t��e�֖�^��%�F�vm�P�<�0��4�HNse��5�������n�m�����4��o���|�W�hm�̟�iD|X|��[���g�� �=db��\�|����s@w�`�7��=��H�{��*��3�����4��2�ݳ�0�0.������y`\��s�0.W����g����ɸ�k�*�e�h�� ���߱�~`���ӷZ=;�W.�",��7n\����r?,�H~B询�J�FyQ�F�EU�UTO��@�R"]�Iq�7��׮�j;Ȱm�������2l[F�ͬ@f �A��̊;�@����dض�4X��E���l��@�e�`�HVQ 3��(��$ͱ�+,':���D��|M*,�Q����L*��&y�`�;�6tX����˴�ò�DҾ���u�t.J?h�\�D��*<h�c[�A�y�S 3� �n>��"����`��8:���O h�׳�K�Qk���g�A���~ ��<��G�������? ν�      Q      x������ � �      S      x��<�r#Ǒg�+��a��~�FΌ���CJ�=
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
�[BNW��C1��[��5	�α�D�B,2/CMuo��3�:Po��X�F�tU(���T�@���j�T7�csn;��E��;��W����M=%�H����.h�7�-�����?�=D�\�vD�������rln�3Ϩ/�=[�)>,��p;衎���μ3޽8���B˨s5G��O��Ѧ+Q�O��pn#��y��*�]1���vV1ꇒ3����\? �VG#�*�]<#�r�i��*l9e�����sйcR��S"����) a!��Q�	�N�p}��|_OӚé��+a�G� ��fi6{�m��f"#[���p�"	�aDˉ��Ok�x7�b����-���n�έ��+��t���9N�w�˗&�SR`��c�O@J)%�9��ć(˓�����	���o���X�wF�����ɲ�;6��]�b�Ɵ̜�Hc1��.��S��4mHv���;)�Vo��D�5$�h�@qDl�;�$��p]�̑w�J\���3E��v���G({�}� �ԋ�>��c��1�\s'���^&�b�7�/�˰B5��p�k;�o�6��d��νQ�Qn p  ��.(	��z�on�t-G��yw�Z �t�<���e^���o���7�f��tܚ��������?��`���S�-_~��a�p�JR�R�4XЁ����.�6����c�9-����f��g�����	���l��{pr��*O�Z��.��s��c�9α���{���A^(y�?��bZG�퐬zY�Q(OO�}�+ae�CSK��ǪФx���~ԛ������-��Ƌ�LXc���It\;Tc}J�3Y6���K�}�>u�z�س��2M�c���1��fx���J�P]�`T\�D=�[�!<�mF�[����k��w,i�ښ�ȴ��H���-��w�I�_]��i�%�wW�ӗɯD��3(�Q~k"��*;��ژ&z7��O[�wP�⊌��t�����hwٲ �7��N����R�j�[	&GN��d?D�3j��B�����"�=�TS�]h�SH৚ـ�~e�:4�!$�0v;�Ѐ:ME�(��)ʰ���e�ʃ��YU=E�D�G�{�i>k��'�:�5����}��������ۿ�~����{
��M�����<��G��xh!�-;���F�ӛt<�x�&��ܤ㡃p�ޤ�;���+q��&��ࡇ�#_ 0_�Y���p���؊�右��x� ���&,�>��%,[�Y��|�p�j���yz�OX�|baMX`
L�#k[7�����k[�����h���侗�wb�KX!����c�b�v�Jؠxqz/J��N�EQ$�H��B��(��9	�E����S�7����:g�Bؠq�})a9��I�vr�Lp�N�_���G��Hw��Bn����'��sp�ʉ�bv��.B��#��@]�2�yh�"�?I�u���h����1���4����� `q��;��g@؈|���#������JL��}!�=�QZ��9����5V˫����c!�B��o��t���w^!��'����B�cS!o#����t��7�H��85�@��	������o'a�����=o����ǝA�;�Q����iE��s]����g�_�oVh$p�
E��?�NXqH%XYtg.��G�¹A42���I@�F����K�nW}<�a��z�_�.G��Ov)Q�1�,maPgd��!(`ab��d�f��| v]�w�b�B\�נ,Os�^~�Zﮚ�"`ZNu�C����U��d�P<z� �c��|s��Dq�wn�8�����G�/sm�1��5��X���"I�g��b1�7�(ͻV�������S�b�1�c���1:��C�
�j���w��P��i�X��9���r�s�J��pY+��I�OS���4��b��W':�w% ���������߷��;_z�V"S9�#�+�)���z����$z���/�92m�����.��ƫa�#�5حM��Զ��r-�� ��W�]����B��u�m��6ȿP�����P� ��2+@�.� <K�F�5����LZ��Q������X<����A��4f���{�I��؈Яrz���:�k��/�5M����̚g$�!�e����A���J��?AC������Ϟ�
�#����s���o������      V      x������ � �     