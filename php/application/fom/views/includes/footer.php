</div><!-- end of #content_wide -->
<div id="footer"></div>

<div id="debug_bar" style="display: none;"><span>[+|-]</span>
<div id="debug_content"><pre>
	<?php 
		var_dump( $this->session );
	?>
</pre></div>
</div>
<script type="text/javascript">
	$('#debug_bar span').click(function() {
		if( $('#debug_content').is(':visible') ) {
			$('#debug_content').slideUp('fast');
		} else {
			$('#debug_content').slideDown('fast');
		}
	});
</script>
</body>
</html>