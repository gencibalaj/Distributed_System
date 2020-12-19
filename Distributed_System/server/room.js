const dgram = require('dgram')
const server = dgram.createSocket('udp4');


Room = [];

PORT = process.argv[2]




console.log("VIDEO START")

process.on('message', (msg) => {
  if(msg.type == "off"){
    Room = Room.map((item) => { if(item.ID == msg.id){ item.status="off" ;return  item } else{  return item } });
    sendCamOff(msg.id);
    console.log("Room State:", Room)
  }else if(msg.type == "on"){
    console.log("E LSHOJ KAMEREN ",msg.id);
    Room = Room.map((item) => { if(item.ID == msg.id){item.status="off" ;return  item } else{  return item } });
  }else if(msg.type == "leave"){
    Room = Room.filter((item) => {item.ID != msg.id });
    console.log("DUL QIKY PROF")
    console.log(Room)
  }
});

server.on('error', (err) => {
    console.log(`server error:\n${err.stack}`);
    server.close();
  });

  server.on('message', (msg, rinfo) => {
    //console.log(msg.toString("utf-8"))
    
        const userid = msg[0];
        const cmd = msg[1];

        //console.log(cmd)
        if(Room.length == 1){
          //console.log("ELTII")
          var buffer = new Buffer(1);
          buffer[0] = 0;
          server.send(buffer, parseInt(Room[0].PORT), Room[0].IP);
        }
        if(cmd == 1){
          console.log(`User: ${rinfo.address} join room on port ${PORT} with id ${userid}`)
          Room.push({ "IP": rinfo.address, "PORT": rinfo.port, "ID":userid, "status":"on" })
          console.log("Room State:", Room)
        }else if( cmd == 2 ) {
		        Room = Room.filter((item) => item.ID != userid)
		        sendLeave(userid)
          	console.log(`User: ${rinfo.address} left room on port ${PORT} with id ${userid}`)
          	console.log("Room State:", Room)
        }else if(cmd == 0){
          for( var i = 0; i < Room.length; i++){
              if(userid != Room[i].ID){
                  //console.log(`First: ${msg[0]} Second: ${msg[1]}`);
                  server.send(msg, parseInt(Room[i].PORT), Room[i].IP)
              }    
          }
        } 
  });

  const sendLeave = (id) => {
    for(var i = 0; i < Room.length; i++){
		if(id != Room[i].ID){ 
			var buffer = new Buffer(2);
			buffer[0] = id;
			buffer[1] = 2;
			server.send(buffer, parseInt(Room[i].PORT), Room[i].IP)
		}  
    }    
  }

  const sendCamOff = (id) => {
    for(var i = 0; i < Room.length; i++){
		if(id != Room[i].ID){ 
			var buffer = new Buffer(2);
			buffer[0] = id;
			buffer[1] = 9;
			server.send(buffer, parseInt(Room[i].PORT), Room[i].IP)
		}  
    }    
  }


  
  server.on('listening', () => {
    const address = server.address();
    console.log(`server listening ${address.address}:${address.port}`);
  });
  server.bind(PORT)