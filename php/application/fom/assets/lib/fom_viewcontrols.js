$(function() {
	$('#search_result a').live('click', function(event) {
		event.preventDefault();
		var searchUrl = $(this).attr('href');
		$( "#search_content" ).dialog( 'close' );
		createScatter( searchUrl );
	});
});

$(function() {
	$('#t_query').keyup(function() {
		var t_query = $(this).val();
		var t_class = $('#topic_search_form input:radio:checked').val();
		if( t_query.length > 1 ) {
			searchTopics( t_query, t_class );
			$('#search_result').show();
		} else {
			$('#search_result').empty().hide();
		}
	});
});

$(function() {
	$('#queries').change(function() {
		var queryId = $(this).val();
		var clusterUrl = siteUrl + '/cluster/read/' + queryId;
		$('#content').empty().fadeOut('fast');
		$('#post_content').empty().fadeOut('fast');
		$('#query_meta').empty().fadeOut('fast');
		deleteOverlays();
		loadMarkers( clusterUrl );
	});
});

$(function() {
	$( "#legend_content" ).dialog({
		width: 500,
		autoOpen: false
	});
});

$(function() {
	$( "#about_content" ).dialog({
		autoOpen: false
	});

	$( "#about" ).click(function() {
		$( "#about_content" ).dialog( "open" );
		return false;
	});
});

$(function() {
	$( "#search_content" ).dialog({
		autoOpen: false,
		modal: true
	});

	$( "#search" ).click(function() {
		$( "#search_content" ).dialog( "open" );
		return false;
	});
});

$(function() {
	$('#showhide_panels').click(function(e) { 
		if( $('#post_content, #content').is(':visible') ) {
			$('#post_content, #content').fadeOut('fast');
		} else {
			$('#post_content, #content').fadeIn('fast');
		}
	});
});

$(function() {
	$('#content a.postUrl').live('click', function(event) {
		event.preventDefault();
		var postUrl = $(this).attr('href');
		loadClusterContent( postUrl );
	});
});

$(function() {
	$('#post_content a').live('click', function(event) {
		event.preventDefault();
		var linkUrl = $(this).attr('href');
		loadLinkContent( linkUrl );
	});
});