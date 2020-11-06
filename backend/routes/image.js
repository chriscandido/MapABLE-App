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

//adds Image upon Submit
// dapat may kukunin to na path pagka-upload, tas yun ilalagay sa imgPath na variable
router.route('/upload').post((req, res) => {
    // const imgPath = __dirname.replace('routes','conv3.png');
    const imgPath = req.body.image;
    const userID = req.body.userID;
    const imageBuffer = fs.readFileSync(imgPath);
    // To convert to base64, imageBuffer.toString('base64')
    const imageType = 'image/'+path.extname(imgPath).replace('.','').toLowerCase();

    const newItem = new Image({
        userID,
        date,
        image: {
                data: imageBuffer,
                contentType: imageType
            }
    });

    newItem.save()
            .then(() => res.json('Image uploaded!'))
            .catch(err => res.status(400).json('Error: ' + err))

    // const checkUser = {_id: mongoose.Types.ObjectId(req.body.userID)};

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