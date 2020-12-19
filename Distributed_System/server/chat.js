const net = require('net') 
const { fork } = require('child_process');
const User = require('./models/user')

require('./db')

PORT =  parseInt(process.argv[2]);
RoomID = parseInt(process.argv[3]);
let clients = []

const server = net.createServer();

const audio = fork("audio.js", [PORT+1000])
const room = fork("video.js", [PORT+1100])


const midi = async (email, token) => {
    //console.log(token);
    try {
        if(token){
            await User.methods.auth({email:email,token:token})
            return true;
        }
    } catch(e) {
        return false;
    }
}

server.on('connection', (socket) => {
   
    console.log('New client : ' + socket.remoteAddress + ':' + socket.remotePort);
    clients.push(socket)    
    
    socket.on('data',async (data) => {
        console.log(data.toString('utf-8'))
        try {
                const toUser= JSON.parse(data.toString('utf-8'))
                const allGood = await midi(toUser.name, toUser.token);
                if(!allGood){
                    return;
                }
            
                if(Object.keys(toUser)[0] == "data" && Object.keys(toUser)[1] == "name") {         
                        clients.forEach(client => {
                        if(client != socket)
                            client.write(JSON.stringify({data:toUser.data, name: toUser.name}))
                        })
                    }else if(Object.keys(toUser)[0] == "cmd"){

                        if(toUser.cmd == "camoff"){
                            room.send({"type":"off","id": toUser.id});
                        }else if(toUser.cmd == "camon"){
                            console.log("KOSOVO REP" );
                            room.send({"type":"on","id": toUser.id});
                        }else if(toUser.cmd == "micoff"){
                            audio.send({"type":"off","id": toUser.id});
                        }else if(toUser.cmd == "micon"){
                            audio.send({"type":"on","id": toUser.id});
                        }else if(toUser.cmd == "leaveroom"){
                            console.log("User",toUser.id,"left the room!!!!");
                            audio.send({"type":"leave","id": toUser.id});
                            room.send({"type":"leave","id": toUser.id});
                        }
                    }
                    else socket.emit('error',new Error('INVALID_MSG_FORMAT'))
               
        }catch(e){
                socket.emit('error',e)      
        }
     })    

    socket.on('close',() =>{
        console.log(`Client ${socket.remoteAddress}:${socket.remotePort} left`)
        clients = clients.filter(client=> client != socket)
    })
    
    socket.on('error',e => console.log("Erro happend"))
})

server.on('close',() => console.log("I'm shuting down"))


server.on('error',(err) => console.log(err))


server.listen(PORT,'0.0.0.0',() => {
    console.log('Server started on', server.address())
})