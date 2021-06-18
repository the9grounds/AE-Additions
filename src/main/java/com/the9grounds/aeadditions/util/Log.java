package com.the9grounds.aeadditions.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.the9grounds.aeadditions.Constants;

public class Log {

	public static void trace(String message, Object... params) {
		log(Level.TRACE, message, params);
	}

	public static void debug(String message, Object... params) {
		log(Level.DEBUG, message, params);
	}

	public static void info(String message, Object... params) {
		log(Level.INFO, message, params);
	}

	public static void warning(String message, Object... params) {
		log(Level.WARN, message, params);
	}

	public static void error(String message, Object... params) {
		log(Level.ERROR, message, params);
	}

	private static void log(Level logLevel, String message, Object... params) {
		LogManager.getLogger(Constants.MOD_ID).log(logLevel, message, params);
	}

	public static void fatalError(String offendingItem, Throwable exception) {
		LogManager.getLogger().error("AE Additions severe error - could not create AE2 Part from ItemStack! This should not happen!\n"
				+ "[AE Additions SEVERE] Contact MasterYodA with the following stack trace.\n"
				+ "[AE Additions SEVERE] Offending item: '%s'", offendingItem, exception);
	}

}
