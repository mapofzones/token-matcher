# token-matcher

Status of Last Deployment:<br>
<img src="https://github.com/mapofzones/token-matcher/workflows/Java%20CI/badge.svg"><br>

## Requirements

Running directly:
* java 11
* maven

Running in a container:
* Docker

## Usage

Running directly:
* `mvn package -DskipTests` or `mvn package`
* `java -jar /opt/app.jar --spring.profiles.active=prod`

Config parameters:
* `TM_SYNC_TIME` - time between iterations in 'TokenMatcher' 
* `TM_THREADS` - number of threads for 'TokenMatcher'
* `PF_SYNC_TIME`time between iterations in 'PathFinder'
* `PF_THREADS` - number of threads for 'PathFinder'
* `TPF_SYNC_TIME` - time between iterations in 'TokenPriceFinder'
* `TPF_THREADS` - number of threads for 'TokenPriceFinder'
* `PRO_API_KEY` - key for coingecko "pro-api"
* `DB_URL` - URL of DB
* `DB_USER` - DB username
* `DB_PASS` - DB password

Running in a container:
* `docker build -t token-matcher:v1 .`
* `docker run --env TM_SYNC_TIME="120s" --env TM_THREADS=4  --env PF_SYNC_TIME="120s" --env PF_THREADS=1 --env TPF_SYNC_TIME="120s" --env TPF_THREADS=1 --env DB_URL=jdbc:postgresql://<ip>:<port>/<db> --env DB_USER=<db_user> --env DB_PASS=<db_user_pass> --env PRO_API_KEY=<pro-api-key> -it -d --network="host" token-matcher:v1`