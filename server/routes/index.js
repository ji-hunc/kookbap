const express = require("express");
const app = express();
var router = express.Router();

router.get("/", (req, res) => {
    res.send("Hello!");
});

module.exports = router;
