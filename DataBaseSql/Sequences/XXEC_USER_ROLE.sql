DROP SEQUENCE XXECLAIM.XXEC_USER_ROLE_S ;

CREATE SEQUENCE XXECLAIM.XXEC_USER_ROLE_S
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 5
  NOORDER;


CREATE PUBLIC SYNONYM XXEC_USER_ROLE_S FOR XXECLAIM.XXEC_USER_ROLE_S;

