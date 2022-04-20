package example.greetings;


import example.greetings.Controller.MessageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql( value = {"/user-creation.sql","/post_listCreation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql( value = {"/post_listDelete.sql","/user_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoutingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageController controller;

    @Test
    public void TestNoRegistrationOne() throws  Exception{
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    public void TestNoRegistrationConfigSecurityRegAccess() throws  Exception{
        this.mockMvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/register"));
    }


    @WithUserDetails("Alex")
    @Test
    public void TestRegisteredRedirectNoRightsUser() throws  Exception{
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news"));
    }

    @WithUserDetails("w0")
    @Test
    public void TestRegisteredRedirectNoRightsModerator() throws  Exception{
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news"));
    }

    @WithUserDetails("Nick")
    @Test
    public void TestRegisteredAccessToAdminPanel() throws  Exception{
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @WithUserDetails("w0")
    @Test
    public void TestRegisteredAllPagesModerator() throws  Exception{
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/chat"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/user/changeSecurity"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("Alex")
    @Test
    public void TestRegisteredAllPagesUser() throws  Exception{

        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/chat"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/user/changeSecurity"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("Admin")
    @Test
    public void TestRegisteredAllPagesAdmin() throws  Exception{
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/chat"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/user/changeSecurity"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("Nick")
    @Test
    public void TestRegisteredAllPagesSuperAdmin() throws  Exception{
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/chat"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/user/changeSecurity"))
                .andDo(print())
                .andExpect(status().isOk());
    }



}
