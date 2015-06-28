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
    
 /*  $scope.forSelect ='';    
  
    $rootScope.friendsList.sort(function(a, b){
        var nameA = a.toLowerCase(),
            nameB = b.toLowerCase();
        if (nameA < nameB) { //sort string ascending                                                                                                                                 
            return -1;
        }
        if (nameA > nameB) {
            return 1;
        }
        return 0; //default return value (no sorting)                                                                                                                                
    });
   $rootScope.friendsList.each(function(index, Element) {
        $scope.forSelect += '<option value="' + Element.name + '">' + Element.name +'</option>';
    });
    alert($scope.forSelect);
   */ 
})



.controller('PointsCtrl', function ($scope, $stateParams, OpenFB, $rootScope) {
    //alert("friends ctrl");
    OpenFB.get("/me/friends", {limit: 50,fields: 'id,name,first_name,picture.width(120).height(120)'})
        .success(function (result) {
		//console.log("in pointsCtrl - success");
		//console.log(result.data);
            $rootScope.friendsFBHash = new Object();
            //$rootScope.friendsList = [];
            for ( var i=0; i < result.data.length; i++) {
                $rootScope.friendsFBHash[result.data[i].name]=result.data[i];
              //  $rootScope.friendsList.push(result.data[i].name);
            }
            
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
           // $rootScope.friends = result.data;
         //alert(JSON.stringify($scope.friends)) ;
        })
        .error(function(data) {
			    console.log("in pointsCtrl - failure");
            //alert(data.error.message);
        });
})

.controller('MeCtrl', function ($scope, $stateParams, OpenFB,kkService,$http, $rootScope) {
    //alert(angular.element(e).injector().get('kkService'));
    //alert(JSON.stringify(angular.element(document.body).scope()));
    OpenFB.get("/me", {fields: 'id,name,first_name,picture.width(120).height(120)'})
        .success(function (result) {
		//console.log("meCtrl-success");
            //$scope.kkData = kkService.save({user: $scope.me.name},{id: $scope.me.id}  );
            //console.log(JSON.stringify($scope.kkData));
            // Simple POST request example (passing data) :
            var str = 'https://karmakorner.co/kk/webapi/people/getPoints/';
            str += $scope.me.name + '/' + $scope.me.id;
            //alert(str);
            $http({
                method: 'POST',
                url:str 
                }).
              success(function(data, status, headers, config) {
                $rootScope.kkData=data;
                calculatePoints($rootScope);
                // this callback will be called asynchronously
                // when the response is available
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
})


// .controller('DashCtrl', function($scope) {})

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