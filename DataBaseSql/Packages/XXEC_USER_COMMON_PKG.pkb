CREATE OR REPLACE PACKAGE Body Xxec_User_Common_Pkg
/**************************************************************************
REM Copyright (c) 2014 Oracle Corporation. All rights reserved.
REM ***********************************************************************
REM Source Code Control Hdr  : $Id: $
REM File name                : XXEC_USER_COMMON_PKG.pkb
REM Doc Ref(s)               : <ref to any important documents>
REM Project                  : Hong Kong Hospital Authority eClaim System Project
REM Description              : Common Function used for eClaim
REM
REM Change History Information
REM --------------------------
REM Version  Date         Author               Change Reference / Description
REM -------  -----------  -------------------  -------------------------------
REM DRAFT 1  12-10-2018   Desmond              Initial Draft Version
REM **************************************************************************/
IS
--
FUNCTION get_user_roles (p_user_name VARCHAR2)
  RETURN role_tbl
IS
  lv_user_name  VARCHAR2(100);
  CURSOR c_user_roles IS
  SELECT DISTINCT role_code
  FROM   xxec_user_role ur
  WHERE  user_name = lv_user_name
  AND    TRUNC(SYSDATE) BETWEEN start_date AND NVL(end_date,SYSDATE + 1)
  UNION
  SELECT 'PAYOFFICER'
  FROM   FND_USER_RESP_GROUPS_DIRECT FUR
         ,FND_USER FU
         , FND_RESPONSIBILITY_TL FRT
  WHERE FUR.USER_ID = FU.USER_ID
  AND FU.USER_NAME = lv_user_name
  AND FRT.RESPONSIBILITY_ID = FUR.RESPONSIBILITY_ID
  AND UPPER(FRT.RESPONSIBILITY_NAME) LIKE '%PAYROLL%OFFICER%'
  UNION
  SELECT 'PAYMGR'
  FROM   FND_USER_RESP_GROUPS_DIRECT FUR
         ,FND_USER FU
         , FND_RESPONSIBILITY_TL FRT
  WHERE FUR.USER_ID = FU.USER_ID
  AND FU.USER_NAME = lv_user_name
  AND FRT.RESPONSIBILITY_ID = FUR.RESPONSIBILITY_ID
  AND UPPER(FRT.RESPONSIBILITY_NAME) LIKE '%PAYROLL%MANAGER%'
  ;
  ln_count NUMBER:=0;
  lv_result role_tbl;
  lv_prj_preparer_flag VARCHAR2(1) := 'N';
  lv_last_role VARCHAR2(10);
BEGIN
  lv_user_name := p_user_name;
  --
  FOR r_user_role IN c_user_roles
  LOOP
     ln_count := ln_count + 1;
     --
--      IF r_user_role.role_code = gv_preparer_role_code THEN
--         lv_prj_preparer_flag := 'Y';
--      ELSE
--         lv_last_role := r_user_role.role_code;
--      END IF;
  END LOOP;

  --  1) If no user role is found
  --     Grant "Project Preparer" role to user
  CASE
  WHEN ln_count=0 THEN
       DBMS_OUTPUT.PUT_LINE('count = 0');
       add_user_role (lv_user_name, gv_preparer_role_code,TRUNC(SYSDATE));
--       lv_result := gv_preparer_role_code;
  WHEN ln_count>1 THEN
       remove_user_role (lv_user_name
                       , gv_preparer_role_code);
--        lv_result := lv_last_role;
--   WHEN ln_count=2 THEN
--        IF lv_prj_preparer_flag = 'Y' THEN
--           lv_result := lv_last_role;
--        ELSE
--           NULL;
--        END IF;
  ELSE
        NULL;
  END CASE;

  OPEN c_user_roles;
  FETCH c_user_roles BULK COLLECT INTO lv_result;
  DBMS_OUTPUT.PUT_LINE('2nd open: '|| lv_result.COUNT);
  CLOSE c_user_roles;

  RETURN lv_result;
END;

PROCEDURE add_user_role (p_user_name VARCHAR2
                       , p_role_code VARCHAR2
                       , p_start_date DATE
                       , p_end_date DATE DEFAULT NULL
                       , p_project_id NUMBER DEFAULT NULL
                       , p_job_group_id NUMBER DEFAULT NULL
                       , p_cluster_code VARCHAR2 DEFAULT NULL
                       , p_department_id NUMBER DEFAULT NULL
                       , p_ward VARCHAR2 DEFAULT NULL
                       )
IS

BEGIN
    DBMS_OUTPUT.PUT_LINE('insert role...');
    INSERT INTO xxec_user_role (USER_ROLE_ID, user_name, role_code, start_date, end_date, project_id, job_group_id, cluster_code, department_id, ward)
    VALUES (XXEC_USER_ROLE_S.NEXTVAL,p_user_name, p_role_code, p_start_date, p_end_date, p_project_id, p_job_group_id, p_cluster_code, p_department_id, p_ward);
END;

PROCEDURE remove_user_role (p_user_name VARCHAR2
                       , p_role_code VARCHAR2
                  --     , p_start_date DATE
                  --     , p_end_date DATE DEFAULT NULL
                       , p_project_id NUMBER DEFAULT NULL
                       , p_job_group_id NUMBER DEFAULT NULL
                       , p_cluster_code VARCHAR2 DEFAULT NULL
                       , p_department_id NUMBER DEFAULT NULL
                       , p_ward VARCHAR2 DEFAULT NULL
                       )
IS 
BEGIN
     DELETE FROM xxec_user_role
     WHERE user_name = p_user_name
     AND   role_code = p_role_code;
END;

--
-- PROCEDURE set_user_context (p_user_name VARCHAR2
--                        , p_user_role VARCHAR2
--                        , p_role_cluster cluster_tbl
--                        )
-- IS
--
-- BEGIN
--     gv_user_name := p_user_name;
--     gv_user_role := p_user_role;
--     gv_role_cluster := p_role_cluster;
-- END;

--
END  Xxec_User_Common_Pkg;
/
SHOW ERROR;
