package example.greetings;

import example.greetings.Controller.MessageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageController controller;

    @Test
    public void testHelloPage() throws Exception{
        this.mockMvc.perform(get("/")) //подмененный веб-слой. запрос на страницу
                .andDo(print())   // вывод результата сервера
                .andExpect(status().isOk()) // код возврата 200
                .andExpect(content().string(containsString("Hello, guest"))); // проверка соддержания строки
    }

    @Test
    public void NoLoginNoAccess() throws Exception{
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()) //проверка будет ли редирект
                .andExpect(redirectedUrl("http://localhost/login")); // проверка redirect
    }

    @Sql( value = {"/user-creation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql( value = {"/user_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void correctLoginTest() throws Exception{
        this.mockMvc.perform(formLogin().user("w0").password("w")) //login
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void badCredentals() throws Exception{
        this.mockMvc.perform(post("/login").param("user","bugiwugi"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
