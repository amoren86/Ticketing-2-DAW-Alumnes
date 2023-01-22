-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jun 24, 2022 at 01:55 PM
-- Server version: 8.0.29
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `marianao`
--

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `name`) VALUES
(2, 'Sala JÚPITER'),
(1, 'Sala SATURN'),
(3, 'Sala VENUS');

--
-- Dumping data for table `companies`
--

INSERT INTO `companies` (`id`, `name`) VALUES
(2, 'T-SYSTEMS'),
(1, 'INET');

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `role`, `full_name`, `password`, `extension`, `room_id`, `place`, `company_id`) VALUES
('alex', 'EMPLOYEE', 'Àlex Macia Pérez', '$2a$10$D25CUjZHnolADZpCmQzWLee7eDX5LEA1jpa3chdrNZ8Ad5u4gHUbm', 3515, 1, 'L15', NULL),
('genis', 'TECHNICIAN', 'Genís Esteve i Prats', '$2a$10$fN.nOfWlRY/LLotIWiseoeh/foQ1vFCY9bnpXmK3k8.VwW7F1xoPi', 1515, NULL, NULL, 1),
('laia', 'SUPERVISOR', 'Laia Vives i Marsans', '$2a$10$EwsBI6trHD56ncjlsxAmwuic5R/qAzx6AyekBpCafndN.CiFuwJjK', 1501, NULL, NULL, 1),
('lola', 'EMPLOYEE', 'Lola Valls i Vilalta', '$2a$10$vTJ82FGgKP36.WHHtWAGNOvCt0bF27/0HzN9OFo1EuU722Z0PKhde', 2501, 3, 'L08', NULL),
('maria', 'EMPLOYEE', 'Maria Lopez i Castells', '$2a$10$EogCF6kJDxTPsfQFciZjROaSBd/8Ok3orVe49KdEebVdyVTYrCKs2', 3513, 1, 'L13', NULL),
('raul', 'TECHNICIAN', 'Raul Casanova i Ferrer', '$2a$10$Zt92wjlBEPx2zXwdTfA4ZeM2cFAAX4MXY4y9y1BKMEZmYbNh.8dz6', 1504, NULL, NULL, 2),
('robert', 'EMPLOYEE', 'Robert Planes i Pujol', '$2a$10$H1S18hqeIbIoPgVU7ECURu6nsitQ0/sGSEJ9Z0Dw6rBV/bloAmCTS', 3510, 1, 'L10', NULL),
('toni', 'EMPLOYEE', 'Antoni Bosc i Cases', '$2a$10$T1lgKgp5XiQAuvTiq4alQeWkgCVHpsHVgmgk/X7wIkJwHypR6TMP2', 2508, 3, 'L08', NULL);
COMMIT;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`id`, `category`, `description`) VALUES
(1, 'HARDWARE', 'El ratolí no funciona bé, a vegades desapareix o no es mou. A més sovint he de posar un paper a sota. Ho podeu revisar? Gràcies'),
(2, 'NETWORK', 'Internet funciona molt lent, fent servir el navegador Firefox les pàgines triguen molt a carregar-se i s\'obren finestres emergents de propaganda. He provat d\'instal·lar Chrome però em demana la contrasenya d\'administrador. Necessito una solució urgentment!!!'),
(3, 'SUPPORT', 'Estic intentant fer el desglossament de l\'IVA pels apunts importats al programa de comptabilitat però no me\'n surto. Necessito un cop de ma perquè la propera setmana ve l\'auditor. Gràcies'),
(4, 'PRINTER', 'No puc imprimir a doble cara a la impressora de la Sala VENUS. Tot i que marco l\'opció abans d\'enviar a imprimir documents no surten a doble cara');


--
-- Dumping data for table `actions`
--

INSERT INTO `actions` (`id`, `type`, `performer_username`, `date`, `ticket_id`, `priority`, `technician_username`, `description`, `hours`) VALUES
(1, 'OPENING', 'alex', '2019-10-01 08:15:31', 1, NULL, NULL, NULL, NULL),
(2, 'OPENING', 'maria', '2019-09-24 07:54:13', 2, NULL, NULL, NULL, NULL),
(3, 'OPENING', 'lola', '2019-09-24 17:32:04', 3, NULL, NULL, NULL, NULL),
(4, 'OPENING', 'toni', '2019-09-27 11:27:50', 4, NULL, NULL, NULL, NULL),
(5, 'ASSIGNMENT', 'laia', '2019-10-02 11:21:15', 1, 4, 'raul', NULL, NULL),
(6, 'INTERVENTION', 'raul', '2019-10-04 13:25:11', 1, NULL, NULL, 'És un ratolí inalàmbric, les piles estaven gastades. S\'han substituït però segueix sense funcionar bé del tot. Cal canviar el ratolí.', 2),
(7, 'INTERVENTION', 'genis', '2019-10-05 15:44:37', 1, NULL, NULL, 'Ratolí inalàmbric canviat per model USB.', 1),
(8, 'CLOSE', 'raul', '2019-10-05 19:02:00', 1, NULL, NULL, NULL, NULL),
(9, 'ASSIGNMENT', 'laia', '2019-09-26 13:39:14', 2, 9, 'genis', NULL, NULL),
(10, 'INTERVENTION', 'genis', '2019-09-26 15:15:54', 2, NULL, NULL, 'Google Chrome instal·lat. Firefox actualitzat i restablert a la configuració de fàbrica, l\'ordinador està ple de virus. S\'ha actualitzat l\'antivirus, un escaneig complet en profunditat i programació d\'una tasca per automatitzar un escaneig dos cops per setmana (dilluns i dijous al migdia)', 4),
(11, 'ASSIGNMENT', 'laia', '2019-09-29 14:32:04', 3, 3, 'raul', NULL, NULL);


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
