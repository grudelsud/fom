<!DOCTYPE html>
<html>

<head>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<title>Flux of MEME</title>

	<link type="text/css" href="<?php echo assets_url('assets/css') ?>/style.css" rel="stylesheet" media="screen" />
  
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> 
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-1.5.1.min.js"></script>
	<script type="text/javascript">
	var initialLocation = new google.maps.LatLng(45, 10);

	$(document).ready(function() {	
		var myOptions = {
				zoom: 6,
				center: initialLocation,
				mapTypeId: google.maps.MapTypeId.HYBRID
			};
		var map_style = [
			{
				featureType: "administrative",
				stylers: [
					{ saturation: 51 },
					{ lightness: 15 },
					{ gamma: 0.95 },
					{ visibility: "on" }
				]
			},{
				featureType: "road",
				stylers: [
					{ visibility: "off" }
				]
			},{
				featureType: "poi",
				stylers: [
					{ visibility: "off" }
				]
			},{
				featureType: "landscape",
				stylers: [
					{ hue: "#ff0000" }
				]
			},{
				stylers: [
					{ hue: "#ff0000" },
					{ saturation: -61 },
					{ gamma: 0.4 }
				]
			}
		];

		map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

		map.mapTypes.set('map_style', new google.maps.StyledMapType(map_style));
		map.setMapTypeId('map_style');
	});
	</script>
</head>

<body>
<div id="header">
	<div id="logo"><a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo.png" alt="Flux of MEME"/></a></div><!-- end of #logo -->
</div><!-- end of #header -->

<div id="map_canvas"></div>
<div id="content_auth">
<?php
$attributes = array( 'id' => 'form_auth' );

$form_field['user_login'] = array('name' => 'user_login', 'id' => 'user_login');
$form_field['user_pass'] = array('name' => 'user_pass', 'id' => 'user_pass');
$form_field['submit'] = array('name' => 'submit', 'id' => 'submit');

echo form_open('auth/login', $attributes);

echo form_label('Login:', 'user_login');
echo form_input( $form_field['user_login'] );
echo "<br />";

echo form_label('Password:', 'user_login');
echo form_password( $form_field['user_pass'] );
echo "<br />";

echo form_submit($form_field['submit'], 'Login!');

echo form_close();
?>
</div><!-- end of #content_auth -->
<div id="footer"></div>

</body>
</html>