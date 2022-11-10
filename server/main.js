const express = require('express')
const app = express()
const port = 3000


var indexRouter = require('./routes/index.js');
var menuRouter = require('./routes/menu.js');

app.use('/', indexRouter);
app.use('/menu', menuRouter);

app.listen(port, () => {
  console.log(`Example app listening on port on ${port}`)
})