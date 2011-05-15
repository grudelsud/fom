<?php 

	if( is_object( $queries ) ) {
		$query_sel = $queries->id_query;

		$label = date('D j M, Y', strtotime( $queries->t_start )).' - '.date('D j M, Y', strtotime( $queries->t_end ));
		$query_array[ $queries->t_granularity ][ $queries->id_query ] = $label;
		
	} else {
		$query_sel = $queries[0]->id_query;

		foreach ( $queries as $query ) {
			$query_meta = json_decode( $query->meta );
			if( 'range' == $query->t_granularity ) {
				$label = date('D j M, Y', strtotime( $query->t_start )).' - '.date('D j M, Y', strtotime( $query->t_end ));
			} else if( 'hour' == $query->t_granularity ) {
				$label = date('D j M, Y \hH', strtotime( $query->t_start ));
			} else {
				$label = date('D j M, Y', strtotime( $query->t_start )).' [F:'.$query_meta->minFollCount.']';
			}
			$query_array[ $query->t_granularity ][ $query->id_query ] = $label;
		}
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
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/keydragzoom_packed.js"></script>

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/fom_mapcontrols.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/protovis-r3.2.js"></script>
	<script type="text/javascript">
	var initialLocation = new google.maps.LatLng(45, 10);
	var siteUrl = '<?php echo site_url(); ?>';
	var assetsUrl = '<?php echo assets_url('assets') ?>';
	var clusterUrl = '<?php echo site_url('cluster/read/'.$query_sel); ?>';
	
	$(document).ready(function() {
		initialize( initialLocation );
		loadMarkers( clusterUrl );
	});
	</script>
</head>
<body>
	<div id="header">
		<a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo_small.png" alt="Flux of MEME" style="float:left;"/></a>
		<div id="navigation">
		<?php 
		if( isset($query_array) ) {
			echo form_label('Select date ', 'queries'). form_dropdown('queries', $query_array, $query_sel, 'id="queries"'); 
		}?><span id="disp_stats"><img src="<?php echo assets_url('assets/img') ?>/data_grid.png" alt="display query stats" onClick="showQueryStats()" /></span><span id="ajax_loader"></span>
		</div>
		<div id="query_meta" style="display:none;"></div>
	</div><!-- end of #header -->
	<div id="map_canvas"></div>

	<div id="content" style="display: none;"></div>
	<div id="post_content" style="display: none;"></div>

	<!-- <div id="stat" style="position: absolute; width: 400px; height: 400px; background: #996; z-index: 200;"><script type="text/javascript+protovis">showStats();</script></div> -->

	<div id="footer"></div>

<script type="text/javascript" >
$(function() {
	$('#queries').change(function() {
		var queryId = $(this).val();
		var clusterUrl = '<?php echo site_url('cluster/read'); ?>/' + queryId;
		$('#content').empty().fadeOut('fast');
		$('#post_content').empty().fadeOut('fast');
		$('#query_meta').empty().fadeOut('fast');
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