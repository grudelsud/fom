<h1>User list</h1>
<ul>
<?php
	foreach ($result as $user) :
?>
	<li><?php echo $user->user_login." - ".anchor('user/profile/'.$user->id_user, 'profile') ?></li>
<?php
	endforeach;
?>
</ul>
<?php echo form_open('user/insert'); ?>
<label for="user_login"></label>

<p>
	<label for="user_login">login</label>
	<input type="text" name="user_login" id="user_login" />
</p>
<p>
	<label for="user_pass">password</label>
	<input type="password" name="user_pass" id="user_pass" />
</p>
<p>
	<label for="user_email">email</label>
	<input type="text" name="user_email" id="user_email" />
</p>
<p>
	<input type="submit" name="submit" id="submit" value="Submit" />
</p>
</form>