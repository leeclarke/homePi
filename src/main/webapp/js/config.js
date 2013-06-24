define([], function() {
  var config = {};

  _.templateSettings = {
    interpolate: /\{\{(.+?)\}\}/g
  };

  return config;
});