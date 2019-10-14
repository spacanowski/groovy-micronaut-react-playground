create table campain (
    id varchar(50) PRIMARY KEY,
    campain_date date,
    datasource varchar(256),
    campain_name varchar(256),
    clicks int,
    impressions int
);

create table data_loaded (
    id varchar(50) PRIMARY KEY,
    is_data_loaded bool DEFAULT 'f'
);

insert into data_loaded(id, is_data_loaded) values (1, 'f');