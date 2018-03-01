const people = require('../controllers/peopleController');

module.exports = (app) => {

    app.route('/people')
        .get(people.list_all);

    app.route('/people/add')
        .post(people.add);
};