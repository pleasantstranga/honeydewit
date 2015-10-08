package com.honeydewit.calculators;

import android.content.Context;
import android.os.Parcelable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.honeydewit.R;
import com.honeydewit.utils.StringUtils;

public class CalculatorView extends LinearLayout {
	private EditText calculatorEquateDisplay;
	//private EditText calculatorAnswerDisplay;
	private String setValue;
	private Button button0;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private Button button9;
	private Button buttonAdd;
	private Button buttonSubtract;
	private Button buttonMultiply;
	private Button buttonDivide;
	private Button buttonDecimalPoint;
	private Button buttonEquals;
	private Button buttonDelete;
	private Button buttonClear;
	//private Button buttonMemoryClear;
	private Button buttonSet;

	public CalculatorView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.calculator, null);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		calculatorEquateDisplay = (EditText) layout.findViewById(R.id.textWindow);
		calculatorEquateDisplay.setText("0");
		//calculatorAnswerDisplay = (EditText) layout.findViewById(R.id.answerTextWindow);


		button0 = (Button) layout.findViewById(R.id.button0);
		button1 = (Button) layout.findViewById(R.id.button1);
		button2 = (Button) layout.findViewById(R.id.button2);
		button3 = (Button) layout.findViewById(R.id.button3);
		button4 = (Button) layout.findViewById(R.id.button4);
		button5 = (Button) layout.findViewById(R.id.button5);
		button6 = (Button) layout.findViewById(R.id.button6);
		button7 = (Button) layout.findViewById(R.id.button7);
		button8 = (Button) layout.findViewById(R.id.button8);
		button9 = (Button) layout.findViewById(R.id.button9);

		buttonAdd = (Button) layout.findViewById(R.id.buttonAdd);
		buttonSubtract = (Button) layout.findViewById(R.id.buttonSubtract);
		buttonMultiply = (Button) layout.findViewById(R.id.buttonMultiply);
		buttonDivide = (Button) layout.findViewById(R.id.buttonDivide);

		buttonDecimalPoint = (Button) layout.findViewById(R.id.buttonDecimalPoint);
		buttonEquals = (Button) layout.findViewById(R.id.buttonEquals);
		buttonDelete = (Button) layout.findViewById(R.id.buttonDelete);
		buttonClear = (Button) layout.findViewById(R.id.buttonClear);

		//buttonMemoryClear = (Button) layout.findViewById(R.id.buttonMemoryClear);
		buttonSet = (Button) layout.findViewById(R.id.buttonSet);

		CalculatorInitializer initializer = new CalculatorInitializer();
		initializer.initialise(context, this);

		addView(layout);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
	}

	public EditText getCalculatorEquationDisplay() {
		return calculatorEquateDisplay;
	}

	public void setCalculatorEquationDisplay(EditText calculatorEquationDisplay) {
		this.calculatorEquateDisplay = calculatorEquationDisplay;
	}
	/**
	public EditText getCalculatorAnswerDisplay() {
		return calculatorAnswerDisplay;
	}

	public void setCalculatorAnswerDisplay(EditText calculatorAnswerDisplay) {
		this.calculatorAnswerDisplay = calculatorAnswerDisplay;
	}
	**/
	public boolean isNumberButton(Button b) {
		return StringUtils.isInteger(b.getText().toString());
	}

	public boolean isOneButton(Button b) {
		return b.equals(button1);
	}

	public boolean isTwoButton(Button b) {
		return b.equals(button2);
	}

	public boolean isThreeButton(Button b) {
		return b.equals(button3);
	}

	public boolean isFourButton(Button b) {
		return b.equals(button4);
	}

	public boolean isFiveButton(Button b) {
		return b.equals(button5);
	}

	public boolean isSixButton(Button b) {
		return b.equals(button6);
	}

	public boolean isSevenButton(Button b) {
		return b.equals(button7);
	}

	public boolean isEightButton(Button b) {
		return b.equals(button8);
	}

	public boolean isNineButton(Button b) {
		return b.equals(button9);
	}

	public boolean isZeroButton(Button b) {
		return b.equals(button0);
	}

	public boolean isAddButton(Button b) {
		return b.equals(buttonAdd);
	}

	public boolean isSubtractButton(Button b) {
		return b.equals(buttonSubtract);
	}

	public boolean isMultiplyButton(Button b) {
		return b.equals(buttonMultiply);
	}

	public boolean isDivideButton(Button b) {
		return b.equals(buttonDivide);
	}

	public boolean isDecimalButton(Button b) {
		return b.equals(buttonDecimalPoint);
	}

	public boolean isEqualsButton(Button b) {
		boolean isEqualsButton = false;
		if(b != null) {
			isEqualsButton = b.equals(buttonEquals);
		}
		return isEqualsButton;
	}

	public boolean isDeleteButton(Button b) {
		return b.equals(buttonDelete);
	}

	public boolean isClearButton(Button b) {
		return b.equals(buttonClear);
	}

	public boolean isOperatorButton(Button b) {
		return isDivideButton(b) || isMultiplyButton(b) || isAddButton(b) || isSubtractButton(b);
	}
	/**
	public boolean isMemoryClearButton(Button b) {
		return b.equals(buttonMemoryClear);
	}
**/
	public boolean isSetButton(Button b) {
		return b.equals(buttonSet);
	}

	public boolean isEquationExists() {
		return getCalculatorEquationAsString().length() > 0;
	}

	public Button getButton0() {
		return button0;
	}

	public Button getButton1() {
		return button1;
	}

	public Button getButton2() {
		return button2;
	}

	public Button getButton3() {
		return button3;
	}

	public Button getButton4() {
		return button4;
	}

	public Button getButton5() {
		return button5;
	}

	public Button getButton6() {
		return button6;
	}

	public Button getButton7() {
		return button7;
	}

	public Button getButton8() {
		return button8;
	}

	public Button getButton9() {
		return button9;
	}

	public Button getButtonAdd() {
		return buttonAdd;
	}

	public Button getButtonSubtract() {
		return buttonSubtract;
	}

	public Button getButtonMultiply() {
		return buttonMultiply;
	}

	public Button getButtonDivide() {
		return buttonDivide;
	}

	public Button getButtonDecimalPoint() {
		return buttonDecimalPoint;
	}

	public Button getButtonEquals() {
		return buttonEquals;
	}

	public Button getButtonDelete() {
		return buttonDelete;
	}

	public Button getButtonClear() {
		return buttonClear;
	}
	/**
	public Button getButtonMemoryClear() {
		return buttonMemoryClear;
	}
**/
	public Button getButtonSet() {
		return buttonSet;
	}

	public void setButton0(Button button0) {
		this.button0 = button0;
	}

	public void setButton1(Button button1) {
		this.button1 = button1;
	}

	public void setButton2(Button button2) {
		this.button2 = button2;
	}

	public void setButton3(Button button3) {
		this.button3 = button3;
	}

	public void setButton4(Button button4) {
		this.button4 = button4;
	}

	public void setButton5(Button button5) {
		this.button5 = button5;
	}

	public void setButton6(Button button6) {
		this.button6 = button6;
	}

	public void setButton7(Button button7) {
		this.button7 = button7;
	}

	public void setButton8(Button button8) {
		this.button8 = button8;
	}

	public void setButton9(Button button9) {
		this.button9 = button9;
	}

	public void setButtonAdd(Button buttonAdd) {
		this.buttonAdd = buttonAdd;
	}

	public void setButtonSubtract(Button buttonSubtract) {
		this.buttonSubtract = buttonSubtract;
	}

	public void setButtonMultiply(Button buttonMultiply) {
		this.buttonMultiply = buttonMultiply;
	}

	public void setButtonDivide(Button buttonDivide) {
		this.buttonDivide = buttonDivide;
	}

	public void setButtonDecimalPoint(Button buttonDecimalPoint) {
		this.buttonDecimalPoint = buttonDecimalPoint;
	}

	public void setButtonEquals(Button buttonEquals) {
		this.buttonEquals = buttonEquals;
	}

	public void setButtonDelete(Button buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public void setButtonClear(Button buttonClear) {
		this.buttonClear = buttonClear;
	}

	public void setButtonSet(Button buttonSet) {
		this.buttonSet = buttonSet;
	}
/**
	public void setButtonMemoryClear(Button buttonMemoryClear) {
		this.buttonMemoryClear = buttonMemoryClear;
	}
 **/
/**
	public boolean isAnswerPopulated() {
		return this.getCalculatorAnswerDisplay().getText().length() == 0;
	}
**/
	public boolean isNonZeroCharacterExists() {
		for (char c : this.getCalculatorEquationAsString().toCharArray()) {
			if (c != '0') {
				return true;
			}
		}
		return false;
	}

	public boolean isEquationEmpty() {
		return this.getCalculatorEquationDisplay().getText() == null || this.getCalculatorEquationDisplay().getText().length() == 0;
	}

	public boolean isNumberToggled() {
		return this.getCalculatorEquationDisplay().getText().charAt(0) == '-';
	}

	public boolean isNonZeroNumberExists() {
		boolean isNonZeroNumberExists = false;
		Integer x;
		try {
			x = Integer.parseInt(this.getCalculatorEquationAsString());
			if (x != null) {
				if (x < 0 || x > 0) {
					isNonZeroNumberExists = true;
				}
			}
		} catch (Exception e) {
			isNonZeroNumberExists = false;
		}
		return isNonZeroNumberExists;
	}

	private Editable removeLastCharacterEquation() {
		return this.getCalculatorEquationDisplay().getText() != null ? this.getCalculatorEquationDisplay().getText().delete(getCalculatorEquationDisplay().getText().length() - 1, getCalculatorEquationDisplay().getText().length()) : null;
	}

	public Editable replaceLastCharacterEquation(char c) {
		removeLastCharacterEquation();
		return this.getCalculatorEquationDisplay().getText().append(c);

	}

	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	public boolean containsCalcuableOperation() {
		String equation = getCalculatorEquationAsString();
		Integer operatorIndex = getOperatorIndex(equation);
		return operatorIndex != null && StringUtils.isDouble(equation.substring(operatorIndex + 1));
	}

	public String getCalculatorEquationAsString() {
		return null != getCalculatorEquationDisplay().getText() ? getCalculatorEquationDisplay().getText().toString() : "";
	}

	public String getPreviousOperationAsString() {
		String equation = getCalculatorEquationAsString();
		for (int index = equation.length() - 1; index >= 0; index--) {
			if (isOperator(equation.charAt(index))) {
				return equation.substring(index);
			}

		}
		return equation;
	}

	private Integer getOperatorIndex(String equation) {
		if(equation != null) {
			if (equation.contains("+")) {
				return equation.indexOf("+");
			}
			if (equation.contains("-")) {
				return equation.indexOf("-");
			}
			if (equation.contains("*")) {
				return equation.indexOf("*");
			}
			if (equation.contains("/")) {
				return equation.indexOf("/");
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	public boolean isLastCharacterOperator() {
		boolean isOperator = false;
		if(getCalculatorEquationAsString() != null && getCalculatorEquationAsString().length() > 0) {
			isOperator = isOperator(getCalculatorEquationAsString().charAt(getCalculatorEquationAsString().length() - 1));
		}
		return isOperator;
	}

	public boolean isCurrentNumberContainDecimal() {
		boolean isCurrentNumberContainDecimal = false;
		String equation = getCalculatorEquationAsString();
		Integer indexOfOperator = getOperatorIndex(equation);
		if (null != indexOfOperator) {
			for (int x = indexOfOperator; x < equation.length(); x++) {
				if (equation.charAt(x) == '.') {
					 isCurrentNumberContainDecimal = true;
				}
			}
		} else if (equation != null && equation.contains(".")) {
			isCurrentNumberContainDecimal = true;
		}
		return isCurrentNumberContainDecimal;
	}

	public String getSetValue() {
		return setValue;
	}

	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}

	/**
	 * Initialises the calculator view
	 */
	public  class CalculatorInitializer {

		private Button previousButton;
		private String previousOperation;

		public void initialise(Context context, CalculatorView calculatorView) {

			new CalculatorOnClickInitializer(calculatorView);
		}

		private class CalculatorOnClickInitializer implements OnClickListener {
			private CalculatorView calculatorView;


			public CalculatorOnClickInitializer(CalculatorView calculatorView) {

				this.calculatorView = calculatorView;

				calculatorView.getButton0().setOnClickListener(this);
				calculatorView.getButton1().setOnClickListener(this);
				calculatorView.getButton2().setOnClickListener(this);
				calculatorView.getButton3().setOnClickListener(this);
				calculatorView.getButton4().setOnClickListener(this);
				calculatorView.getButton5().setOnClickListener(this);
				calculatorView.getButton6().setOnClickListener(this);
				calculatorView.getButton7().setOnClickListener(this);
				calculatorView.getButton8().setOnClickListener(this);
				calculatorView.getButton9().setOnClickListener(this);

				calculatorView.getButtonAdd().setOnClickListener(this);
				calculatorView.getButtonSubtract().setOnClickListener(this);
				calculatorView.getButtonMultiply().setOnClickListener(this);
				calculatorView.getButtonDivide().setOnClickListener(this);

				calculatorView.getButtonDecimalPoint().setOnClickListener(this);
				calculatorView.getButtonEquals().setOnClickListener(this);
				calculatorView.getButtonDelete().setOnClickListener(this);
				calculatorView.getButtonClear().setOnClickListener(this);
				//calculatorView.getButtonMemoryClear().setOnClickListener(this);
				calculatorView.getButtonSet().setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				Button currentButton = ((Button) v);
				Editable equation = calculatorView.getCalculatorEquationDisplay().getText();
				int equationLength = equation != null ? equation.length() : 0;
				if (calculatorView.isClearButton(currentButton)) {
					calculatorView.getCalculatorEquationDisplay().setText("");
				}
				/**else if (calculatorView.isMemoryClearButton(currentButton)) {
					calculatorView.getCalculatorEquationDisplay().setText("");
					previousOperation = null;
				} **/
				else if (calculatorView.isSetButton(currentButton)) {
					calculatorView.setSetValue(calculatorView.getCalculatorEquationDisplay().getText().toString());
				} else if (calculatorView.isDeleteButton(currentButton)) {
					if (equationLength > 0) {
						if(isExponentialNumber()) {
							removeExponent();
						}
						else {
							calculatorView.getCalculatorEquationDisplay().setText(equation.subSequence(0, --equationLength));
						}
					}
				}
				if (calculatorView.isNumberButton(currentButton)) {
					if (calculatorView.isZeroButton(currentButton)) {
						if (calculatorView.isEquationEmpty() || calculatorView.isNonZeroCharacterExists()) {
							calculatorView.getCalculatorEquationDisplay().append(currentButton.getText());
						}
					} else {
						removeZero();
						calculatorView.getCalculatorEquationDisplay().append(currentButton.getText());
					}
				} else if (calculatorView.isDecimalButton(currentButton) && !calculatorView.isCurrentNumberContainDecimal()) {
					if (calculatorView.isLastCharacterOperator() || calculatorView.getCalculatorEquationAsString().length() == 0) {
						calculatorView.getCalculatorEquationDisplay().append("0");
					}
					calculatorView.getCalculatorEquationDisplay().append(currentButton.getText());
				} else if (calculatorView.isOperatorButton(currentButton)) {
					if(getCalculatorEquationAsString() != null && getCalculatorEquationAsString().length() > 0) {
						if (calculatorView.isLastCharacterOperator()) {
							calculatorView.replaceLastCharacterEquation(currentButton.getText().charAt(0));
							previousOperation = null;
						} else {
							if (calculatorView.containsCalcuableOperation()) {
								Double answer = Calculate.evaluate(calculatorView.getCalculatorEquationAsString());
								calculatorView.getCalculatorEquationDisplay().setText(String.valueOf(answer));
								previousOperation = calculatorView.getPreviousOperationAsString();
								calculatorView.getCalculatorEquationDisplay().append(currentButton.getText());
							} else {
								calculatorView.getCalculatorEquationDisplay().append(currentButton.getText());
								previousOperation = null;
							}
						}
					}

				} else if (calculatorView.isEqualsButton(currentButton)) {
					if (!calculatorView.isLastCharacterOperator() && calculatorView.containsCalcuableOperation()) {
						Double answer = Calculate.evaluate(calculatorView.getCalculatorEquationAsString());
						previousOperation = calculatorView.getPreviousOperationAsString();
						if(answer != null) {
							calculatorView.getCalculatorEquationDisplay().setText(String.valueOf(answer));
						}


					} else if (calculatorView.isEqualsButton(previousButton) && previousOperation != null) {
						Double answer = Calculate.evaluate(calculatorView.getCalculatorEquationAsString().concat(previousOperation));
						if(answer != null) {
							calculatorView.getCalculatorEquationDisplay().setText(String.valueOf(answer));
						}
					}
				}
				previousButton = currentButton;
			}

		}

	}
	private boolean isExponentialNumber() {
		return getCalculatorEquationAsString() != null && getCalculatorEquationAsString().contains("E");
	}
	private void removeExponent() {
		if(isExponentialNumber()) {
			int index = getCalculatorEquationAsString().indexOf("E");
			getCalculatorEquationDisplay().setText(getCalculatorEquationAsString().substring(0,index));
		}
	}
	private void removeZero()  {
		if(getCalculatorEquationAsString() != null) {
			if(getCalculatorEquationAsString().equals("0")) {
				getCalculatorEquationDisplay().setText("");
			}
		}
	}

}