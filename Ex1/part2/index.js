const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const Utils = require('./Utils');
const app = express();

const port = process.env.PORT || 3000;

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'public')));
app.get('/', (req, res) => res.send('index.html'));
app
  .route('/api/distance')
  .get((req, res) => {
    const { lat1, lon1, lat2, lon2 } = req.query;
    if (!(lat1 && lon1 && lat2 && lon2))
      return res.status(400).send({ distance: null, status: 'INVALID_SYNTAX' });
    const distance = Utils.getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2);
    return res.send({ distance, status: 'OK' });
    // lat1=10.870324&lon1=106.803163&lat2=10.860936&lon2=106.758425
  })
  .post((req, res) => {
    const { coor1, coor2 } = req.body;
    if (!(coor1 && coor2))
      return res.status(400).send({ distance: null, status: 'INVALID_SYNTAX' });
    const lat1 = coor1.lat,
      lon1 = coor1.lon;
    const lat2 = coor2.lat,
      lon2 = coor2.lon;
    if (!(lat1 && lon1 && lat2 && lon2))
      return res.status(400).send({ distance: null, status: 'INVALID_VALUE' });
    const distance = Utils.getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2);
    return res.send({ distance, status: 'OK' });
  });
app.listen(port, () => console.log(`server is running at port ${port}`));
