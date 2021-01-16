const router = require('express').Router();
const mongoose = require('mongoose');
const Feedback = require('../models/feedback.model');
const User = require('../models/user.model');

router.route('/').get((req, res) => {
  const query = req.body;
  Item.find(query)
    .then(items => res.json(items))
    .catch(err => res.status(400).json('Error: ' + err));
});

//adds Feedback upon Submit
router.route('/submit').post( async (req, res) => {

    const userID = req.body.userID;
    const model = req.body.model;
    const feedback = req.body.feedback;

    const checkUser = {_id: mongoose.Types.ObjectId(userID)};

    // Find user from user database
    var user  = await User.findOne(checkUser);

    const newFeedback = new Feedback({
      userID,
      model,
      feedback
    });

    // save Images in a different collection
    newFeedback.save()
            .then(() => res.json('Report added!'))
            .catch(err => res.status(400).json('Error: ' + err))
        res.status(200).send(user);
}
);

module.exports = router;
