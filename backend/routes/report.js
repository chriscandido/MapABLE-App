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
    const incident = req.body.incident;
    const frequency = req.body.frequency;
    const a1 = req.body.a1;
    const a2 = req.body.a2;
    const a3 = req.body.a3;
    const a4 = req.body.a4;
    const a5 = req.body.a5;
    const a6 = req.body.a6;
    const a7 = req.body.a7;
    const image = req.body.image;
    const geometry = {
		"type": "Point",
		"coordinates": [parseFloat(req.body.lon), parseFloat(req.body.lat)]
    };
    
    const newItem = new Item({
        userID,
        type,
        incident,
        frequency,
        a1,
        a2,
        a3,
        a4,
        a5,
        a6,
        a7,
        image,
        geometry
    });

    newItem.save()
            .then(() => res.json('Report added!'))
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