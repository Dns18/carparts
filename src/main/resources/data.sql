INSERT INTO part_category (id, name, description, shelf_location) VALUES
(1, 'Fékrendszer', 'Féktárcsák, betétek, féknyergek', 'A-1'),
(2, 'Futómű', 'Lengéscsillapítók, szilentek, rugók', 'B-2');

INSERT INTO car_part (id, part_number, name, price, stock_quantity, category_id) VALUES
(1, 'BRK-101', 'Hűtött féktárcsa 300mm', 24500, 12, 1),
(2, 'BRK-102', 'Fékbetét készlet első', 14900, 3, 1),
(3, 'SUS-201', 'Gázos lengéscsillapító', 31000, 6, 2);