-- Dumping database structure for bug_predictor
CREATE DATABASE IF NOT EXISTS `bug_predictor` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bug_predictor`;
 
 
CREATE TABLE IF NOT EXISTS `commit_filenames` (
  `sha_ts` int NOT NULL,
  `sha` varchar(40) NOT NULL,
  `filename` varchar(512) NOT NULL,
  KEY `filename_sha_ix` (`sha`)
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
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  KEY `jira_jira_ix` (`jira`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOAD DATA LOCAL INFILE 'files.csv' INTO TABLE commit_filenames FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (sha_ts, sha, filename);
LOAD DATA LOCAL INFILE 'commits.csv' INTO TABLE commit_jira FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (sha, jira);
LOAD DATA LOCAL INFILE 'jira.csv' INTO TABLE jira FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES (jira, priority, created, updated);

SET group_concat_max_len = 12000;
CREATE VIEW vw_score_normalized_time AS
SELECT 
 SUBSTRING_INDEX(cf.filename, '/', -1) "filename",
 SUM(1/(1+ EXP(12-12*(cf.sha_ts - cf.created_on)/(unix_timestamp(NOW()) -  cf.created_on)))) "score",
 group_concat((cf.sha_ts - cf.created_on)/(unix_timestamp(NOW()) -  cf.created_on) ORDER BY cf.sha_ts SEPARATOR ':') "x",
 group_concat(round(1/(1+ EXP(12-12*((cf.sha_ts - cf.created_on)/(unix_timestamp(NOW()) -  cf.created_on)))),4) ORDER BY cf.sha_ts SEPARATOR ':') "y",
 count(j.jira) "num_bugs"
FROM commit_filenames cf, commit_jira cj, jira j
WHERE j.jira = cj.jira AND cj.sha = cf.sha
GROUP BY SUBSTRING_INDEX(cf.filename, '/', -1)
ORDER BY 2 desc;

CREATE VIEW vw_score_timestamp AS
SELECT 
 SUBSTRING_INDEX(cf.filename, '/', -1) "filename",
 SUM(1/(1+ EXP(12-12*(cf.sha_ts - cf.created_on)/(unix_timestamp(NOW()) -  cf.created_on)))) "score",
 group_concat(cf.sha_ts ORDER BY cf.sha_ts SEPARATOR ':') "x",
 group_concat(round(1/(1+ EXP(12-12*((cf.sha_ts - cf.created_on)/(unix_timestamp(NOW()) -  cf.created_on)))),4) ORDER BY cf.sha_ts SEPARATOR ':') "y",
 count(j.jira) "num_bugs"
FROM commit_filenames cf, commit_jira cj, jira j
WHERE j.jira = cj.jira AND cj.sha = cf.sha
GROUP BY SUBSTRING_INDEX(cf.filename, '/', -1)
ORDER BY 2 desc;

CREATE VIEW vw_score AS
SELECT * FROM vw_score_timestamp;