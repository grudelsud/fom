<?php $this->load->view('includes/header'); ?>

<?php
$attributes = array( 'id' => 'form_xmlrpctester' );

$form_field['function'] = array(
	'cluster' => array('cluster_store'=>'store'),
	'query' => array('query_store'=>'store', 'query_exec'=>'exec'),
	'post' => array('post_create'=>'create','post_read'=>'read','post_delete'=>'delete'), 
	'log' => array('log'=>'log')
);
$form_field['server'] = array('name' => 'server', 'id' => 'server', 'value' => site_url('xmlrpc'));

if( !isset( $max_params ) || !is_numeric( $max_params ) ) {
	$max_params = 12;
}

for( $i = 1; $i < $max_params + 1; $i++ ) {
	$form_field['param'.$i] = array('name' => 'param'.$i, 'id' => 'param'.$i);
}

$hidden = array('max_params' => $max_params);

echo form_open('xmlrpc_client/post_params', $attributes, $hidden);

?><div id="result"><p>Result</p>
<?php
echo '<textarea name="result" id="result">'.$result.'</textarea>';
?>
</div><?php

echo form_label('Function', 'function');
echo form_dropdown('function', $form_field['function'], 'query');
echo "<br />";

echo form_label('Server', 'server');
echo form_input( $form_field['server'] );
echo "<br />";

for( $i = 1; $i < $max_params + 1; $i++ ) {
	echo form_label('Param '.$i, 'param'.$i);
	echo form_input( $form_field['param'.$i] );
	echo "<br />";
}

echo form_submit('submit', 'Call function!');

echo form_close();
?>

<?php $this->load->view('includes/footer'); ?>