create or replace view V_PROJ_REQ as
select proj.project_id, proj.project_ver_id, 
proj.project_name,proj.project_name_c,
proj.project_owner_id,proj.project_owner,
proj.project_preparer_id,proj.project_preparer,
proj.project_status_code,
proj.department_id,
0 as extension,
0 as invitation,
0 as tot_appl,
0 as os_appl,
0 as appr_work_hour,
0 as used_work_hour,
0 as avai_work_hour,
proj.from_date,proj.to_date
from xxec_project proj;