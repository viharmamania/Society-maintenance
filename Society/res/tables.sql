create table if not exists users
(
	user_id			integer,
	user_name		varchar2(10),
	password		varchar2(255),
	primary key		(user_id)
);

CREATE TABLE IF NOT EXISTS society
(
	society_id 		INTEGER,
	name 			VARCHAR2(100),
	address			VARCHAR2(300),
	reg_number		VARCHAR2(100),
	reg_timestamp	timestamp,
	primary key		(society_id)
);

create table if not exists property
(
	property_id		integer,
	society_id		integer,
	owner_name		varchar2(100),
	owner_number	varchar2(20),
	owner_email		varchar2(50),
	balance			double,
	primary key		(property_id),
	foreign key 	(society_id) references society(society_id)
);

create table if not exists property_type
(
	property_type	varchar2(10),
	description		varchar2(20),
	primary key		(property_type)
);

create table if not exists flat
(
	property_id		integer,
	property_type	varchar2(20),
	primary key		(property_id),
	foreign key		(property_id) references property(property_id),
	foreign key		(property_type) references property_type(property_type)
); 

create table if not exists shop
(
	property_id		integer,
	show_details	varchar2(100),
	primary key		(property_id),
	foreign key		(property_id) references property(property_id)
);

create table if not exists charge
(
	charge_id		integer,
	description		varchar2(30),
	amount			double,
	temp_charge		boolean,
	is_cancelled	boolean,
	modified_by		integer,
	last_modified	timestamp,
	primary key		(charge_id),
	foreign key 	(modified_by) references users(user_id)
);

create table if not exists asset
(
	asset_id		integer,
	description		varchar2(100),
	amount			double,
	primary key		(asset_id)
);

create table if not exists property_asset
(
	property_id		integer,
	asset_id		integer,
	asset_number	integer,
	asset_details	varchar2(100),
	is_cancelled	boolean,
	modified_by		integer,
	last_modified	timestamp,
	primary key		(property_id, asset_id, asset_number),
	foreign key		(property_id) references property(property_id),
	foreign key		(asset_id) references asset(asset_id),
	foreign key 	(modified_by) references users(user_id)
);

create table if not exists payment
(
	payment_id			integer,
	property_id			integer,
	mode_of_payment		integer,
	transaction_number	varchar2(20),
	remarks				varchar2(100),
	cancellation_timestamp	timestamp,
	is_cancelled		boolean,
	modified_by			integer,
	last_modified		timestamp,
	primary key			(payment_id),
	foreign key			(property_id) references property(property_id),
	foreign key 		(modified_by) references users(user_id)
);

create table if not exists bill
(
	bill_id			integer,
	property_id		integer,
	amount			double,
	bill_timestamp	timestamp,
	payment_id		integer,
	is_cancelled	boolean,
	modified_by		integer,
	last_modified	timestamp,
	primary key		(bill_id),
	foreign key		(property_id) references property(property_id),
	foreign key		(payment_id) references payment(payment_id),
	foreign key 	(modified_by) references users(user_id)
);