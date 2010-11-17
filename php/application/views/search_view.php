<?php $this->load->view('includes/header'); ?>
<?php $this->load->view('includes/search_form'); ?>

<div id="trends">
	<h1>Trends</h1>
	<ul>
<?php
foreach( $trends as $trend ) {
	echo '<li><a href="'.$trend->url.'">'.$trend->name.'</a></li>';
}
?>
	</ul>
</div>

<?php $this->load->view('includes/footer'); ?>