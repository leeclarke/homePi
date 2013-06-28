define([
	'text!templates/home.html',
	], 

function(template) { //, NavbarView, FooterView) {

  var HomeView = Backbone.View.extend({
    id: 'main',
    tagName: 'div',
    className: 'container-fluid',
    el: 'body',
    template: _.template(template),

    events: {
    	"click .gplus-btn" : "doGplusAuth"
    },

    doGplusAuth: function() {
    	//window.open('./services/user/googleauth');
    	window.location.href = './services/user/googleauth';
    },

    initialize: function() {
    },

    render: function() {
      this.$el.html(this.template());
      return this;
    },
  });

  return HomeView;
});