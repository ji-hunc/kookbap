const express = require('express');
const request = require('request');
const app = express()
var router = express.Router();

var menuJsonObject;
var hanulMenus;
var studentMenus;


router.get('/', (req, res) => {
	res.json(menuJsonObject);
	// res.json(menuJsonObject);
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

	hanulMenus = menuJsonObject['한울식당(법학관 지하1층)'];
	for (var i=21; i < 22; i++) {
		var date = "2022-11-" + i;

		menuEachDate = hanulMenus[date];
		cafeteriaName = Object.keys(menuEachDate);
		
		for (var j=0; j<cafeteriaName.length; j++) {
			var menu = menuEachDate[cafeteriaName[j]]["메뉴"];
			var price = menuEachDate[cafeteriaName[j]]["가격"];
			if (!(menu.length == 0) && !(price == "")) {
				price = price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");

				menu = menu.split("\r\n");

				console.log(menu[1], price);
			}
		}
	}
});

module.exports = router;