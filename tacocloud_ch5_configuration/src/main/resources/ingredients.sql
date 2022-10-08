-- There are 2 Spring profile with 2 different datasources (H2 and MySQL). 
-- H2 respects the order in this list when Spring executes the script.
-- But MySQL reorders this list in alphabet order by id. 
-- This order in this list is differrent from previous chapters,
-- so that the JUnit tests can pass in both Spring profiles with 2 different orders of Ingredient data.

insert into Ingredient (id, name, type)
values ('COTO', 'Corn Tortilla', 0);
insert into Ingredient (id, name, type)
values ('FLTO', 'Flour Tortilla', 0);
insert into Ingredient (id, name, type)
values ('CARN', 'Carnitas', 1);
insert into Ingredient (id, name, type)
values ('GRBF', 'Ground Beef', 1);
insert into Ingredient (id, name, type)
values ('LETC', 'Lettuce', 2);
insert into Ingredient (id, name, type)
values ('TMTO', 'Diced Tomatoes', 2);
insert into Ingredient (id, name, type)
values ('CHED', 'Cheddar', 3);
insert into Ingredient (id, name, type)
values ('JACK', 'Monterrey Jack', 3);
insert into Ingredient (id, name, type)
values ('SLSA', 'Salsa', 4);
insert into Ingredient (id, name, type)
values ('SRCR', 'Sour Cream', 4);

insert into User (id, username, password, fullname, street, city, state, zip, phone_number)
values (0, 'habuma00', '$2a$10$5FMjSXhNdbgI0Jl1FSgOV.hEUgvg8y.7JElV3GJiKWApEG3Q8n5uq', 'Craig Walls', 
'123 North Street', 'Cross Roads', 'TX', '76227', '1231231234');
-- the 'id' field needs a default value for inserting