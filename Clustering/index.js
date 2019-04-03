const functions = require('firebase-functions');
const admin = require('firebase-admin')
admin.initializeApp(functions.config().firebase)

function euclideanDistance(x, y, clusters, k){
	var cl=0, ldist=10000, dist=0
	for (let i=0; i<k; i++){
		dist = Math.sqrt(Math.pow(x - clusters[i].x, 2) + Math.pow(y - clusters[i].y, 2))
		//console.log(dist)
		if (dist < ldist){
			ldist = dist
			cl = i
		}

	}
	//console.log(ldist)
	//console.log(mse)
	return [cl, ldist]
}

function KMeans(arr, cnt, vehicles){
	var prevMse=0;
	for (var k=2; k<=50; k++){
	var clusters = [], density = []
	for (let i=1; i<=k; i++){
	let x = 22 + 2*Math.random()
	let y = 72 + 2*Math.random()
	clusters.push({"x":x.toFixed(6), "y": y.toFixed(6)})
	density.push([])
	}

	var epochs = 10, mse=0
	for (let i = 1; i <= epochs; i++){
		mse=0
		for (let j = 0; j<cnt; j++){
				let d = euclideanDistance(arr[j].x, arr[j].y, clusters, k)
				
				density[d[0]].push(arr[j])
				mse+=d[1]
		}
		//console.log(density)
		for (let j=0; j<k; j++){
			let sux=0, suy=0, iter=0
			for(let q in density[j]){
				sux+=density[j][q].x
				suy+=density[j][q].y
				iter++
			}
			//console.log(sux)
			if (iter!==0){
			sux = sux/iter
			suy = suy/iter
			clusters[j].x = sux.toFixed(8) 
			clusters[j].y = suy.toFixed(8)
			}
			density[j] = []
		}
		//console.log(mse)
	}
	if(k!==2){
		if(prevMse - mse <= 1){
			console.log(k)
			/*jshint loopfunc:true */
			var p = 0;
			admin.firestore().collection('clusters').get().then(function(snap){
				snap.forEach(doc=>{
					doc.ref.delete()
				})
				console.log("HIHIHI")
			
				for (var p=0; p<k; p++){
					admin.firestore().collection('clusters').add(clusters[p]).then(()=>
						console.log("dope")).catch(()=>console.log("not dope"))
				}

			})
			break
		}
	}
		prevMse = mse
}
var vehi = [], c = 0, count=0
for (let p=0; p<k;p++){
	c = Math.floor(density[p].length / cnt * vehicles)
	count+=c
	vehi.push(c)
}
for (let p=1; p<=vehicles-count; p++){
	let ind = Math.floor(k*Math.random())
	vehi[ind]+=1
}
return vehi
}


exports.retrieve = functions.https.onRequest((request, response)=>{
	var coor = [], cnt=0
	admin.firestore().collection('coor').get().then(function(snapshot){
		snapshot.forEach(doc=>{
			coor.push({"x":doc._fieldsProto.x.doubleValue, "y":doc._fieldsProto.y.doubleValue})
			cnt = cnt+1;
		})
		var Allocation = KMeans(coor, cnt, 50)
		return response.send(Allocation)

	}).catch((error)=>
	console.log(error))
	// /response.send(error))
	//console.log(cnt)
	//console.log(coor)
	//var Allocation = KMeans(coor, cnt, 50)
	//response.json(Allocation)

})

exports.addRequest = functions.https.onRequest((request, response)=>{
	var userID = request.query.uid;
	var driverID = request.query.did;
	var hospID = request.query.hid;

	admin.firestore().collection('request').add({"uid":userID, "did":driverID, "hid":hospID})

	admin.database().ref("/Hospitals").child('h'+hospID+'/requests').push({"uid":userID, "did":driverID, "hid":hospID})
	.then(()=>
		response.json({"msg":"Done"}))
	.catch(()=>
		response.json({"msg":"Not Done"}))
	
})
exports.removeRequest = functions.https.onRequest((request, response)=>{
	admin.database().ref("/").child("request").orderByChild('did').equalTo(request.query.did).on("value", function(snap){
		snap.ref.remove();
	})
	
	admin.database().ref('/Hospitals/h'+request.query.hid).child('requests').orderByChild('did').equalTo(request.query.did).on("value", function(snap){
		snap.ref.remove()
	})
	response.json({"msg":"Done"})
})

