<?php 
	$query_sel = $queries[0]->id_query;
	foreach ( $queries as $query ) {
		if( 'range' == $query->t_granularity ) {
			$label = date('D j M, Y', strtotime( $query->t_start )).' - '.date('D j M, Y', strtotime( $query->t_end ));
		} else {
			$label = date('D j M, Y', strtotime( $query->t_start ));
		}
		$query_array[ $query->t_granularity ][ $query->id_query ] = $label;
	}
?>
<!DOCTYPE html>
<html>

<head>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<title>Flux of MEME</title>
  
	<link type="text/css" href="<?php echo assets_url('assets/css') ?>/map_style.css" rel="stylesheet" media="screen" />
	<style type="text/css"> @import "<?php echo assets_url('assets/lib') ?>/jquery.datepick/redmond.datepick.css"; </style>

	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> 

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-1.5.min.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/markermanager_packed.js"></script>

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/fom_mapcontrols.js"></script>
	<script type="text/javascript">
	var initialLocation = new google.maps.LatLng(45, 10);
	var siteUrl = '<?php echo site_url(); ?>';
	var clusterUrl = '<?php echo site_url('cluster/read/'.$query_sel); ?>';
	
	$(document).ready(function() {
		initialize( initialLocation );
		loadMarkers( clusterUrl );
	});
	</script>
</head>
<body>
	<div id="header">
		<a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo_small.png" alt="Flux of MEME"/></a>
		<div id="navigation">
		<?php 
		if( isset($query_array) ) {
			echo form_label('Select date ', 'queries'). form_dropdown('queries', $query_array, $query_sel, 'id="queries"'); 
		}?>
		</div>
	</div><!-- end of #header -->
	<div id="map_canvas"></div>

	<div id="content" style="display: none;"></div>
	<div id="post_content" style="display: none;"></div>

	<div id="footer"></div>

<script type="text/javascript" >
$(function() {
	$('#queries').change(function() {
		var clusterUrl = '<?php echo site_url('cluster/read'); ?>/' + $(this).val();
		$('#content').empty().fadeOut('fast');
		$('#post_content').empty().fadeOut('fast');
		deleteOverlays();
		loadMarkers( clusterUrl );
	});
});

$(function() {
	$('#content a').live('click', function(event) {
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
</script>
</body> 
</html> 