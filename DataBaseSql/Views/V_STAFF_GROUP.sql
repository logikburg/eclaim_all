create or replace view v_staff_group as
SELECT   flex_value Staff_group

FROM     fnd_flex_values_vl v, FND_FLEX_VALUE_SETS vs

WHERE    v.flex_value_set_id = vs.flex_value_set_id

AND      flex_value_set_name = 'XXPAY_STAFF_GROUP'

ORDER BY flex_value;
