package com.meadowhawk.homepi.util;

/**
 * Custome String Utils
 * 
 * @author Lee.Clarke
 */
public class StringUtil {
	private StringUtil() {
		// Utility class all static
	}

	/**
	 * Returns true if a string is null or if the trimmed value has a length of 0.
	 * 
	 * @param value - string to test
	 * @return - true it null or empty
	 */
	public static boolean isNullOrEmpty(String value) {
		boolean status = false;
		if (value == null) {
			status = true;
		} else if (value.trim().length() == 0) {
			status = true;
		}

		return status;
	}
	
	/**
	 * Returns a null if the string is empty 
	 * 
	 * @param value - string to test
	 * @return value - string, null if empty
	 */
	public static String ignoreNullOrEmpty(String value) {		
		if (isNullOrEmpty(value)) {
			value = null;
		} 
		return value;
	}
	
	/**
	 * Returns uppercase string if a string is not null or if the trimmed value has a length of > 0.
	 * 
	 * @param value - string to test
	 * @return value - string in upperCase
	 */
	public static String toUpperCase(String value) {
		
		if (!isNullOrEmpty(value)) {
			value = value.toUpperCase();
		} 

		return value;
	}

	/**
	 * Safely parses to an Integer or results in a null value if not parsable. This is intended for cases where the
	 * value can be ignored or if a null value will trigger an error later in validation.
	 * 
	 * @param string - string value
	 * @return Integer or null if string isn't valid
	 */
	public static Integer safeParseToInteger(String string) {
		Integer newInt = null;
		try {
			if (!StringUtil.isNullOrEmpty(string)) {
				newInt = Integer.parseInt(string);
			}
		} catch (Exception e) {
			newInt = null;
		}

		return newInt;
	}
	
	/**
	 * Safely parses to an Long or results in a null value if not parsable. This is intended for cases where the
	 * value can be ignored or if a null value will trigger an error later in validation.
	 * 
	 * @param string - string value
	 * @return Long or null if string isn't valid
	 */
	public static Long safeParseToLong(String string) {
		Long newInt = null;
		try {
			if (!StringUtil.isNullOrEmpty(string)) {
				newInt = Long.parseLong(string);
			}
		} catch (Exception e) {
			newInt = null;
		}

		return newInt;
	}

	/**
	 * Ensures that the object is not null before trying to trim a String value.
	 * @param string - string to trim
	 * @return - trimmed string or null
	 */
	public static String safeTrim(String string) {
		if(string != null){
			return string.trim();
		}
		return null;
	}
}
