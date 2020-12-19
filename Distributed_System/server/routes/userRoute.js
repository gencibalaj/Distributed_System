const router = require('express').Router()
const userController = require('../controllers/userController')
const roomController = require('../controllers/roomController')
const User = require('../models/user')
const uc = new userController()
const rc = new roomController()


const midi = async (req,res,next) => {
    console.log(req.body);
    try {
        if(req.body.token){
            await User.methods.auth(req.body)
            next()
        }
       
    } catch(e) {
        res.send({status:"ERROR",error:e})
    }
}

//Maybe TODO -> get(where tokeni is as header)//
//createroom
//Authenticate: token

router.post('/register',uc.adduser.bind(uc))
router.post('/login',uc.loginUser.bind(uc))
router.post('/logout',midi,uc.logoutUser.bind(uc))
router.post('/createroom',midi,rc.createRoom.bind(rc))
router.post('/joinroom/:id',midi,rc.joinRoom.bind(rc))
router.post('/auth',midi, (req, res) => { res.send({status:"OK"})});
router.post('/attendance/:id',midi,rc.attendanceCal.bind(rc))
router.post('/leaveroom/:id',midi,rc.leaveRoom.bind(rc))

module.exports = router