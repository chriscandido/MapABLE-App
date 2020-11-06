const mongoose = require('mongoose');

const Schema = mongoose.Schema;

//user refers to those logging in to the app, log-in logs
const userSchema = new Schema({
  "name": String,
  "username": String,
  "date": { type: Date, default: Date.now }
});

const User = mongoose.model('User', userSchema);

module.exports = User;