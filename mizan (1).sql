-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 25, 2025 at 05:16 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mizan`
--

-- --------------------------------------------------------

--
-- Table structure for table `budget`
--

CREATE TABLE `budget` (
  `budget_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `budget`
--

INSERT INTO `budget` (`budget_id`, `user_id`, `category_id`, `amount`, `start_date`, `end_date`, `is_active`) VALUES
(1, 1, NULL, 5000.00, '2025-03-01', '2025-03-31', 1),
(2, 1, 3, 800.00, '2025-03-01', '2025-03-31', 1),
(3, 1, 4, 300.00, '2025-03-01', '2025-03-31', 1),
(4, 1, 5, 500.00, '2025-03-01', '2025-03-31', 1),
(5, 1, 6, 1000.00, '2025-03-01', '2025-03-31', 1),
(6, 2, NULL, 7000.00, '2025-03-01', '2025-03-31', 1),
(7, 2, 13, 600.00, '2025-03-01', '2025-03-31', 1),
(8, 2, 14, 200.00, '2025-03-01', '2025-03-31', 1),
(9, 2, 15, 400.00, '2025-03-01', '2025-03-31', 1),
(10, 2, 16, 800.00, '2025-03-01', '2025-03-31', 1);

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `category_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL CHECK (`type` in ('Income','Expense')),
  `icon` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`category_id`, `user_id`, `name`, `type`, `icon`) VALUES
(1, 1, 'Salary', 'Income', 'salary-icon.png'),
(2, 1, 'Freelance', 'Income', 'freelance-icon.png'),
(3, 1, 'Food', 'Expense', 'food-icon.png'),
(4, 1, 'Transportation', 'Expense', 'transport-icon.png'),
(5, 1, 'Entertainment', 'Expense', 'entertainment-icon.png'),
(6, 1, 'Shopping', 'Expense', 'shopping-icon.png'),
(7, 1, 'Rent', 'Expense', 'rent-icon.png'),
(8, 1, 'Utilities', 'Expense', 'utilities-icon.png'),
(9, 1, 'Healthcare', 'Expense', 'health-icon.png'),
(10, 1, 'Education', 'Expense', 'education-icon.png'),
(11, 2, 'Salary', 'Income', 'salary-icon.png'),
(12, 2, 'Investment', 'Income', 'investment-icon.png'),
(13, 2, 'Food', 'Expense', 'food-icon.png'),
(14, 2, 'Transportation', 'Expense', 'transport-icon.png'),
(15, 2, 'Entertainment', 'Expense', 'entertainment-icon.png'),
(16, 2, 'Shopping', 'Expense', 'shopping-icon.png'),
(17, 2, 'Rent', 'Expense', 'rent-icon.png'),
(18, 2, 'Utilities', 'Expense', 'utilities-icon.png'),
(19, 2, 'Travel', 'Expense', 'travel-icon.png'),
(20, 2, 'Gifts', 'Expense', 'gifts-icon.png'),
(21, 1, 'Test Category', 'Income', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `type` varchar(20) NOT NULL CHECK (`type` in ('Income','Expense')),
  `date` date NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `user_id`, `category_id`, `amount`, `type`, `date`, `description`, `payment_method`) VALUES
(1, 1, 1, 8500.00, 'Income', '2025-11-01', 'Monthly Salary', 'Bank Transfer'),
(2, 1, 3, 45.50, 'Expense', '2025-11-02', 'Lunch at restaurant', 'Cash'),
(3, 1, 4, 30.00, 'Expense', '2025-11-03', 'Taxi fare', 'Cash'),
(4, 1, 5, 120.00, 'Expense', '2025-11-04', 'Movie tickets', 'Credit Card'),
(5, 1, 6, 250.75, 'Expense', '2025-11-05', 'Clothes shopping', 'Credit Card'),
(6, 1, 7, 2000.00, 'Expense', '2025-11-06', 'Monthly rent', 'Bank Transfer'),
(8, 1, 9, 150.00, 'Expense', '2025-11-08', 'Doctor visit', 'Cash'),
(9, 1, 10, 300.00, 'Expense', '2025-11-09', 'Online course', 'Credit Card'),
(10, 1, 2, 500.00, 'Income', '2025-11-10', 'Freelance project', 'Bank Transfer'),
(11, 2, 11, 12000.00, 'Income', '2025-03-01', 'Monthly Salary', 'Bank Transfer'),
(12, 2, 13, 35.00, 'Expense', '2025-03-02', 'Groceries', 'Cash'),
(13, 2, 14, 25.00, 'Expense', '2025-03-03', 'Bus fare', 'Cash'),
(14, 2, 15, 80.00, 'Expense', '2025-03-04', 'Concert tickets', 'Credit Card'),
(15, 2, 16, 180.50, 'Expense', '2025-03-05', 'Shoes', 'Credit Card'),
(16, 2, 17, 2500.00, 'Expense', '2025-03-06', 'Apartment rent', 'Bank Transfer'),
(17, 2, 18, 280.00, 'Expense', '2025-03-07', 'Water and internet', 'Bank Transfer'),
(18, 2, 19, 500.00, 'Expense', '2025-03-08', 'Weekend trip', 'Credit Card'),
(19, 2, 20, 75.00, 'Expense', '2025-03-09', 'Birthday gift', 'Cash'),
(20, 2, 12, 300.00, 'Income', '2025-03-10', 'Stock dividends', 'Bank Transfer'),
(21, 1, 21, 150.00, 'Income', '2025-11-08', 'Test Description - Updated', 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `created_at` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `email`, `password`, `phone_number`, `created_at`) VALUES
(1, 'ahmed_user', 'ahmed@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0501111111', '2025-01-15'),
(2, 'sara_finance', 'sara@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0502222222', '2025-01-16'),
(3, 'mohammed_sa', 'mohammed@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0503333333', '2025-01-17'),
(4, 'fatima_m', 'fatima@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0504444444', '2025-01-18'),
(5, 'khalid_2025', 'khalid@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0505555555', '2025-01-19'),
(6, 'nora_budget', 'nora@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0506666666', '2025-01-20'),
(7, 'faisal_money', 'faisal@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0507777777', '2025-01-21'),
(8, 'lama_saver', 'lama@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0508888888', '2025-01-22'),
(9, 'yousef_fin', 'yousef@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0509999999', '2025-01-23'),
(10, 'huda_personal', 'huda@email.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0501010101', '2025-01-24'),
(11, 'newuser', 'newuser@gmail.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '098765432', '2025-11-08'),
(12, 'test', 'test@gmail.com', '+/s4bv6mfoFvLdoKjJSpjrIDdXrrs/VfGDdVoZLURGc=', '0987654321', '2025-11-08');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `budget`
--
ALTER TABLE `budget`
  ADD PRIMARY KEY (`budget_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`category_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `budget`
--
ALTER TABLE `budget`
  MODIFY `budget_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `budget`
--
ALTER TABLE `budget`
  ADD CONSTRAINT `budget_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `budget_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE SET NULL;

--
-- Constraints for table `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `category_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
