INSERT INTO "society" VALUES(1,'Test Society','dombivali','1234','11-11-2015','111', 15, 5.5);

INSERT INTO "users" VALUES('hardik',1,'DZVtspimtCo9pZSMZnOiOg==','hardik@hsm.com','Hardik');
INSERT INTO "users" VALUES('vihar',1,'YFbnSJ/QSq9ATQ3mUUJe2jxamsIjPn0J','vihar@1903.com','vihar');

INSERT INTO "wing" VALUES(1,1,'A - Wing',3);
INSERT INTO "wing" VALUES(1,2,'B - Wing',3);

INSERT INTO "property_group" VALUES(1,'flat','Flats');
INSERT INTO "property_group" VALUES(1,'shop','Shops');

INSERT INTO "property_type" VALUES(1,'s_flat','Small Flat');
INSERT INTO "property_type" VALUES(1,'b_flat','Big Flat');
INSERT INTO "property_type" VALUES(1,'s_shop','Small Shop');
INSERT INTO "property_type" VALUES(1,'b_shop','Big Shop');

INSERT INTO "asset_type" VALUES (1, "bike", "Bike", 2);
INSERT INTO "asset_type" VALUES (1, "car", "Car", 3);

INSERT INTO "charge" VALUES (1, 1, "Society Charges", 100, 1, 0, 0);
INSERT INTO "charge" VALUES (1, 2, "Bike Charges", 50, 0, 0, 0);
INSERT INTO "charge" VALUES (1, 3, "Car Charges", 100, 0, 0, 0);
INSERT INTO "charge" VALUES (1, 4, "Big Flat and Shop Charges", 110, 0, 0, 0);
INSERT INTO "charge" VALUES (1, 5, "Small Flat and Shop Charges", 90, 0, 0, 0);
INSERT INTO "charge" VALUES (1, 6, "Shop Charges", 100, 0, 0, 0);

INSERT INTO "floor" VALUES(1,1,0,1);
INSERT INTO "floor" VALUES(1,1,1,2);
INSERT INTO "floor" VALUES(1,1,2,2);
INSERT INTO "floor" VALUES(1,1,3,2);
INSERT INTO "floor" VALUES(1,2,0,3);
INSERT INTO "floor" VALUES(1,2,1,4);
INSERT INTO "floor" VALUES(1,2,2,4);
INSERT INTO "floor" VALUES(1,2,3,4);

INSERT INTO "floor_plan" VALUES(1,'1','A-Ground',3);
INSERT INTO "floor_plan" VALUES(1,'2','A-Floor',2);
INSERT INTO "floor_plan" VALUES(1,'3','B-Ground',2);
INSERT INTO "floor_plan" VALUES(1,'4','B-Floor',3);

INSERT INTO "floor_plan_design" VALUES(1, 1,'1','shop','s_shop');
INSERT INTO "floor_plan_design" VALUES(1, 1,'2','shop','s_shop');
INSERT INTO "floor_plan_design" VALUES(1, 1,'3','shop','s_shop');
INSERT INTO "floor_plan_design" VALUES(1, 2,'1','flat','b_flat');
INSERT INTO "floor_plan_design" VALUES(1, 2,'2','flat','b_flat');
INSERT INTO "floor_plan_design" VALUES(1, 3,'1','shop','b_shop');
INSERT INTO "floor_plan_design" VALUES(1, 3,'2','shop','b_shop');
INSERT INTO "floor_plan_design" VALUES(1, 4,'1','flat','s_flat');
INSERT INTO "floor_plan_design" VALUES(1, 4,'2','flat','s_flat');
INSERT INTO "floor_plan_design" VALUES(1, 4,'3','flat','b_flat');

