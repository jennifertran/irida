// The contents of this file gets load via Dandelion Datatables to the end of IIFE.

/*
 *  Add the toolbar to the table.
 *
 *  Since Datatables is dynamically adding the toolbar buttons beside the search input, we need a way for
 *  AngularJS to compile the template for these buttons.  Here we are getting a reference to AngularJS's
 *  $compile function outside of Angular by using `injector().invoke()`
 */
var $target = $("[ng-app]");
angular.element($target).injector().invoke(["$rootScope", "$compile", function ($rootScope, $compile) {
  var toolbar = $(".filter-row > div")[0],
    tools = $("#toolbar");
  $(toolbar).html(function () {
    return $compile(tools)($rootScope);
  });
}]);

// Handle clicking the table rows.
document.querySelector("#samplesTable tbody").addEventListener("click", datatable.tbodyClickEvent, false);