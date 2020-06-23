CREATE OR REPLACE PACKAGE Body xxec_project_util_pkg as

FUNCTION get_higher_rank (p_rank1 IN VARCHAR2,
                          p_rank2 IN VARCHAR2,
                          p_rank3 IN VARCHAR2 DEFAULT NULL,
                          p_rank4 IN VARCHAR2 DEFAULT NULL,
                          p_rank5 IN VARCHAR2 DEFAULT NULL)
    RETURN VARCHAR2 IS
BEGIN
                          RETURN p_rank1;
end;

FUNCTION get_grade_val(p_rank1 IN VARCHAR2,
                  CUR_DATE IN DATE,
                  STATUS IN VARCHAR2
                  ) return number is
BEGIN

RETURN 2;

end;

END xxec_project_util_pkg;
