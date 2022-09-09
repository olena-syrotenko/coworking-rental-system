CREATE DATABASE `coworking`;
USE `coworking`;

CREATE TABLE `brand` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(30) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `XAK1brand` (`name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `equipment_type` (
     `id` int NOT NULL AUTO_INCREMENT,
     `name` varchar(30) NOT NULL,
     `price` decimal(10,2) NOT NULL,
     `image` varchar(45) DEFAULT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `XAK1equipment_type` (`name`),
     KEY `XIE1equipment_type` (`price`),
     CONSTRAINT `price` CHECK ((`price` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `equipment` (
    `id` int NOT NULL AUTO_INCREMENT,
    `id_equipment_type` int NOT NULL,
    `id_brand` int NOT NULL,
    `model` varchar(30) NOT NULL,
    `description` varchar(500) DEFAULT ' ',
    `amount` int NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `XAK1equipment` (`model`),
    KEY `R_10` (`id_equipment_type`),
    KEY `R_11` (`id_brand`),
    CONSTRAINT `equipment_ibfk_1` FOREIGN KEY (`id_equipment_type`) REFERENCES `equipment_type` (`id`),
    CONSTRAINT `equipment_ibfk_2` FOREIGN KEY (`id_brand`) REFERENCES `brand` (`id`),
    CONSTRAINT `amount` CHECK ((`amount` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `room_type` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(30) NOT NULL,
    `description` varchar(500) DEFAULT NULL,
    `image` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `XAK1room_type` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `room` (
    `id` int NOT NULL AUTO_INCREMENT,
    `area` decimal(5,1) NOT NULL,
    `max_places` int NOT NULL,
    `id_room_type` int NOT NULL,
    PRIMARY KEY (`id`),
    KEY `R_1` (`id_room_type`),
    CONSTRAINT `room_ibfk_1` FOREIGN KEY (`id_room_type`) REFERENCES `room_type` (`id`),
    CONSTRAINT `area` CHECK ((`area` > 0)),
    CONSTRAINT `max_places` CHECK ((`max_places` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `place` (
     `id` int NOT NULL AUTO_INCREMENT,
     `id_room` int NOT NULL,
     `area` decimal(5,1) NOT NULL,
     PRIMARY KEY (`id`),
     KEY `R_2` (`id_room`),
     CONSTRAINT `place_ibfk_1` FOREIGN KEY (`id_room`) REFERENCES `room` (`id`) ON DELETE CASCADE,
     CONSTRAINT `place_area` CHECK ((`area` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `time_unit` (
         `id` int NOT NULL AUTO_INCREMENT,
         `duration` varchar(30) NOT NULL,
         PRIMARY KEY (`id`),
         UNIQUE KEY `XAK1time_unit` (`duration`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tariff` (
      `id` int NOT NULL AUTO_INCREMENT,
      `name` varchar(30) NOT NULL,
      `id_time_unit` int NOT NULL,
      `id_room_type` int NOT NULL,
      `price` decimal(10,2) NOT NULL,
      PRIMARY KEY (`id`),
      UNIQUE KEY `XAK1tariff` (`name`),
      KEY `R_5` (`id_time_unit`),
      KEY `R_23` (`id_room_type`),
      CONSTRAINT `tariff_ibfk_1` FOREIGN KEY (`id_time_unit`) REFERENCES `time_unit` (`id`),
      CONSTRAINT `tariff_ibfk_2` FOREIGN KEY (`id_room_type`) REFERENCES `room_type` (`id`) ON DELETE CASCADE,
      CONSTRAINT `tariff_price` CHECK ((`price` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `service` (
       `id` int NOT NULL AUTO_INCREMENT,
       `name` varchar(30) NOT NULL,
       PRIMARY KEY (`id`),
       UNIQUE KEY `XAK1service` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tariff_has_service` (
                  `id_tariff` int NOT NULL,
                  `id_service` int NOT NULL,
                  PRIMARY KEY (`id_tariff`,`id_service`),
                  KEY `R_4` (`id_service`),
                  CONSTRAINT `tariff_has_service_ibfk_1` FOREIGN KEY (`id_tariff`) REFERENCES `tariff` (`id`) ON DELETE CASCADE,
                  CONSTRAINT `tariff_has_service_ibfk_2` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `role` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(30) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `XAK1role` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(30) NOT NULL,
    `password` varchar(100) NOT NULL,
    `id_role` int NOT NULL,
    `is_blocked` bit(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `XAK1user` (`email`),
    KEY `R_9` (`id_role`),
    CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_role`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `personal_data` (
    `first_name` varchar(30) NOT NULL,
    `last_name` varchar(30) NOT NULL,
    `middle_name` varchar(30) DEFAULT NULL,
    `passport_id` char(9) NOT NULL,
    `ITN` char(10) NOT NULL,
    `authority` varchar(60) NOT NULL,
    `id` int NOT NULL,
    `phone_number` varchar(12) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `XAK1personal_data` (`passport_id`,`ITN`),
    KEY `XIE1personal_data` (`first_name`,`last_name`,`middle_name`),
    CONSTRAINT `personal_data_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `status` (
      `id` int NOT NULL AUTO_INCREMENT,
      `name` varchar(30) NOT NULL,
      PRIMARY KEY (`id`),
      UNIQUE KEY `XAK1status` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rent_application` (
       `id` int NOT NULL AUTO_INCREMENT,
       `create_date` timestamp NOT NULL,
       `last_change` timestamp NULL DEFAULT NULL,
       `rent_start` timestamp NOT NULL,
       `rent_end` timestamp NOT NULL,
       `id_place` int NOT NULL,
       `id_status` int NOT NULL,
       `lease_agreement` varchar(12) DEFAULT NULL,
       `id_user` int NOT NULL,
       `id_admin` int DEFAULT NULL,
       `rent_amount` decimal(10,2) NOT NULL,
       `id_tariff` int NOT NULL,
       PRIMARY KEY (`id`),
       UNIQUE KEY `XAK1rent_application` (`lease_agreement`),
       KEY `R_12` (`id_place`),
       KEY `R_22` (`id_user`),
       KEY `R_24` (`id_tariff`),
       KEY `XIE1rent_application` (`rent_start`,`rent_end`),
       KEY `rent_application_ibfk_2` (`id_status`),
       KEY `id_admin` (`id_admin`),
       CONSTRAINT `rent_application_ibfk_1` FOREIGN KEY (`id_place`) REFERENCES `place` (`id`),
       CONSTRAINT `rent_application_ibfk_2` FOREIGN KEY (`id_status`) REFERENCES `status` (`id`) ON UPDATE CASCADE,
       CONSTRAINT `rent_application_ibfk_3` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`),
       CONSTRAINT `rent_application_ibfk_4` FOREIGN KEY (`id_tariff`) REFERENCES `tariff` (`id`),
       CONSTRAINT `rent_application_ibfk_5` FOREIGN KEY (`id_admin`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
       CONSTRAINT `DateRentValidation` CHECK ((`rent_start` < `rent_end`)),
       CONSTRAINT `rent_amount` CHECK ((`rent_amount` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rent_equipment` (
     `id_equipment` int NOT NULL,
     `id_application` int NOT NULL,
     `amount` int NOT NULL,
     PRIMARY KEY (`id_equipment`,`id_application`),
     KEY `R_20` (`id_application`),
     CONSTRAINT `rent_equipment_ibfk_1` FOREIGN KEY (`id_equipment`) REFERENCES `equipment` (`id`),
     CONSTRAINT `rent_equipment_ibfk_2` FOREIGN KEY (`id_application`) REFERENCES `rent_application` (`id`) ON DELETE CASCADE,
     CONSTRAINT `rent_eq_amount` CHECK ((`amount` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getTimeUnitCount`(_start timestamp, _end timestamp, _time_unit varchar(30)) RETURNS int
    READS SQL DATA
    DETERMINISTIC
BEGIN
RETURN CASE lower(_time_unit)
    WHEN 'hour' THEN hour(timediff(_start,_end))
    WHEN 'day' THEN timestampdiff(day,_start,_end) + 1
    WHEN 'week' THEN timestampdiff(week,_start,_end) + 1
    WHEN 'month' THEN timestampdiff(month,_start,_end) + 1
    WHEN 'year' THEN timestampdiff(year,_start,_end) + 1
    ELSE 0
END;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AvailableEquipment`(IN _start_date Timestamp, IN _end_date Timestamp)
BEGIN
SELECT e.id, (amount - coalesce(rentedAmount,0)) AS availableAmount
FROM equipment e LEFT JOIN (SELECT id_equipment, amount AS rentedAmount
    FROM rent_application JOIN rent_equipment ON rent_application.id = id_application
    WHERE id_status NOT IN (SELECT id FROM status WHERE name IN ('Відхилено','Скасовано')) AND
          ((_start_date between rent_start AND rent_end) OR
           ( rent_start between _start_date AND _end_date ))) temp_t ON e.id = temp_t.id_equipment
WHERE amount - coalesce(rentedAmount,0) > 0;
END ;;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AvailableRoom`( IN _start_date Timestamp, IN _end_date Timestamp, IN _id_room_type int)
BEGIN
SELECT room.id AS id_room, room.area, count(place.id) AS availableAmount, id_room_type, name, description, image
FROM room JOIN room_type ON id_room_type = room_type.id
    JOIN place ON room.id = place.id_room
WHERE id_room_type = _id_room_type AND place.id NOT IN
	(SELECT id_place FROM rent_application
     WHERE id_status NOT IN (SELECT id FROM status WHERE name IN ('Відхилено','Скасовано')) AND
     ((_start_date between rent_start AND rent_end) OR
     ( rent_start between _start_date AND _end_date )))
GROUP BY room.id
ORDER BY room.id;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckPayment`()
BEGIN
SET SQL_SAFE_UPDATES = 0;
UPDATE rent_application
SET id_status = (Select id FROM status WHERE name = 'Скасовано')
WHERE id IN (SELECT id FROM (SELECT id FROM rent_application
WHERE ((timestampdiff(day, create_date, rent_start) > 2 AND timestampdiff(day, now(), rent_start) < 2)
           OR (timestampdiff(day, create_date, rent_start) <= 2 AND timestampdiff(hour, now(), rent_start) <= 1))
  AND id_status = (Select id FROM status WHERE name = 'Підтверджено')) AS tmp) ;
SET SQL_SAFE_UPDATES = 1;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `FirstAvailablePlace`(IN _start_date Timestamp, IN _end_date Timestamp, IN _id_room int)
BEGIN
SELECT place.id, place.area, place.id_room
FROM place
WHERE id_room = _id_room AND place.id NOT IN
	(SELECT id_place FROM rent_application
     WHERE id_status NOT IN (SELECT id FROM status WHERE name IN ('Відхилено','Скасовано')) AND
     ((_start_date between rent_start AND rent_end) OR
     ( rent_start between _start_date AND _end_date )))
LIMIT 1;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `CountRentAmountBeforeInsert` BEFORE INSERT ON `rent_application` FOR EACH ROW
    SET new.rent_amount = getTimeUnitCount(new.rent_start, new.rent_end,
  				(SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = new.id_tariff))*
                  (SELECT price FROM tariff WHERE tariff.id = new.id_tariff);;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `SetDatesBeforeInsert` BEFORE INSERT ON `rent_application` FOR EACH ROW 
    BEGIN
    DECLARE dur VARCHAR(30);
    SET dur = (SELECT duration FROM tariff JOIN time_unit ON time_unit.id = id_time_unit WHERE tariff.id = new.id_tariff);
    IF dur != 'hour'
    THEN SET new.rent_start = timestamp(date_add(new.rent_start,interval 8 hour));
 	     SET new.rent_end = timestamp(date_sub(date_sub(date_sub(new.rent_end,interval 59 second),interval 59 minute), interval 3 hour));
END IF;
SET new.create_date = now();
 SET new.last_change = now();
END ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `CountRentAmountBeforeUpdate` BEFORE UPDATE ON `rent_application` FOR EACH ROW
    SET new.rent_amount = getTimeUnitCount(new.rent_start, new.rent_end,
    				(SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = new.id_tariff))*
                    (SELECT price FROM tariff WHERE tariff.id = new.id_tariff) +
                    (SELECT coalesce(sum(rent_equipment.amount * equipment_type.price*getTimeUnitCount(new.rent_start, new.rent_end,
                        (SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = new.id_tariff))),0)
  					    FROM rent_equipment JOIN equipment ON equipment.id = rent_equipment.id_equipment
                            JOIN equipment_type ON equipment_type.id = equipment.id_equipment_type
                        WHERE rent_equipment.id_application = new.id) ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `CheckStatusBeforeUpdate` BEFORE UPDATE ON `rent_application` FOR EACH ROW begin
    IF new.id_status != (SELECT id FROM status WHERE name = 'Сплачено') AND new.lease_agreement is not null
    THEN SIGNAL SQLSTATE '45000' SET message_text = 'You cannot add lease agreement number while rent application is not paid';
END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `GenerateLeaseAgreementNumberBeforeUpdate` BEFORE UPDATE ON `rent_application` FOR EACH ROW begin
    IF new.id_status = (SELECT id FROM status WHERE name = 'Сплачено')
    THEN SET new.lease_agreement = concat('CWH-',new.id,'-',month(new.rent_start),'/',day(new.rent_start));
END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `LastChangeBeforeUpdate` BEFORE UPDATE ON `rent_application` FOR EACH ROW SET new.last_change = now() ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `CheckEquipmentAmountBeforeInsert` BEFORE INSERT ON `rent_equipment` FOR EACH ROW begin
DECLARE rentedAmount int;
SET rentedAmount = (SELECT sum(amount) FROM rent_equipment WHERE rent_equipment.id_application= new.id_application);
IF (coalesce(rentedAmount, 0) + new.amount) > 2
THEN SIGNAL SQLSTATE '45000' SET message_text = 'You cannot rent more than 2 numbers of one type of equipment';
END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `IncreaseRentAmountAfterInsert` AFTER INSERT ON `rent_equipment` FOR EACH ROW UPDATE rent_application
SET rent_amount = rent_amount + new.amount * (SELECT price FROM equipment JOIN equipment_type ON equipment_type.id = equipment.id_equipment_type
WHERE equipment.id = new.id_equipment) * getTimeUnitCount(rent_start, rent_end,
    				(SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = id_tariff))
WHERE id = new.id_application ;;
DELIMITER ;

DELIMITER ;;
TRIGGER `CheckEquipmentAmountBeforeUpdate` BEFORE UPDATE ON `rent_equipment` FOR EACH ROW begin
DECLARE rentedAmount int;
SET rentedAmount = (SELECT SUM(amount) FROM rent_equipment WHERE rent_equipment.id_application= new.id_application);
IF (COALESCE(rentedAmount, 0) - old.amount + new.amount) > 2
THEN SIGNAL SQLSTATE '45000' SET message_text = 'You cannot rent more than 2 numbers of one type of equipment';
END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `ChangeRentAmountAfterUpdate` AFTER UPDATE ON `rent_equipment` FOR EACH ROW
    UPDATE rent_application
    SET rent_amount = rent_amount - old.amount * (SELECT price FROM equipment JOIN equipment_type ON equipment_type.id = equipment.id_equipment_type
WHERE equipment.id = old.id_equipment) * getTimeUnitCount(rent_start, rent_end,
    				(SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = id_tariff))
+ new.amount * (SELECT price FROM equipment JOIN equipment_type ON equipment_type.id = equipment.id_equipment_type
WHERE equipment.id = new.id_equipment) * getTimeUnitCount(rent_start, rent_end,
    				(SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = id_tariff))
WHERE id = new.id_application ;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `DecreaseRentAmountAfterDelete` AFTER DELETE ON `rent_equipment` FOR EACH ROW
    UPDATE rent_application
    SET rent_amount = rent_amount - old.amount * (SELECT price FROM equipment JOIN equipment_type ON equipment_type.id = equipment.id_equipment_type
WHERE equipment.id = old.id_equipment) * getTimeUnitCount(rent_start, rent_end,
    				(SELECT duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = id_tariff))
WHERE id = old.id_application;;
DELIMITER ;
