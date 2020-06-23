DROP TYPE cluster_tbl;

CREATE TYPE cluster_tbl IS TABLE OF VARCHAR2(10);

DROP TYPE role_tbl;

CREATE TYPE role_tbl IS TABLE OF VARCHAR2(10);

CREATE OR REPLACE PACKAGE Xxec_User_Common_Pkg
/**************************************************************************
REM Copyright (c) 2014 Oracle Corporation. All rights reserved.
REM ***********************************************************************
REM Source Code Control Hdr  : $Id: $
REM File name                : XXEC_USER_COMMON_PKG.pks
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
-- logon user profile
gv_person_id NUMBER;
gv_user_name VARCHAR2(50);
gv_user_role VARCHAR2(10); 
gv_user_org_id NUMBER;
gv_user_cluster VARCHAR2(10); -- user's mother cluster
gv_role_cluster cluster_tbl; -- enaible multi cluster role
gv_user_hospital_code VARCHAR2(10);
-- constant
gv_preparer_role_code VARCHAR2(10):='SHSPRJPRE';

FUNCTION get_user_roles (p_user_name VARCHAR2 ) RETURN  role_tbl;

PROCEDURE add_user_role (p_user_name VARCHAR2
                       , p_role_code VARCHAR2
                       , p_start_date DATE
                       , p_end_date DATE DEFAULT NULL
                       , p_project_id NUMBER DEFAULT NULL
                       , p_job_group_id NUMBER DEFAULT NULL
                       , p_cluster_code VARCHAR2 DEFAULT NULL
                       , p_department_id NUMBER DEFAULT NULL
                       , p_ward VARCHAR2 DEFAULT NULL                       
                       );

PROCEDURE remove_user_role (p_user_name VARCHAR2
                       , p_role_code VARCHAR2
                  --     , p_start_date DATE
                  --     , p_end_date DATE DEFAULT NULL
                       , p_project_id NUMBER DEFAULT NULL
                       , p_job_group_id NUMBER DEFAULT NULL
                       , p_cluster_code VARCHAR2 DEFAULT NULL
                       , p_department_id NUMBER DEFAULT NULL
                       , p_ward VARCHAR2 DEFAULT NULL                       
                       );  

                       
END Xxec_User_Common_Pkg;
/
show err;
