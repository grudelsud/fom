<!DOCTYPE html>
<html>

<head>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<title>Flux of MEME</title>
  
	<link type="text/css" href="<?php echo assets_url('assets/css') ?>/map_style.css" rel="stylesheet" media="screen" />
	<style type="text/css"> @import "<?php echo assets_url('assets/lib') ?>/jquery.datepick/redmond.datepick.css"; </style>

	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> 

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery.datepick/jquery.datepick.js"></script>

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/fom_mapcontrols.js"></script>
	<script type="text/javascript"> 
	var initialLocation = new google.maps.LatLng(45.07339937951305, 7.674121856689453);
	var siteUrl = '<?php echo site_url(); ?>';
	var clusterUrl = '<?php echo site_url('cluster/read/1/json'); ?>';

	navigator.geolocation.getCurrentPosition(
		function(position) {
			initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
		}, function() {}
	);

	$(document).ready(function() {
		initialize( initialLocation );
		loadMarkers( clusterUrl );
	});
	</script> 
</head> 
<body>
	<div id="header">
		<a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo_small.png" alt="Flux of MEME"/></a>
		<!-- <input id="date" name="date" type="text"> -->
	</div><!-- end of #header -->
	<div id="map_canvas"></div>
	<div id="content"></div>
	<div id="footer"></div>

<script type="text/javascript">
$(function() {
	$('#date').datepick({dateFormat: 'yyyy-mm-dd'});
});
</script>
</body> 
</html> 