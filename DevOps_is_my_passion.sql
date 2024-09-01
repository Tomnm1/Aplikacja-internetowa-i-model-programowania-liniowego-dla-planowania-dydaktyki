PGDMP  1                    |            inz    16.3 (Debian 16.3-1.pgdg120+1)    16.3 5    c           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            d           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            e           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            f           1262    16388    inz    DATABASE     n   CREATE DATABASE inz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE inz;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            g           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    4            �            1259    16420    budynki    TABLE     ;   CREATE TABLE public.budynki (
    budynek text NOT NULL
);
    DROP TABLE public.budynki;
       public         heap    postgres    false    4            �            1259    16433    formy przedmiotow    TABLE     �   CREATE TABLE public."formy przedmiotow" (
    id integer NOT NULL,
    id_przedmiotu integer NOT NULL,
    liczba_godzin integer NOT NULL,
    typ text NOT NULL,
    max_osob_w_grupie integer
);
 '   DROP TABLE public."formy przedmiotow";
       public         heap    postgres    false    4            �            1259    16432    formy przedmiotow_id_seq    SEQUENCE     �   ALTER TABLE public."formy przedmiotow" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."formy przedmiotow_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    220            �            1259    16476    grupy    TABLE     �   CREATE TABLE public.grupy (
    id integer NOT NULL,
    numer integer NOT NULL,
    liczba_studentow integer NOT NULL,
    kierunek integer NOT NULL
);
    DROP TABLE public.grupy;
       public         heap    postgres    false    4            �            1259    16475    grupy_id_seq    SEQUENCE     �   ALTER TABLE public.grupy ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.grupy_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    227            �            1259    16456    kierunki    TABLE     �   CREATE TABLE public.kierunki (
    id integer NOT NULL,
    kierunek text NOT NULL,
    specjalizacja text,
    semestr integer NOT NULL,
    liczba_studentow integer NOT NULL
);
    DROP TABLE public.kierunki;
       public         heap    postgres    false    4            �            1259    16455    kierunki_id_seq    SEQUENCE     �   ALTER TABLE public.kierunki ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.kierunki_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    223            �            1259    16396 
   pracownicy    TABLE     �   CREATE TABLE public.pracownicy (
    id integer NOT NULL,
    imie text NOT NULL,
    nazwisko text NOT NULL,
    stopien text,
    preferencje json
);
    DROP TABLE public.pracownicy;
       public         heap    postgres    false    4            �            1259    16473    pracownicy_id_seq    SEQUENCE     �   ALTER TABLE public.pracownicy ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pracownicy_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    216            �            1259    16440    prowadzacy_przedmioty    TABLE     �   CREATE TABLE public.prowadzacy_przedmioty (
    przedmiot integer NOT NULL,
    prowadzacy integer NOT NULL,
    liczba_grup integer
);
 )   DROP TABLE public.prowadzacy_przedmioty;
       public         heap    postgres    false    4            �            1259    16486    przedmiot_grupa    TABLE     d   CREATE TABLE public.przedmiot_grupa (
    grupa integer NOT NULL,
    przedmiot integer NOT NULL
);
 #   DROP TABLE public.przedmiot_grupa;
       public         heap    postgres    false    4            �            1259    16403 
   przedmioty    TABLE     �   CREATE TABLE public.przedmioty (
    id integer NOT NULL,
    nazwa text NOT NULL,
    jezyk text,
    egzamin boolean NOT NULL,
    obieralny boolean NOT NULL,
    planowany boolean DEFAULT true NOT NULL,
    kierunek integer NOT NULL
);
    DROP TABLE public.przedmioty;
       public         heap    postgres    false    4            �            1259    16474    przedmioty_id_seq    SEQUENCE     �   ALTER TABLE public.przedmioty ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.przedmioty_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    4    217            �            1259    16389    sale    TABLE     �   CREATE TABLE public.sale (
    numer integer NOT NULL,
    budynek text NOT NULL,
    pietro smallint,
    liczba_miejsc integer,
    typ text,
    opiekun integer,
    wyposazenie json
);
    DROP TABLE public.sale;
       public         heap    postgres    false    4            V          0    16420    budynki 
   TABLE DATA           *   COPY public.budynki (budynek) FROM stdin;
    public          postgres    false    218   �>       X          0    16433    formy przedmiotow 
   TABLE DATA           g   COPY public."formy przedmiotow" (id, id_przedmiotu, liczba_godzin, typ, max_osob_w_grupie) FROM stdin;
    public          postgres    false    220   �>       _          0    16476    grupy 
   TABLE DATA           F   COPY public.grupy (id, numer, liczba_studentow, kierunek) FROM stdin;
    public          postgres    false    227   �>       [          0    16456    kierunki 
   TABLE DATA           Z   COPY public.kierunki (id, kierunek, specjalizacja, semestr, liczba_studentow) FROM stdin;
    public          postgres    false    223   ?       T          0    16396 
   pracownicy 
   TABLE DATA           N   COPY public.pracownicy (id, imie, nazwisko, stopien, preferencje) FROM stdin;
    public          postgres    false    216   F?       Y          0    16440    prowadzacy_przedmioty 
   TABLE DATA           S   COPY public.prowadzacy_przedmioty (przedmiot, prowadzacy, liczba_grup) FROM stdin;
    public          postgres    false    221   �?       `          0    16486    przedmiot_grupa 
   TABLE DATA           ;   COPY public.przedmiot_grupa (grupa, przedmiot) FROM stdin;
    public          postgres    false    228   �?       U          0    16403 
   przedmioty 
   TABLE DATA           _   COPY public.przedmioty (id, nazwa, jezyk, egzamin, obieralny, planowany, kierunek) FROM stdin;
    public          postgres    false    217   �?       S          0    16389    sale 
   TABLE DATA           `   COPY public.sale (numer, budynek, pietro, liczba_miejsc, typ, opiekun, wyposazenie) FROM stdin;
    public          postgres    false    215    @       h           0    0    formy przedmiotow_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public."formy przedmiotow_id_seq"', 1, false);
          public          postgres    false    219            i           0    0    grupy_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.grupy_id_seq', 1, false);
          public          postgres    false    226            j           0    0    kierunki_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.kierunki_id_seq', 1, true);
          public          postgres    false    222            k           0    0    pracownicy_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.pracownicy_id_seq', 1, false);
          public          postgres    false    224            l           0    0    przedmioty_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.przedmioty_id_seq', 1, false);
          public          postgres    false    225            �           2606    16426    budynki budynki_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.budynki
    ADD CONSTRAINT budynki_pkey PRIMARY KEY (budynek);
 >   ALTER TABLE ONLY public.budynki DROP CONSTRAINT budynki_pkey;
       public            postgres    false    218            �           2606    16439 (   formy przedmiotow formy przedmiotow_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public."formy przedmiotow"
    ADD CONSTRAINT "formy przedmiotow_pkey" PRIMARY KEY (id);
 V   ALTER TABLE ONLY public."formy przedmiotow" DROP CONSTRAINT "formy przedmiotow_pkey";
       public            postgres    false    220            �           2606    16480    grupy grupy_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.grupy
    ADD CONSTRAINT grupy_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.grupy DROP CONSTRAINT grupy_pkey;
       public            postgres    false    227            �           2606    16462    kierunki kierunki_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.kierunki
    ADD CONSTRAINT kierunki_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.kierunki DROP CONSTRAINT kierunki_pkey;
       public            postgres    false    223            �           2606    16402    pracownicy prowadzacy_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.pracownicy
    ADD CONSTRAINT prowadzacy_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.pracownicy DROP CONSTRAINT prowadzacy_pkey;
       public            postgres    false    216            �           2606    16444 0   prowadzacy_przedmioty prowadzacy_przedmioty_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.prowadzacy_przedmioty
    ADD CONSTRAINT prowadzacy_przedmioty_pkey PRIMARY KEY (przedmiot, prowadzacy);
 Z   ALTER TABLE ONLY public.prowadzacy_przedmioty DROP CONSTRAINT prowadzacy_przedmioty_pkey;
       public            postgres    false    221    221            �           2606    16490 $   przedmiot_grupa przedmiot_grupa_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.przedmiot_grupa
    ADD CONSTRAINT przedmiot_grupa_pkey PRIMARY KEY (grupa, przedmiot);
 N   ALTER TABLE ONLY public.przedmiot_grupa DROP CONSTRAINT przedmiot_grupa_pkey;
       public            postgres    false    228    228            �           2606    16409    przedmioty przedmioty_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.przedmioty
    ADD CONSTRAINT przedmioty_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.przedmioty DROP CONSTRAINT przedmioty_pkey;
       public            postgres    false    217            �           2606    16395    sale sale_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (numer, budynek);
 8   ALTER TABLE ONLY public.sale DROP CONSTRAINT sale_pkey;
       public            postgres    false    215    215            �           1259    16415    fki_A    INDEX     ;   CREATE INDEX "fki_A" ON public.sale USING btree (opiekun);
    DROP INDEX public."fki_A";
       public            postgres    false    215            �           2606    16468 6   formy przedmiotow formy przedmiotow_id_przedmiotu_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public."formy przedmiotow"
    ADD CONSTRAINT "formy przedmiotow_id_przedmiotu_fkey" FOREIGN KEY (id_przedmiotu) REFERENCES public.przedmioty(id) NOT VALID;
 d   ALTER TABLE ONLY public."formy przedmiotow" DROP CONSTRAINT "formy przedmiotow_id_przedmiotu_fkey";
       public          postgres    false    220    217    3246            �           2606    16481    grupy grupy_kierunek_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.grupy
    ADD CONSTRAINT grupy_kierunek_fkey FOREIGN KEY (kierunek) REFERENCES public.kierunki(id) NOT VALID;
 C   ALTER TABLE ONLY public.grupy DROP CONSTRAINT grupy_kierunek_fkey;
       public          postgres    false    3254    227    223            �           2606    16450 ;   prowadzacy_przedmioty prowadzacy_przedmioty_prowadzacy_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prowadzacy_przedmioty
    ADD CONSTRAINT prowadzacy_przedmioty_prowadzacy_fkey FOREIGN KEY (prowadzacy) REFERENCES public.pracownicy(id);
 e   ALTER TABLE ONLY public.prowadzacy_przedmioty DROP CONSTRAINT prowadzacy_przedmioty_prowadzacy_fkey;
       public          postgres    false    3244    216    221            �           2606    16445 :   prowadzacy_przedmioty prowadzacy_przedmioty_przedmiot_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prowadzacy_przedmioty
    ADD CONSTRAINT prowadzacy_przedmioty_przedmiot_fkey FOREIGN KEY (przedmiot) REFERENCES public."formy przedmiotow"(id);
 d   ALTER TABLE ONLY public.prowadzacy_przedmioty DROP CONSTRAINT prowadzacy_przedmioty_przedmiot_fkey;
       public          postgres    false    221    3250    220            �           2606    16491 *   przedmiot_grupa przedmiot_grupa_grupa_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.przedmiot_grupa
    ADD CONSTRAINT przedmiot_grupa_grupa_fkey FOREIGN KEY (grupa) REFERENCES public.grupy(id);
 T   ALTER TABLE ONLY public.przedmiot_grupa DROP CONSTRAINT przedmiot_grupa_grupa_fkey;
       public          postgres    false    228    3256    227            �           2606    16496 .   przedmiot_grupa przedmiot_grupa_przedmiot_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.przedmiot_grupa
    ADD CONSTRAINT przedmiot_grupa_przedmiot_fkey FOREIGN KEY (przedmiot) REFERENCES public.przedmioty(id);
 X   ALTER TABLE ONLY public.przedmiot_grupa DROP CONSTRAINT przedmiot_grupa_przedmiot_fkey;
       public          postgres    false    217    228    3246            �           2606    16463 #   przedmioty przedmioty_kierunek_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.przedmioty
    ADD CONSTRAINT przedmioty_kierunek_fkey FOREIGN KEY (kierunek) REFERENCES public.kierunki(id) NOT VALID;
 M   ALTER TABLE ONLY public.przedmioty DROP CONSTRAINT przedmioty_kierunek_fkey;
       public          postgres    false    217    223    3254            �           2606    16427    sale sale_budynek_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_budynek_fkey FOREIGN KEY (budynek) REFERENCES public.budynki(budynek) NOT VALID;
 @   ALTER TABLE ONLY public.sale DROP CONSTRAINT sale_budynek_fkey;
       public          postgres    false    218    3248    215            �           2606    16410    sale sale_opiekun_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_opiekun_fkey FOREIGN KEY (opiekun) REFERENCES public.pracownicy(id);
 @   ALTER TABLE ONLY public.sale DROP CONSTRAINT sale_opiekun_fkey;
       public          postgres    false    215    3244    216            V      x�s����� }M      X      x������ � �      _      x������ � �      [   $   x�3���K�/�M,��N���4�420������ {�      T   /   x�3��JLN��t:ڔxtOjyfrgAQ~Zjq~QLg�W� "�W      Y      x������ � �      `      x������ � �      U   1   x�3��M,I�M,��NTp�,�.J-�K����,�LbC.#�Jb���� �d&      S   3   x�3�t�4�461�,��>ڔ��_����D\�Y#���xec���� �     