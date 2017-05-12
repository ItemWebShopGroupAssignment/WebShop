CREATE SCHEMA IF NOT EXISTS `item_web_shop`;

USE `item_web_shop`;

CREATE TABLE IF NOT EXISTS `categories`(
	category_name VARCHAR(50) NOT NULL,
    contents TEXT,
    PRIMARY KEY(category_name)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `items` (
	art_number VARCHAR(50) NOT NULL,
	item_name VARCHAR(50) NOT NULL,
	price FLOAT(8,2) NOT NULL,
	description TEXT,
	image BLOB,
	stock_balance INT UNSIGNED NOT NULL,
	storage_formats VARCHAR(50) NOT NULL,
	category VARCHAR(50) NOT NULL,
	PRIMARY KEY (art_number),
	CONSTRAINT fk_item_category 
	FOREIGN KEY (category) REFERENCES categories (category_name) 
	ON UPDATE CASCADE 
	ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `shopping_carts`(
	cart_id INT(5) AUTO_INCREMENT,
    PRIMARY KEY(cart_id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `cart_items` (
	art_number VARCHAR(50) NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    price FLOAT(8,2) NOT NULL,
    description TEXT,
    image BLOB,
    stock_balance INT UNSIGNED NOT NULL,
    storage_format VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    cart_id INT(5) NOT NULL,
    PRIMARY KEY(art_number),
    CONSTRAINT fk_item_cart
    FOREIGN KEY (cart_id) REFERENCES shopping_carts(cart_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT fk_item_cart_category
    FOREIGN KEY (category) REFERENCES categories(category_name)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `ordered_items` (
  art_number VARCHAR(50) NOT NULL,
  item_name VARCHAR(50) NOT NULL,
  price FLOAT(8,2) NOT NULL,
  stock_balance INT(10) UNSIGNED NOT NULL,
  storage_format VARCHAR(50) NOT NULL,
  order_id INT(5) UNSIGNED NOT NULL,
  PRIMARY KEY(art_number),
    CONSTRAINT fk_order
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS `orders` (
	order_id INT(5) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (order_id)
) ENGINE=INNODB;
