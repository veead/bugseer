CREATE DATABASE IF NOT EXISTS `bug_predictor`;
use bug_predictor;

create table commit_filenames
(sha varchar(40) not null,
filename varchar(512) not null);

create table commit_jira
(sha varchar(40) not null,
jira varchar(64) not null);

create table jira (
jira varchar(20) NOT NULL,
priority varchar(20) NOT NULL,
created datetime NOT NULL,
updated datetime NOT NULL
);

LOAD DATA LOCAL INFILE 'files.csv' INTO TABLE commit_filenames FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (sha, filename);
LOAD DATA LOCAL INFILE 'commits.csv' INTO TABLE commit_jira FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (sha, jira);
LOAD DATA LOCAL INFILE 'jira.csv' INTO TABLE jira FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES (jira, priority, created, updated);

