<#import "parts/common.ftlh" as c>

<@c.page>
    <h3>${userToCheck}</h3>
    <div>${type}</div>
    <ul class="list-group">
        <#list userList as user>
            <li class="list-group-item">
                <a href="/userMessages/${user.id}">${user.getUsername()}</a>
            </li>
        </#list>
    </ul>
</@c.page>