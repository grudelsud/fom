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
		$('#footer').empty().append( event.latLng.toString() );
	});
}

function showStats()
{
	var queryId = $('#queries').val();
	$('#query_meta').empty().append( queryMetaArray[queryId] );
	$.ajax({
		url: siteUrl + '/cluster/stat/'+queryId+'/jpg',
		dataType: 'text',
		success: function(data) {
			$('#query_meta').append( '<img src="'+data+'" alt="query stats" />').toggle('fast');
		}
	});

//	new pv.Panel()
//	.width(150)
//	.height(150)
//	.anchor("center").add(pv.Label)
//	.text("Hello, world!")
//	.root.render();
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

	var content = '<h3>Cluster Meta</h3>';
	
	content += '<ul class="cluster_meta"><li>geo: &mu; ['+lat+', '+lon+'] - &sigma; ['+slat+', '+slon+']</li>';
	content += '<li>surface: '+ clusterArea( slat, slon ) +' km<sup>2</sup></li></ul>';
	content += '<h3 class="cluster_topics">Topics</h3>';

	$('#post_content').empty().fadeOut('fast');
	$('#content').empty().append( content ).fadeIn('fast');
	
	$.ajax({
		url: siteUrl + '/cluster/read_semantic/'+cluster.id_cluster,
		dataType: 'json',
		success: function(data) {
			var semClusterList = $('<ul class="cluster_topics"></ul>');
			$.each(data, function(i, semCluster) {
				var terms = semCluster.terms_meta;
				var score = Math.round(semCluster.score*1000)/1000;
				terms = terms.replace(/[^a-zA-Z 0-9]+/g, ' ');
				semClusterList.append('<li>[score: '+score+']'+terms+'</li>');
			});
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
	$('#content').append('<h3 class="cluster_posts">Post list:</h3><p>'+postList+'</p>');
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