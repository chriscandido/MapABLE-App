const router = require('express').Router();
const mongoose = require('mongoose');
let Item = require('../models/report.model');
let Image = require('../models/image.model');
let User = require('../models/user.model');

function stringToArray(str) {
  var i;
  var res = str.split(",")
  for (i = 0; i < res.length; i++) {
    res[i] = res[i].replace('[','').replace(/"/g,'').replace(']','');
  };
  return(res);
}

router.route('/').get((req, res) => {
  const query = req.body;
  Item.find(query)
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

router.route('/viewport').get((req, res) => {
  const latitude = req.body.latitude;
  const longitude = req.body.longitude;

  Item.find(query)
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

//adds Report upon Submit
router.route('/submit').post( async (req, res) => {

    const userID = req.body.userID;
    const type = req.body.type;
    const date = req.body.date;
    const report = stringToArray(req.body.report);
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

    console.log("----SAVING IMAGE------");
    console.log("userID: " + newImage.userID);
    console.log("date: " + newImage.date);

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

    console.log("------ ETO HAHANAPIN NA NIYA SI USER -------")
    // Find user from user database
    var user  = await User.findOne(checkUser);

    console.log("------ NAHANAP NIYA SI USER -------");
    console.log("userID found:" + user._id);

    user.numOfReports += 1;

    user.save();

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
