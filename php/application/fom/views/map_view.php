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
	<style type="text/css"> @import "<?php echo assets_url('assets/css') ?>/TableTools.css"; </style>
	<link type="text/css" href="<?php echo assets_url('assets/css') ?>/style.css" rel="stylesheet" media="screen" />
  
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery-ui-1.8.13.custom.min.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/jquery.dataTables.min.js"></script>

	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/ZeroClipboard.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/TableTools.js"></script>

	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> 
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/markermanager_packed.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/keydragzoom_packed.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>

	<script type="text/javascript">
		var siteUrl = '<?php echo site_url(); ?>';
		var assetsUrl = '<?php echo assets_url('assets') ?>';
		var clusterUrl = '<?php echo site_url('cluster/read/'.$query_sel); ?>';
	</script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/fom_mapcontrols.js"></script>
	<script type="text/javascript" src="<?php echo assets_url('assets/lib') ?>/fom_viewcontrols.js"></script>
</head>
<body>
	<div id="header">
		<div id="logo"><a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo_small.png" alt="Flux of MEME" style="float:left;"/></a></div><!-- end of #logo -->
		<div id="menu" style="display: none;"><ul><li><a href="<?php echo site_url('auth/logout'); ?>">logout</a></li></ul></div><!-- end of #menu -->
		<div id="navigation">
		<span id="search"><img src="<?php echo assets_url('assets/img') ?>/google_custom_search.png" alt="search topic" /></span>
		<?php 
		if( isset($query_array) ) {
			echo form_label('or select date: ', 'queries'). form_dropdown('queries', $query_array, $query_sel, 'id="queries"'); 
		}?>
		<span id="disp_stats"><img src="<?php echo assets_url('assets/img') ?>/data_grid.png" alt="display query stats" onClick="showQueryStats()" /></span>
		<span id="showhide_panels"><img src="<?php echo assets_url('assets/img') ?>/cog.png" alt="toggle panels" /></span>
		<span id="ajax_loader" style="display:none"><img src="<?php echo assets_url('assets/img') ?>/ajax-loader.gif" alt="loading..." /></span>
		</div><!-- end of #navigation -->
		<div id="query_meta" style="display:none;"></div>
		<div id="subnav"><span id="about"><img src="<?php echo assets_url('assets/img') ?>/information.png" alt="search topic" /></span></div>
	</div><!-- end of #header -->

	<div id="map_canvas"></div>

	<div id="content" style="display: none;"></div>
	<div id="post_content" style="display: none;"></div>

	<div id="about_content" title="About Flux of MEME">
		<p>Yet another data mining tool - geo-clustering and topic extraction. Uses HAC + LDA, developed in Java and PHP with an unbelievable number of libraries.</p>
		<p>Concept, direction and development:<br/><a href="http://tom.londondroids.com">TMA</a></p>
		<p>Research and development support:<br/>Giuseppe Serra &amp; Federico Frappi</p>
		<p>With the precious sponsorship of:<br/>Telecom Italia - Working Capital</p>
		<p>Special thanks:<br/>Marco Bertini, MICC - UniFi</p>
	</div>
	<div id="search_content" title="Flux of MEME - Search topic">
		<form name="topic_search_form" id="topic_search_form" method="post" action="">
		<span id="search_ajax_loader"><img src="<?php echo assets_url('assets/img') ?>/ajax-loader.gif" alt="loading..." /></span>
		<input type="text" name="t_query" id="t_query">
		<label><input class="radio" type="radio" name="t_class" value="all" id="t_class_1" checked> all</label>
		<label><input class="radio" type="radio" name="t_class" value="topics" id="t_class_2"> topics</label>
		<label><input class="radio" type="radio" name="t_class" value="keywords" id="t_class_3"> keywords</label>
		</form>
		<div id="search_result"></div>
	</div>

	<div id="legend_content" title="Colour legend">
	</div>

	<div id="footer"></div>
</body> 
</html> 