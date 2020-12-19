const User = require('../models/user')
const {hash} = require('bcrypt')
const jwt = require("jsonwebtoken")

    

//TODO change format of body req
class userController {
    constructor() {
        
    }
    
    async adduser (req,res) {
        try {
            await User.methods.findUser(req.body)
            const psw = await hash(req.body.password,8)
            req.body.password = psw
            User.methods.addUser(req.body)
            res.send({status:"OK"})
        } catch {
            res.send({status:"ERROR",error:"USER_EXISTS"})
        }
    }

    

    async loginUser(req,res) {
        console.log(req.body)
        try { 
            const user = await User.methods.login(req.body)
            const token = await jwt.sign({token:user['_id']},'qelsi')
            user.token = token
            await User.methods.updateToken(user)
            res.send({status:"OK",result:token})
        } catch(e){
            res.send({status:"ERROR",error:e})
        }
    }

    async logoutUser(req,res) {
        try {
            const user = req.body
            user.token = " "
            await User.methods.updateToken(user)
            res.send({status:"OK"})
        } catch(e){
            res.send({status:"ERROR",error:e})
        }
    }
}   

module.exports = userController