const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const reportSchema = new Schema({
  "userID": String,
  "type": String,
  "date": String,
  // "date": { type: Date, default: Date.now },
  "report": String,
  "image": String,
  // "img": {data: Buffer, contentType: String},
  "geometry": {
    "type": {
      "type": "String" // usually just Point
    },
    "coordinates": {
      "type": [
        "Number" // Coordinates of Report [Longitude, Latitude]
      ]
    }
  }
});

const Report = mongoose.model('Report', reportSchema);

module.exports = Report;