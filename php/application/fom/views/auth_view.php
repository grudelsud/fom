<?php $this->load->view('includes/header'); ?>

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

<?php $this->load->view('includes/footer'); ?>