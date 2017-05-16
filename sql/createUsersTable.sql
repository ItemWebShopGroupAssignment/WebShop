USE item_web_shop;

CREATE TABLE users (
    username VARCHAR(50) NOT NULL,
    pwd VARCHAR(50) NOT NULL,
    PRIMARY KEY(username)
) ENGINE=INNODB;