create table if not exists corpora (

id int,
name varchar(200),
type varchar(200)
);

create table if not exists corpus (
corpora_id int,
document_id int
);

create table if not exists documents (
id int,
email varchar(200),
submitted_date date,
author varchar(200),
title varchar(200),
length int,
body_link varchar(200)
);

create table if not exists reports (
id int,
name varchar(200),
run_date timestamp
);

create table if not exists analyses (
id int,
corpora_id int,
document_id int,
report_id int,
analysis_name varchar(200),
score_name varchar(200),
score_value varchar(200)
);