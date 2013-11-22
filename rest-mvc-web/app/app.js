module.exports = Em.Application.create({
	name : 'REST MVC Sample',
	rootElement : '#wrapper',
	store : DS.Store.create({
		revision : 4,
		adapter : DS.FixtureAdapter.create({
			simulateRemoteResponse : false
		})
	}),
	isAdmin : false,
	LOG_TRANSITIONS: true
});