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

	<style type="text/css"> @import "<?php echo assets_url('assets/css') ?>/dark-hive/jquery-ui-1.8.13.custom.css"; </style>
	<style type="text/css"> @import "<?php echo assets_url('assets/css') ?>/datatable.css"; </style>
	<link type="text/css" href="<?php echo assets_url('assets/css') ?>/style.css" rel="stylesheet" media="screen" />
  
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-ui-1.8.13.custom.min.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery.dataTables.min.js"></script>

	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> 
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/markermanager_packed.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/keydragzoom_packed.js"></script>

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/fom_mapcontrols.js"></script>

	<script type="text/javascript">
	var initialLocation = new google.maps.LatLng(45, 10);
	var siteUrl = '<?php echo site_url(); ?>';
	var assetsUrl = '<?php echo assets_url('assets') ?>';
	var clusterUrl = '<?php echo site_url('cluster/read/'.$query_sel); ?>';
	
	$(document).ready(function() {
		initialize( initialLocation );
		loadMarkers( clusterUrl );
		$('#logo').hover(function(e) { $('#menu').fadeIn('fast'); } );
		$('#menu').hover(function(e) {}, function(e) {$('#menu').fadeOut('fast');});
	});
	</script>
</head>
<body>
	<div id="header">
		<div id="logo"><a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo_small.png" alt="Flux of MEME" style="float:left;"/></a></div><!-- end of #logo -->
		<div id="menu" style="display: none;"><ul><li><a href="<?php echo site_url('auth/logout'); ?>">logout</a></li></ul></div><!-- end of #menu -->
		<div id="navigation">
		<?php 
		if( isset($query_array) ) {
			echo form_label('Select date: ', 'queries'). form_dropdown('queries', $query_array, $query_sel, 'id="queries"'); 
		}?>
		<span id="disp_stats"><img src="<?php echo assets_url('assets/img') ?>/data_grid.png" alt="display query stats" onClick="showQueryStats()" /></span>
		<span id="showhide_panels"><img src="<?php echo assets_url('assets/img') ?>/cog.png" alt="toggle panels" /></span>
		<span id="ajax_loader"></span>
		</div><!-- end of #navigation -->
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
	$('#showhide_panels').click(function(e) { $('#post_content').toggle(); $('#content').toggle(); });
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