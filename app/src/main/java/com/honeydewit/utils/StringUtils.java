package com.honeydewit.utils;


public class StringUtils {
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean isPositiveInteger(String s) {
	    try { 
	       Integer number =  Integer.parseInt(s); 
	       return number >0;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	}
	public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	       
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	public static boolean isPositiveDouble(String s) {
	    try { 
	        double d = Double.parseDouble(s);
	        return d > 0;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	}
	/** Checks if a string is empty or null **/
	public static boolean isEmpty(String string) {
		return null == string || string.trim().isEmpty();
	}
	
	public static Integer lastCharIndexOfString(String string, String pattern) {
		return string.lastIndexOf(pattern) + pattern.length();
	}
	
	
}
