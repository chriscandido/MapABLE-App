const router = require('express').Router();
let Image = require('../models/image.model');

const mongoose = require('mongoose');
const fs = require('fs');
const path = require('path');

// Retriving the image
router.route('/').get((req, res) => {
    Image.find({}, (err, items) => {
        if (err) {
            console.log(err);
        }
        else {
            res.render('app', { items: items });
        }
    });
});

module.exports = router;