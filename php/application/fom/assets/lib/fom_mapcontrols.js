/**
 * 
 */
var map;
var markersArray = [];

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
		position: location,
		map: map
	});

	google.maps.event.addListener(marker, 'click', function(event) {
		loadContent(cluster);
	});
	markersArray.push(marker);
}

function loadContent( cluster )
{
	var content;

	content  = '<ul class="cluster_meta"><li>coordinates: ['+cluster.meanLat+', '+cluster.meanLon+']</li>';
	content += '<li>surface: '+ Math.PI * cluster.stdDevLat * cluster.stdDevLon +'</li></ul>';

	$('#post_content').empty().fadeOut('fast');
	$('#content').empty().append( content ).fadeIn('fast');
	$.ajax({
		url: siteUrl + '/cluster/read_semantic/'+cluster.id_cluster+'/json',
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
		for(marker in markersArray) {
			var mlat = marker.position.lat();
			var mlon = marker.position.lon();
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
}

//Shows any overlays currently in the array
function showOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(map);
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
}