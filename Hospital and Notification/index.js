const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotif = functions.https.onRequest((request, response)=>{
	//var registrationToken = "dvrQU2V61bo:APA91bE4Bkeb86tVAklq6mFw6dZP9fAJ2-fGxz-s4kmLnbac0vYNGpMVmR55w-JbbXzVQV1wQ_2aiq7KP_6UDt0e4_K-XLvIIduNMC_Dfn1ikm0siaHOvRglxs3Hh50pVg8hsOAewUxN"
	//var uid = "foINo2rUmnh5HQ6cidBpi9296N43"
var registrationToken;
//admin.database()
//.ref("123/request")
//.push({"req":"Request : Emergency"})
admin.firestore().collection("users").doc("user1").collection("requests").add({usrname : request.query.name,
usrage: "20", usrsex: "Gay", drivername : "Vidhayak Chacha", contactno:"9924246767", vehicleno:"GJ6 BR 1234"});

admin.firestore().collection("users").doc("user1").get().then(function(snap){
  registrationToken = snap._fieldsProto.token.stringValue;
console.log(registrationToken)
var message = {
  notification: {
    title: 'Emergency',
    body: request.query.name + ' in Danger'
  },
  data : {usrname : request.query.name,
usrage: "20", usrsex: "Gay", drivername : "Vidhayak Chacha", contactno:"9924246767", vehicleno:"GJ6 BR 1234"
  },
  token: registrationToken
};
// Send a message to the device corresponding to the provided
// registration token.
admin.messaging().send(message)
return response.send("Done")
}).catch((error)=>{
  return response.send(error)
})
});
