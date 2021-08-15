Az applikáció egy állatmenhelyi nyilvántartó, melyben különböző adatokat lehet eltárolni a menhelyen élő állatokról.


Docker utasítások:

mvn clean package

docker build -t animalshelterapp .

docker stop animalshelterapp

docker rm animalshelterapp

docker run --name animalshelterapp --network animalshelternetwork -p 8080:8080 -d animalshelterapp



# Vizsgaremek

A feladatod egy backend API projekt elkészítése, általad választott témában.  
A témákhoz összeszedtünk néhány ötletet, kérlek írd be magad ahhoz a témához, amit te választanál. Érdemes mindenkinek egyedi alkalmazást készíteni, próbáljatok meg osztozkodni a témákon.  
Nem csak ezek közül a témák közül lehet választani, ha saját ötleted van, akkor nyugodtan írd hozzá a listához.

[témaötletek](https://docs.google.com/document/d/1F30RkobWaX8L44ikgZ3GXKc0w7bEcgfUXIGEzSGSwHM/edit?usp=sharing)

## Követelmények

* Maven projekt
* Spring Boot alkalmazás
* REST API, Swagger, OpenAPI dokumentáció
* SQL backend (pl. MySQL, MariaDB)
* Flyway sémamigráció, SQL táblalétrehozás, adatbetöltés
* Hibakezelés
* Spring Data JPA repository
* Integrációs tesztek
* Konténerizált alkalmazás

## Feladat nagysága

* Legalább két 1-n kapcsolatban lévő tábla
* Legalább két SQL migráció
* Legalább két entitás
* Legalább két controller
* Minden bemenő paraméter validálása
* Legalább egy property beolvasása
* Minden HTTP metódusra legalább egy végpont (`GET`, `POST`, `PUT`, `DELETE`)
* Legalább 60%-os tesztlefedettség, amely tartalmaz egység és integrációs teszteket is
* Egy `Dockerfile`
