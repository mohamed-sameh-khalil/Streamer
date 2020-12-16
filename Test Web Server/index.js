const express = require('express');
const path = require('path');
const redis = require('redis');

const PORT = 3000 || process.env.PORT;

let client = redis.createClient();
app = express();

client.on('connect',() => {
    console.log("CONNECTED TO REDIS!!!");
})

app.listen(PORT,()=>{console.log(`Connected to port ${PORT}`)});


//This get will be POST and we will get redis key from front end, this is GET for testing purposes
app.get('/', function(req, res, next){
    client.get("1-1",(err,obj) => {
        if(!obj)
            res.end("<h1>Image not found</h1>")
        else{
            var img = Buffer.from(obj, 'base64');

            res.writeHead(200, {
                'Content-Type': 'image/png',
                'Content-Length': img.length
            });
            res.end(img);     
        }
    })
  });