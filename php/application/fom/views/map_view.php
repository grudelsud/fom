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
		<div id="logo"><a href="<?php echo base_url(); ?>"><img src="<?php echo assets_url('assets/img') ?>/logo.png" alt="Flux of MEME"/></a></div><!-- end of #logo -->
		<div id="search">
			<label for="t_query">Search: </label>
			<input type="text" name="t_query" id="t_query">
			<?php 
				$t_class_array = array('all' => 'All', 'topics' => 'Topics', 'keywords' => 'Keywords');
				echo form_label('Context: ', 't_class'). form_dropdown('t_class', $t_class_array, 'all', 'id="t_class"');
			?>
			<div id="ajax_loader_search">&nbsp;</div>
		</div><!-- end of #search -->
		<div id="search_result"></div>

		<div id="functions">
			<span id="showhide_panels"><img src="<?php echo assets_url('assets/img') ?>/layers_1_icon.png" alt="toggle panels" /></span>
			<span id="help"><img src="<?php echo assets_url('assets/img') ?>/lightbulb_icon.png" alt="help" /></span>
			<span id="logout"><a href="<?php echo site_url('auth/logout'); ?>" title="logout"><img src="<?php echo assets_url('assets/img') ?>/on-off_icon.png" alt="logout" /></a></span>
		</div><!-- end of #functions -->

	</div><!-- end of #header -->

	<div id="map_canvas"></div>
	<div id="content" style="display: none;"></div>
	<div id="post_content" style="display: none;"></div>

	<div id="footer">
		<div id="query_select">
		<?php 
		if( isset($query_array) ) {
			echo form_label('Showing clusters for: ', 'queries'). form_dropdown('queries', $query_array, $query_sel, 'id="queries"'); 
		}?>
		<span id="show_query_stats">Show query stats</span>
		<div id="ajax_loader_stats">&nbsp;</div>
		</div>

		<div id="geo_coords"></div>
		<div id="about"><img src="<?php echo assets_url('assets/img') ?>/info_icon.png" alt="about" /></div>
	</div>

	<!-- modal windows -->
	<div id="query_meta" title="Query stats"></div>
	<div id="legend_content" title="Search results"></div>
	<div id="help_content" title="Quick help">
		<h3>Icons</h3>
		<ul>
			<li><img src="<?php echo assets_url('assets/img') ?>/layers_1_icon.png" alt="toggle panels" /> Show / hide content layers (read below for a description of the content)</li>
			<li><img src="<?php echo assets_url('assets/img') ?>/lightbulb_icon.png" alt="help" /> Open this help dialog</li>
			<li><img src="<?php echo assets_url('assets/img') ?>/on-off_icon.png" alt="logout" /> Logout</li>
			<li><img src="<?php echo assets_url('assets/img') ?>/chart_line_icon.png" alt="stats" /> Show stats for the current set of clusters</li>
			<li><img src="<?php echo assets_url('assets/img') ?>/info_icon.png" alt="about" /> Credits</li>
		</ul>
		<h3>Topic / Keyword search</h3>
		<ul>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/02_search.png" width="480" height="301"> Type some text to execute a live search, and select the context of the search (can be one of: all, topics, keywords).</li>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/03_search_result.png" width="480" height="301"> Groups or clusters are shown in a colour gradient from green (most recent) to purple (oldest), a predominant colour will indicate a concentration of topics around a specific period. The scatter chart reports the total number of clusters and average size of clusters in the specified date.</li>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/04_search_result_hide.png" width="480" height="301"> Click on a day to hide its clusters from the map, click on it again to have them back.</li>
		</ul>
		<h3>Location box</h3>
		<ul>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/05_dragzoom.png" width="480" height="301"> Press shift then drag to select a location box and display relative stats.</li>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/06_dragzoom_select.png" width="480" height="301"> Select a time interval, then press submit.</li>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/07_dragzoom_stat.png" width="480" height="301"> Stats for a location box within the specified period include: average number and size of clusters, distribution of languages (searchable, and downloadable in CSV format).</li>
		</ul>
		<h3>Cluster content</h3>
		<ul>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/08_cluster_select.png" width="480" height="301"> Click on one of the little lightnings on the map to display its content. The main stat panel shows: centroid coordinates, radius and surface (based on standard deviation), topics with LDA score and single terms with TF, keywords with TF, number of posts (full list downloadable in CSV format).</li>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/09_post_content.png" width="480" height="301"> Select one post of the list to display its content. If a post contains any related content, it will be accessible through a link.</li>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/10_link_content.png" width="480" height="301"> Click on a link to show its content, right click to the URL and open a new tab in the browser to show the related source.</li>
		</ul>
		<h3>Query stat</h3>
		<ul>
			<li><img src="<?php echo assets_url('assets/img/help') ?>/11_query_stats.png" width="480" height="301"> Click on the query stats icon to show a panel containing the following stats: list of topics with highest TF-IDF, query-specific settings.</li>
		</ul>
	</div>
	<div id="about_content" title="About Flux of MEME">
		<p>Yet another data mining tool - geo-clustering and topic extraction. Uses HAC + LDA, developed in Java and PHP with an unbelievable number of libraries.</p>
		<p>Concept, direction and development:<br/><a href="http://tom.londondroids.com">TMA</a></p>
		<p>Research and development support:<br/>Giuseppe Serra &amp; Federico Frappi</p>
		<p>With the precious sponsorship of:<br/>Telecom Italia - Working Capital</p>
		<p>Special thanks:<br/>Marco Bertini, MICC - UniFi</p>
		<p>Uh, almost forgot: I must have used an attribution-non-commercial icon set downloaded from somewhere, but can't remember the origin, so feel free to contact me and let me know so I can amend this line</p>
	</div>
</body> 
</html>