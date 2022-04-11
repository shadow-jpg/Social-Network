<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group">
            <label class="col-sm-2 col-form-label"> User Name :</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" name="username" placeholder="Username"/>
            </div>
        </div>

        <div class="form-group">
            <div>
                <label class="col-sm-2 col-form-label"> Password:  </label>
                <div class="col-sm-5">
                    <input type="password" name="password" placeholder="password"/>
                </div>
            </div>
        </div>

        <#if isRegisterForm>
<#--            <div class="form-group">-->
<#--                <div>-->
<#--                    <label class="col-sm-2 col-form-label"> Email:  </label>-->
<#--                    <div class="col-sm-5">-->
<#--                        <input type="email" name="email" placeholder="mail@gmail.com"/>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
            <div class="g-recaptcha" data-sitekey="6Lc6DpseAAAAAK-kwqwHSnDfn4auIN-zTjDoBQHw"></div>
            <#if captchaError??>
                <div class ="alert-danger" roles="alert">${captchaError}</div>
            </#if>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />

        <#if !isRegisterForm>
            <a href="/register">Register</a>
            <br>
        </#if>

        <a href="/main"><button type="submit" value="Sign In" class="btn btn-primary"><#if isRegisterForm>CREATE New Account<#else>Sign in</#if></button></a>
    </form>

</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden"  class="form-control" name="_csrf" value="${_csrf.token}" />
        <button type="submit" class="btn btn-primary" type="submit" value="Sign Out">Log out</button>
    </form>
</#macro>