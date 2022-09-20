CREATE TABLE company (
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person (
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

SELECT p.name AS person_name, c.name AS company
FROM person p
LEFT JOIN company c
ON p.company_id = c.id WHERE c.id != 5;

WITH
employes_number AS (
SELECT company_id, count(*) FROM person GROUP BY company_id)

SELECT c.name AS company_name, e.count AS employes_number
FROM company AS c
LEFT JOIN employes_number AS e
ON c.id = e.company_id
WHERE e.count = (SELECT MAX(count) FROM employes_number);