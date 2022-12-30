# stranger map

One year ago a friend of mine and I had an idea about a small app where people can see each other on a map. We want to create this app as a little challenge between christmas and new year. So this repository is the finished result of it.

There wasn't any change to it since one year. So I decided to do some cleanup as well as refreshing some functionality and make it public.

## Requirements
- Java 17
- yarn
 
## Setup

1. Run `./gradlew clean build` in the backend directory
2. Run `yarn install` in the frontend directory

## Run the app

1. Start a new redis instance: `docker run --name redis-dev -p 6379:6379 -d redis`
2. Run `./gradlew bootRun` in the backend directory
3. Run `yarn serve` in the frontend directory
4. Visit `http://localhost:8000` in your browser

## Features

1. Choose an emoji
2. Define a description
3. Join the server
4. See nearby users on the map
5. See the users and you are moving on the map
6. Show the description of a user by clicking on his name

## Architecture

### Frontend

Vue with vuetify is currently used for the frontend. There are two screens.
The "Join" screen where the user can choose his emoji and enter the description.

The "MainMap" shows all user emojis nearby, as well as your own user. Vuelayers is used to display the map.

The updated position of the current user is sent with each position update event to the server. It is done by a REST request for simplicity reasons. The response of this request contains the position of the others players nearby.

### API

For simplicity reasons, the API contains just 2 REST requests. Not even an authentication is necessary.

The "Join" request is called when the user joins the map. The request contains of the choosen emoji and a description. The response contains a session id as UUID for the user.

Position updates are send and received with the "Peers" request.

### Backend

The backend is mainly based on Redis functionalities. Redis is used to store sessions and the positions. Even the map calculations are done by Redis.

## Disclaimer

This project was started for fun and is not ready for production use.