# Smart Campus Sensor & Room Management API

This project is a RESTful API developed using JAX-RS for the Smart Campus coursework. It manages rooms, sensors, and sensor readings in a campus environment.

## Technologies Used

The main technologies used in this project are:

- Java 17
- Maven
- JAX-RS (Jersey)
- Grizzly HTTP Server
- Jackson for JSON processing
- HashMap and ArrayList for in-memory storage

Java is used as the main programming language. Maven is used to manage dependencies and build the project. JAX-RS is used to create the REST API. Grizzly is used as the lightweight server to run the application. Jackson is used to convert Java objects into JSON responses and to read JSON request bodies.

## Step 01: How to build and run

Before running the project, install Java and Maven to the system.

### Step 01: Open Terminal
Open the terminal and go to the project folder.

### Step 02: Compile the project
Run the following command:

## Step 02: Compile the project

Run the following command:

```bash
mvn compile
```

1. Run
```
mvn exec:java
```

2. Open
```
http://localhost:8080/api/v1

Commit message:
```

Add README introduction and run instructions

## API Endpoints

The API contains three main areas: rooms, sensors and sensor readings.

### Rooms

```
- `GET /api/v1/rooms` - get all rooms
- `POST /api/v1/rooms` - create a new room
- `GET /api/v1/rooms/{roomId}` - get one room by ID
- `DELETE /api/v1/rooms/{roomId}` - delete a room
```

