-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO status VALUES (0, 'SICK');
INSERT INTO status VALUES (1, 'HEALTHY');

INSERT INTO hospitalisation_status VALUES (0, 'HOSPITALISED');
INSERT INTO hospitalisation_status VALUES (1, 'DISCHARGED');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (1, 'Leo', '2010-09-07', 1, 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (2, 'Basil', '2012-08-06', 6, 2, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (6, 'George', '2010-01-20', 4, 5, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (8, 'Max', '2012-09-04', 1, 6, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,status_id) VALUES (13, 'Sly', '2012-06-08', 1, 10, 1);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO shops(id,name) VALUES (1, 'shop1');

INSERT INTO discounts(id,percentage,start_date,finish_date) VALUES (1, 30, '2020-09-01', '2020-10-01');
INSERT INTO discounts(id,percentage,start_date,finish_date) VALUES (2, 50, '2020-04-21', '2020-08-01');

INSERT INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (1, 30.0, '2020-09-01', '2020-10-01','special',1);
INSERT INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (2, 30.0, '2020-09-01', '2020-10-01','special',3);
INSERT INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (3, 30.0, '2020-04-01', '2020-05-01','special',7);
INSERT INTO stays(id,price,start_date,finish_date,special_cares,pet_id) VALUES (4, 30.0, '2020-09-01', '2020-10-01','special',7);

INSERT INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (1, '2020-02-12', '2020-02-20','treatment1','diagnosis1',30,1,1);
INSERT INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (2, '2020-04-15', '2020-04-19','treatment2','diagnosis2',50,7,1);
INSERT INTO hospitalisations(id,start_date,finish_date,treatment,diagnosis,total_price,pet_id,hospitalisation_status) VALUES (3, '2020-03-28', null, 'treatment3','diagnosis3',70,7,0);


INSERT INTO products(id,name,price,stock,shop_id,discount_id) VALUES (1, 'product1', 15, 5,1,1);
INSERT INTO products(id,name,price,stock,shop_id,discount_id) VALUES (2, 'product2', 25, 10,1,null);
INSERT INTO products(id,name,price,stock,shop_id,discount_id) VALUES (3, 'product3', 18, 15,1,2);
INSERT INTO products(id,name,price,stock,shop_id,discount_id) VALUES (4, 'product4', 35, 10,1,null);
INSERT INTO products(id,name,price,stock,shop_id,discount_id) VALUES (5, 'product5', 15, 10,1,null);

INSERT INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (1, 'order1', 'supplier1', 10, '2020-03-12 12:30', 1, 1, 2);
INSERT INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (2, 'order2', 'supplier2', 25, '2020-03-10 13:30', 0, 1, 2);
INSERT INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (3, 'order3', 'supplier3', 10, '2020-03-12 12:30', 1, 1, 4);
INSERT INTO orders(id,name,supplier,product_number,order_date,order_status,shop_id,product_id) VALUES (4, 'order4', 'supplier4', 25, '2020-04-20 13:30', 0, 1, 4);

