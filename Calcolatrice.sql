-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Nov 10, 2023 alle 07:55
-- Versione del server: 10.4.28-MariaDB
-- Versione PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Calcolatrice`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `History`
--

CREATE TABLE `History` (
  `Id` int(255) NOT NULL,
  `Expression` text NOT NULL,
  `Result` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `History`
--

INSERT INTO `History` (`Id`, `Expression`, `Result`) VALUES
(3, '3+3', 6),
(3, '33 33 *', 1089),
(3, '132', 132),
(3, '9', 9),
(1, '9 33 *', 297),
(1, '8 4 +', 12),
(1, '3 3 +', 6),
(1, ' 3+3 ', 6),
(1, '3+3+6+9', 21),
(5, '3+3', 6),
(6, '6*6', 36),
(6, ' 36*6 ', 216);

-- --------------------------------------------------------

--
-- Struttura della tabella `Users`
--

CREATE TABLE `Users` (
  `Id` int(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `Users`
--

INSERT INTO `Users` (`Id`, `Username`, `Password`) VALUES
(1, 'Fransua', 'letmein'),
(3, 'Lolo', 'letmein'),
(4, 'ciao', 'ciao'),
(5, 'Monir', 'letmein'),
(6, 'Frigo', 'letmein');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `Users`
--
ALTER TABLE `Users`
  MODIFY `Id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
