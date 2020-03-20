CREATE TABLE staff
(staffID text,
password text,
primary key (staffID));

CREATE TABLE table
(tableNum int,
seatsAvailable int,
occupied bool,
primary key (tableNum));

CREATE TABLE orders
(orderID int,
tableNum int,
timeOrdered bigint,
orderConfirmed bool,
primary key (orderID),
foreign key (tableNum) references table(tableID));

CREATE TABLE food
(foodID int,
foodName text,
foodDescription text,
calories int,
price decimal,
available bool,
primary key (foodID));

CREATE TABLE foodOrders
(orderID int,
foodID int,
primary key (orderID),
primary key (foodID),
foreign key (orderID) references orders(orderID),
foreign key (foodID) references food(foodID));

CREATE TABLE foodIngredients
(foodID int,
ingredients text,
primary key (foodID),
foreign key (foodID) references food(foodID));
