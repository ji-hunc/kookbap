const express = require('express');
const request = require('request');
const app = express()
var router = express.Router();

var menuJsonObject;


router.get('/', (req, res) => {
	res.json(menuJsonObject);
	// console.log("CONNECT: someone enter the /menu");
});

const options = {
    	uri: "https://kmucoop.kookmin.ac.kr/menu/menujson.php?sdate=2022-11-01&edate=2022-11-30"
	};
request(options, function(error, response, body) {
    if (error) {
        console.log(error);
    }
    menuJsonObject = JSON.parse(body);
	// console.log(menuJsonObject);
});

module.exports = router;