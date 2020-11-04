const router = require('express').Router();
const mongoose = require('mongoose');
let Item = require('../models/report.model');
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
    const image = req.body.image; // file path nalang to or file name kasi separate yung sa image upload
    const geometry = {
		"type": "Point",
		"coordinates": [parseFloat(req.body.lon), parseFloat(req.body.lat)]
    };

    const newItem = new Item({
        userID,
        type,
        date,
        report,
        image,
        geometry
    });

    const checkUser = {_id: mongoose.Types.ObjectId(req.body.userID)};
    // Para lang macheck na before masubmit si report dapat linked siya sa isang userID

    var result = User.findOne(checkUser, (err, result) => {
        if (result == null){
            res.status(400).send("User is not found in database");
        } else {

        newItem.save()
            .then(() => res.json('Report added!'))
            .catch(err => res.status(400).json('Error: ' + err))
        res.status(200).send(result);
        }
    });
}
);

module.exports = router;

// //Log-in route
// router.route('/login').post((req, res) => {
//   const query = {
//     username: req.body.username,
//     password: req.body.password
// }

//   var result = Item.findOne(query, (err, result) => {
//     if (result != null){
//         res.status(200).send(result);
//     } else {
//         res.status(400).send();
//     }
//   });
// });

// //deletes an item
// router.route('/:id').delete((req, res) => {
//   Item.findByIdAndDelete(req.params.id)
//     .then(() => res.json('Item deleted.'))
//     .catch(err => res.status(400).json('Error: ' + err));
// });

// //updates info about item
// router.route('/update/:id').post((req, res) => {
//   Item.findById(req.params.id)
//     .then(item => {
//       item.name = req.body.name;
//       item.mobile_number = req.body.number;
//       item.email = req.body.email;
//       item.username = req.body.username;
//       item.password = req.body.password;

//       item.save()
//         .then(() => res.json('Item updated!'))
//         .catch(err => res.status(400).json('Error: ' + err));
//     })
//     .catch(err => res.status(400).json('Error: ' + err));
// });
