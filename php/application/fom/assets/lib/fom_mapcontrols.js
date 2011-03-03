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
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
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
	
	circle = new google.maps.Circle({
		center: location,
		clickable: false,
		fillColor: "#FF0000",
		fillOpacity: clusterOpacity( cluster.posts_meta ),
		map: map,
		radius: clusterArea(cluster.stdDevLat, cluster.stdDevLon),
		strokeColor: "#FFFFFF",
		strokeWeight: 2,
		zIndex: 1
	});
	
	google.maps.event.addListener(marker, 'click', function(event) {
		loadContent(cluster);
	});
	markersArray.push(marker);
	circlesArray.push(circle);
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
		
	return Math.PI * stdDevLat * stdDevLon * 30000;
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

function loadContent( cluster )
{
	var content;

	content  = '<ul class="cluster_meta"><li>coordinates: ['+cluster.meanLat+', '+cluster.meanLon+']</li>';
	content += '<li>surface: '+ clusterArea( cluster.stdDevLat, cluster.stdDevLon ) +'</li></ul>';

	$('#post_content').empty().fadeOut('fast');
	$('#content').empty().append( content ).fadeIn('fast');
	$.ajax({
		url: siteUrl + '/cluster/read_semantic/'+cluster.id_cluster,
		dataType: 'json',
		success: function(data) {
			var semClusterList = $('<ul class="cluster_semantic"></ul>');
			$.each(data, function(i, semCluster) {
				var terms = semCluster.terms_meta;
				terms = terms.replace(/[^a-zA-Z 0-9]+/g, ' ');
				semClusterList.append('<li>'+terms+'</li>');
			});
			$('#content').append( semClusterList );
		}
	});

	var postList = '';
	var postArray = (cluster.posts_meta).split( ' ' );
	for( i in postArray ) {
		postList += '<a href="'+siteUrl+'/cluster/read_post/'+postArray[i]+'">'+i+'</a> ';
	}
	$('#content').append('<ul class="cluster_posts"><li>post: '+postList+'</li></ul>');
}

function loadClusterContent( postUrl )
{
	$('#post_content').empty().fadeIn('fast');
	$.ajax({
		url: postUrl,
		dataType: 'json',
		success: function(data) {
			var content = $('<ul class="cluster_content"></ul>');
			content.append('<li>coordinates: ['+data.lat+', '+data.lon+']</li>');
			content.append('<li>content: '+data.content+'</li>');
			
			if( data.links ) {
				var linkList = '';
				var linkArray = ($.trim(data.links)).split( ' ' );
				for( i in linkArray ) {
					linkList += '<a href="'+siteUrl+'/cluster/read_link/'+linkArray[i]+'">'+i+'</a> ';
				}
				content.append('<li>links: '+linkList+'</li>');
			}
			$('#post_content').append( content );
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