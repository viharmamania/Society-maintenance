package com.vhi.hsm.utils;

import java.io.File;

public interface Constants {

	public interface Path {
		String RESOURCE = "res" + File.separator;
	}

	public interface Table {

		public interface User {
			String TABLE_NAME = "users";

			public interface FieldName {
				String USER_NAME = "user_name";
				String EMAIL = "email";
				String PASSWORD = "password";
				String FULL_NAME = "full_name";
				String IS_DELETED = "is_deleted";
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
				String SOCIETY_CODE = "society_code";
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

		public interface AssetType {
			String TABLE_NAME = "asset_type";

			public interface FieldName {
				String ASSET_TYPE = "asset_type";
				String DESCRIPTION = "description";
				String CHARGE = "charge";
			}
		}

		public interface Bill {
			String TABLE_NAME = "bill";

			public interface FieldName {
				String BILL_ID = "bill_id";
				String AMOUNT = "amount";
				String BILL_TIMESTAMP = "bill_timestamp";
				String IS_CANCELLED = "is_cancelled";
				String MODIFIED_BY = "modified_by";
				String LAST_MODIFIED = "last_modified";
			}
		}
		
		public interface BillCharge {
			String TABLE_NAME = "bill_charge";
			
			public interface FieldName {
				String AMOUNT = "amount";
			}
		}

		public interface Charge {
			String TABLE_NAME = "charge";

			public interface FieldName {
				String CHARGE_ID = "charge_id";
				String DESCRIPTION = "description";
				String AMOUNT = "amount";
				String TEMP_CHARGE = "temp_charge";
				String IS_CANCELLED = "is_cancelled";
			}
		}

		public interface Floor {
			String TABLE_NAME = "floor";

			public interface FieldName {
				String FLOOR_NUMBER = "floor_number";
			}
		}

		public interface Payment {
			String TABLE_NAME = "payment";

			public interface FieldName {
				String PAYMENT_ID = "payment_id";
				String MODE_OF_PAYMENT = "mode_of_payment";
				String TRANSACTION_NUMBER = "transaction_number";
				String REMARKS = "remarks";
				String CANCELLATION_TIMESTAMP = "cancellation_timestamp";
				String IS_CANCELLED = "is_cancelled";
				String MODIFIED_BY = "modified_by";
				String LAST_MODIFIED = "last_modified";
			}
		}

		public interface Property {
			String TABLE_NAME = "property";

			public interface FieldName {
				String PROPERTY_ID = "property_id";
				String OWNER_NAME = "owner_name";
				String OWNER_NUMBER = "owner_number";
				String OWNER_EMAIL = "owner_email";
				String BALANCE = "balance";
				String NOT_USED = "not_used";
			}
		}

		public interface PropertyAsset {
			String TABLE_NAME = "property_asset";

			public interface FieldName {
				String ASSET_NUMBER = "asset_number";
				String ASSET_DETAILS = "asset_details";
				String IS_CANCELLED = "is_cancelled";
				String ASSET_TYPE = "asset_type";
			}
		}

		public interface Wing {
			String TABLE_NAME = "wing";

			public interface FieldName {
				String WING_ID = "wing_id";
				String WING_NAME = "wing_name";
				String NUMBER_OF_FLOORS = "no_of_floors";
			}
		}

		public interface ChargeToProperty {
			String TABLE_NAME = "charge_to_property";
		}

		public interface ChargeToPropertyGroup {
			String TABLE_NAME = "charge_to_property_group";
		}

		public interface ChargeToPropertyType {
			String TABLE_NAME = "charge_to_property_type";
		}

	}

	String DB_NAME = Path.RESOURCE + "hsm_mgmt.sqlite";

}
