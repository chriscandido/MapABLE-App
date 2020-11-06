const router = require('express').Router();
let Item = require('../models/user.model');
let UserLog = require('../models/userlog.model');

router.route('/').get((req, res) => {
  Item.find()
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

//gets ID of a user 
router.route('/getID').get((req, res) => {
  
  const query = {
    username: req.query['username'],
    password: req.query['password']
  }

  Item.findOne(query)
  .then(item => res.json(item))
  .catch(err => res.status(400).json('Error: ' + err));

});

//adds User when upon sign-up
router.route('/signup').post((req, res) => {
  const name = req.body.name;
  const number = req.body.number;
  const email = req.body.email;
  const username = req.body.username;
  const password = req.body.password;

  const checkEmail = {email: email};

  var result = Item.findOne(checkEmail, (err, result) => {
    if (result != null){
        res.status(400).send();
    } else {
      const newItem = new Item({
        name,
        number,
        email,
        username,
        password
    });
      console.log(result);

      newItem.save()
        .then(() => res.json('User added!'))
        .catch(err => res.status(400).json('Error: ' + err));
      res.status(200).send(result);
    }
  });
});

//Log-in route
router.route('/login').post((req, res) => {
  const query = {
    username: req.body.username,
    password: req.body.password
}

  var result = Item.findOne(query, (err, result) => {
    if (result != null){
      const newLog = new UserLog({
        name: result.name,
        username: result.username
      });

      newLog.save();

        res.status(200).send(result);
    } else {
        res.status(400).send();
    }
  });
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
      item.mobile_number = req.body.number;
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
