package com.ajbtechnologies.calculators;

import de.congrace.exp4j.ExpressionBuilder;

public class Calculate {
	
	
	public static Double evaluate(final String operation) {
		Double result = null;
		try {
			result =  new ExpressionBuilder(operation).build().calculate();	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Double evaluate(Float doubleNum) {
		Double result = null;
		try {
			result =  new ExpressionBuilder(doubleNum.toString()).build().calculate();	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}



}