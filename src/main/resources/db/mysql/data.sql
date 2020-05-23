-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT IGNORE INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT IGNORE INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT IGNORE INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT IGNORE INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT IGNORE INTO authorities VALUES ('vet1','veterinarian');

INSERT IGNORE INTO vets(id,first_name,last_name) VALUES (1, 'James', 'Carter');
INSERT IGNORE INTO vets(id,first_name,last_name) VALUES (2, 'Helen', 'Leary');
INSERT IGNORE INTO vets(id,first_name,last_name) VALUES (3, 'Linda', 'Douglas');
INSERT IGNORE INTO vets(id,first_name,last_name) VALUES (4, 'Rafael', 'Ortega');
INSERT IGNORE INTO vets(id,first_name,last_name) VALUES (5, 'Henry', 'Stevens');
INSERT IGNORE INTO vets(id,first_name,last_name) VALUES (6, 'Sharon', 'Jenkins');

INSERT IGNORE INTO specialties(id,name) VALUES (1, 'radiology');
INSERT IGNORE INTO specialties(id,name) VALUES (2, 'surgery');
INSERT IGNORE INTO specialties(id,name) VALUES (3, 'dentistry');

INSERT IGNORE INTO vet_specialties(vet_id,specialty_id) VALUES (2, 1);
INSERT IGNORE INTO vet_specialties(vet_id,specialty_id) VALUES (3, 2);
INSERT IGNORE INTO vet_specialties(vet_id,specialty_id) VALUES (3, 3);
INSERT IGNORE INTO vet_specialties(vet_id,specialty_id) VALUES (4, 2);
INSERT IGNORE INTO vet_specialties(vet_id,specialty_id) VALUES (5, 1);

INSERT IGNORE INTO types(id,name) VALUES (1, 'cat');
INSERT IGNORE INTO types(id,name) VALUES (2, 'dog');
INSERT IGNORE INTO types(id,name) VALUES (3, 'lizard');
INSERT IGNORE INTO types(id,name) VALUES (4, 'snake');
INSERT IGNORE INTO types(id,name) VALUES (5, 'bird');
INSERT IGNORE INTO types(id,name) VALUES (6, 'hamster');

INSERT IGNORE INTO pet_status(id,name) VALUES (1, 'SICK');
INSERT IGNORE INTO pet_status(id,name) VALUES (2, 'HEALTHY');

INSERT IGNORE INTO hospitalisation_status(id,name) VALUES (1, 'HOSPITALISED');
INSERT IGNORE INTO hospitalisation_status(id,name) VALUES (2, 'DISCHARGED');

INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435','owner1');
INSERT IGNORE INTO owners(id,first_name,last_name,address,city,telephone,username) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487','owner1');

INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (1, 'Leo', '2010-09-07', 1, 1, 1);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (2, 'Basil', '2012-08-06', 6, 2, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (6, 'George', '2010-01-20', 4, 5, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6, 1);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (8, 'Max', '2012-09-04', 1, 6, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10, 2);
INSERT IGNORE INTO pets(id,name,birth_date,type_id,owner_id,pet_status_id) VALUES (13, 'Sly', '2012-06-08', 1, 10, 2);

INSERT IGNORE INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT IGNORE INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT IGNORE INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT IGNORE INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT IGNORE INTO shops(id,name) VALUES (1, 'shop1');

INSERT IGNORE INTO discounts(id,percentage,start_date,finish_date) VALUES (1,30, '2020-09-01', '2020-10-01');
INSERT IGNORE INTO discounts(id,percentage,start_date,finish_date) VALUES (2,50, '2020-04-21', '2020-08-01');
INSERT IGNORE INTO discounts(id,percentage,start_date,finish_date) VALUES (3,30, '2020-08-01', '2020-09-01');

INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (1, 30.0, '2020-07-15', '2020-07-19','test special cares',1);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (2, 30.0, '2020-09-01', '2020-10-01','special',3);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (3, 30.0, '2020-07-01', '2021-07-01','special',7);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (4, 30.0, '2020-03-01', '2020-06-25','special',7);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (5, 30.0, '2020-11-01', '2020-11-11','special',2);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (6, 30.0, '2020-05-20', '2020-05-25','special',2);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (7, 30.0, '2020-02-02', '2020-02-05','special',13);
INSERT IGNORE INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (8, 30.0, '2020-02-02', '2020-11-05','special',13);


INSERT IGNORE INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (1, '2020-02-12', '2020-02-20','treatment1','diagnosis1',30,1,2);
INSERT IGNORE INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (2, '2020-04-15', '2020-04-19','treatment2','diagnosis2',50,7,2);
INSERT IGNORE INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (3, '2020-03-28', null, 'treatment3','diagnosis3',70,7,1);
INSERT IGNORE INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (4, '2020-03-28', null, 'treatment4','diagnosis4',50,1,1);

INSERT IGNORE INTO products(id,name,price,stock,shop_id,discount_id) VALUES (1,'product1',15.0,5,1,1);
INSERT IGNORE INTO products(id,name,price,stock,shop_id,discount_id) VALUES (2,'product2',25.0,10,1,null);
INSERT IGNORE INTO products(id,name,price,stock,shop_id,discount_id) VALUES (3,'product3',18.0,15,1,2);
INSERT IGNORE INTO products(id,name,price,stock,shop_id,discount_id) VALUES (4,'product4',35.0,10,1,null);
INSERT IGNORE INTO products(id,name,price,stock,shop_id,discount_id) VALUES (5,'product5',15.0,10,1,3);
INSERT IGNORE INTO products(id,name,price,stock,shop_id,discount_id) VALUES (6,'product6',15.0,10,1,null);

INSERT IGNORE INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (1, 'order1', 'supplier1', 10, CURRENT_TIMESTAMP, 0, 1, 2);
INSERT IGNORE INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (2, 'order2', 'supplier2', 25, '2020-03-10 13:30', 0, 1, 2);
INSERT IGNORE INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (3, 'order3', 'supplier3', 10, '2020-03-12 12:30', 1, 1, 4);
INSERT IGNORE INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (4, 'order4', 'supplier4', 25, '2020-03-20 13:30', 0, 1, 4);
INSERT IGNORE INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (5, 'order5', 'supplier5', 30, '2020-04-20 13:30', 1, 1, 4);

