CREATE TABLE product (
  product_id varchar(255) PRIMARY KEY NOT NULL,
  description varchar(255),
  name varchar(255),
  price decimal(19,2),
  user_id varchar(255) NOT NULL
);