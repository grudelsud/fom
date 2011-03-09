</div><!-- end of #content -->

<div id="footer">
</div><!-- end of #footer -->
</div><!-- end of #wrapper -->

<div id="debug_bar"><span>[+|-]</span>
<div id="debug_content"><pre><?php var_dump( $this->session ); ?></pre></div>
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