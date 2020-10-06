// add connection to server here
const express = require('express');
const mongoose = require('mongoose');

// require('dotenv').config();

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

const userRouter = require('./routes/user');
app.use('/users', userRouter);

app.listen(port, () => {
    console.log(`Server is running on port: ${port}`);
});