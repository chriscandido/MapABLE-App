var mongoose = require('mongoose');

const Schema = mongoose.Schema;

var imageSchema = new Schema({
    "userID": String,
    "reportID": String,
    "date": { type: Date, default: Date.now },
    "image": {data: Buffer,
            contentType: { type: String, default: 'image/png' }}
});

const Image = mongoose.model('Image', imageSchema);

module.exports = Image;