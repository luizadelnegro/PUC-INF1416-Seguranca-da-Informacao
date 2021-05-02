CREATE TABLE user(
    id integer NOT NULL auto_increment,
    email varchar(200),
    cert varchar(200),
    salt varchar(200),
    PRIMARY KEY (id)
);


SET character_set_client = utf8;
SET character_set_connection = utf8;
SET character_set_results = utf8;
SET collation_connection = utf8_general_ci;