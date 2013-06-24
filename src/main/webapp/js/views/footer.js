define([
  'text!templates/footer.html'
],

function(template) {
  var HomeView = Backbone.View.extend({
    
    el: '.app-footer',
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

  return HomeView;
});