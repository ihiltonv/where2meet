const express = require('express');
const bodyParser = require('body-parser');
const pino = require('express-pino-logger')();

const app = express();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(pino);

let cors = require('cors')
app.use(cors()) // Use this after the variable declaration
let whitelist = ['http://localhost:3000/events/1556998321650'];

//const server = require('http').createServer(app);
//let io = require('socket.io').listen(server);

//const port = 3002;
//io.listen(port);
//console.log('listening on port ', port);


//io.on('connection', (socket) => {
//    console.log("new client connected");
//    console.log("requesting event");
    //console.log(io);
//    socket.emit('REQUEST_EVENT',server);
    //console.log(socket);
//    io.sockets.on("SEND_CONNECT",(socket) => {
//        console.log("connected recieved");
//        console.log(data.event);
//    });
//    io.on('disconnect',(socket)=>{
//        console.log("goodbye");
//    });

//});

//const WebSocket = require('ws');

//const wss = new WebSocket.Server({
  //port: 3434,
  //Access-Control-Allow-Origin: 'localhost:3000'
//});

const io = require('socket.io')();
const port = 3434;
io.listen(port)
console.log('Listening on port ' + port + '...')

io.on('connection',(client)=>{
    console.log("new client connected");
    //let sclient = io.client;
    //console.log(client == sclient);
    client.emit('REQ_ID',function(){
        io.on('REQ_ID',(client),(event_id)=>{
            console.log('recieved id' + event_id);
        })
    });

    io.on("UPDATE",(client)=>{
        console.log("updating");
    });
});
