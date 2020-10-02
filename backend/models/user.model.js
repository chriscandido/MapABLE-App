const mongoose = require('mongoose');

const Schema = mongoose.Schema;

//user refers to those logging in to the app
const userSchema = new Schema({
  "name": {"type": "String"},
  "mobile_number": {"type": "String"},
  "email": {"type": "String"},
  "username": {"type": "String"},
  "password": {"type": "String"},
});

const User = mongoose.model('User', userSchema);

module.exports = User;