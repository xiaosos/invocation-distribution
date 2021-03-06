
-- 存储每一个已配置的 Job 的详细信息
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储每一个已配置的 Job 的详细信息';



--  存储已配置的 Trigger 的信息
CREATE TABLE  IF NOT EXISTS QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
KEY `IDX_QRTZ_T_J` (SCHED_NAME,JOB_NAME,JOB_GROUP),
KEY `IDX_QRTZ_T_JG` (SCHED_NAME,JOB_GROUP) ,
KEY `IDX_QRTZ_T_C` (SCHED_NAME,CALENDAR_NAME),
KEY `IDX_QRTZ_T_G` (SCHED_NAME,TRIGGER_GROUP) ,
KEY `IDX_QRTZ_T_STATE` (SCHED_NAME,TRIGGER_STATE),
KEY `IDX_QRTZ_T_N_G_STATE` (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE) ,
KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (SCHED_NAME,NEXT_FIRE_TIME),
KEY `IDX_QRTZ_T_NFT_ST` (SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME) ,
KEY `IDX_QRTZ_T_NFT_MISFIRE` (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME) ,
KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE),
KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE) ,
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP) on delete cascade on update cascade )
ENGINE=InnoDB COMMENT='存储已配置的 Trigger 的信息';


-- 存储简单的 Trigger，包括重复次数，间隔，以及已触的次数
CREATE TABLE  IF NOT EXISTS QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) on delete cascade on update cascade )
ENGINE=InnoDB COMMENT='存储简单的 Trigger，包括重复次数，间隔，以及已触的次数';



-- 存储 Cron Trigger，包括 Cron 表达式和时区信息
CREATE TABLE  IF NOT EXISTS  QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) on delete cascade on update cascade )
ENGINE=InnoDB COMMENT='存储 Cron Trigger，包括 Cron 表达式和时区信息';



CREATE TABLE  IF NOT EXISTS  QRTZ_SIMPROP_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
STR_PROP_1 VARCHAR(512) NULL,
STR_PROP_2 VARCHAR(512) NULL,
STR_PROP_3 VARCHAR(512) NULL,
INT_PROP_1 INT NULL,
INT_PROP_2 INT NULL,
LONG_PROP_1 BIGINT NULL,
LONG_PROP_2 BIGINT NULL,
DEC_PROP_1 NUMERIC(13,4) NULL,
DEC_PROP_2 NUMERIC(13,4) NULL,
BOOL_PROP_1 VARCHAR(1) NULL,
BOOL_PROP_2 VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) on delete cascade on update cascade )
ENGINE=InnoDB;

-- Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，<span style="color:#800080;">JobStore</span> 并不知道如何存储实例的时候)
CREATE TABLE  IF NOT EXISTS  QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP )
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
 on delete cascade on update cascade)
ENGINE=InnoDB COMMENT='Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，<span style="color:#800080;">JobStore</span> 并不知道如何存储实例的时候)';

-- 以 Blob 类型存储 Quartz 的 Calendar 信息
CREATE TABLE  IF NOT EXISTS  QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(200) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB COMMENT='以 Blob 类型存储 Quartz 的 Calendar 信息';

-- 存储已暂停的 Trigger 组的信息
CREATE TABLE  IF NOT EXISTS  QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB COMMENT='存储已暂停的 Trigger 组的信息';


-- 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
CREATE TABLE  IF NOT EXISTS  QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID),
KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (SCHED_NAME,INSTANCE_NAME),
KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY),
KEY `IDX_QRTZ_FT_J_G` (SCHED_NAME,JOB_NAME,JOB_GROUP),
KEY `IDX_QRTZ_FT_JG` (SCHED_NAME,JOB_GROUP),
KEY `IDX_QRTZ_FT_T_G` (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
KEY `IDX_QRTZ_FT_TG` (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB COMMENT='存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息';


-- 存储少量的有关 Scheduler 的状态信息，和别的 Scheduler 实例(假如是用于一个集群中)
CREATE TABLE  IF NOT EXISTS  QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB COMMENT='存储少量的有关 Scheduler 的状态信息，和别的 Scheduler 实例(假如是用于一个集群中)';

-- 存储程序的悲观锁的信息(假如使用了悲观锁)
CREATE TABLE  IF NOT EXISTS  QRTZ_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB COMMENT='存储程序的悲观锁的信息(假如使用了悲观锁)';

delete from QRTZ_JOB_DETAILS;
delete from QRTZ_TRIGGERS;
delete from QRTZ_SIMPLE_TRIGGERS;
delete from QRTZ_CRON_TRIGGERS;
delete from QRTZ_SIMPROP_TRIGGERS;
delete from QRTZ_BLOB_TRIGGERS;
delete from QRTZ_CALENDARS;
delete from QRTZ_PAUSED_TRIGGER_GRPS;
delete from QRTZ_FIRED_TRIGGERS;
delete from QRTZ_SCHEDULER_STATE;
delete from QRTZ_LOCKS;