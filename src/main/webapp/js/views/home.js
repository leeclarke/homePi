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
    	//TODO: add click event to gplus-btn
    	"click .gplus-btn" : "doGplusAuth"
    },

    doGplusAuth: function() {
    	alert('Sorry, haven\'t coded this just yet.');
    },

    initialize: function() {
    },

    render: function() {
      this.$el.html(this.template());
      return this;
    }
  });

  return HomeView;
});