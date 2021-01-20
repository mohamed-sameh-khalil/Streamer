const express = require('express');
const path = require('path');
const redis = require('redis');
const fs = require('fs');
const { basename } = require('path');
//const cv = require('opencv4nodejs')

const PORT = 3000 || process.env.PORT;

let client = redis.createClient();
app = express();

client.on('connect',() => {
    console.log("CONNECTED TO REDIS!!!");
    setInterval(updateRedis,30);
    setInterval(updatePhoto,30);
})

app.listen(PORT,()=>{console.log(`Connected to port ${PORT}`)});

imgindex = 0;
images = fs.readdirSync("./Jpg Test");

app.get('/public/out.jpg', function(req, res, next){
    res.sendFile(path.join(__dirname,'/public/out.jpg'))
});

function updatePhoto()
{
    //Update Local Image
    /*
    fs.readFile(path.join(__dirname,"Jpg Test",basename(images[imgindex])),(err,data)=>
                {
                    if(err)
                        console.log("ERR-IN");
                    else
                    {
                        fs.writeFile("./public/out.jpg",data, (err)=> 
                        {if(err) console.log(err);});
                        imgindex++;
                        if(imgindex==images.length)
                        {
                            imgindex=0;
                        }
                    }
                })
    */
   //Update Redis Image
   client.get("1-1",(err,obj) => {
    if(!obj)
        console.log("<h1>Image not found</h1>")
    else{
        //var img = Buffer.from(obj, 'base64');
        fs.writeFile("./public/out.jpg",obj,'base64', (err)=> {if(err) console.log(err);});  
    }
   });
}

function updateRedis()
{
    redisimage = fs.readFileSync(path.join(__dirname,"Jpg Test",basename(images[imgindex])),"base64");
    client.set("1-1",redisimage);
    imgindex++;
    if(imgindex==images.length)
    {
      imgindex=0;
    }
}

//This get will be POST and we will get redis key from front end, this is GET for testing purposes
app.get('/', function(req, res, next){
    res.sendFile(path.join(__dirname,'/stream.html'));
    /*client.get("1-1",(err,obj) => {
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
    })*/
    /*fs.readdir("C:/Users/lenovo/Downloads/Wallpapers",(err,images) => {
        if(err)
            console.log("ERR");
        else{
            i = 0;
            res.setHeader('Content-Type', 'image/gif');
            images.forEach((image)=>{
                fs.readFile("C:/Users/lenovo/Downloads/Wallpapers" + "/" + path.basename(image),(err,data)=>
                {
                    if(err)
                        console.log("ERR-IN");
                    else
                    {
                        console.log("Sent " + i);
                        i++;
                        res.write(data);
                    }
                })
            })
            //res.end();
        }
    });*/
  });