<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">
<@c.page>
    <div class="container my-3">
        <div class="col-sm-6 my-3">
            ${userPage.username}
        </div>
        <div class="my-3">
            <#if !isCurrentUser>
                <#if isSub>
                    <a href="/user/subscribe/${userPage.id}"> <button type="button" class="btn btn-dark" value="unsub">Subscribe</button></a>
                <#else>
                <a href="/user/unsubscribe/${userPage.id}"><button type="button" class="btn btn-dark" value="sub">Unsubscribe</button></a>
                </#if>
            </#if>
        </div>
    </div>
    <div class="container my-3">
        <div class="col">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Subscription</h5>
                    <a href="/user/subscriptions/${userPage.id}">${subscriptions}</a>
                    </h5>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Subs</h5>
                    <a href="/user/subs/${userPage.id}">${subs}</a>
                    </h5>
                </div>
            </div>
        </div>
    </div>
    <#if message??>
        <#if isCurrentUser>
            <#include "parts/postRedactor.ftlh">
        </#if>
    </#if>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/userMessage" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}"
                       placeholder="Search by #">
                <button type="submit" class="btn btn-primary ml-2">find</button>
            </form>
        </div>
    </div>
    <#include "parts/postPage.ftlh">


</@c.page>