const router = require('express').Router();
let Item = require('../models/user.model');

router.route('/').get((req, res) => {
  Item.find()
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

//adds User when upon sign-up
router.route('/signup').post((req, res) => {
  const name = req.body.name;
  const mobile_number = req.body.mobile_number;
  const email = req.body.email;
  const username = req.body.username;
  const password = req.body.password;

  const newItem = new Item({
    name,
    mobile_number,
    email,
    username,
    password
});

  newItem.save()
    .then(() => res.json('Item added!'))
    .catch(err => res.status(400).json('Error: ' + err));
});

//finds announcement using id
router.route('/:id').get((req, res) => {
  Item.findById(req.params.id)
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

//deletes an item
router.route('/:id').delete((req, res) => {
  Item.findByIdAndDelete(req.params.id)
    .then(() => res.json('Item deleted.'))
    .catch(err => res.status(400).json('Error: ' + err));
});

//updates info about item
router.route('/update/:id').post((req, res) => {
  Item.findById(req.params.id)
    .then(item => {
      item.name = req.body.name;
      item.mobile_number = req.body.mobile_number;
      item.email = req.body.email;
      item.username = req.body.username;
      item.password = req.body.password;

      item.save()
        .then(() => res.json('Item updated!'))
        .catch(err => res.status(400).json('Error: ' + err));
    })
    .catch(err => res.status(400).json('Error: ' + err));
});

module.exports = router;
