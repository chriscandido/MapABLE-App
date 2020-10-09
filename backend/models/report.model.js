const mongoose = require('mongoose');

const Schema = mongoose.Schema;

//user refers to those logging in to the app
// to be fixed - for Gilson
// Schema should contain: username, type, description, location, picture, date and time
const userSchema = new Schema({
  "username": {"type": "String"},
  "type": {"type": "String"},
  "description": {"type": "String"},
  "location": {"type": "String"},
  "datetime": {"type": "Date"},
});

const User = mongoose.model('User', userSchema);

module.exports = User;