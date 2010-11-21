<?php
$attributes = array( 'id' => 'form_query' );

$form_field['terms']       = array('name' => 'terms', 'id' => 'terms');
$form_field['since']       = array('name' => 'since', 'id' => 'since');
$form_field['until']       = array('name' => 'until', 'id' => 'until');
$form_field['where']       = array('name' => 'where', 'id' => 'where');
$form_field['granularity'] = array('poi' => 'poi', 'neighborhood' => 'neighborhood', 'city' => 'city', 'admin' => 'admin', 'country' => 'country');
$form_field['source']      = array('all' => 'all', 'twitter' => 'twitter', 'teamlife' => 'teamlife');

echo form_open('search/query_post', $attributes);

echo form_label('Terms', 'terms');
echo form_input( $form_field['terms'] );
echo "<br />";

echo form_label('Since', 'since');
echo form_input( $form_field['since'] );
echo "<br />";


echo form_label('Until', 'until');
echo form_input( $form_field['until'] );
echo "<br />";

echo form_label('Where', 'where');
echo form_input( $form_field['where'] );
$js_findplace = 'onClick="find_place()"';
echo form_button('find', 'Find place', $js_findplace);
echo "<br />";

echo form_label('Granularity', 'granularity');
echo form_dropdown('granularity', $form_field['granularity'], 'neighborhood');
echo "<br />";

echo form_label('Source', 'source');
echo form_dropdown('source', $form_field['source'], 'twitter');
echo "<br />";

echo form_submit('submit', 'Search!');

echo form_close();
?>