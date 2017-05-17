USE item_web_shop;

INSERT INTO users
VALUES ('admin', 'abc123');

SELECT * FROM users
WHERE username LIKE 'admin'
COLLATE utf8_swedish_ci;