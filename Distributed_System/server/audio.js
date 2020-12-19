const dgram = require('dgram')
const server = dgram.createSocket('udp4');
const JoinAudio = require("./join"); 

Room = [];

SoundBytes = [];

var iii = 0;
PORT = process.argv[2]

var SoundID = [];
var SoundArray = [];

server.on('error', (err) => {
    console.log(`server error:\n${err.stack}`);
    server.close();
});

process.on('message', (msg) => {
  if(msg.type == "off"){
    Room = Room.map((item) => { if(item.ID == msg.id){ item.status="off" ;return  item } else{  return item } });
    console.log("Room State:", Room)
  }else if(msg.type == "on"){
    Room = Room.map((item) => { if(item.ID == msg.id){item.status="off" ;return  item } else{  return item } });
  }else if(msg.type == "leave"){
    Room = Room.filter((item) => {item.ID != msg.id });
  }
});




  server.on('message', (msg, rinfo) => {
    //console.log(msg.toString("utf-8"))
        const userid = msg[0];
        const cmd = msg[1];
        //console.log(userid,":",cmd)
        if(Room.length == 1){
          var buffer = new Buffer(1);
          buffer[0] = 0;
          server.send(buffer, parseInt(Room[0].PORT), Room[0].IP);
        }
        if(cmd == 1){
          console.log(`User: ${rinfo.address} join room on port ${PORT} with id ${userid}`)
          Room.push({ "IP": rinfo.address, "PORT": rinfo.port, "ID":userid, "status":"on"})
          console.log("Room State:", Room)

        }else if( cmd == 2 ) {
		    Room = Room.filter((item) => item.ID != userid)
		    sendLeave(userid)
          	console.log(`User: ${rinfo.address} left room on port ${PORT} with id ${userid}`)
          	console.log("Room State:", Room)
        }else if(cmd == 0){
            if(Room.length > 2){
              if(!SoundID.includes(userid)){
                SoundID.push(userid);
                SoundArray.push(msg.slice(2))
                ActiveRoom = Room.filter(item => item.status == "on")
                if(SoundID.length == ActiveRoom.length){
                  SendJoindAudio()
                  SoundID = [];
                  SoundArray = [];
                }
              }  
            }else{
              Room.forEach(u => {
                if(u.ID != userid){
                  //console.log("Sending");
                  server.send(msg.slice(2),u.PORT, u.IP)
                }
              });
            }
        }
  });

  const generateJoind = (user) =>{
    //console.log(SoundID)
    const soundBytesToJoin = SoundArray.filter((e,i) => SoundID[i] != user.ID && IsMicOn(SoundID[i]))
    
    if(soundBytesToJoin.length > 1){
      const sound = JoinAudio(soundBytesToJoin)
      server.send(sound, user.PORT, user.IP)
    }else if(soundBytesToJoin.length == 1){
      server.send(soundBytesToJoin[0], user.PORT, user.IP)
    }
  }

  const IsMicOn = (id) => {
    for(var i = 0; i < Room.length; i++){
        if(Room[i].ID == id){
          return Room[i].status == "on";
        }
    }
  }

  const SendJoindAudio = () => {
    Room.forEach(user => {
      generateJoind(user);
    });
  } 

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
  
  server.on('listening', () => {
    const address = server.address();
    console.log(`server listening ${address.address}:${address.port}`);
  });
  server.bind(PORT)