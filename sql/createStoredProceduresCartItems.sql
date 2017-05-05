USE `item_web_shop`;

DELIMITER $$

/* Insert a new item to the cart. */
CREATE PROCEDURE `insert_into_cart`(
	IN art_number VARCHAR(50),
    IN item_name VARCHAR(50),
    IN price FLOAT(8,2),
    IN description TEXT,
    IN image BLOB,
    IN amount INT(5) UNSIGNED,
    IN storage_format VARCHAR(50),
    IN category VARCHAR(50),
    IN cart_id INT(5) UNSIGNED
)
BEGIN
	INSERT INTO cart_items
    VALUES(art_number, item_name, price, description, image, amount,
     storage_format, category, cart_id);
END$$

/* Removes an item from the cart. */
CREATE PROCEDURE `remove_from_cart` (
	IN cart_id INT(5) UNSIGNED,
    IN art_number VARCHAR(50)
)
BEGIN
	DELETE FROM cart_items
    WHERE cart_items.cart_id = cart_id
		AND cart_items.art_number LIKE art_number
        COLLATE utf8_swedish_ci;
END$$

/* Set the amount of an existing cart item. */
CREATE PROCEDURE `set_amount_to_cart` (
	IN cart_id INT(5) UNSIGNED,
	IN art_number VARCHAR(50),
    IN amount INT(5) UNSIGNED
)
BEGIN
	UPDATE cart_items
    SET stock_balance = amount
    WHERE cart_items.cart_id = cart_id
		AND cart_items.art_number LIKE art_number
        COLLATE utf8_swedish_ci;
END $$

/* Get all items of the specified cart. */
CREATE PROCEDURE `get_cart_items` (
	IN cart_id INT(5) UNSIGNED
)
BEGIN
	SELECT art_number, item_name, price, description, image, stock_balance, storage_format, category FROM cart_items
    WHERE cart_items.cart_id = cart_id;
END $$

DELIMITER ;