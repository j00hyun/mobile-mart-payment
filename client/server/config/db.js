var mysql = require('mysql');
const db = mysql.createPool({
	    host : 'localhost',
	    user : 'root',
	    password : 'root',
	    database : 'test'
});

module.exports = db;
