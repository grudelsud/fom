<?php $this->load->view('includes/header'); ?>

<?php
$attributes = array( 'id' => 'form_xmlrpctester' );

$form_field['function'] = array('query' => 'query');
$form_field['server'] = array('name' => 'server', 'id' => 'server', 'value' => 'http://vampireweekend.local/fom/index.php/xmlrpc');

$form_field['param1'] = array('name' => 'param1', 'id' => 'param1');
$form_field['param2'] = array('name' => 'param2', 'id' => 'param2');
$form_field['param3'] = array('name' => 'param3', 'id' => 'param3');
$form_field['param4'] = array('name' => 'param4', 'id' => 'param4');
$form_field['param5'] = array('name' => 'param5', 'id' => 'param5');
$form_field['param6'] = array('name' => 'param6', 'id' => 'param6');

echo form_open('xmlrpc_client/post_params', $attributes);

?><div id="result"><p>Result</p>
<?php
echo '<textarea name="result" id="result" cols="80" rows="15">'.$result.'</textarea>';
?>
</div><?php

echo form_label('Function', 'function');
echo form_dropdown('function', $form_field['function'], 'query');
echo "<br />";

echo form_label('Server', 'server');
echo form_input( $form_field['server'] );
echo "<br />";

echo form_label('Param 1', 'param1');
echo form_input( $form_field['param1'] );
echo "<br />";

echo form_label('Param 2', 'param2');
echo form_input( $form_field['param2'] );
echo "<br />";

echo form_label('Param 3', 'param3');
echo form_input( $form_field['param3'] );
echo "<br />";

echo form_label('Param 4', 'param4');
echo form_input( $form_field['param4'] );
echo "<br />";

echo form_label('Param 5', 'param5');
echo form_input( $form_field['param5'] );
echo "<br />";

echo form_label('Param 6', 'param6');
echo form_input( $form_field['param6'] );
echo "<br />";

echo form_submit('submit', 'Call function!');

echo form_close();
?>

<?php $this->load->view('includes/footer'); ?>