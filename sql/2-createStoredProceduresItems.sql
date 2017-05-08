CREATE SCHEMA IF NOT EXISTS `item_web_shop`;

USE `item_web_shop`;

DELIMITER $$

/* Insert a new item to the inventory. */
CREATE PROCEDURE `insert_into_inventory`(
	IN art_number VARCHAR(50),
    IN item_name VARCHAR(50),
    IN price FLOAT(8,2),
    IN description TEXT,
    IN image BLOB,
    IN amount INT(5) UNSIGNED,
    IN storage_format VARCHAR(50),
    IN category VARCHAR(50)
)
BEGIN
	INSERT INTO items
    VALUES(art_number, item_name, price, description, image, amount,
     storage_format, category);
END$$

/* Removes an item from the ionventory. */
CREATE PROCEDURE `remove_from_inventory` (
    IN art_number VARCHAR(50)
)
BEGIN
	DELETE FROM items
    WHERE items.art_number LIKE art_number
        COLLATE utf8_swedish_ci;
END$$

/* Update the amount of an existing inventory item. */
CREATE PROCEDURE `set_amount_to_inventory` (
	IN art_number VARCHAR(50),
    IN amount INT(5) UNSIGNED
)
BEGIN
	UPDATE items
    SET stock_balance = amount
    WHERE items.art_number LIKE art_number
        COLLATE utf8_swedish_ci;
END $$

/* Get all items from the inventory. */
CREATE PROCEDURE `get_inventory_items` (
)
BEGIN
	SELECT * FROM items;
END $$

/* Get the selected item from the inventory. */
CREATE PROCEDURE `get_inventory_item` (
    IN art_number VARCHAR(50)
)
BEGIN
	SELECT * FROM items
    WHERE items.art_number LIKE art_number
        COLLATE utf8_swedish_ci;
END $$

DELIMITER ;