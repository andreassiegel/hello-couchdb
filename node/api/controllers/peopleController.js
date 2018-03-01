const nano = require("nano-blue")("http://localhost:5985");

const dbName = 'people';
var peopleDb = nano.use(dbName);

nano.db.list((error, databases) => {
    if (error) {
        throw error;
    }

	if (databases.indexOf(dbName) < 0) {
		nano.db.create(dbName, function(error, body, headers) {
			if (error)
				throw error;
        });
        console.log('Created database ' + dbName);
    } else {
        console.log('Using database ' + dbName);
    }
});

exports.list_all = (req, res) => {

    peopleDb.fetch({}, (err, data) => {
        if (err) {
            res.statusCode = err.statusCode;
            res.json(err);
            res.end();
        }

        var result = [];
        data.rows.forEach(element => {
            result.push(element.doc);
        });
        res.statusCode = 200;
        res.json(result);
        res.end();
    });
};

exports.add = (req, res) => {

    var human = req.body;
    human.origin = 'Node';

    peopleDb.insert(human, (err, data) => {
        if (err) {
            res.statusCode = err.statusCode;
            res.json(err);
            res.end();
        }

        console.log(data);

        peopleDb.get(data.id, {}, (err, data) => {
            if (err) {
                res.statusCode = err.statusCode;
                res.json(err);
                res.end();
            }

            res.statusCode = 200;
            res.json(data);
            res.end();
        });
    });
};

