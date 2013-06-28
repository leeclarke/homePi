/*Angluar controller*/
function TodoCtrl($scope,$http) {
  $scope.msg = {text:'msg'};
  $scope.user = {userName:'Joe'};
    
  $scope.todos = [
    {text:'learn angular', done:true},
    {text:'build an angular app', done:false}];

  $scope.addTodo = function() {
    $scope.todos.push({text:$scope.todoText, done:false});
    $scope.todoText = '';
  };

  $scope.remaining = function() {
    var count = 0;
    angular.forEach($scope.todos, function(todo) {
      count += todo.done ? 0 : 1;
    });
    return count;
  };

    $scope.getUser =  function(){
        return 'test'
    }
    
    $scope.loadUser = function(){
        $http.get('/services/user/profile/test_user')
            .success(function(data, status, headers, config) {
                console.log('got resp!  data= ' + data);
              })
            .error(function(data, status, headers, config) {
                alert('got error');
                console.log('got error: ' + status + '\n ' + data);
              });
    }

  $scope.authUser = function(){
    //window.open('./services/user/googleauth');
    window.location.href = './services/user/googleauth';
     
  }

  $scope.archive = function() {
    var oldTodos = $scope.todos;
    $scope.todos = [];
    angular.forEach(oldTodos, function(todo) {
      if (!todo.done) $scope.todos.push(todo);
    });
  };
}

