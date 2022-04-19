<#assign
    know = Session.SPRING_SECURITY_CONTEXT??
>
    <#if know>
        <#assign
            user  =Session.SPRING_SECURITY_CONTEXT.authentication.principal
            name = user.getUsername()
            canModerate=user.canModerate()
            isAdmin =user.isAdmin()
<#--            isSup=user.isSupreme()-->
            currentUserId= user.getId()
        >

    <#else>
        <#assign

        name = "unknown user"
        isAdmin =false
        currentUserId= -1
        >
    </#if>
