CREATE TABLE `SUBJECT_ACCESS_REQUIREMENT` (
  	`SUBJECT_ID` bigint(20)  NOT NULL,
	`SUBJECT_TYPE` ENUM('ENTITY', 'EVALUATION') NOT NULL,
    `REQUIREMENT_ID` bigint(20) NOT NULL,
  	PRIMARY KEY (`SUBJECT_ID`, `SUBJECT_TYPE`, `REQUIREMENT_ID`),
    CONSTRAINT `SUBJECT_ACCESS_REQUIREMENT_REQUIREMENT_ID_FK` FOREIGN KEY (`REQUIREMENT_ID`) REFERENCES `ACCESS_REQUIREMENT` (`ID`) ON DELETE CASCADE
)