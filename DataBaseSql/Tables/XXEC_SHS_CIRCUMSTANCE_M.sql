CREATE TABLE XXEC_SHS_CIRCUMSTANCE_M (
    CIRCUMSTANCE_ID NUMBER(10) NOT NULL,
    CIRCUMSTANCE_CODE VARCHAR2(10),
    DISPLAY_SEQ NUMBER,
    DESCRIPTIONQ VARCHAR2(4000),
    MANPOWER_SHORTAGE VARCHAR2(1),
    Q_DELIVERABLE_QUIDE VARCHAR2(4000),
    Q_DELIVERABLE_EXAMPL VARCHAR2(4000),
    START_DATE DATE,
    END_DATE DATE,
    REC_STATE VARCHAR2(1 BYTE),
    CREATED_BY VARCHAR2(20 BYTE),
	  CREATED_ROLE_ID VARCHAR2(20 BYTE),
	  CREATED_DATE TIMESTAMP (6),
	  UPDATED_BY VARCHAR2(20 BYTE),
	  UPDATED_ROLE_ID VARCHAR2(20 BYTE),
  	UPDATED_DATE TIMESTAMP (6)
);

ALTER TABLE "XXEC_SHS_CIRCUMSTANCE_M" ADD CONSTRAINT "XXEC_SHS_CIRCUMSTANCE_M_PK" PRIMARY KEY ("CIRCUMSTANCE_ID") USING INDEX TABLESPACE "XXHA_TS_TX_IDX" ENABLE;
