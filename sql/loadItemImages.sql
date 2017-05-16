USE item_web_shop;

UPDATE items
/*SET image = lOAD_FILE('../Database game covers/Dishonored_2_cover_art.jpg')*/
SET image = LOAD_FILE('C://Users/elev/workspace/ItemWebShop/Database game covers/Dishonored_2_cover_art.jpg')
WHERE art_number LIKE 'art:212-4527BE';