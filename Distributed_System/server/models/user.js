const mongose = require('mongoose')
const {Schema} = require('mongoose')
const validator = require('validator')
const bcrypt = require('bcrypt')


const userSchema = new Schema({
    name: {
        type: String,
        require: true,
        validate(value) {
            if(value.length > 20 && value.length<3)
                    throw Error('NAME_INVALID')
            }
        },
    email: {
        type: String,
        require: true,
        lowercase: true,
        trim: true,
        validate(value) {
            if(!validator.isEmail(value))
                throw Error('EMAIL_INVALID')
        }
    },
    password: {
        type: String,
        require: true,  
    },
    token:{
        type: String,
        require: false,  
    }
})

const User = mongose.model('User',userSchema)  

userSchema.methods.addUser = (user) => {
    new User(user).save(() => { 
        console.log("hina")
    }) 
 }

userSchema.methods.auth = (user) => {
    return new Promise((resolve,reject)=> {
        if( !user.token ||  user.token == ' ') { 
            reject("INVALID_TOKEN")
            return
        }
        User.findOne({email:user.email,token:user.token},(err,doc) => { 
            if(!err && doc) 
                resolve(doc)
            else
                reject("INVALID_TOKEN")
        })
    }) 
}

userSchema.methods.login = (obj) => {
    return new Promise((resolve,reject) => {
        User.find({email:obj.email},(err,doc) => { 
            if(doc.length == 0) 
                reject("INVALID_CREDENTIALS")
            else {
                bcrypt.compare(obj.password,doc[0].password).then((rez) => {
                    if(rez)
                        resolve(doc[0])
                    else 
                        reject("INVALID_CREDENTIALS")
                }).catch((e) => reject(e))  
            }
        })
    })
}

userSchema.methods.updateToken = (user) => {
   return new Promise((resolve,reject) => {
    User.findOneAndUpdate({email:user.email},{token:user.token},(err,doc) => {
        if(err)
            reject(err)
        else 
            resolve(doc)
    })
   })
}

userSchema.methods.findUser = (user) => {
        return new Promise((resolve,reject) => {
            User.find({email:user.email},(err,doc) => { 
                if(doc.length == 0) 
                resolve("error")
                else
                reject(doc)
            })
        })
 }




module.exports = userSchema    