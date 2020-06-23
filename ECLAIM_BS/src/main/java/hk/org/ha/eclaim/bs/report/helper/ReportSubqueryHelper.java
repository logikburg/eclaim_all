package hk.org.ha.eclaim.bs.report.helper;

import java.util.ArrayList;
import java.util.List;

import hk.org.ha.eclaim.bs.security.po.DataAccessPo;

public class ReportSubqueryHelper {

	public static String getDataAccessScopeQuery(List<DataAccessPo> dataAccessList, String tableAlias) {
		
		if (dataAccessList == null || dataAccessList.isEmpty()) {
			return "";
		}

		// loop DataAccessScope
		List<StringBuilder> sqlBuilder = new ArrayList<StringBuilder>();
		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);

			List<String> queryClause = new ArrayList<String>();
			if (d.getClusterCode() != null) {
				queryClause.add(tableAlias + ".cluster_code = :cluster_code" + i);
			}
			if (d.getInstCode() != null) {
				queryClause.add(tableAlias + ".inst_code = :inst_code" + i);
			}
			if (d.getDeptCode() != null) {
				queryClause.add(tableAlias + ".dept_code = :dept_code" + i);
			}
			if (d.getStaffGroupCode() != null) {
				queryClause.add(tableAlias + ".staff_group_code = :staff_group_code" + i);
			}

			StringBuilder dataAccessSql = new StringBuilder();
			for (int j = 0; j < queryClause.size(); j++) {
				if (j == queryClause.size() - 1) {
					dataAccessSql.append(queryClause.get(j));
				} else {
					dataAccessSql.append(queryClause.get(j)).append(" and ");
				}
			}
			if (dataAccessSql.length() > 0) {
				dataAccessSql.append(" ) ");
				dataAccessSql.insert(0, " ( ");
			}

			sqlBuilder.add(dataAccessSql);
		}

		// build result sql of data access scope
		StringBuilder resultSql = new StringBuilder();
		for (int j = 0; j < sqlBuilder.size(); j++) {
			if (j == sqlBuilder.size() - 1) {
				resultSql.append(sqlBuilder.get(j));
			} else {
				resultSql.append(sqlBuilder.get(j)).append(" or ");
			}
		}
		if (resultSql.length() > 0) {
			resultSql.append(" ) ");
			resultSql.insert(0, " ( ");
		}

		return resultSql.toString();
	}
}
