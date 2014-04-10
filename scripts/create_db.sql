-- Dumping database structure for bugseer
CREATE DATABASE IF NOT EXISTS `bugseer` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bugseer`;
 
 
CREATE TABLE IF NOT EXISTS `commit_filenames` (
  `sha_ts` int NOT NULL,
  `sha` varchar(40) NOT NULL,
  `filename` varchar(512) NOT NULL,
  `created_ts` int NOT NULL,
  KEY `filename_sha_ix` (`sha`),
  KEY `filename_sha_ts_ix` (`sha_ts`),
  KEY `filename_filename_ix` (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
CREATE TABLE IF NOT EXISTS `commit_jira` (
  `sha` varchar(40) NOT NULL,
  `jira` varchar(64) NOT NULL,
  KEY `jira_jira_ix` (`jira`),
  KEY `jira_sha_ix` (`sha`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 
CREATE TABLE IF NOT EXISTS `jira` (
  `jira` varchar(20) NOT NULL,
  `priority` varchar(20) NOT NULL,
  `updated` datetime NOT NULL,
  KEY `jira_jira_ix` (`jira`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `weights` (
	`a` INT(10) NOT NULL,
	`b` INT(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

TRUNCATE TABLE commit_filenames;
TRUNCATE TABLE commit_jira;
TRUNCATE TABLE jira;
TRUNCATE TABLE weights;

LOAD DATA LOCAL INFILE 'files.csv' INTO TABLE commit_filenames FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (sha_ts, sha, filename);
LOAD DATA LOCAL INFILE 'commits.csv' INTO TABLE commit_jira FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (sha, jira);
LOAD DATA LOCAL INFILE 'jira.csv' INTO TABLE jira FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES (jira, priority, updated);

update commit_filenames cf inner join (select filename, min(sha_ts) ts from commit_filenames group by filename) cf1 on cf1.filename = cf.filename set cf.created_ts = cf1.ts;
insert into weights values( 12,12 );

SET GLOBAL group_concat_max_len = 12000;
CREATE OR REPLACE VIEW vw_score_normalized_time AS
SELECT 
 SUBSTRING_INDEX(cf.filename, '/', -1) "filename",
 SUM(1/(1+ EXP((select a from weights)-(select b from weights)*(cf.sha_ts - cf.created_ts)/(unix_timestamp(NOW()) -  cf.created_ts)))) "score",
 group_concat((cf.sha_ts - cf.created_ts)/(unix_timestamp(NOW()) -  cf.created_ts) ORDER BY cf.sha_ts SEPARATOR ':') "x",
 group_concat(round(1/(1+ EXP((select a from weights)-(select b from weights)*((cf.sha_ts - cf.created_ts)/(unix_timestamp(NOW()) -  cf.created_ts)))),4) ORDER BY cf.sha_ts SEPARATOR ':') "y",
 group_concat(cj.jira ORDER BY cf.sha_ts SEPARATOR ':') "jira",
 count(j.jira) "num_bugs"
FROM commit_filenames cf, commit_jira cj, jira j
WHERE j.jira = cj.jira AND cj.sha = cf.sha
GROUP BY SUBSTRING_INDEX(cf.filename, '/', -1)
ORDER BY 2 desc;

CREATE OR REPLACE VIEW vw_score_timestamp AS
SELECT 
 SUBSTRING_INDEX(cf.filename, '/', -1) "filename",
 SUM(1/(1+ EXP((select a from weights)-(select b from weights)*(cf.sha_ts - cf.created_ts)/(unix_timestamp(NOW()) -  cf.created_ts)))) "score",
 group_concat(cf.sha_ts ORDER BY cf.sha_ts SEPARATOR ':') "x",
 group_concat(round(1/(1+ EXP((select a from weights)-(select b from weights)*((cf.sha_ts - cf.created_ts)/(unix_timestamp(NOW()) -  cf.created_ts)))),4) ORDER BY cf.sha_ts SEPARATOR ':') "y",
 group_concat(cj.jira ORDER BY cf.sha_ts SEPARATOR ':') "jira",
 count(j.jira) "num_bugs"
FROM commit_filenames cf, commit_jira cj, jira j
WHERE j.jira = cj.jira AND cj.sha = cf.sha
GROUP BY SUBSTRING_INDEX(cf.filename, '/', -1)
ORDER BY 2 desc;

CREATE OR REPLACE VIEW vw_score AS
SELECT * FROM vw_score_timestamp;
