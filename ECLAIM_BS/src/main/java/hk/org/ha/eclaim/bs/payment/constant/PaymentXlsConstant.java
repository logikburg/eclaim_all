package hk.org.ha.eclaim.bs.payment.constant;

public class PaymentXlsConstant {
	
	public static final int COLUMN_INDEX_ASSIGN_NO = 0;
	public static final int COLUMN_INDEX_NAME = 1;
	public static final int COLUMN_INDEX_WORK_LOCATION = 2;
	public static final int COLUMN_INDEX_JOB = 3;
	public static final int COLUMN_INDEX_HOUR_TYPE = 4;

	public static final int COLUMN_INDEX_COA_INST = 5;
	public static final int COLUMN_INDEX_COA_FUND = COLUMN_INDEX_COA_INST + 1;
	public static final int COLUMN_INDEX_COA_SECTION = COLUMN_INDEX_COA_INST + 2;
	public static final int COLUMN_INDEX_COA_ANALYSTIC = COLUMN_INDEX_COA_INST + 3;
	public static final int COLUMN_INDEX_COA_TYPE = COLUMN_INDEX_COA_INST + 4;

	public static final int COLUMN_INDEX_REASON = COLUMN_INDEX_COA_TYPE + 1;
	public static final int COLUMN_INDEX_EARNED_MONTH = COLUMN_INDEX_COA_TYPE + 2;
	public static final int COLUMN_INDEX_MONTH_START_DAY = COLUMN_INDEX_COA_TYPE + 3;
	public static final int COLUMN_INDEX_TOTAL_HOURS = COLUMN_INDEX_MONTH_START_DAY + 32;

}
