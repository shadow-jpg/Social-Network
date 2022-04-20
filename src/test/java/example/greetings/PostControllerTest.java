package example.greetings;

import example.greetings.Controller.PostController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_dev_test.properties")
@Sql( value = {"/user-creation.sql","/post_listCreation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql( value = {"/post_listDelete.sql","/user_delete.sql",}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("w0")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostController controller;

    @Test
    public void AuthenticationByDefaultTest() throws Exception{
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(authenticated());
    } //если не работает этот метод не работает вся бд

    @Test
    public void  addSimpleMessage() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .param("tag","new one")
                .param("text", "fifth")
                .with(csrf());
        this.mockMvc.perform( multipart)
                .andDo(print())
                .andExpect(authenticated());
                //.andExpect() добавить проверку с вывода когда починю freemarker под pagination
    }

    //подправить в последствии на обычный файл и сделать проверку на корректность
    @Test
    public void  addMessageWithFile() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file","12313131313".getBytes())
                .param("tag","new one")
                .param("text", "fifth")
                .with(csrf());
        this.mockMvc.perform( multipart)
                .andDo(print())
                .andExpect(authenticated());
        //.andExpect() добавить проверку с вывода когда починю freemarker под pagination
    }

    @Test
    public void newsTest() throws  Exception{
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div").string("w0"));
    }

    @Test
    public void newsListTest() throws Exception{
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div").nodeCount(0));
    }


}
