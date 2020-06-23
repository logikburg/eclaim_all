package hk.org.ha.eclaim.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EClaimLogger {
	
	private static final Logger logger = LoggerFactory.getLogger(EClaimLogger.class);
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message.replaceAll("\r", "").replaceAll("\n", ""));
		}
	}
	
	public static void debug(String message, Throwable t) {
		if (logger.isDebugEnabled()) {
			logger.debug(message.replaceAll("\r", "").replaceAll("\n", ""), t);
		}
	}
	
	public static void info(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message.replaceAll("\r", "").replaceAll("\n", ""));
		}
	}
	
	public static void info(String message, Throwable t) {
		if (logger.isInfoEnabled()) {
			logger.info(message.replaceAll("\r", "").replaceAll("\n", ""), t);
		}
	}
	
	public static void warn(String message) {		
		if (logger.isWarnEnabled()) {
			logger.warn(message.replaceAll("\r", "").replaceAll("\n", ""));
		}
	}
	
	public static void warn(String message, Throwable t) {
		if (logger.isWarnEnabled()) {
			logger.warn(message.replaceAll("\r", "").replaceAll("\n", ""), t);
		}
	}
	
	public static void error(String message) {
		logger.error(message.replaceAll("\r", "").replaceAll("\n", ""));
	}
	
	public static void error(String message, Throwable t) {
		logger.error(message.replaceAll("\r", "").replaceAll("\n", ""), t);
	}
	
}
