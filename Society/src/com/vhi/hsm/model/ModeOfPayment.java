package com.vhi.hsm.model;

public enum ModeOfPayment {
	
	SELECT("Select"), 
	CASH("Cash"), 
	CHEQUE("Cheque"), 
	NEFT("NEFT");

	private final String mode;

	private ModeOfPayment(String str) {
		this.mode = str;
	}

	String getMode() {
		return this.mode;
	}
	
	@Override
	public String toString() {
		return this.mode;
	}
	
	public boolean isValideModeOfPayment() {
		return this != SELECT;
	}

	public static String[] getNames() {
		ModeOfPayment[] modes = ModeOfPayment.class.getEnumConstants();
		String[] result = new String[modes.length];
		for (int i = 0; i < modes.length; i++) {
			result[i] = modes[i].toString();
		}
		return result;
	}

}
