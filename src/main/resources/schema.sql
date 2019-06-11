DROP TABLE IF EXISTS products;

CREATE TABLE products (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `description` varchar(255) COLLATE utf8_lithuanian_ci DEFAULT NULL,
    `image` varchar(255) COLLATE utf8_lithuanian_ci DEFAULT NULL,
    `name` varchar(20) COLLATE utf8_lithuanian_ci DEFAULT NULL,
    `price` decimal(19,2) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_lithuanian_ci;
