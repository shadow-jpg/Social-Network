<#import "parts/common.ftlh" as c>
<#import  "parts/login.ftlh" as l>

<@c.page>
add new user
    <div class ="alert-danger" roles="alert">
        ${message?ifExists}
    </div>

<@l.login "/register" true />
</@c.page>