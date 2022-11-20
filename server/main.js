const express = require('express')
const app = express()
const port = 3000
var bodyParser = require('body-parser');
var parser = bodyParser.urlencoded({extended: false});
const fileUpload = require("express-fileupload");

var indexRouter = require('./routes/index.js');
var menuRouter = require('./routes/menu.js');
var reviewRouter = require('./routes/review.js');
var rankRouter = require('./routes/rank.js');

app.use(express.static('public'));
app.use(fileUpload());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use('/', indexRouter);
app.use('/menu', menuRouter);
app.use('/review', reviewRouter);
app.use('/rank', rankRouter);

app.listen(port, () => {
  console.log(`Example app listening on port on ${port}`)
})