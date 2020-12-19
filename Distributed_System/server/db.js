const  mongoose = require('mongoose')

const con = mongoose.connect('mongodb://localhost/users', {useNewUrlParser: true})

module.exports = con 

