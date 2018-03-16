# Exercise part 2

## Technology

* NodeJS
* Express (v4.16.2)
* Body-parser (1.18.2)
* HTML5
* jQuery (v3.3.1)
* Bootstrap (v3.3.7)

## How to use

* Install dependencies: `yarn` (yarn) or `npm install` (npm)
* Run server: `node index`

## Note

* Api path: `/api/distance`
* Support both get and post request

### Get request

* Pass data through params in url: **lat1**, **lon1**, **lat2**, **lon2**
* Example:

```url
http://localhost:3000/api/distance?lat1=10.870324&lon1=106.803163&lat2=10.860936&lon2=106.758425
```

### Post request

* Pass data through body (json): **coor1**, **coor2**
* Example:

```json
{
  "coor1": {
    "lat": 10.870324,
    "lon": 106.803163
  },
  "coor2": {
    "lat": 10.860936,
    "lon": 106.758425
  }
}
```

### Try get request on browser

* Run server
* Open browser and access url:

```url
http://localhost:3000
```

* Enter two coordinator
* Click `Get distance` button

## Deployed server: [https://distance-cal.herokuapp.com/](https://distance-cal.herokuapp.com/ "Distance")
