CREATE TABLE user (
  user_id varchar(255) PRIMARY KEY NOT NULL,
  email varchar(255) UNIQUE,
  password varchar(255),
  user_name varchar(255) UNIQUE
);