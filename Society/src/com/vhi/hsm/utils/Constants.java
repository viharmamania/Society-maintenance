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
				String EMAIL = "email";
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
		
		public interface PropertyType {
			String TABLE_NAME = "property_type";
			
			public interface FieldName {
				String PROPERTY_TYPE = "property_type";
				String DESCRIPTION = "description";
			}
		}
		
		public interface PropertyGroup {
			String TABLE_NAME = "property_group";
			
			public interface FieldName {
				String PROPERTY_GROUP = "property_group";
				String DESCRIPTION = "description";
			}
		}
		
		public interface FloorPlan {
			String TABLE_NAME = "floor_plan";
			
			public interface FieldName {
				String FLOOR_PLAN_ID = "floor_plan_id";
				String DESCRIPTION = "description";
				String NO_OF_PROPERTY = "no_of_property";
			}
		}
		
		public interface FloorPlanDesing {
			String TABLE_NAME = "floor_plan_desing";
			
			public interface FieldName {
				String PROPERTY_NUMBER = "property_number";
			}
		}
		
	}

	String DB_NAME = Path.RESOURCE + "hsm_mgmt.sqlite";
	
}
