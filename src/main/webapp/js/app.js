define([
	'views/home',
	'views/navbar',
	'views/footer'
	], 
	function(HomeView, NavbarView, FooterView) {
		var App = function() {
			this.views.home = new HomeView();
			this.views.home.render();

			this.views.navbar = new NavbarView();
			this.views.navbar.render();

			this.views.footer = new FooterView();
			this.views.footer.render();
		};

  	App.prototype = {
  		views: {}

	};

  return App;
});