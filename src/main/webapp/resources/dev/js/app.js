/* global angular */

angular.module('irida', ['irida.services', 'irida.directive']);

angular.module('irida.directive', [])
  .directive('contenteditable', function () {
    return {
      restrict: 'A', // only activate on element attribute
      require : '?ngModel', // get a hold of NgModelController
      link    : function (scope, element, attrs, ngModel) {
        if (!ngModel) return; // do nothing if no ng-model

        // Specify how UI should be updated
        ngModel.$render = function () {
          element.html(ngModel.$viewValue || '');
        };

        // Listen for change events to enable binding
        element.bind('blur keyup change', function () {
          scope.$apply(read);
        });
        read(); // initialize

        // Write data to the model
        function read() {
          ngModel.$setViewValue(element.html());
        }
      }
    };
  });

angular.module('irida.services', ['ngResource'])
  .service('AjaxService', function ($http, $q) {
    'use strict';
    return {
      create: function (url, data) {
        var deferred = $q.defer();
        $http({
          method : 'POST',
          url    : url,
          data   : data,
          headers: {
            'Content-Type': 'application/json'
          }
        })
          .success(function (data) {
            deferred.resolve(data);
          })
          .error(function (data) {
            deferred.reject(data);
          });
        return deferred.promise;
      },
      get   : function (url) {
        if (url) {
          var deferred = $q.defer();

          $http.get(url)
            .success(function (data) {
              deferred.resolve(data);
            })
            .error(function () {
              deferred.reject('An error occurred while getting projects');
            });

          return deferred.promise;
        }
      },
      patch : function (url, data) {
        if (url && data) {
          var deferred = $q.defer();

          $http({
            method : 'PATCH',
            url    : url,
            data   : data,
            headers: {
              'Content-Type': 'application/json'
            }
          })
            .success(function (data) {
              deferred.resolve(data);
            })
            .error(function (data) {
              deferred.reject(data);
            });
          return deferred.promise;
        }
      },
      delete: function (url) {
        if (url) {
          var deferred = $q.defer();

          $http({
            method : 'DELETE',
            url    : url,
            headers: {
              'Content-Type': 'application/json'
            }
          })
            .success(function (data) {
              deferred.resolve(data);
            })
            .error(function (data, status, headers, config) {
              console.log(data);
              console.log(status);
              console.log(headers);
              console.log(config);
              deferred.reject(status);
            });
          return deferred.promise;
        }
      }
    };
  });