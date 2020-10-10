const mongoose = require('mongoose');

const Schema = mongoose.Schema;

//user refers to those logging in to the app
// to be fixed - for Gilson
// Schema should contain: username, type, description, location, picture, date and time
const reportSchema = new Schema({
  "report": {"type": "String"},
});

const Report = mongoose.model('Report', reportSchema);

module.exports = Report;