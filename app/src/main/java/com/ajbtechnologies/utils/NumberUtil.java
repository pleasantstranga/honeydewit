package com.ajbtechnologies.utils;

public class NumberUtil {
	public static boolean isWhole(double x) {
        return x - Math.floor(x) == 0;
    }
	public static String returnNumberString(Double number) {
        if(number != null) {
            return String.valueOf(number);
        }
		else return "0";
	}
    public static boolean isDouble(String numberAsText) {
        try {
           Double.valueOf(numberAsText);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
}
