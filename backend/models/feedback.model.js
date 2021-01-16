const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// Feedback Schema
const feedbackSchema = new Schema({
  "userID": String,                                           // who submitted the feedback
  "date": { type: Date, default: Date.now },                  // when feedback was submitted
  "model": String,                                            // phone model
  "feedback": String                                          // actual feedback
});

const Feedback = mongoose.model('Feedback', feedbackSchema);

module.exports = Feedback;