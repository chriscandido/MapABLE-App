const router = require('express').Router();
const mongoose = require('mongoose');
let Item = require('../models/report.model');
let Image = require('../models/image.model');
let User = require('../models/user.model');

router.route('/').get((req, res) => {
  Item.find()
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

//adds Report upon Submit
router.route('/submit').post((req, res) => {

    const userID = req.body.userID;
    const type = req.body.type;
    const date = req.body.date;
    const report = req.body.report;
    const imageBuffer = Buffer.from(req.body.image, 'base64');
    // To convert to base64, imageBuffer.toString('base64')
    // const imageType = 'image/'+path.extname(imgPath).replace('.','').toLowerCase();

    const geometry = {
		"type": "Point",
		"coordinates": [parseFloat(req.body.lon), parseFloat(req.body.lat)]
    };

    const newImage = new Image({
      userID,
      date,
      image: {
              data: imageBuffer
          }
    });

    // save Images in a different collection
    newImage.save();

    const newItem = new Item({
        userID,
        type,
        date,
        report,
        image: newImage._id, // ObjectID of image in the image collection
        geometry
    });

    const checkUser = {_id: mongoose.Types.ObjectId(req.body.userID)};

    newItem.save()
            .then(() => res.json('Report added!'))
            .catch(err => res.status(400).json('Error: ' + err))
        res.status(200).send(result);

    // Para lang macheck na before masubmit si report dapat linked siya sa isang userID

    // var result = User.findOne(checkUser, (err, result) => {
    //     if (result == null){
    //         res.status(400).send("User is not found in database");
    //     } else {

    //     newItem.save()
    //         .then(() => res.json('Report added!'))
    //         .catch(err => res.status(400).json('Error: ' + err))
    //     res.status(200).send(result);
    //     }
    // });
}
);

module.exports = router;
