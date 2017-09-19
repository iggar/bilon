# bilon

Find the nearest public bicycles around you.

## Configuration

This service uses the following environment variables:

`PORT` - PORT number where the server will run (default: 3000).

`NUM_RESULTS` - number of results to be shown (default: 5).

`BASE_COORDINATES` - coordinates of the point around which the search
   will be performed in the format: ``{:latitude <lat> :longitude <lon>}``
   (default: ``{:latitude 51.561948 :longitude -0.013139}` which are the
   coordinates for Leyton (London, UK).

## Usage

### Running from the console with Lein

Example: starting the server with default values:

```
lein run
```

Example: show ten results using the WebLoyalty building as a base reference:
```
NUM_RESULTS=10 BASE_COORDINATES="{:latitude 51.5147524 :longitude -0.1461681}" lein run
```

### Generating and executing uberjar

```
lein uberjar
```

then

```
java -jar target/uberjar/bilon-0.1.0-SNAPSHOT-standalone.jar
```

## Endpoints

#### `GET /api/bikelist`

Returns the results as an JSON object.
This resource has restricted access with basic authentication.
To fetch data, you need to provide `<user>:<password>`, e.g.:

```
curl -u juxt:txuj http://localhost:3000/api/bikelist
```


#### `GET /bikelist`

Returns the results as an HTML page with a table and links to Google Maps.

#### `GET /main`

Returns HTML page with some information and current configuration.


#### `GET /healthcheck`

Returns HTTP 200 with text "This is fine!"
