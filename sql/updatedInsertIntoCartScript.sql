USE `item_web_shop`;
DROP procedure IF EXISTS `insert_into_cart`;

DELIMITER $$
USE `item_web_shop`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_into_cart`(
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
     storage_format, category, cart_id, art_number + cart_id);
END$$

DELIMITER ;

