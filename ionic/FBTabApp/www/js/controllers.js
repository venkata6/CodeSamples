angular.module('starter.controllers', [])

.controller('AppCtrl', function ($scope, $state, OpenFB) {

    $scope.logout = function () {
        OpenFB.logout();
        $state.go('app.login');
    };

    $scope.revokePermissions = function () {
        OpenFB.revokePermissions().then(
            function () {
                $state.go('app.login');
            },
            function () {
                alert('Revoke permissions failed');
            });
    };

})
    
.controller('LoginCtrl', function ($scope, $location, OpenFB ) {
    $scope.facebookLogin = function () {
        OpenFB.login('email,read_stream,publish_actions,user_friends').then(
            function () {
               //$location.path('/app/person/me/feed');
                $location.path('/tab/home');
            },
            function () {
                alert('OpenFB login failed');
            });
    };


})

.controller('PointsGiveCtrl', function ($scope, $stateParams, OpenFB, $rootScope) {
    //alert("friends ctrl");
   $scope.currentDate = new Date();
   
   var data = [] ;    
   for ( var i=0; i < $rootScope.friendsList.length; i++) {  
       data.push ( { id :  i+1 ,
                     name : $rootScope.friendsList[i] 
                   } );
   }
   $scope.friends = data;

   $scope.selectFriend = function()
    { 
        console.log("hello controllers");   
   }
})

.controller('PointsPostCtrl', function ($scope, $stateParams, OpenFB,$http, $rootScope) {
    //alert("friends ctrl");
   $scope.currentDate = new Date();
    console.log("in post points ");
    var uri = 'https://karmakorner.co/kk/webapi/people/postPoints/';
    var data = {} ;
    data["nameFrom"] = $rootScope.me.name;
    data["idFrom"] =  $rootScope.me.id;
    data["nameTo"] = 'Sony Jose';
    data["points"] = '10';
    data["notes"] = 'test from mobile';
    //data["date"] = nil;

    var data1 = JSON.stringify(data);
    /*
    var comment = $('#comment').val();
    var kkLink = '<a href="https://apps.facebook.com/karmakorner/">send using karmakorner</a>';
    var friendTagId = friendCache.taggableFriendMap[data["nameTo"]];
    var commentText = comment ; //+ ' - ' + kkLink;                                                                   var checked = $('#postToFB').prop('checked') ; 
    */
    
    
    console.log(data1);
    
    var headers = {
            //'Access-Control-Allow-Origin' : '*',
            //'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS',
            //'Content-Type': 'application/json',
            //'Accept': 'application/json'
        };
    
    var req = {
        method: 'POST',
        url: uri,
        headers: headers ,
        data: data1 
    }
    console.log(req);
    $http(req).
        success (function( retdata ) { 
            console.log("sucess posting data");
        }).
        error( function(xhr, status, error) {                                                                                 console.log("Error:posting data");
          });
    

})



.controller('PointsCtrl', function ($scope, $stateParams, OpenFB, $rootScope) {
    //alert("friends ctrl");
    loadFriendsPoints($rootScope);
})

.controller('MeCtrl', function ($scope, $stateParams, OpenFB,kkService,$http, $rootScope) {
    getFriends(OpenFB,$rootScope,function(){
                        loadMe(OpenFB, $http, $rootScope);
                    });
    
})

.controller('ChatsCtrl', function($scope /*, Chats */) {
  $scope.chats = Chats.all();
  $scope.remove = function(chat) {
    Chats.remove(chat);
  }
})

.controller('PointsDetailCtrl', function($scope, $stateParams,$rootScope) {
  loadDetails($rootScope,$stateParams.fbName,$stateParams.direction);
  //alert($stateParams.fbId + " " + $stateParams.direction);
  //$scope.chat = Chats.get($stateParams.chatId);
})

.controller('AccountCtrl', function($scope) {
  $scope.settings = {
    enableFriends: true
  };
});

function loadMe(OpenFB, $http, $rootScope) {
    OpenFB.get("/me", {fields: 'id,name,first_name,picture.width(120).height(120)'})
        .success(function (result) {
		console.log("/me success");
        var str = 'https://karmakorner.co/kk/webapi/people/getPoints/';
        str += $rootScope.me.name + '/' + $rootScope.me.id;
        var data1 = '{"friends":' + JSON.stringify($rootScope.friendsList) + '}';
            $http({
                method: 'POST',
                url:str,
                data:data1
                }).
              success(function(data, status, headers, config) {
                $rootScope.kkData=data;
                calculatePoints($rootScope);
       
              }).
              error(function(data, status, headers, config) {
                //alert(status);
                // called asynchronously if an error occurs
                // or server returns response with an error status.
              });   
        })
        .error(function(data) {
		            console.log("meCtrl-failure");
			    //alert(data.error.message);
        });
}

function loadDetails( $scope, fbName, direction){
    var data = $scope.kkData; 
    
    if ( direction == "to") {
        var detailsTo = [] ;
        for ( var i=0; i < data.pointsToList.length; i++) {
            Element =  data.pointsToList[i];
            if ( Element.nameTo == fbName)  {
                detailsTo.push(Element);
            }
        }
        $scope.details = detailsTo;
    }

    if ( direction == "from") {
        var detailsFrom = [] ;
        for ( var i=0; i < data.pointsFromList.length; i++) {
            Element =  data.pointsFromList[i];
            if ( Element.nameFrom == fbName)  {
                detailsFrom.push(Element);
            }
        }
        $scope.details = detailsFrom;
    }

}

