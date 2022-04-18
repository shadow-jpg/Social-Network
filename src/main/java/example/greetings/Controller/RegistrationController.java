package example.greetings.Controller;


import example.greetings.Models.CaptchaResponseDto;
import example.greetings.Models.User;
import example.greetings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String addUser(User user,
                          Model model,
                          @RequestParam("g-recaptcha-response") String captchaResponce){

        String url = String.format(CAPTCHA_URL, secret, captchaResponce); //шаблон капчи
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }


       if(!userService.addUser(user)){
           model.addAttribute("message", "User exists. Try over nickname!");
           return "register";
       }

       return"redirect:/login";
    }

    @GetMapping("/activation/{code}")
    public String activate(Model model,
                           @PathVariable String code){

        boolean isActivated =userService.activateUser(code);

        if(isActivated){
            model.addAttribute("message","User mail activated!");
        } else{
            model.addAttribute("message","Email haven't passed the activation. No code,Sorry!");
        }

        return "login";
    }

}
