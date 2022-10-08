-- If there’s a file named schema.sql in the root of the application’s classpath, 
-- then the SQL in that file will be executed against the database when the application starts
-- Spring Boot will also execute a file named data.sql from the root of the classpath when application starts
-- file names must be schema.sql and data.sql
create table if not exists Ingredient (
id varchar(4) not null primary key,
name varchar(25) not null,
type varchar(10) not null
);

create table if not exists Taco (
id int GENERATED BY DEFAULT AS IDENTITY primary key,
name varchar(50) not null,
createdAt timestamp not null
);

create table if not exists Taco_Ingredients (
taco bigint not null,
ingredient varchar(4) not null
);

alter table Taco_Ingredients
add foreign key (taco) references Taco(id);

alter table Taco_Ingredients
add foreign key (ingredient) references Ingredient(id);

create table if not exists Taco_Order (
id int GENERATED BY DEFAULT AS IDENTITY primary key,
deliveryName varchar(50) not null,
deliveryStreet varchar(50) not null,
deliveryCity varchar(50) not null,
deliveryState varchar(10) not null,
deliveryZip varchar(10) not null,
ccNumber varchar(16) not null,
ccExpiration varchar(5) not null,
ccCVV varchar(3) not null,
placedAt timestamp not null
);

create table if not exists Taco_Order_Tacos (
tacoOrder bigint not null,
taco bigint not null
);

alter table Taco_Order_Tacos
add foreign key (tacoOrder) references Taco_Order(id);

alter table Taco_Order_Tacos
add foreign key (taco) references Taco(id);