// this function will get the friends list if we have the permissions                                                                                                                
function getFriends(OpenFB,$rootScope,callback) {

// We need two API calls to Facebook because FB changed the API in graph 2.0                                                                                                         
// to get friends LIST is tough now                                                                                                                                                  
// for any games , first call with /me/invitable_friends and /me/friends                                                                                                             
// when a friends never played your game ( here karmakorner )  "me/invitable_friends" will give a list in which all friends will be there                                            
// your friends starts playing your game ( here karmakorner ) his name will come in "/me/friends" list ,( and not in "me/invitable_friends"                                          
//                                                                                                               
    OpenFB.get("/me/friends", {limit: 50,fields: 'id,name,first_name,picture.width(120).height(120)'})
        .success(function (result) {
		console.log("in /me/friends - success");
		//console.log(result.data);
            $rootScope.friendsFBHash = new Object();
            $rootScope.friendsList = [];
            for ( var i=0; i < result.data.length; i++) {
                $rootScope.friendsFBHash[result.data[i].name]=result.data[i];
                $rootScope.friendsList.push(result.data[i].name);
            }
            
        })
        .error(function(data) {
			    console.log("in pointsCtrl - failure");
            //alert(data.error.message);
        });           
               
    OpenFB.get("/me/invitable_friends", {limit: 50,fields: 'id,name,first_name,picture.width(120).height(120)'})
        .success(function (result) {
		console.log("in me/invitable_friends - success");
		    for ( var i=0; i < result.data.length; i++) {
                $rootScope.friendsFBHash[result.data[i].name]=result.data[i];
                $rootScope.friendsList.push(result.data[i].name);
            }
          callback();        
        })
        .error(function(data) {
			    console.log("in pointsCtrl - failure");
            //alert(data.error.message);
        });        
    
}

function loadFriendsPoints($rootScope) {
           for ( var i=0; i < $rootScope.sortedListTo.length; i++) {
                var name = $rootScope.sortedListTo[i].name;
                var fbdata = $rootScope.friendsFBHash[name];
                $rootScope.sortedListTo[i].fbdata=fbdata; // load the data from FB 
            }
        
            for ( var i=0; i < $rootScope.sortedListFrom.length; i++) {
                var name = $rootScope.sortedListFrom[i].name;
                var fbdata = $rootScope.friendsFBHash[name];
                $rootScope.sortedListFrom[i].fbdata=fbdata; // load the data from FB 
            }   
    
}

function calculatePoints($scope) {

   var data = $scope.kkData;    
   var toHash=[];   // exists to remove duplicates in ToList
   var toHashPoints={}; // to sum the points 
   var fromHash=[]; // exists to remove duplicates in FromList
   var fromHashPoints={};  // to sum the points 
   var toList = [];  // will have the assembled links
   var fromList = [];  // will have assembled links 
    
    var Element = null;
    for ( var i=0; i < data.pointsToList.length; i++) {
        Element =  data.pointsToList[i];
        toHash[Element.nameTo]= Element.nameTo;
        if ( isNaN(toHashPoints[Element.nameTo])  ) {
            toHashPoints[Element.nameTo] = parseInt(Element.points);
        }
        else {
            toHashPoints[Element.nameTo] += parseInt(Element.points);
        }
    };


    var forSorting = {} ;  //for sorting the names by points                                                                                                                 
    var sortedList = [] ;
    for (var key in toHash)  {
        if (toHash.hasOwnProperty(key)) {
            forSorting={} ;
            forSorting['name'] = key;
            forSorting['points'] = toHashPoints[key];
            sortedList.push(forSorting);  //not yet sorted                                              
            }
        }
        sortedList.sort(function(a, b){
            var pointsA = a.points;
            var pointsB = b.points;
            if (pointsA > pointsB) { //sort string descending 
                return -1;
            }
            if (pointsA < pointsB) {
                return 1;
            }
            return 0; //default return value (no sorting)  
        });

        $scope.sortedListTo=sortedList;  // sorted points List    
       

        for ( var i=0; i < data.pointsFromList.length; i++) {
            Element =  data.pointsFromList[i];
            fromHash[Element.nameFrom]= Element.nameFrom;
            if ( isNaN(fromHashPoints[Element.nameFrom])  ) {
                fromHashPoints[Element.nameFrom] = parseInt(Element.points);
            }
            else {
                fromHashPoints[Element.nameFrom] += parseInt(Element.points);
            }
        };

        forSorting = {} ;  //for sorting the names by points
    
        sortedList = [] ;
        for (var key in fromHash)  {
            if (fromHash.hasOwnProperty(key)) {
                forSorting={} ;
                forSorting['name'] = key;
                forSorting['points'] = fromHashPoints[key];
                sortedList.push(forSorting);  //not yet sorted   
            }
        }
        sortedList.sort(function(a, b){
            var pointsA = a.points;
            var pointsB = b.points;
            if (pointsA > pointsB) { //sort string descending     
                
                return -1;
            }
            if (pointsA < pointsB) {
                return 1;
            }
            return 0; //default return value (no sorting)    
            
        });

        $scope.sortedListFrom=sortedList;  // sorted points List  
}