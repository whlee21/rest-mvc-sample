var App = require('app');

App.HelloController = Em.ObjectController.extend({
	name : 'helloController',

	message : '',

	actions : {
		getMessage : function() {
			if (this.get('message') != '') {
				this.set('message', '');
			}
			App.ajax.send({
				name : 'hello.guice',
				sender : this,
				data : {
					x : 'hello'
				},
				success : 'getMessageSuccessCallback',
				error : 'getMessageErrorCallback'
			});
		},
	},

	getMessageSuccessCallback : function(data) {
		this.set('message', data);
	},

	getMessageErrorCallback : function(error) {
		console.log("failed to invoke get message from the server");
	}
});