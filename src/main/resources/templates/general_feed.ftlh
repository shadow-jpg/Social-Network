<#import "parts/common.ftlh" as c>

<@c.page>

    <#include "parts/postRedactor.ftlh">

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/news" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}"
                       placeholder="Search by #Tag" />
                <button class="btn btn-primary ml-2">find</button>
            </form>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/news" class="form-inline">
                <input type="text" name="filterName" class="form-control" value="${filterWarning?ifExists}"
                       placeholder="Search by Nickname" />
                <button class="btn btn-primary ml-2">find user</button>
            </form>
        </div>
    </div>

    <#include "parts/postPage.ftlh">

</@c.page>