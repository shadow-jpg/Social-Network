<#import "parts/common.ftlh" as c>

<@c.page>
    <div>${message?ifExists}</div>
    <a href="/user/changeSecurity">change security</a>
</@c.page>