drop table if exists payment;
drop table if exists bill_charge;
drop table if exists bill;
drop table if exists charge_to_property;
drop table if exists charge_to_property_type;
drop table if exists charge_to_property_group;
drop table if exists charge;
drop table if exists property_asset;
drop table if exists property;
drop table if exists floor;
drop table if exists floor_plan_design;
drop table if exists floor_plan;
drop table if exists asset_type;
drop table if exists property_type;
drop table if exists property_group;
drop table if exists wing;
drop table if exists users;
drop table if exists fine;
drop table if exists society;

/*----------*/

CREATE TABLE IF NOT EXISTS society
(
	society_id 		INTEGER,
	name 			VARCHAR2(100),
	address			VARCHAR2(300),
	reg_number		VARCHAR2(100),
	reg_timestamp	timestamp,
	society_code	varchar2(5),
	payment_due_date	integer,
	late_fine_interest	double,
	primary key		(society_id)
);

create table if not exists fine
(
	society_id		integer,
	fine_low		double,
	fine_high		double,
	perc_charge		double,
	primary key		(society_id, fine_low, fine_high),
	foreign key		(society_id) references society(society_id)
);

create table if not exists users
(
	user_name		varchar2(10),
	society_id		integer,
	password		varchar2(255),
	email			varchar2(50),
	full_name		varchar2(100),
	primary key		(user_name),
	foreign key		(society_id) 		references society(society_id)
);

create table if not exists wing
(
	society_id		integer,
	wing_id			integer,
	wing_name		varchar2(20),
	no_of_floors	integer,
	primary key		(society_id, wing_id),
	foreign key		(society_id) 		references society(society_id)
);

create table if not exists property_group
(
	society_id		integer,
	property_group	varchar2(10),
	description		varchar2(20),
	primary key		(society_id, property_group),
	foreign key		(society_id) 		references society(society_id)
);

create table if not exists property_type
(
	society_id		integer,
	property_type	varchar2(10),
	description		varchar2(20),
	primary key		(society_id, property_type),
	foreign key		(society_id) 		references society(society_id)
);

create table if not exists charge
(
	society_id		integer,
	charge_id		integer,
	description		varchar2(30),
	amount			double,
	is_default		boolean,
	temp_charge		boolean,
	is_cancelled	boolean,
	primary key		(society_id, charge_id),
	foreign key 	(society_id) references society(society_id)
);

create table if not exists asset_type
(
	society_id		integer,
	asset_type		varchar2(10),
	description		varchar2(20),
	charge_id		integer,
	primary key		(society_id, asset_type),
	foreign key		(society_id) 		references society(society_id),
	foreign key 	(society_id, charge_id) references charge(society_id, charge_id)		
);

create table if not exists floor_plan
(
	society_id		integer,
	floor_plan_id	varchar2(10),
	description		varchar2(20),
	no_of_property	integer,
	primary key		(society_id, floor_plan_id),
	foreign key		(society_id) 		references society(society_id)
);

create table if not exists floor_plan_design
(
	society_id		integer,
	floor_plan_id	varchar2(10),
	property_number	integer,
	property_group	varchar2(10),
	property_type	varchar2(10),
	primary key		(society_id, floor_plan_id, property_number),
	foreign key		(society_id, floor_plan_id) references floor_plan(society_id, floor_plan_id),
	foreign key		(society_id, property_group) 	references property_group(society_id, property_group),
	foreign key		(society_id, property_type) 	references property_type(society_id, property_type)
);

create table if not exists floor
(
	society_id		integer,
	wing_id			integer,
	floor_number	integer,
	floor_plan_id	integer,
	primary key		(society_id, wing_id, floor_number),
	foreign key		(society_id, wing_id) 	references wing(society_id, wing_id),
	foreign key		(society_id, floor_plan_id) 		references floor_plan(society_id, floor_plan_id)
);

create table if not exists property
(
	property_id		integer,
	society_id		integer,
	wing_id			integer,
	floor_number	integer,
	floor_plan_id	integer,
	property_number	integer,
	owner_name		varchar2(100),
	owner_number	varchar2(20),
	owner_email		varchar2(50),
	property_name	varchar2(50),
	net_payable		double,
	not_used		boolean,
	last_payment_id	integer,
	primary key		(property_id),
	foreign key		(society_id, wing_id, floor_number) 	
									references floor(society_id, wing_id, floor_number),
	foreign key		(society_id, floor_plan_id, property_number) 			
									references floor_plan_design(society_id, floor_plan_id, property_number)
);

create table if not exists property_asset
(
	property_id		integer,
	society_id		integer,
	asset_number	integer,
	asset_details	varchar2(100),
	asset_type		varchar2(10),
	is_cancelled	boolean,
	primary key		(property_id, asset_number),
	foreign key		(property_id) 				references property(property_id),
	foreign key		(society_id, asset_type) 	references asset_type(society_id, asset_type)
);

create table if not exists charge_to_property_group
(
	society_id		integer,
	charge_id		integer,
	property_group	varchar2(10),
	primary key		(society_id, charge_id, property_group),
	foreign key		(society_id, charge_id) references charge(society_id, charge_id),
	foreign key		(society_id, property_group) references property_group(society_id, property_group)
);

create table if not exists charge_to_property_type
(
	society_id		integer,
	charge_id		integer,
	property_type	varchar2(10),
	primary key		(society_id, charge_id, property_type),
	foreign key		(society_id, charge_id) references charge(society_id, charge_id),
	foreign key		(society_id, property_type) references property_type(society_id, property_type)
);

create table if not exists charge_to_property
(
	society_id		integer,
	charge_id		integer,
	property_id		integer,
	primary key		(society_id, charge_id, property_id),
	foreign key		(society_id, charge_id) references charge(society_id, charge_id),
	foreign key		(property_id) references property(property_id)
);

create table if not exists payment
(
	payment_id			integer,
	society_id			integer,
	property_id			integer,
	amount				double,
	mode_of_payment		varchar2(20),
	transaction_number	varchar2(20),
	remarks				varchar2(100),
	cancellation_timestamp	timestamp,
	is_cancelled		boolean,
	modified_by			integer,
	last_modified		timestamp,
	payment_date 		timestamp,
	cheque_number		varchar(30),
	primary key			(payment_id),
	foreign key			(property_id) references property(property_id),
	foreign key 		(modified_by) references users(user_name)
);

create table if not exists bill
(
	bill_id			integer,
	society_id		integer,
	property_id		integer,
	amount			double,
	bill_timestamp	timestamp,
	payment_id		integer,
	is_cancelled	boolean,
	modified_by		varcahr2(10),
	last_modified	timestamp,
	primary key		(bill_id),
	foreign key		(property_id) references property(property_id),
	foreign key		(payment_id) references payment(payment_id),
	foreign key 	(modified_by) references users(user_name)
);

create table if not exists bill_charge
(
	bill_id			integer,
	charge_id		integer,
	amount			double,
	primary key		(bill_id, charge_id),
	foreign key		(bill_id) references bill(bill_id)
);

/*
 * System Charges
 */

INSERT INTO "charge" VALUES (1, -10, "Fine", 0, 0, 0, 0);
INSERT INTO "charge" VALUES (1, -11, "Previous Balance amount", 0 , 0, 0, 0);
