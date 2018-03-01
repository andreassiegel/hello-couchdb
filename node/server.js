const express = require('express');
const bodyParser = require('body-parser');
const routes = require('./api/routes/peopleRoutes');

const app = express();
const port = process.env.PORT || 3000;

app.use(bodyParser.json());

routes(app);

app.listen(port, () => {
    console.log('Node.js server started on port ' + port);
});