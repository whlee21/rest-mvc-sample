window.App = require('app');

require('messages');

require('controllers');
require('templates');
require('views');
require('router');

require('ajax');

App.initialize();

console.log('after initialize');