CREATE TABLE "ECLAIM"."XXEC_WORKFLOW_HIST" 
   (	"WF_HIST_UID" NUMBER(10,0) NOT NULL, 
	"PROJECT_ID" NUMBER(10,0) NOT NULL, 
	"ACTION_BY" VARCHAR2(20) NOT NULL, 
	"ACTION_ROLE_ID" VARCHAR2(20) NOT NULL, 
	"ACTION_DATE" TIMESTAMP (6) NOT NULL, 
	"ACTION_TAKEN" VARCHAR2(20), 
	"SENT_TO_USER_ID" VARCHAR2(20), 
	"SENT_TO_ROLE_ID" VARCHAR2(20), 
	"REC_STATE" VARCHAR2(1), 
	"CREATED_BY" VARCHAR2(20), 
	"CREATED_ROLE_ID" VARCHAR2(20), 
	"CREATED_DATE" TIMESTAMP (6), 
	"UPDATED_BY" VARCHAR2(20), 
	"UPDATED_ROLE_ID" VARCHAR2(20), 
	"UPDATED_DATE" TIMESTAMP (6), 
	"PROJECT_VER_ID" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ; 
  
CREATE UNIQUE INDEX "ECLAIM"."RQ_WORKFLOW_HIST_PK" ON "ECLAIM"."XXEC_WORKFLOW_HIST" ("WF_HIST_UID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
  
  ALTER TABLE "ECLAIM"."XXEC_WORKFLOW_HIST" ADD CONSTRAINT "RQ_WORKFLOW_HIST_PK" PRIMARY KEY ("WF_HIST_UID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;