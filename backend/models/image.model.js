var mongoose = require('mongoose');

const Schema = mongoose.Schema;

var imageSchema = new Schema({
    "userID": { type: String, default: 'No detected userID' },
    "date": String,
    "image": {data: Buffer,
            contentType: { type: String, default: 'image/jpeg' }}
});

const Image = mongoose.model('Image', imageSchema);

module.exports = Image;