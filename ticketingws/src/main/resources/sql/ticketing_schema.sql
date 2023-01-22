-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Temps de generació: 04-12-2022 a les 15:58:16
-- Versió del servidor: 8.0.31
-- Versió de PHP: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de dades: `marianao`
--

-- --------------------------------------------------------

--
-- Estructura de la taula `actions`
--

CREATE TABLE `actions` (
  `type` varchar(31) NOT NULL,
  `id` bigint NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `priority` int DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `hours` int DEFAULT NULL,
  `performer_username` varchar(255) NOT NULL,
  `ticket_id` bigint NOT NULL,
  `technician_username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de la taula `companies`
--

CREATE TABLE `companies` (
  `id` bigint NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de la taula `rooms`
--

CREATE TABLE `rooms` (
  `id` bigint NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de la taula `tickets`
--

CREATE TABLE `tickets` (
  `id` bigint NOT NULL,
  `category` varchar(255) NOT NULL,
  `description` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de la taula `users`
--

CREATE TABLE `users` (
  `role` varchar(31) NOT NULL,
  `username` varchar(25) NOT NULL,
  `extension` int DEFAULT NULL,
  `full_name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `place` varchar(100) DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  `company_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Índexs per a les taules bolcades
--

--
-- Índexs per a la taula `actions`
--
ALTER TABLE `actions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ticket_x_date` (`ticket_id`,`date` DESC),
  ADD KEY `type` (`type`),
  ADD KEY `FKfg2ab9wjrg68jliat7wp1te1o` (`performer_username`),
  ADD KEY `FK6xlobo57y0tx70axtk9sdl6e9` (`technician_username`);

--
-- Índexs per a la taula `companies`
--
ALTER TABLE `companies`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_50ygfritln653mnfhxucoy8up` (`name`);

--
-- Índexs per a la taula `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_1kuqhbfxed2e8t571uo82n545` (`name`);

--
-- Índexs per a la taula `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`id`);

--
-- Índexs per a la taula `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`),
  ADD KEY `role` (`role`),
  ADD KEY `full_name` (`full_name`),
  ADD KEY `role_x_full_name` (`role`,`full_name`),
  ADD KEY `FKlp7mqwp35k0xb2vyjw7rsi9gb` (`room_id`),
  ADD KEY `FKin8gn4o1hpiwe6qe4ey7ykwq7` (`company_id`);

--
-- AUTO_INCREMENT per les taules bolcades
--

--
-- AUTO_INCREMENT per la taula `actions`
--
ALTER TABLE `actions`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la taula `companies`
--
ALTER TABLE `companies`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la taula `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la taula `tickets`
--
ALTER TABLE `tickets`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- Restriccions per a les taules bolcades
--

--
-- Restriccions per a la taula `actions`
--
ALTER TABLE `actions`
  ADD CONSTRAINT `FK6xlobo57y0tx70axtk9sdl6e9` FOREIGN KEY (`technician_username`) REFERENCES `users` (`username`),
  ADD CONSTRAINT `FK9ubu4eah8nrpxwpur4c88awoy` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`),
  ADD CONSTRAINT `FKfg2ab9wjrg68jliat7wp1te1o` FOREIGN KEY (`performer_username`) REFERENCES `users` (`username`);

--
-- Restriccions per a la taula `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKin8gn4o1hpiwe6qe4ey7ykwq7` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`),
  ADD CONSTRAINT `FKlp7mqwp35k0xb2vyjw7rsi9gb` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
