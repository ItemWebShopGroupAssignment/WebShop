USE `item_web_shop`;

DELIMITER $$

CREATE PROCEDURE `create_category` (
	IN category_name VARCHAR(50),
    IN contents TEXT
)
BEGIN
	INSERT INTO categories
    VALUES(category_name, contents);
END $$

CREATE PROCEDURE `remove_category` (
	IN category_name VARCHAR(50)
)
BEGIN
	DELETE FROM categories
    WHERE categories.category_name LIKE category_name
    COLLATE utf8_swedish_ci;
END $$

CREATE PROCEDURE `get_categories` ()
BEGIN
	SELECT * FROM categories;
END $$

DELIMITER ;