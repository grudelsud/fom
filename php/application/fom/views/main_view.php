<?php $this->load->view('includes/header'); ?>

<div id="query">
<h1>Queries</h1>
<table>
	<tr><th>action</th><th>terms</th><th>date</th></tr>
<?php
foreach ($queries as $query) {
	$query_values = json_decode($query->query);
	echo '<tr id="q'.$query->id_query.'">'.
		'<td>'.
		'<a class="view" href="'.site_url('cluster/read/'.$query->id_query.'/json').'"><img src="'.assets_url('assets/img').'/add.png" alt="+" /></a>'.
		'<a href="'.site_url('query/delete/'.$query->id_query).'"><img src="'.assets_url('assets/img').'/delete.png" alt="-" /></a>'.
		'</td>'.
		'<td>'.$query_values->terms.'</td>'.
		'<td>'.$query->created.'</td>'.
		'</tr>';
}
?>
</table>
</div>

<div id="cluster">
	<h1>Clusters</h1>
	<div id="result">
	</div>
</div>
<script type="text/javascript">
	$('#query a.view').click(function(e) {
		e.preventDefault();
		$.ajax({
			url: $(this).attr('href'),
			dataType: 'json',
			type: 'GET',
			success: function(data) {
				$('#result').empty();
				$.each(data, function(i, item){
					var divTag = $('<div></div>');
					var ulTag = $('<ul></ul>');
					$.each(item, function(key, value){
						if( key == 'id_cluster' ) {
							divTag.attr('id', 'c'+value);
							divTag.append('<h2><a class="delete" href="<?php echo site_url('cluster/delete'); ?>/'+value+'"><img src="<?php echo assets_url('assets/img') ?>/delete.png" alt="-" /></a> Cluster</h2>');
						} else {
							ulTag.append('<li>['+key+'='+value+']</li>');
						}
					});
					divTag.append(ulTag);
					$('#result').append(divTag);
				});
			}
		});
	});
	$('#cluster a.delete').live('click', function(e) {
		e.preventDefault();
		$.ajax({
			url: $(this).attr('href'),
			dataType: 'json',
			type: 'GET',
			success: function(data) {
				$.each(data, function(i, item){
					$('#c'+item).empty();
				});
			}
		});
	});
</script>

<?php $this->load->view('includes/footer'); ?>