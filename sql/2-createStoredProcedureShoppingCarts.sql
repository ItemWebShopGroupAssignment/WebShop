USE `item_web_shop`

DELIMITER $$
/* Create a new shopping cart. */
CREATE PROCEDURE `create_shopping_cart`(
	IN cart_id INT(5) UNSIGNED
)
BEGIN
	INSERT INTO shopping_carts
    VALUES(cart_id);
END $$

/* Remove a shopping cart. */
CREATE PROCEDURE `remove_shopping_cart` (
	IN cart_id INT(5) UNSIGNED
)
BEGIN
	DELETE FROM shopping_carts
    WHERE shopping_carts.cart_id = cart_id;
END $$

/* Get the list of available shopping carts. */
CREATE PROCEDURE `get_shopping_carts` ()
BEGIN
	SELECT * FROM shopping_carts;
END $$

DELIMITER ;