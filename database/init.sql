CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','USER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `contact-hub`.`user`
(`id`,
`email`,
`first_name`,
`last_name`,
`password`,
`role`)
VALUES
(1,
"rumesha@mail.com",
"Rumesha",
"Ranathunga",
"$2a$10$KA2INoBSxSyT8mZ9TzEFgeyA0Elfpm.4lMzye0/n/gWSJYAaFNZZG",
"Admin");