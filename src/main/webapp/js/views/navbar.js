define([
  'text!templates/navbar.html'
],

function(template) {
  var NavView = Backbone.View.extend({
    el: '.navbar',
    //tagname: 'div'
    template: _.template(template),

    events: {
    },

    initialize: function() {
    },

    render: function() {
      this.$el.html(this.template());
      return this;
    }
  });

  return NavView;
});