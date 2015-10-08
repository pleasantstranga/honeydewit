package com.honeydewit.calculators;

import com.honeydewit.utils.StringUtils;

public class FormulaFormatter {
	private static final String MULTIPLY = "*";
	
	public static String format(String formula) {
		StringBuilder builder = new StringBuilder(formula);
		String previousChar = "";	
		int index = 0;
		int length = formula.length();
		while(index < length) {
			String currentChar = ((Character)builder.charAt(index)).toString();

			if(StringUtils.isInteger(previousChar) && currentChar.equals("(")) {
				builder.insert(index, MULTIPLY);
				index++;
			}
			if(previousChar.equals(")") && StringUtils.isInteger(currentChar)) {
				builder.insert(index, MULTIPLY);
				index++;
			}
			if(currentChar.equals("(") && previousChar.equals(")")) {
				builder.insert(index, MULTIPLY);
				index++;
			}
			if(currentChar.equals(".") && !StringUtils.isInteger(previousChar)) {
				builder.insert(index, "0");
				index++;
			}
	
			length = builder.length();
			index++;
			previousChar = currentChar;
		}
		return builder.toString();
	}
}
