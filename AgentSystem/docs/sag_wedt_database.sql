--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.9
-- Dumped by pg_dump version 9.5.9

-- Started on 2018-01-07 20:45:16 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2143 (class 1262 OID 35521)
-- Name: sag_wedt; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE sag_wedt WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


\connect sag_wedt

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12395)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2145 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 181 (class 1259 OID 35522)
-- Name: article_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 182 (class 1259 OID 35524)
-- Name: Article; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Article" (
    id bigint DEFAULT nextval('article_id_seq'::regclass) NOT NULL,
    data_downloaded text,
    tags text,
    text_of_article text,
    vector text,
    site_from text,
    url text,
    title text
);


--
-- TOC entry 2138 (class 0 OID 35524)
-- Dependencies: 182
-- Data for Name: Article; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO "Article" (id, data_downloaded, tags, text_of_article, vector, site_from, url, title) VALUES (1, 'a', 'b', 'c', 'd', 'e', 'f', 'g');


--
-- TOC entry 2146 (class 0 OID 0)
-- Dependencies: 181
-- Name: article_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('article_id_seq', 1, false);


--
-- TOC entry 2022 (class 2606 OID 35532)
-- Name: Article_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Article"
    ADD CONSTRAINT "Article_pk" PRIMARY KEY (id);


-- Completed on 2018-01-07 20:45:16 CET

--
-- PostgreSQL database dump complete
--

