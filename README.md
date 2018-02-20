# Clojure REST API example

## Prerequisite

 - [Docker](https://docs.docker.com/install/#server)
 - [Docker Compose](https://docs.docker.com/compose/install/)

## Instalation

Clone the repository on your computer

    https://github.com/gabriel-montagne/expenses.git (or ssh)

Navigate to the new directory

    $> cd expenses
    
Run docker-compose
    
    $> docker-compose up
    
NOTE:
If you want to rebuild the image run

    $> docker-compose up --build

## Running

After the containers are up and running open your browser and navigate to
 - [localhost:3000/swagger_ui](http://localhost:3000/swagger_ui)

NOTE:

 There is one user added during migrations: 
 
 ```
    id = '1'
    name = 'admin'
 ```
 you can use his id to test.

## Users management

TBD

## Token auth

TBD 

## OAuth

TBD