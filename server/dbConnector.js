var mysql = require("mysql");

//로컬 db 접속
var db = mysql.createConnection({
    host: "127.0.0.1",
    user: "root",
    password: "12341234",
    database: "Kookbob",
    port: "3306",
});

//서버 db 접속
// var db = mysql.createConnection({
//     host: "13.209.133.64",
//     user: "root",
//     password: "1234",
//     database: "Kookbob",
//     port: "50609",
// });

db.connect();

module.exports = db;
