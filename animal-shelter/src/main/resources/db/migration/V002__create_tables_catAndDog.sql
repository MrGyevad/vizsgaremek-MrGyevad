create table cat (
    id integer not null auto_increment,
    name varchar(255),
    age int,
    breed varchar(255),
    gender varchar(255),
    last_play timestamp,
    has_water_and_food boolean,
    adopted boolean,
    best_friend_id integer,
    animal_shelter_id integer,
    primary key (id));
create table dog (
    id integer not null auto_increment,
    name varchar(255),
    age int,
    breed varchar(255),
    gender varchar(255),
    last_walk timestamp,
    has_water_and_food boolean,
    adopted boolean,
    best_friend_id integer,
    animal_shelter_id integer,
    primary key (id));
create table animal_shelter
(
    id   integer not null auto_increment,
    name varchar(255),
    primary key (id)
);
create table cat_lists(
    id integer not null,
    animal_shelter_id integer,
    cat_id integer,
    primary key (id));

create table dog_lists(
    id integer not null,
    animal_shelter_id integer,
    dog_id integer,
    primary key (id));
alter table cat
    add constraint cat_animal_shelter_fk
        foreign key (animal_shelter_id) references animal_shelter(id);
alter table cat
    add constraint cat_best_friend_fk
        foreign key (best_friend_id) references best_friend(id);
alter table dog
    add constraint dog_animal_shelter_fk
        foreign key (animal_shelter_id) references animal_shelter(id);
alter table dog
    add constraint dog_best_friend_fk
        foreign key (best_friend_id) references best_friend(id);
alter table cat_lists
    add constraint cat_list_animal_shelter_fk
        foreign key (animal_shelter_id) references animal_shelter(id);
alter table dog_lists
    add constraint dog_list_animal_shelter_fk
        foreign key (animal_shelter_id) references animal_shelter(id);