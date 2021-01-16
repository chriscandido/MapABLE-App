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
  "status": { type: String, default: "Unverified" },
  "image": String,
  "geometry": {
    "type": { type: String, default: "Point" },
    "coordinates": {
      "type": [
        "Number" // Coordinates of Report [Longitude, Latitude]
      ]
    }
  }
});

const Report = mongoose.model('Report', reportSchema);

module.exports = Report;