<#import "parts/common.ftlh" as c>
<#import  "parts/login.ftlh" as l>
<@c.page>
    Авторизуйтесь
    <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
        <div class ="alert-danger" roles="alert">
            ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
        </div>
    </#if>
        <div class ="alert-danger" roles="alert">
            ${message?ifExists}
        </div>
<@l.login "/login" false />
</@c.page>
