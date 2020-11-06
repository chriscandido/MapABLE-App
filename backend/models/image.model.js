var mongoose = require('mongoose');

const Schema = mongoose.Schema;

var imageSchema = new Schema({
    "userID": String,
    "reportID": { type: String, default: 'ilalagay dito si objectID ni report pang link' },
    "date": String,
    "image": {data: Buffer,
            contentType: { type: String, default: 'image/png' }}
});

const Image = mongoose.model('Image', imageSchema);

module.exports = Image;