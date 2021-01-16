const mongoose = require('mongoose');

const Schema = mongoose.Schema;

//user refers to those logging in to the app
const userSchema = new Schema({
  "name": String,
  "number": String,
  "email": String,
  "username": String,
  "password": String,
  "numOfReports": {type: Number, default: 0},
  "image": {data: Buffer,
    contentType: { type: String, default: 'image/jpeg' }}
});

const User = mongoose.model('User', userSchema);

module.exports = User;