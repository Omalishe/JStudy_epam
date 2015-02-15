
<form class="form-horizontal" action="auth" method="post">
<fieldset>
<legend>${pageTitle}</legend>
<div class="control-group">
    <label class="control-label" for="user_name"><fmt:message key='lblLogin'/></label>
  <div class="controls">
    <input id="user_name" name="user_name" placeholder="<fmt:message key='plhLogin'/>" class="input-xlarge" type="text">
  </div>
</div>
<div class="control-group">
  <label class="control-label" for="user_password"><fmt:message key='lblPassword'/></label>
  <div class="controls">
      <input id="user_password" name="user_password" placeholder="<fmt:message key='plhPassword'/>" class="input-xlarge" type="password">
  </div>
</div>
<div class="control-group">
  <label class="control-label" for="login"></label>
  <div class="controls">
    <button id="login" name="user_login" value="true" class="btn btn-success"><fmt:message key="btnLogin"/></button>
  </div>
</div>
<input type="hidden" name="action" value ="login">
</fieldset>
</form>
