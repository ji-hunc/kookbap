const express = require('express');
const request = require('request');
const app = express()
var router = express.Router();

var mysql = require('mysql');
var db = mysql.createConnection({
  host     : '127.0.0.1',
  user     : 'root',
  password : 'wlgns620',
  database : 'Kookbob',
  port     : '3306'
});
db.connect();

// router.get('/', (req, res) => {
//     db.query(`SELECT * FROM review`, function(error, results) {
//         if (error) {
//             console.log(error);
//         }
//         console.log(results);
//     })
//     res.send(results);
// });

router.get('/', function(request, response) {
    db.query(`SELECT * FROM review`, function(error, results) {
        console.log(results);
        response.json(results);
    })
});

router.post('/post', function(request, response) {
    console.log("Enter!!!");
    // console.log(request);
    console.log(request.body);

    response.json(
        {
        "success": true,
        "message": "GOOD"
        }
    )
})

module.exports = router;