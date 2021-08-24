CREATE TABLE cat
(
    id                 INT AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255)       NULL,
    age                INT                NULL,
    breed              VARCHAR(255)       NULL,
    gender             VARCHAR(255)       NULL,
    last_play          datetime           NULL,
    has_water_and_food BIT(1)             NULL,
    adopted            BIT(1)             NULL,
    animal_shelter_id  INT                NULL,
    CONSTRAINT pk_cat PRIMARY KEY (id)
);

CREATE TABLE dog
(
    id                 INT AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255)       NULL,
    age                INT                NULL,
    breed              VARCHAR(255)       NULL,
    gender             VARCHAR(255)       NULL,
    last_walk          datetime           NULL,
    has_water_and_food BIT(1)             NULL,
    adopted            BIT(1)             NULL,
    animal_shelter_id  INT                NULL,
    CONSTRAINT pk_dog PRIMARY KEY (id)
);

ALTER TABLE best_friend
    ADD CONSTRAINT FK_BESTFRIEND_ON_CAT FOREIGN KEY (cat_id) REFERENCES cat (id);

ALTER TABLE best_friend
    ADD CONSTRAINT FK_BESTFRIEND_ON_DOG FOREIGN KEY (dog_id) REFERENCES dog (id);

ALTER TABLE cat
    ADD CONSTRAINT FK_CAT_ON_ANIMALSHELTERID FOREIGN KEY (animal_shelter_id) REFERENCES animal_shelter (id);

ALTER TABLE dog
    ADD CONSTRAINT FK_DOG_ON_ANIMALSHELTERID FOREIGN KEY (animal_shelter_id) REFERENCES animal_shelter (id);