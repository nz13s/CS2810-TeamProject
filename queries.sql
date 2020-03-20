
-- [1] queries to get all foods and their details (including ingredients) from a given orderID (1 an an example).

SELECT foodID FROM foodOrders WHERE orderID = 1;
--then make a for loop that uses these foodIDs and executes:
SELECT * FROM food WHERE foodID = 'variable';
SELECT ingredients FROM foodIngredients WHERE foodID = 'variable';


-- [2] query to get the table number for a given orderID (1 as an example).

SELECT tableNum FROM orders WHERE orderID = 1;


-- [3] query that checks if a table is occupied based on a given tableNum (1 as an example).
SELECT occupied FROM table WHERE tableNum == 1;


-- [4] query that changes the state of a table to occupied (table 1 as an example).
UPDATE table SET occupied = 'y' WHERE tableNum = 1;


-- [5] query that changes the state of a table to not occupied (table 1 as an example).
UPDATE table SET occupied = 'n' WHERE tableNum = 1;


-- [6] query that checks which orders have been confirmed.
SELECT orderID, tableNum, timeOrdered FROM orders WHERE timeOrdered != 0;


-- [7] query that checks which orders are ready to be served.
SELECT orderID, tableNum, timeOrdered FROM orders WHERE orderReady != 0;


-- [8] query that checks which orders have been served.
SELECT orderID, tableNum, timeOrdered FROM orders WHERE orderServed != 0;


-- [9] query that checks which tables are suitable, based on a given number of people (13 as an example).
SELECT tableNum, seatsAvailable FROM table WHERE occupied = 'n' AND seatsAvailable >= 13;











