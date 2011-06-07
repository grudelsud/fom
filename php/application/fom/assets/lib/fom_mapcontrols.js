/**
 * 
 */
var map;
var markersArray = [];
var circlesArray = [];

function initialize( initialLocation )
{
	var myOptions = {
		zoom: 6,
		center: initialLocation,
		mapTypeId: google.maps.MapTypeId.HYBRID
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	mgr = new MarkerManager(map);
	google.maps.event.addListener(map, 'click', function(event) {
		$('#footer').empty().append( 'clicked on: ' + event.latLng.toString() );
	});
	map.enableKeyDragZoom({
		boxStyle: {
			border: "1px dashed #DDDDDD",
			backgroundColor: "transparent",
			opacity: 1.0
		},
		veilStyle: {
			backgroundColor: "#333355",
			opacity: 0.70,
			cursor: "crosshair"
		},
		visualEnabled: true,
		visualPosition: google.maps.ControlPosition.LEFT,
		visualPositionOffset: new google.maps.Size(35, 0),
		visualPositionIndex: null,
		visualSprite: "http://maps.gstatic.com/mapfiles/ftr/controls/dragzoom_btn.png",
		visualSize: new google.maps.Size(20, 20),
		visualTips: {
			off: "Turn on",
			on: "Turn off"
		}
	});
	var dz = map.getDragZoomObject();
	google.maps.event.addListener(dz, 'dragend', function (bnds) {
		dragZoom(bnds);
	});
}

/**
 * this function called after click on top-left icon for query details
 */
function showQueryStats()
{
	// if it's already visible, just turn it off
	if( $('#query_meta').is(':visible') ) {
		$('#query_meta').empty().toggle('fast');
	} else {
		// display a spinner while waiting result from the server
		var queryId = $('#queries').val();
		$('#ajax_loader').empty().html('<img src="'+assetsUrl+'/img/ajax-loader.gif" alt="loading">');
		
		$('#query_meta').empty();
		
		$.ajax({
			url: siteUrl + '/cluster/stat/'+queryId,
			dataType: 'json',
			success: function(data) {
				// all good, received results! remove the spinner and display the stats
				var queryStats = $('<ul></ul>');
				queryStats.append('<li>topics/cluster: '+data.numberOfTopics+'</li>');
				queryStats.append('<li>terms/topic: '+data.numberOfWords+'</li>');
				queryStats.append('<li>min followers:'+data.minFollCount+'</li>');

				queryStats.append('<li>time granularity: '+data.tGranularity+'</li>');
				queryStats.append('<li>geo granularity: '+data.geoGranularity+'</li>');

				queryStats.append('<li>geo clusters: '+data.gClusterNumTot+'</li>');
				queryStats.append('<li>sem clusters: '+data.sClusterNumTot+'</li>');
				queryStats.append('<li>tot terms: '+data.termsNumTot+' ('+data.termsNumOverbias+' over a bias of '+data.tfBias+')</li>');

				$('#query_meta').append( queryStats ).append('<img src="'+data.chartUrl+'" title="TF-IDF chart"/>').toggle('fast');
				$('#ajax_loader').empty();
			}
		});
	}
}

/**
 * this function only displays #content and #post_content panels with query forms for posts and clusters
 */
function dragZoom(bnds)
{
	var swLat = bnds.getSouthWest().lat();
	var swLon = bnds.getSouthWest().lng();
	var neLat = bnds.getNorthEast().lat();
	var neLon = bnds.getNorthEast().lng();
	
	var divClusterStatForm = $('<div id="div_clusterstat_form"></div>');
	var divPostStatForm = $('<div id="div_poststat_form"></div>');

	var clusterStatForm = '<h3>Cluster stats</h3><form name="cluster_stat_form" id="cluster_stat_form" method="post" action="">';
	clusterStatForm += '<label for="c_since">since:</label><input type="text" name="c_since" id="c_since">';
	clusterStatForm += '<label for="c_until">until:</label><input type="text" name="c_until" id="c_until">';
//	clusterStatForm += '<label><input type="radio" name="c_timespan" value="daily" id="timespan_0" checked>daily</label>';
//	clusterStatForm += '<label><input type="radio" name="c_timespan" value="hourly" id="timespan_1">hourly</label>';
	clusterStatForm += '<input type="submit" name="submit" id="submit" value="Submit">';
	clusterStatForm += '<input type="hidden" name="swLat" id="swLat" value="'+swLat+'"><input type="hidden" name="swLon" id="swLon" value="'+swLon+'">';
	clusterStatForm += '<input type="hidden" name="neLat" id="neLat" value="'+neLat+'"><input type="hidden" name="neLon" id="neLon" value="'+neLon+'"></form>';
	clusterStatForm += '<div id="div_cluster_stat_result"></div>';

	var postStatForm = '<h3>Post stats</h3><form name="post_stat_form" id="post_stat_form" method="post" action="">';
	postStatForm += '<label for="p_since">since:</label><input type="text" name="p_since" id="p_since">';
	postStatForm += '<label for="p_until">until:</label><input type="text" name="p_until" id="p_until">';
	postStatForm += '<label><input class="radio" type="radio" name="p_timespan" value="daily" id="timespan_2" checked> daily</label>';
	postStatForm += '<label><input class="radio" type="radio" name="p_timespan" value="hourly" id="timespan_3"> hourly</label>';
	postStatForm += '<input type="submit" name="submit" id="submit" value="Submit">';
	postStatForm += '<input type="hidden" name="swLat" id="swLat" value="'+swLat+'"><input type="hidden" name="swLon" id="swLon" value="'+swLon+'">';
	postStatForm += '<input type="hidden" name="neLat" id="neLat" value="'+neLat+'"><input type="hidden" name="neLon" id="neLon" value="'+neLon+'"></form>';
	postStatForm += '<div id="div_post_stat_result"></div>';

	divClusterStatForm.append( clusterStatForm );
	divPostStatForm.append( postStatForm );

	$('#content').empty().append( clusterStatForm ).fadeIn('fast');
	$('#post_content').empty().append( postStatForm ).fadeIn('fast');

	$('#c_since').live('click', function() { $(this).datepicker({showOn:'focus', dateFormat: 'yy-mm-dd'}).focus(); });
	$('#c_until').live('click', function() { $(this).datepicker({showOn:'focus', dateFormat: 'yy-mm-dd'}).focus(); });

	$('#p_since').live('click', function() { $(this).datepicker({showOn:'focus', dateFormat: 'yy-mm-dd'}).focus(); });
	$('#p_until').live('click', function() { $(this).datepicker({showOn:'focus', dateFormat: 'yy-mm-dd'}).focus(); });

	$('#cluster_stat_form').live('submit', function(e) {
		e.preventDefault();
		showClusterStats();
	});

	$('#post_stat_form').live('submit', function(e) {
		e.preventDefault();
		showPostStats();
	});
}

function showClusterStats()
{
	var params = $('#cluster_stat_form').serialize();
	var searchUrl = siteUrl + '/cluster/dzstat/'+params.replace(/&/g,'/');

	$('#div_cluster_stat_result').empty().html('<img src="'+assetsUrl+'/img/ajax-loader.gif" alt="loading">');

	$.ajax({
		url: searchUrl,
		dataType: 'json',
		success: function( data ) {
			var chartNumPost = '<img src="'+data.charts.chartUrlPosts+'" alt="chart num posts" />';
			var chartNumCluster = '<img src="'+data.charts.chartUrlClusters+'" alt="chart num posts" />';
			
			$('#div_cluster_stat_result').empty().append( chartNumPost + chartNumCluster );
		}
	});
}

function showPostStats() 
{
	var params = $('#post_stat_form').serialize();
	var searchUrl = siteUrl + '/search/dzstat/'+params.replace(/&/g,'/');

	$('#div_post_stat_result').empty().html('<img src="'+assetsUrl+'/img/ajax-loader.gif" alt="loading">');

	$.ajax({
		url: searchUrl,
		dataType: 'json',
		success: function(data) {
			var title = 'Post stats for interval [' + $('#p_since').val() + ' - ' + $('#p_until').val() + ']';
			var res = $('<ul></ul>');
			$.each(data.result, function(day, langs) {
				var values = '';
				$.each(langs, function(lang, count) {
					values += lang+'['+count+'] ';
				});
				res.append('<li>'+day+': '+values+'</li>');
			});
			$('#div_post_stat_result').empty().append( title ).append( res );
		}
	});
}

function loadMarkers( clusterUrl )
{
	$.ajax({
		url: clusterUrl,
		dataType: 'json',
		success: function(data) {
			deleteOverlays();
			$.each(data, function(i, cluster) {
				addMarker( cluster );
			});
		}
	});
}

function addMarker( cluster ) 
{
	var location = new google.maps.LatLng( cluster.meanLat, cluster.meanLon );

	marker = new google.maps.Marker({
		flat: true,
		position: location,
		map: map,
		zIndex: 2
	});
	markersArray.push(marker);	
	
	google.maps.event.addListener(marker, 'click', function(event) {
		deleteCircles();
		circle = new google.maps.Circle({
			center: location,
			clickable: false,
			fillColor: "#FF0000",
			fillOpacity: clusterOpacity( cluster.posts_meta ),
			map: map,
			radius: 80 * clusterArea(cluster.stdDevLat, cluster.stdDevLon),
			strokeColor: "#FFFFFF",
			strokeWeight: 2,
			zIndex: 1
		});
		circlesArray.push(circle);
		loadContent(cluster);
	});
}

function clusterOpacity( posts_meta )
{
	var postArray = posts_meta.split( ' ' );
	var opacity = 0.3;
	if( postArray.length > 10 ) {
		if( postArray.length > 20 ) {
			opacity = 0.8;
		} else {
			opacity = 0.5;
		}
	}
	return opacity;
}

function clusterArea( stdDevLat, stdDevLon )
{
//	var latDegminsec = dec2degminsec( stdDevLat );
//	var lonDegminsec = dec2degminsec( stdDevLon );

	// conversion values defined on wikipedia: http://en.wikipedia.org/wiki/Geographic_coordinate_system
//	var stdDevLatMet = 110600 * latDegminsec[0] + 1843 * latDegminsec[1] + 30.715 * latDegminsec[2];
//	var stdDevLonMet = 111300 * lonDegminsec[0] + 1855 * lonDegminsec[1] + 19.22  * lonDegminsec[2];

//	Longitudinal length equivalents at selected latitudes
//	Latitude 		Town 				Degree 		Minute 		Second 		±0.0001¡
//	60¡ 			Saint Petersburg 	55.65 km 	0.927 km 	15.42 m 	5.56 m
//	51¡ 28' 38" N 	Greenwich 			69.29 km 	1.155 km 	19.24 m 	6.93 m
//	45¡ 			Bordeaux 			78.7 km 	1.31 km 	21.86 m 	7.87 m
//	30¡ 			New Orleans 		96.39 km 	1.61 km 	26.77 m 	9.63 m
//	0¡ 				Quito 				111.3 km 	1.855 km 	30.92 m 	11.13 m

	// assuming we all live in bordeaux, hence 8m
	return Math.PI * stdDevLat * stdDevLon * 6400;
}

function dec2degminsec( value )
{
	var degminsec = [];
	var signVal = (value < 0 ? -1 : 1);
	var deg = Math.floor(value / 1000000) * signVal;
	var min = Math.floor( ((value/1000000) - Math.floor(value/1000000)) * 60 );
	var sec = Math.floor(((((value/1000000) - Math.floor(value/1000000)) * 60) - Math.floor(((value/1000000) - Math.floor(value/1000000)) * 60)) * 100000) *60/100000;

	// deg
	degminsec.push( deg );
	// min
	degminsec.push( min );
	// sec
	degminsec.push( sec );
	
	return degminsec;
}

/**
 *  called when user clicks on a marker
 *  @param cluster is defined in closure "addMarker" by addListener function
 */
function loadContent( cluster )
{
	var lat = Math.round(cluster.meanLat*1000)/1000;
	var lon = Math.round(cluster.meanLon*1000)/1000;

	var slat = Math.round(cluster.stdDevLat*1000)/1000;
	var slon = Math.round(cluster.stdDevLon*1000)/1000;

	var area = Math.round(clusterArea( slat, slon ) * 1000)/1000;
	var radius = 80 * (slat + slon) / 2;
	
	var content = '<h3>Cluster Meta</h3>';
	
	content += '<ul class="cluster_meta"><li>geo: &mu; ['+lat+', '+lon+'] - &sigma; ['+slat+', '+slon+']</li>';
	content += '<li>radius: '+ radius +' km, surface: '+ area +' km<sup>2</sup></li></ul>';
	content += '<h3 class="cluster_topics">Topics</h3>';

	$('#post_content').empty().fadeOut('fast');
	$('#content').empty().append( content ).fadeIn('fast');
	
	$.ajax({
		url: siteUrl + '/cluster/read_semantic/'+cluster.id_cluster,
		dataType: 'json',
		success: function(data) {
			var scoreArray = Array();
			var keysArray = Array();
			$.each(data, function(i, semCluster) {
				var score = Math.round(semCluster.score*1000)/1000;
				var terms = semCluster.terms_meta;
				terms = '[lang: '+semCluster.language+']'+terms.replace(/[^a-zA-Z 0-9]+/g, ' ');
				scoreArray[score] = terms;
				keysArray.push( score );
			});
			var semClusterList = $('<ul class="cluster_topics"></ul>');

			keysArray.sort(function(a,b) {return b-a;});
			for( var i = 0; i < keysArray.length; i++ ) {
				var key = keysArray[i];
				semClusterList.append('<li>[score: '+key+']'+scoreArray[key]+'</li>');
			}
			$('h3.cluster_topics').after( semClusterList );
		}
	});

	$.ajax({
		url: siteUrl + '/cluster/read_keywords/'+cluster.id_cluster,
		dataType: 'json',
		success: function(data) {
			var semClusterList = $('<ul class="cluster_topics"></ul>');
			$.each(data, function(i, semCluster) {
				var terms = semCluster.terms_meta;
				terms = terms.replace(/[^a-zA-Z 0-9]+/g, ' ');
				semClusterList.append('<li>keywords: '+terms+'</li>');
			});
			$('h3.cluster_topics').after( semClusterList );
		}
	});

	var postList = '';
	var postArray = (cluster.posts_meta).split( ' ' );
	for( i in postArray ) {
		postList += '<a href="'+siteUrl+'/cluster/read_post/'+postArray[i]+'">'+i+'</a> ';
	}
	$('#content').append('<h3 class="cluster_posts">Post list [total: '+postArray.length+']:</h3><p>'+postList+'</p>');
}

/**
 * function is called when user clicks on a link in div #content
 * @param postUrl is defined in function loadContent
 */
function loadClusterContent( postUrl )
{
	$('#post_content').empty().fadeIn('fast');
	$.ajax({
		url: postUrl,
		dataType: 'json',
		success: function(data) {
			
			var content = '<h3>Post Content</h3><p>'+data.content+'</p>';
			content += '<h3>Meta</h3><ul class="post_meta"><li>coordinates: ['+data.lat+', '+data.lon+']</li>';
			if(data.coordinates_estimated == 1) {
				content += '<li>user location: '+data.user_location+'</li>';
			}
			content += '</ul>';
			
			if( data.links ) {
				var linkList = '';
				var linkArray = ($.trim(data.links)).split( ' ' );
				for( i in linkArray ) {
					linkList += '<a href="'+siteUrl+'/cluster/read_link/'+linkArray[i]+'">'+i+'</a> ';
				}
				content += '<h3>Links</h3><p>'+linkList+'</p><p class="link_content"></p>';
			}
			$('#post_content').append( content );
		}
	});
}

/**
 * function is called when user clicks on a link in div #post_content
 * @param linkUrl is defined in function loadClusterContent
 */
function loadLinkContent( linkUrl )
{
	$.ajax({
		url: linkUrl,
		dataType: 'json',
		success: function(data) {
			$('p.link_content').empty().append('<a href="'+data.uri+'">[url]</a> '+data.text);
		}
	});
}

function setBounds()
{
	if( markersArray ) {
		var SWlat = 0, SWlon = 0, NElat = 0, NElon = 0;
		for(i in markersArray) {
			var mlat = markersArray[i].position.lat();
			var mlon = markersArray[i].position.lon();
			if( mlat < SWlat ) {
				SWlat = mlat;
			} else if( mlat > NElat ) {
				NElat = mlat;
			}
			if( mlon < SWlon ) {
				SWlon = mlon;
			} else if( mlon > NElon ) {
				NElon = mlon;
			}
		}
	}
	var SW = new google.maps.LatLng( SWlat, SWlon );
	var NE = new google.maps.LatLng( NElat, NElon );

	var bounds = new google.maps.LatLngBounds(SW, NE);
	map.fitBounds(bounds);
}

//Removes the overlays from the map, but keeps them in the array
function clearOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
	}
	if (circlesArray) {
		for (i in circlesArray) {
			circlesArray[i].setMap(null);
		}
	}
}

//Shows any overlays currently in the array
function showOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(map);
		}
	}
	if (circlesArray) {
		for (i in circlesArray) {
			circlesArray[i].setMap(map);
		}
	}
}

//Deletes all markers in the array by removing references to them
function deleteOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
		markersArray.length = 0;
	}
	if (circlesArray) {
		for (i in circlesArray) {
			circlesArray[i].setMap(null);
		}
		circlesArray.length = 0;
	}
}


//Deletes all markers in the array by removing references to them
function deleteCircles() {
	if (circlesArray) {
		for (i in circlesArray) {
			circlesArray[i].setMap(null);
		}
		circlesArray.length = 0;
	}
}