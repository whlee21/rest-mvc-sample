window.App = require('app');

require('messages');

require('controllers');
require('templates');
require('views');
require('router');

require('utils/ajax');

App.initialize();

console.log('after initialize');