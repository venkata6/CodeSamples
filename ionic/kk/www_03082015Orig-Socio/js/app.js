// Ionic karmakorner App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'karmakorner' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'karmakorner.services' is found in services.js
// 'karmakorner.controllers' is found in controllers.js
angular.module('karmakorner', ['ionic', 'openfb','karmakorner.controllers', 'karmakorner.services'])

.run(function ($rootScope, $state, $ionicPlatform, $window, OpenFB) {
    // for testing in local box 
    //OpenFB.init('221283828060895','http://localhost:8100/oauthcallback.html');
    // for testing in device  
    OpenFB.init('221283828060895','https://www.facebook.com/connect/login_success.htmlRemove');
    
    $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
  });
    
 /*  $rootScope.$on('$stateChangeStart', function(event, toState) {
            if (toState.name !== "tab.login" && toState.name !== "tab.logout" &&        !$window.sessionStorage['fbtoken']) {
                $state.go('tab.login');
                event.preventDefault();
            }
        });

        $rootScope.$on('OAuthException', function() {
            //$state.go('login');
        });  */
})

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
 // setup an abstract state for the tabs directive
   .state('tab', {
    url: "/tab",
    abstract: true,
    templateUrl: "templates/tabs.html"
  })

  .state('tab.login', {
             url: "/login",
             views: {
               'tab-login': {
                     templateUrl: "templates/login.html",
                     controller: "LoginCtrl"
                }
              }
          })
  
  .state('tab.logout', {
                url: "/logout",
                views: {
                    'tab-logout': {
                        templateUrl: "templates/logout.html",
                        controller: "LogoutCtrl"
                    }
                }
            })
  // Each tab has its own nav history stack:
 
  .state('tab.dash', {
    url: '/dash',
    views: {
      'tab-dash': {
        templateUrl: 'templates/tab-dash.html',
        controller: 'DashCtrl'
      }
    }
  })

  .state('tab.pointsGot', {
      url: '/points_got',
      views: {
        'tab-got-points': {
          templateUrl: 'templates/tab-points-got.html',
          controller: 'PointsGotCtrl'
        }
      }
    })
  
   .state('tab.pointsGiven', {
      url: '/points_given',
      views: {
        'tab-given-points': {
          templateUrl: 'templates/tab-points-given.html',
          controller: 'PointsGivenCtrl'
        }
      }
    })
  
    .state('tab.chat-detail', {
      url: '/chats/:chatId',
      views: {
        'tab-chats': {
          templateUrl: 'templates/chat-detail.html',
          controller: 'ChatDetailCtrl'
        }
      }
    })

  .state('tab.friends', {
      url: '/friends',
      views: {
        'tab-friends': {
          templateUrl: 'templates/tab-friends.html',
          controller: 'FriendsCtrl'
        }
      }
    })
    .state('tab.friend-detail', {
      url: '/friend/:friendId',
      views: {
        'tab-friends': {
          templateUrl: 'templates/friend-detail.html',
          controller: 'FriendDetailCtrl'
        }
      }
    })

 /* .state('tab.account', {
    url: '/account',
    views: {
      'tab-account': {
        templateUrl: 'templates/tab-account.html',
        controller: 'AccountCtrl'
      }
    }
  });
 */
    // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/tab/dash');

});
