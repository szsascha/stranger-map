# stranger map

One year ago a friend of mine and I had an idea about a small app where people can see each other on a map. We want to create this app as a little challenge between christmas and new year. So this repository is the finished result of it.

There wasn't any change to it since one year. So I decided to do some cleanup as well as refreshing some functionality and make it public.

## Requirements
- Java 17
 
## Setup

1. Run `./gradlew clean build` in the backend directory

## Run the app

1. Start a new redis instance: `docker run --name redis-dev -p 6379:6379 -d redis`
2. Run `./gradlew bootRun` in the backend directory