INSERT INTO "property" VALUES(1,1,1,0,1,1,'Mr. A','1','','A001',0,0,NULL);
INSERT INTO "property" VALUES(2,1,1,0,1,2,'Mr. B','2','','A002',0,0,NULL);
INSERT INTO "property" VALUES(3,1,1,0,1,3,'Mr. C','3','','A003',0,0,NULL);
INSERT INTO "property" VALUES(4,1,1,1,2,1,'Mr. D','4','','A101',0,0,NULL);
INSERT INTO "property" VALUES(5,1,1,1,2,2,'Mr. E','5','','A102',0,1,NULL);
INSERT INTO "property" VALUES(6,1,1,2,2,1,'Mr. F','6','','A201',0,1,NULL);
INSERT INTO "property" VALUES(7,1,1,2,2,2,'Mr. G','7','','A202',0,0,NULL);
INSERT INTO "property" VALUES(8,1,1,3,2,1,'Mr. H','8','','A301',0,0,NULL);
INSERT INTO "property" VALUES(9,1,1,3,2,2,'Mr. I','9','','A302',0,0,NULL);
INSERT INTO "property" VALUES(10,1,2,0,3,1,'Mr. J','10','','B001',0,1,NULL);
INSERT INTO "property" VALUES(11,1,2,0,3,2,'Mr. K','11','','B002',0,0,NULL);
INSERT INTO "property" VALUES(12,1,2,1,4,1,'Mr. L','12','','B101',0,0,NULL);
INSERT INTO "property" VALUES(13,1,2,1,4,2,'Mr. M','13','','B102',0,0,NULL);
INSERT INTO "property" VALUES(14,1,2,1,4,3,'Mr. N','14','','B103',0,1,NULL);
INSERT INTO "property" VALUES(15,1,2,2,4,1,'Mr. O','15','','B201',0,1,NULL);
INSERT INTO "property" VALUES(16,1,2,2,4,2,'Mr. P','16','','B202',0,0,NULL);
INSERT INTO "property" VALUES(17,1,2,2,4,3,'Mr. Q','17','','B203',0,0,NULL);
INSERT INTO "property" VALUES(18,1,2,3,4,1,'Mr. R','18','','B301',0,0,NULL);
INSERT INTO "property" VALUES(19,1,2,3,4,2,'Mr. S','19','','B302',0,1,NULL);
INSERT INTO "property" VALUES(20,1,2,3,4,3,'Mr. T','20','','B303',0,0,NULL);

INSERT INTO "charge_to_property_group" VALUES (1, 6, "shop");

INSERT INTO "charge_to_property_type" VALUES (1, 4, "b_flat");
INSERT INTO "charge_to_property_type" VALUES (1, 4, "b_shop");
INSERT INTO "charge_to_property_type" VALUES (1, 5, "s_flat");
INSERT INTO "charge_to_property_type" VALUES (1, 5, "s_shop");

INSERT INTO "property_asset" VALUES (1, 1, 1, "Bike", "bike", 0);
INSERT INTO "property_asset" VALUES (3, 1, 1, "Car", "car", 0);
INSERT INTO "property_asset" VALUES (6, 1, 1, "Car", "car", 1);
INSERT INTO "property_asset" VALUES (7, 1, 1, "Bike 1", "bike", 0);
INSERT INTO "property_asset" VALUES (7, 1, 2, "Bike 2", "bike", 0);
INSERT INTO "property_asset" VALUES (7, 1, 3, "Car 1", "car", 0);
INSERT INTO "property_asset" VALUES (10, 1, 1, "Bike", "bike", 1);
INSERT INTO "property_asset" VALUES (13, 1, 1, "Car", "car", 0);
INSERT INTO "property_asset" VALUES (14, 1, 1, "Bike", "bike", 0);
INSERT INTO "property_asset" VALUES (15, 1, 1, "Bike", "bike", 1);
INSERT INTO "property_asset" VALUES (17, 1, 1, "Car 1", "car", 1);
INSERT INTO "property_asset" VALUES (17, 1, 2, "Car 2", "car", 0);
INSERT INTO "property_asset" VALUES (20, 1, 1, "Bike", "bike", 0);