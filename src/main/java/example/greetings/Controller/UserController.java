package example.greetings.Controller;

import example.greetings.Models.Role;
import example.greetings.Models.User;
import example.greetings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PostController.class);
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user,
                               @RequestParam(required = false) String warning,
                               Model model) {

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        if (warning != null && !warning.isEmpty()) {
            model.addAttribute("message", warning);
        }
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERADMIN')")
    @PostMapping
    public String userSave(@AuthenticationPrincipal User CurrentUser,
                           @RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam("userId") User user,
                           RedirectAttributes redirectAttributes) {
        if (userService.saveUser(user, username, form) == null) {
            logger.info("Администратор {} изменил имя пользователя:{} на {}.",CurrentUser.getUsername(),user.getUsername(),username);
            return "redirect:/user";
        } else {
            logger.info("Администратор {} пытался изменить имя пользователя {} на {}.Но оно существует.",CurrentUser.getUsername(),username,user.getUsername());
            String warning = "already exist";
            redirectAttributes.addAttribute("warning", warning);
            return "redirect:/user/" + user.getId();
        }
    }

    @GetMapping("changeSecurity")
    public String profile(Model model,
                          @AuthenticationPrincipal User user,
                          @PathVariable(required = false) String warning) {
        model.addAttribute("warning", warning);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "changeSecurity";
    }


    @PostMapping("changeSecurity")
    public String updateData(@AuthenticationPrincipal User user,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             Model model) {
        String resultData = userService.updateData(user, username, password, email);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        if (resultData == null) {
            logger.info("Пользователь {} сменил свои данные",user.getUsername());
            return "changeSecurity";
        } else {
            logger.info("Пользователь {} пытался сменить свои данные",user.getUsername());
            model.addAttribute("warning", resultData);
            return "changeSecurity";
        }
    }

    @GetMapping("/subscribe/{user}")
    public String subscribe(@AuthenticationPrincipal User sub,
                            @PathVariable(name = "user") User chanel,
                            Model model) {
        UserService.sub(sub, chanel);
        return "redirect:/userMessages/" + chanel.getId();
    }

    @GetMapping("/unsubscribe/{user}")
    public String unsubscribe(@AuthenticationPrincipal User sub,
                              @PathVariable(name = "user") User chanel,
                              Model model) {
        UserService.unsub(sub, chanel);
        return "redirect:/userMessages/" + chanel.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String subList(Model model,
                          @PathVariable User user,
                          @PathVariable String type) {
        model.addAttribute("userToCheck", user.getUsername());
        model.addAttribute("type", type);
        if ("subscriptions".equals(type)) {
            model.addAttribute("userList", user.getSubscriptions());
        } else {
            model.addAttribute("userList", user.getUser_subs());
        }
        return "sub";
    }

}
