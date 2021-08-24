CREATE TABLE animal_shelter
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)       NULL,
    CONSTRAINT pk_animalshelter PRIMARY KEY (id)
);

CREATE TABLE best_friend
(
    id     INT AUTO_INCREMENT NOT NULL,
    cat_id INT                NULL,
    dog_id INT                NULL,
    CONSTRAINT pk_bestfriend PRIMARY KEY (id)
);