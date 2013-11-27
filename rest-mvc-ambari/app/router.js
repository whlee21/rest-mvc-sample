var App = require('app');
App.Router = Em.Router.extend({
	enableLogging : true,
	isFwdNavigation : true,
	backBtnForHigherStep : false,

	loggedIn : false,
	
		root : Em.Route.extend({
			index : Em.Route.extend({
				route : '/',
				redirectsTo : 'hello'
			}),

			hello : Em.Route.extend({
				route : '/hello',
	
				enter : function(router, context) {

				},
	
				connectOutlets : function(router, context) {
					$('title').text(Em.I18n.t('app.name'));
					router.get('applicationController').connectOutlet('hello');
				},
				
				getMessage: function (router, event) {
					App.router.get('helloController').getMessage();
				},
			})

	})

});
