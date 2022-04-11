<#import "parts/common.ftlh" as c>
<@c.page>
    User Editor
    <form action="/user" method="post">
        <input type="text" name="username" value="${user.username}">
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <input type="hidden" name="userId" value="${user.id}">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Save</button>
    </form>
    <#if message??>
        <div class="m-2" name="message">
            <span>${message}</span><br>
        </div>
    </#if>
    <a href="/user">back</a>
</@c.page>