package example.greetings.service;

import com.mysql.cj.util.StringUtils;
import example.greetings.Models.Role;
import example.greetings.Models.User;
import example.greetings.interfaces.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSenderService mailSenderService;

    public static void sub(User sub, User chanel) {
        chanel.getUser_subs().add(sub);
    }

    public static void unsub(User sub, User chanel) {
        chanel.getUser_subs().remove(sub);
    }

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user){

        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null){

            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
//password coding
 //       user.setPassword(passwordEncoder.encode(user.getPassword()));
//
        userRepo.save(user);

        if(!StringUtils.isNullOrEmpty(user.getEmail())){
            String CodeMessage =String.format(
                    "Hello %s \n"+
                            "Welcome to new social network application. Visit our link to activate your email: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSenderService.send(user.getEmail(),"Activation code", CodeMessage);
        }

        return true;
    }

    public boolean activateUser(String code) {

        User user =userRepo.findByActivationCode(code);
        if (user ==null){
            return  false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public String saveUser(User user, String username, Map<String, String> form) {
        if(userRepo.findByUsername(username)==null || username.equals(user.getUsername())) {
            user.setUsername(username);

            Set<String> roles = Arrays.stream(Role.values())
                    .map(Role::name)
                    .collect(Collectors.toSet());


            user.getRoles().clear();


            for (String key : form.keySet()) {
                if (roles.contains(key)) {

                    user.getRoles().add(Role.valueOf(key));
                }

            }


            userRepo.save(user);
            return null;
        }else{
            return "exist";
        }
    }


    public String updateData(User user,String username,String password, String email) {

        String userMail =user.getEmail();
        boolean isEmailUpdate =(email != null && !email.equals(userMail)) ||(userMail != null && !email.equals(email));

        if(isEmailUpdate){
            user.setEmail(email);

            if(!StringUtils.isNullOrEmpty(email)){

                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if(!StringUtils.isNullOrEmpty(password)){
            user.setPassword(password);
        }

        if(username!=null && !username.equals(user.getUsername())){
            try {
                userRepo.findByUsername(username);
                return "with this nickname exist already!";
            }catch (NullPointerException e){
                user.setUsername(username);
                userRepo.save(user);
            }
        }else{
            userRepo.save(user);
        }

    return  null;
    }
}
