DROP PUBLIC SYNONYM XXEC_USER_ROLE;

DROP TABLE XXECLAIM.XXEC_USER_ROLE;

CREATE TABLE XXECLAIM.XXEC_USER_ROLE (
    USER_ROLE_ID NUMBER NOT NULL,
	USER_NAME VARCHAR2(50)  NOT NULL,
    PROJECT_ID INTEGER  NULL,
    JOB_GROUP_ID INTEGER  NULL,
    CLUSTER_CODE VARCHAR2(4)  NULL,
    DEPARTMENT_ID INTEGER  NULL,
    WARD VARCHAR2(25)  NULL,
    ROLE_CODE VARCHAR2(10)  NOT NULL,
    START_DATE DATE  NOT NULL,
    END_DATE DATE  NULL
) ;

CREATE PUBLIC SYNONYM XXEC_USER_ROLE FOR XXECLAIM.XXEC_USER_ROLE;
