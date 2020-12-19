const Room = require('../models/room')
const { fork } = require('child_process');
//TODO Move Function to another file



const portFromId = (id) => {
    index = ids.indexOf(id)
    MeetPeople[index]++;
    return [ports[index]+1100,MeetPeople[index],ports[index]+1000,ports[index]]
  }
  
  const roomId = () => {
      id = randomNr();
      if(ids.includes(id)){
        return roomId();
      }else {
        return id;
      }
  }
  
  const randomNr = () => {
    var min = 10000;
    var max = 99999;
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

var ids = [];
var ports = []; 
var MeetPeople = [];


var LAST_PORT = 9000;
  
//</TODO>

class roomController {
    constructor() {
        
    }

    async createRoom(req,res) {
        const id = randomNr()
        req.body.RoomID = id;
        req.body.type = 1;
        req.body.role = "HOST";
        Room.methods.addRoom(req.body)
        ids.push(id)
        ports.push(LAST_PORT)
        MeetPeople.push(1)
        fork("chat.js", [LAST_PORT, id]) 
        res.send({status:"OK", result:JSON.stringify({videoPort:LAST_PORT+1100,audioPort:LAST_PORT+1000 ,chatport:LAST_PORT ,meetid:id, nrp:1})});
        console.log(ids, ports)
        LAST_PORT++
    }

    async attendanceCal(req, res){
        try{
            let emails = [];
            let timestaps = [];
            let HostEmail = "";
            
            const docs = await Room.methods.findUsersInRoom(parseInt(req.params.id))
            for(let i =0; i < docs.length; i++){
                const index = emails.indexOf(docs[i].email);
                if(index != -1){
                    timestaps[index].push(docs[i].time.getTime()/1000);
                }else{
                    if(docs[i].role == "HOST")
                        HostEmail = docs[i].email
                    
                    emails.push(docs[i].email);
                    timestaps.push([docs[i].time.getTime()/1000]);
                }
            }
            
            const result = [];
            emails.forEach((item, i) => {
                result.push({email:item, timestaps:timestaps[i].sort()})
            });

            const res2 = this.calculateActiveTime(result,HostEmail);
            console.log(res2);
            res.render('attendance.hbs', {id:req.params.id, result: res2})
        } catch(e){
            res.send({status:"ERROR", error:e});
        }
        
    }

    calculateActiveTime(users, host) {
        

        let result = [];
        users.forEach((user) => {
            var activeTime = 0;
            for(var i = 0; i < user.timestaps.length/2; i++){
                activeTime += user.timestaps[i+1] - user.timestaps[i]
            }
            user.activeTime = activeTime;
            user.percentage = (activeTime/hostTime)*100
            result.push(user);
        })    
        
        var hostTime = 1;

        for(var i = 0; i < result.length; i++){
            if(result[i].email == host){
                hostTime = result[i].activeTime;
                break;
            }
        }


        result = result.map((item) => {item.percentage = (item.activeTime/hostTime)*100; return item})

        

        return {host:host ,result:result};
    }

    async joinRoom(req,res) {   
        const e = portFromId(parseInt(req.params.id));
        req.body.RoomID = req.params.id;
        req.body.type = 1;
        Room.methods.addRoom(req.body);
        const result = JSON.stringify({videoPort:e[0],audioPort:e[2],chatport:e[3] ,meetid: parseInt(req.params.id) ,nrp: e[1] });
        res.send({status:"OK", result:result})
        
    }

    async leaveRoom(req, res) {
        console.log("KOSOVA REP")
        const roomID = parseInt(req.params.id)
        req.body.RoomID = roomID;
        req.body.type = 0;
        delete req.body.token
        Room.methods.addRoom(req.body)
        res.send({status:"OK"})

    }


}

module.exports = roomController;