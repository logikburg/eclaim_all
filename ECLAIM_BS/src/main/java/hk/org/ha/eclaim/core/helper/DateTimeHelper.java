package hk.org.ha.eclaim.core.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US);
	private static final DateTimeFormatter DATE_FORMATTER_FOR_FILENAME = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.US);
	private static final ZoneId SYS_DEFAULT_ZONE_ID = ZoneId.of("Asia/Hong_Kong");
	
	static {
		LocalTime systemLocalTime = LocalTime.now(SYS_DEFAULT_ZONE_ID);
		System.out.println("##### ZoneId: " + SYS_DEFAULT_ZONE_ID + ", Current Time: " + systemLocalTime + " #####");
	}
	
	public static String formatDateToString(Date date) {
		if (date == null) {
			return null;
		}
		
		if (date instanceof java.sql.Date) {
			LocalDate ldt = ((java.sql.Date)date).toLocalDate();
			return ldt.format(DATE_FORMATTER);
		}
		else {
			LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), SYS_DEFAULT_ZONE_ID);
			return ldt.format(DATE_FORMATTER);
		}
	}
	
	public static String formatDateToStringForFilename(Date date) {
		if (date == null) {
			return null;
		}
		
		if (date instanceof java.sql.Date) {
			LocalDate ldt = ((java.sql.Date)date).toLocalDate();
			return ldt.format(DATE_FORMATTER_FOR_FILENAME);
		}
		else {
			LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), SYS_DEFAULT_ZONE_ID);
			return ldt.format(DATE_FORMATTER_FOR_FILENAME);
		}
	}
	
	public static String formatDateTimeToString(Date date) {
		if (date == null) {
			return null;
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), SYS_DEFAULT_ZONE_ID);
		return ldt.format(DATE_TIME_FORMATTER);
	}
	
}
