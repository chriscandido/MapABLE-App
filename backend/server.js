// add connection to server here
const express = require('express');
const mongoose = require('mongoose');
var bodyParser = require('body-parser');

require('dotenv').config();
// var fs = require('fs');
// var path = require('path');

const app = express();
const port = process.env.PORT || 5000;

app.use(express.json());

//MongoDB connection string
const uri = "mongodb+srv://mapable:mapable@mapable-cluster.fjjjm.mongodb.net/mapable-db?retryWrites=true&w=majority"
mongoose.connect(process.env.MONGODB_URI || uri, { useNewUrlParser: true, useCreateIndex: true, useUnifiedTopology: true }).catch(error => console.log(error));

const connection = mongoose.connection;
connection.once('open', () => {
  console.log("MongoDB database connection established successfully");
})

app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

const userRouter = require('./routes/user');
const reportRouter = require('./routes/report');
const imageRouter = require('./routes/image');

app.use('/users', userRouter);
app.use('/reports', reportRouter);
app.use('/images', imageRouter);

app.listen(port, () => {
    console.log(`Server is running on port: ${port}`);
});