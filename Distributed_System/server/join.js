var fs = require('fs');

const JoinSample = (a,b) => {
    var m
    a += 32768;
    b += 32768;

    if ((a < 32768) || (b < 32768)) {
        m = a * b / 32768;
    } else {
        m = 2 * (a + b) - (a * b) / 32768 - 65536;
    }
    if (m == 65536) m = 65535;
        m -= 32768;

    
    return m;
}

const JoinSample2 = (a,b) => {
    return  (2*(a + b) - ((a*b)/128) - 256)
}


const JoinAudio = (Sources) => {
    //const Sources16 = Sources.map(e => new Int16Array(e))
    const result = new Buffer(Sources[0].length)
    //console.log(Sources16);
    for(var i =0; i < Sources[0].length; i++){
        //console.log("MORE THAN TWO");
        result[i] = JoinSample2(Sources[0][i], Sources[1][i])  
        for(var j = 2; j < Sources.length; j++){
            //console.log("MORE THAN THREE");
            result[i] = JoinSample2(result[i], Sources[j][i]);
        }

        //result[i] = JoinSample(Sources16[0][i],Sources16[1][i])
        /*result[i] = 0;
        for(var j =0; j < Sources.length; j++){
            result[i] += Sources[j][i];  
        }
            result[i] = result[i]/Sources.length;*/
    }
    return result;
}

//fs.writeFile("elti6.pcm", JoinAudio([0,1]) , (err)=> {if(!err) console.log("ukry") });

module.exports = JoinAudio




/*
var sum = 0;
int16Arr.forEach(element => {
    sum+= Math.abs(element);   
});

var lev = sum/int16Arr.length

//console.log(lev)
*/
const removeNoise = (int16Arr) =>{
    var byte3 =  new Int16Array(int16Arr.length);
    int16Arr.forEach((e,i) => {
        if(Math.abs(lev - Math.abs(e)) >= lev){
            byte3[i] = 0;
            console.log("EEE")
        }else{
            //console.log("BBB")
            byte3[i] = e;
        }
    });
    console.log(byte3.length);
    fs.writeFile("elti5.pcm",new Buffer(byte3), (err)=> {if(!err) console.log("ukry") });
}


//removeNoise(int16Arr)



