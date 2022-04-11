<#import "parts/common.ftlh" as c>

<@c.page>
    <div class="card my-3">
    <#if not_found??>
        <img src="/img/${not_found??}" class="card-img-top">
    </#if>
    <div class="m-2">
        <span>Not exist</span><br>
    </div>
    </div>

</@c.page>