USE `item_web_shop`;

/* Add some categories. */
CALL `create_category` ('Action', 'Intense Games.');
CALL `create_category` ('Adventure', 'Games about exploration and adventure.');
CALL `create_category` ('RTS', 'Real-time strategy games.');
CALL `create_category` ('MMORPG', 'Massive-Multiplayer Online Role Playing Games.');

CALL `create_shopping_cart` (1);
CALL `insert_into_cart`('art:532-4527EA', 'Battlefield 3000', 599.00, 'A game about soldiers killing each other for no reason.', 
	NULL, 1, 'PC HDDVD', 'Action', 1);
CALL `insert_into_cart`('art:572-4527BE', 'World of Warcraft 2.0', 599.00, 'A game about a fantasy world.', 
	NULL, 1, 'PC Download', 'MMORPG', 1);
CALL `insert_into_cart`('art:512-4527EA', 'Overlord 7', 699.00, 'A game about being an evil overlord.', 
	NULL, 2, 'PS5', 'Adventure', 1);