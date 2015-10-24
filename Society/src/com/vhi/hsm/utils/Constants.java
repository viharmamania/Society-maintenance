package com.vhi.hsm.utils;

public interface Constants {
	
	public interface Path {
		String RESOURCE = "res/";
	}
	
	public interface Table {
		
		public interface User {
			String TABLE_NAME = "users";
			
			public interface FieldName {
				String USER_NAME = "user_name";
				String EMIAL = "email";
				String PASSWORD = "password";
				String FULL_NAME = "full_name";
			}			
		}
		
		public interface Society {
			String TABLE_NAME = "society";
			
			public interface FieldName {
				String SOCIETY_ID = "soceity_id";
				String SOCIETY_NAME = "name";
				String ADDRESS = "address";
				String REG_NUMBER = "reg_number";
				String REG_DATE = "reg_date";
			}			
		}
		
	}

	String DB_NAME = Path.RESOURCE + "hsm_mgmt.sqllite";
	
}
