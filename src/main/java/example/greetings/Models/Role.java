package example.greetings.Models;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,MODERATOR,ADMIN,SUPERADMIN;

    @Override
    public  String getAuthority(){

        return name();

    }
}
