package example.greetings.service;

import example.greetings.Models.Role;
import example.greetings.Models.User;
import example.greetings.interfaces.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSenderService mailSenderService;

    @Test
    public void addUser() {
        User user= new User();
        user.setEmail("email@email.ru");

        boolean testIsFine=userService.addUser(user);

        Assert.assertTrue(testIsFine);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertNotNull(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
        Assert.assertNotNull(user.getActivationCode());

        Mockito.verify(userRepo,Mockito.times(1)).save(user);
        Mockito.verify(mailSenderService,Mockito.times(1))
                .send(ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    public void addUserExist() {
        User user= new User();
        user.setUsername("Nick");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByUsername("Nick");

        boolean testIsFine=userService.addUser(user);

        Assert.assertFalse(testIsFine);
        Mockito.verify(userRepo,Mockito.times(0)).save(user);
        Mockito.verify(mailSenderService,Mockito.times(0))
                .send(ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    public void activateUser() {

        User user =new User();
        user.setActivationCode("someCode");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activation");

        boolean isActivated =userService.activateUser("activation");

        Assert.assertTrue(isActivated);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }
}