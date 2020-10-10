const router = require('express').Router();
let Item = require('../models/report.model');

// router.route('/').get((req, res) => {
//   Item.find()
//     .then(items => res.json(items))
//     .catch(err => res.status(400).json('Error: ' + err));
// });

//adds User when upon sign-up
router.route('/submit').post((req, res) => {

    const report = req.body.report;
  
    const newItem = new Item({
        report
    });

    newItem.save()
        .then(() => res.json('Report added!'))
        .catch(err => res.status(400).json('Error: ' + err))

        console.log('Hello World!');
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
