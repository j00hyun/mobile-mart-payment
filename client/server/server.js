const express = require('express');
const app = express();
const PORT = process.env.PORT || 4000;
const db = require('./config/db');

app.get('/', (req, res) =>{
    res.send('jsoo!');
})

app.get('/hello', (req, res) => {
//	 res.send({ hello : 'Hello react' });
	db.query("SELECT imgpath FROM user", (err, data) => {
		if(!err) res.send({data: data });
		else res.send(err);
	})
})

app.get('/api/products', (req, res) => {
	db.query("SELECT imgpath FROM user", (err, data) => {
		if(!err) res.send({ products : data });
		else res.send(err);
	})
})

app.listen(PORT, () => {
	    console.log(`Server On : http://localhost:${PORT}/`);
})
