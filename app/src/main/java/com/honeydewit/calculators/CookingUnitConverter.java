package com.honeydewit.calculators;

import com.honeydewit.pojos.Unit;

public class CookingUnitConverter {
	

	public static Double convertWeightVolume(Unit fromUnit, Float fromNumUnits, Unit toUnit) {
			if(!fromUnit.getDescription().equals(toUnit.getDescription())) {
				float calculation = (fromUnit.getNumUnitsToConvertWith() * fromNumUnits)/toUnit.getNumUnitsToConvertWith();
				return Calculate.evaluate(calculation);
			}
			return fromNumUnits.doubleValue();
	}

	// Method to convert from degrees Celcius to degrees Fahrenheit
	private static float celciusToFahrenheit(float degCelcius) {
		return degCelcius * 9 / 5 + 32;
	}

	// Method to convert from degrees Fahrenheit to degrees Celcius
	private static float fahrenheitToCelcius(float degFahrenheit) {
		return (degFahrenheit - 32) * 5 / 9;
	}
	
	public static Float convertTemp(String typeFrom, String typeTo, float amountFrom) {
		if(typeFrom.equalsIgnoreCase(typeTo)) return amountFrom;
		else if(typeFrom.equalsIgnoreCase("CELSIUS") && typeTo.equalsIgnoreCase("FAHRENHEIT")) return celciusToFahrenheit(amountFrom);
		else if(typeFrom.equalsIgnoreCase("FAHRENHEIT") && typeTo.equalsIgnoreCase("CELSIUS")) return fahrenheitToCelcius(amountFrom);
		else return 0F;
	}

}
