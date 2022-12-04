/*
db접속 코드 중복 줄이기 위한 파일.
원하는 db선택후 주석 제거하면 됨.
*/

var mysql = require("mysql");

//로컬 db 접속
// var db = mysql.createConnection({
//     host: "127.0.0.1",
//     user: "root",
//     password: "12341234",
//     database: "Kookbob",
//     port: "3306",
// });

// 서버 db 접속
var db = mysql.createConnection({
    host: "13.209.185.52",
    user: "root",
    password: "1234",
    database: "Kookbob",
    port: "54198",
});

db.connect();

module.exports = db;
