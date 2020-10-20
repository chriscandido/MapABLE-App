const mongoose = require('mongoose');

const Schema = mongoose.Schema;

//user refers to those logging in to the app
// to be fixed - for Gilson
// Schema should contain: username, type, description, location, picture, date and time
const reportSchema = new Schema({
  "username": String,
  "type": String,
  "description": String,
  "date": { type: Date, default: Date.now },
  // "img": {data: Buffer, contentType: String},
  "geometry": {
    "type": {
      "type": "String" // usually just Point
    },
    "coordinates": {
      "type": [
        "Number" // Coordinates of Hospital [Longitude, Latitude]
      ]
    }
  }
});

const Report = mongoose.model('Report', reportSchema);

module.exports = Report;