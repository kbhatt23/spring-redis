DROP TABLE IF EXISTS products;
CREATE TABLE products(
   product_id serial PRIMARY KEY,
   product_name VARCHAR (50),
   product_price numeric (10,2) NOT NULL
);