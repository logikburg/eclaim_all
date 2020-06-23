$(function() {

	$('div.bootstrap-tagsinput').on('change', function(event) {
		console.log('bootstrap-tagsinput');
	});

	$('input, select').on(
		'change',
		function(event) {
			var $element = $(event.target), $container = $element.closest('.example');


			if (!$element.data('tagsinput')) {
				console.log("WithOut Text");
				var elt = $('.example_objects_as_tags > > input');
				elt.tagsinput('add', {
					"value" : $element.val(),
					"text" : $element.val()
				});
				$element.typeahead('val', '');
			} else {

				var val = $element.val();
				if (val === null)
					val = "null";
				$('code', $('pre.val', $container)).html(
						($.isArray(val) ? JSON.stringify(val) : "\"" + val.replace('"', '\\"') + "\""));
				$('code', $('pre.items', $container)).html(JSON.stringify($element.tagsinput('items')));
			}
		}).trigger('change');
});