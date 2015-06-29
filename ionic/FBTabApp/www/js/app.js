// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'openfb', 'starter.controllers', 'starter.services'])
    
.run(function ($rootScope, $state, $ionicPlatform, $window, OpenFB) {
	//OpenFB.init('221283828060895','https://www.facebook.com/connect/login_success.html');
	OpenFB.init('221283828060895','http://localhost:8100/oauthcallback.html');

	/*    
// DEBUG START -     
$rootScope.$on('$stateChangeStart',function(event, toState, toParams, fromState, fromParams){
  console.log('$stateChangeStart to '+toState.to+'- fired when the transition begins. toState,toParams : \n',toState, toParams);
});
$rootScope.$on('$stateChangeError',function(event, toState, toParams, fromState, fromParams, error){
  console.log('$stateChangeError - fired when an error occurs during transition.');
  console.log(arguments);
});
$rootScope.$on('$stateChangeSuccess',function(event, toState, toParams, fromState, fromParams){
  console.log('$stateChangeSuccess to '+toState.name+'- fired once the state transition is complete.');
});
// $rootScope.$on('$viewContentLoading',function(event, viewConfig){
//   // runs on individual scopes, so putting it in "run" doesn't work.
//   console.log('$viewContentLoading - view begins loading - dom not rendered',viewConfig);
// });
$rootScope.$on('$viewContentLoaded',function(event){
  console.log('$viewContentLoaded - fired after dom rendered',event);
});
$rootScope.$on('$stateNotFound',function(event, unfoundState, fromState, fromParams){
  console.log('$stateNotFound '+unfoundState.to+'  - fired when a state cannot be found by its name.');
  console.log(unfoundState, fromState, fromParams);
});
	*/
// DEBUG END      
    
    
   // OpenFB.init('221283828060895');

  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleLightContent();
    } 
     //$state.go('login');  
    //$location.path('/login');  
  });

    $rootScope.$on('$stateChangeStart', function(event, toState) {
      if (toState.name !== "login" && toState.name !== "logout" && !$window.sessionStorage['fbtoken']) {
          event.preventDefault();
          $state.go('login');
          
      }
  });
  
  $rootScope.$on('OAuthException', function() {
      $state.go('login');
  });
})


.config(function($stateProvider, $urlRouterProvider,$httpProvider) {

   // We need to setup some parameters for http requests
   // These three lines are all you need for CORS support
   //$httpProvider.defaults.useXDomain = true;
   //$httpProvider.defaults.withCredentials = true;
   //delete $httpProvider.defaults.headers.common['X-Requested-With'];
    
  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: h ttps://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider

  // setup an abstract state for the tabs directive
  .state('tab', {
    url: "/tab",
    abstract: true,
    templateUrl: "templates/tabs.html"
  })

  // Each tab has its own nav history stack:

  .state('login', {
      url: "/login",
      templateUrl: "templates/login.html",
      controller: "LoginCtrl"
  })

  .state('logout', {
      url: "/logout",
      templateUrl: "templates/logout.html",
      controller: "LogoutCtrl"
  })

  .state('tab.home', {
    url: '/home',
    views: {
      'tab-home': {
        templateUrl: 'templates/home.html',
        controller: 'MeCtrl'
      }
    }
  })

  .state('tab.points', {
      url: '/points',
      views: {
        'tab-points': {
          templateUrl: 'templates/tab-points.html',
          controller: 'PointsCtrl'
        }
      }
    })
  .state('tab.give_points', {
      url: '/give_points',
      views: {
        'tab-home': {
          templateUrl: 'templates/give-points.html',
          controller: 'PointsGiveCtrl'
        }
      }
    }) 
  .state('tab.post_points', {
      url: '/post_points',
      views: {
        'tab-home': {
          templateUrl: 'templates/post-points.html',
          controller: 'PointsPostCtrl'
        }
      }
    }) 
    .state('tab.points-detail', {
      url: '/points/detail/:direction/:fbName',
      views: {
        'tab-points': {
          templateUrl: 'templates/chat-detail.html',
          controller: 'PointsDetailCtrl'
        }
      }
    })

  .state('tab.friends', {
    url: '/friends',
    views: {
      'tab-friends': {
        templateUrl: 'templates/tab-account.html',
        controller: 'AccountCtrl'
      }
    }
  });

  // if none of the above states are matched, use this as the fallback
  // $urlRouterProvider.otherwise('/tab/account');
  //$urlRouterProvider.otherwise('/tab/home');
    $urlRouterProvider.otherwise('login');

});
