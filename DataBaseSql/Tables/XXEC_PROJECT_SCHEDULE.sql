CREATE TABLE XXEC_PROJECT_SCHEDULE (
  PROJECT_ID NUMBER(10) NOT NULL,
  PROJECT_VER_ID NUMBER(10) NOT NULL,
  PROJECT_SCHEDULE_ID NUMBER(10) NOT NULL,
  PATTERN_CODE VARCHAR2(5),
  SCHEDULE_DATE TIMESTAMP(6),
  START_TIME VARCHAR2(5),
  END_TIME VARCHAR2(5),
  REC_STATE VARCHAR2(1 BYTE),
  CREATED_BY VARCHAR2(20 BYTE),
  CREATED_ROLE_ID VARCHAR2(20 BYTE),
  CREATED_DATE TIMESTAMP (6),
  UPDATED_BY VARCHAR2(20 BYTE),
  UPDATED_ROLE_ID VARCHAR2(20 BYTE),
  UPDATED_DATE TIMESTAMP (6)
);

ALTER TABLE "XXEC_PROJECT_SCHEDULE" ADD CONSTRAINT "XXEC_PROJECT_SCHEDULE_PK" PRIMARY KEY ("PROJECT_SCHEDULE_ID") USING INDEX TABLESPACE "XXHA_TS_TX_IDX" ENABLE;
