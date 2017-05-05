USE `item_web_shop`;

/* Add some categories. */
CALL `create_category` ('Action', 'Intense Games.');
CALL `create_category` ('Adventure', 'Games about exploration and adventure.');
CALL `create_category` ('RTS', 'Real-time strategy games.');
CALL `create_category` ('MMORPG', 'Massive-Multiplayer Online Role Playing Games.');
CALL `create_category` ('TB', 'Turn Based Games.');
CALL `create_category` ('CG', 'Children Games.');
CALL `create_category` ('Sim', 'Simulation Games.');

CALL `insert_into_inventory`('art:532-4527EA', 'Battlefield 3000', 599.00, 'A game about soldiers killing each other for no reason.', 
	NULL, 10, 'PC HDDVD', 'Action');
CALL `insert_into_inventory`('art:572-4527BE', 'World of Warcraft 2.0', 599.00, 'A game about a fantasy world.', 
	NULL, 10, 'PC Download', 'MMORPG');
CALL `insert_into_inventory`('art:512-4527EA', 'Overlord 7', 699.00, 'A game about being an evil overlord.', 
	NULL, 7, 'PS5', 'Adventure');
    CALL `insert_into_inventory`('art:533-4527FI', 'Underlord 7', 699.00, 'A game about being an evil underlord.', 
	NULL, 13, 'PC', 'Adventure');
    CALL `insert_into_inventory`('art:422-4527BA', 'Civilization III', 499.00, 'The perfect strategy game.', 
	NULL, 10, 'PC', 'TB');
    CALL `insert_into_inventory`('art:511-4527EA', 'Football Manager 33', 699.00, 'Svennis Edition.', 
	NULL, 8, 'PS5', 'Sim');
    CALL `insert_into_inventory`('art:212-4527BE', 'Age of Evil', 599.00, 'A game about being evil.', 
	NULL, 5, 'PC Download', 'Adventure');
    CALL `insert_into_inventory`('art:544-4527EA', 'Bolibompa', 599.00, 'A game for children.', 
	NULL, 11, 'PS5', 'CG');