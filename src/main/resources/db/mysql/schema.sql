CREATE TABLE IF NOT EXISTS users(
	username VARCHAR(255) NOT NULL PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	enabled BOOLEAN NOT NULL
)engine= InnoDB;

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	 FOREIGN KEY (username) REFERENCES users(username),
	 UNIQUE
   (
      username,
      authority
   )
)engine= InnoDB;

CREATE TABLE IF NOT EXISTS vets
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   first_name VARCHAR (30),
   last_name VARCHAR (30),
   INDEX (last_name)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS specialties
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (80),
   INDEX (name)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS vet_specialties
(
   vet_id INT (4) UNSIGNED NOT NULL,
   specialty_id INT (4) UNSIGNED NOT NULL,
   FOREIGN KEY (vet_id) REFERENCES vets (id),
   FOREIGN KEY (specialty_id) REFERENCES specialties (id),
   UNIQUE
   (
      vet_id,
      specialty_id
   )
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS types
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (80),
   INDEX (name)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS owners
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   first_name VARCHAR (30),
   last_name VARCHAR (30),
   address VARCHAR (255),
   city VARCHAR (80),
   telephone VARCHAR (20),
   username VARCHAR (255),
   INDEX (last_name),
   FOREIGN KEY (username) REFERENCES users(username)
)

CREATE TABLE IF NOT EXISTS pet_status
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (80)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS pets
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (30),
   birth_date DATE,
   type_id INT (4) UNSIGNED NOT NULL,
   owner_id INT (4) UNSIGNED NOT NULL,
   pet_status_id INT (4),
   INDEX (name),
   FOREIGN KEY (owner_id) REFERENCES owners (id),
   FOREIGN KEY (type_id) REFERENCES types (id),
   FOREIGN KEY (pet_status_id) REFERENCES pet_status (id)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS visits
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   pet_id INT (4) UNSIGNED NOT NULL,
   visit_date DATE,
   description VARCHAR (255),
   FOREIGN KEY (pet_id) REFERENCES pets (id)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS stays
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   pet_id INT (4) UNSIGNED NOT NULL,
   start_date DATE,
   finish_date DATE,
   price DOUBLE,
   special_cares VARCHAR (80),
   FOREIGN KEY (pet_id) REFERENCES pets (id)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS hospitalisations
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   pet_id INT (4) UNSIGNED NOT NULL,
   start_date DATE,
   finish_date DATE,
   treatment VARCHAR (255),
   diagnosis VARCHAR (255),
   hospitalisation_status VARCHAR (12),
   total_price DOUBLE,
   FOREIGN KEY (pet_id) REFERENCES pets (id)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS hospitalisation_status
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (80)
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS shops
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (80)
)engine= InnoDB;
CREATE TABLE IF NOT EXISTS discounts
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   start_date DATE,
   finish_date DATE,
   percentage DOUBLE
)
engine= InnoDB;
CREATE TABLE IF NOT EXISTS products
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (30),
   price DOUBLE,
   stock INT (10),
   shop_id INT (4) UNSIGNED NOT NULL,
   discount_id INT (4) UNSIGNED NOT NULL,
   UNIQUE (name),
   FOREIGN KEY (shop_id) REFERENCES shops (id),
   FOREIGN KEY (discount_id) REFERENCES discounts (id)
)
engine= InnoDB;

CREATE TABLE IF NOT EXISTS orders
(
   id INT (4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (30),
   supplier VARCHAR (30),
   order_date DATETIME,
   product_number INT (10),
   order_status ENUM
   (
      'INPROCESS',
      'RECEIVED',
      'CANCELED'
   ),
   shop_id INT (4) UNSIGNED NOT NULL,
   product_id INT (4) UNSIGNED NOT NULL,
   FOREIGN KEY (shop_id) REFERENCES shops (id),
   FOREIGN KEY (product_id) REFERENCES products(id)
)
engine= InnoDB;
