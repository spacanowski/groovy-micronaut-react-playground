# groovy-micronaut-react-playground

Simple application in groovy using micronaut framework and react js as frontend

## Running backend localy

```
./gradlew run
```
Will start app on `localhost:8080`

## Running backend localy with db

```
cd local
docker-compose up &
MICRONAUT_ENVIRONMENTS=db ./gradlew run
```
Will start app on `localhost:8080`

## Running frontend localy

```
cd frontend-app
npm install
npm start
```
Will start and serve page at `localhost:3000`

## TODO

- [ ]  Add tests
- [ ]  Add javadocs
- [ ]  Add page styling
- [ ]  Add DB
