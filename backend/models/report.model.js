const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// Report Schema
const reportSchema = new Schema({
  "userID": String,
  "type": String,
  "incident": String,
  "frequency": String,
  "date": { type: Date, default: Date.now },
  "report": [String],
  "image": String,
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