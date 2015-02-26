-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-02-2015 a las 12:41:11
-- Versión del servidor: 5.6.20
-- Versión de PHP: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `announceit`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `anuncios`
--

CREATE TABLE IF NOT EXISTS `anuncios` (
  `Titulo` varchar(30) NOT NULL,
  `Descripción` varchar(200) NOT NULL,
  `Lugar` varchar(30) NOT NULL,
  `Dia` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `anuncios`
--

INSERT INTO `anuncios` (`Titulo`, `Descripción`, `Lugar`, `Dia`) VALUES
('Anuncio1', 'Esta es la descripción del primer anuncio a modo de prueba. Vamos a jugar al tenis en el patio de mi casa, ¿Quién se apunta? Aforo limitado a 10 personas, comida gratis, un saludo!', 'Barcelona calle una cualquiera', '27/05/2015 17.00'),
('Anuncio1', 'Esta es la descripción del primer anuncio a modo de prueba. Vamos a jugar al tenis en el patio de mi casa, ¿Quién se apunta? Aforo limitado a 10 personas, comida gratis, un saludo!', 'Barcelona calle una cualquiera', '27/05/2015 17.00'),
('Anuncio2', 'Esta es la descripción del primer anuncio a modo de prueba. Vamos a jugar al fútbol en el polideportivo de Alicante, ¿Quién se apunta? Aforo limitado a 22 personas, comida gratis, un saludo!', 'Alicante Calle deportiva', '15/07/2015 17.00'),
('Anuncio2', 'Esta es la descripción del primer anuncio a modo de prueba. Vamos a jugar al fútbol en el polideportivo de Alicante, ¿Quién se apunta? Aforo limitado a 22 personas, comida gratis, un saludo!', 'Alicante Calle deportiva', '15/07/2015 17.00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
