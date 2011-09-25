$(function() {

	$('#legend_toggle_hidden').live('click', function(event) {
		$(this).toggleClass('hidden');
		if( $(this).is('.hidden') ) {
			$('#legend_content li').addClass('hidden');
			for(var i = 0; i < circlesArray.length; i++) {
				circlesArray[i].setMap(null);
			}
		} else {
			$('#legend_content li').removeClass('hidden');			
			for(var i = 0; i < circlesArray.length; i++) {
				circlesArray[i].setMap(map);
			}
		}
	});

	// click on scatter element
	$('#legend_content li').live('click', function(event) {
		var category = $(this).attr('id');
		$(this).toggleClass('hidden');
		for(var i = 0; i < circlesArray.length; i++) {
			if(circlesArray[i].scatterCategory == category) {
				if($(this).is('.hidden')) {
					circlesArray[i].setMap(null);					
				} else {
					circlesArray[i].setMap(map);
				}
			}
		}
	});

	// click on live search visualization of scatter data
	$('#search_result a').live('click', function(event) {
		event.preventDefault();
		var searchUrl = $(this).attr('href');
		var searchTerm = $(this).attr('title');
		$('#query_meta').empty().dialog('close');
		$('#search_result').hide();
		$('#t_query').val(searchTerm);
		createScatter(searchUrl, searchTerm);
	});

	//live search of topics
	$('#t_query').keyup(function() {
		var t_query = $(this).val();
		var t_class = $('#t_class').val();
		console.log(t_class);
		if(t_query.length > 1) {
			searchTopics(t_query, t_class);
		} else {
			$('#search_result').empty().hide();
		}
	});


	$('#queries').change(function() {
		var queryId = $(this).val();
		var clusterUrl = siteUrl + '/cluster/read/' + queryId;
		$('#content').empty().fadeOut('fast');
		$('#post_content').empty().fadeOut('fast');
		$('#query_meta').empty().dialog('close');
		deleteOverlays();
		loadMarkers(clusterUrl);
	});

	$('#query_meta').dialog({
		width: 500,
		position: [150, 100],
		autoOpen: false
	});

	$('#show_query_stats').click(function() {
		showQueryStats();
	});

	$('#legend_content').dialog({
		width: 500,
		position: [100, 150],
		autoOpen: false,
		close: function(event, ui) {
			var queryId = $('#queries').val();
			var clusterUrl = siteUrl + '/cluster/read/' + queryId;
			$('#content').empty().fadeOut('fast');
			$('#t_query').val('');
			$('#post_content').empty().fadeOut('fast');
			$('#query_meta').empty().dialog('close');
			deleteOverlays();
			loadMarkers(clusterUrl);
		}
	});

	$('#about_content').dialog({
		autoOpen: false
	});

	$('#about').click(function() {
		$('#about_content').dialog('open');
		return false;
	});

	$('#help_content').dialog({
		autoOpen: false,
		width: 800,
		height: 500
	});

	$('#help').click(function() {
		$('#help_content').dialog('open');
		return false;
	});

	$('#showhide_panels').click(function(e) { 
		if($('#post_content, #content').is(':visible')) {
			$('#post_content, #content').fadeOut('fast');
		} else {
			$('#post_content, #content').fadeIn('fast');
		}
	});

	$('#content a.postUrl').live('click', function(event) {
		event.preventDefault();
		var postUrl = $(this).attr('href');
		loadClusterContent(postUrl);
	});

	$('#post_content a').live('click', function(event) {
		event.preventDefault();
		var linkUrl = $(this).attr('href');
		loadLinkContent(linkUrl);
	});

});