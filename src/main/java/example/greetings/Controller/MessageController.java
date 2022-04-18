package example.greetings.Controller;

import com.mysql.cj.util.StringUtils;
import com.sun.istack.NotNull;
import example.greetings.Models.Message;
import example.greetings.Models.User;
import example.greetings.interfaces.MessageRepo;
import example.greetings.interfaces.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Controller
public class MessageController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadpath;

    @Value("${delete.path}")
    private String deletepath;


    @ExceptionHandler(MissingPathVariableException.class)
    public String testExceptionHandler() {
        return "PageNotFound";
    }

    private void saveFile(MultipartFile file,
                          Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {

            File fileDir = new File(uploadpath);
            if (!fileDir.exists()) {

                fileDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadpath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
    }

    private void deleteFile(@NotNull String fileName)
            throws IOException {
        if (fileName != null) {
            Files.deleteIfExists(Paths.get(deletepath + "\\" + fileName));
        }
    }

    //удаляю по message.id  сам file
    private void fileRemove(Long id) throws IOException {
        Message message = messageRepo.findById(id).get();
        String filename = message.getFilename();
        if (filename != null) {
            Files.deleteIfExists(Paths.get(deletepath + "\\" + filename));
        }
    }


    @GetMapping("/")
    public String greeting(
            Model model) {

        return "greeting";
    }

    @GetMapping(value = "/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/news")
    public String news(@RequestParam(required = false, defaultValue = "") String filter,
                       @RequestParam(required = false, defaultValue = "") String filterName,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable)
            throws NullPointerException {

        Page<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter, pageable);
        } else {
            messages = messageRepo.findAll(pageable);
        }

        if (filterName != null && !filterName.isEmpty()) {
            try {
                Long userName = userRepo.findByUsername(filterName).getId();
                return "redirect:/userMessages/" + userRepo.findByUsername(filterName).getId();
            } catch (NullPointerException e) {
                model.addAttribute("filterWarning", "Not found");
            }
        }

        model.addAttribute("page", messages);
        model.addAttribute("url", "/news");
        model.addAttribute("filter", filter);
        return "general_feed";

    }

    @PostMapping("/news")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag,
                      Model model,
                      @RequestParam("file") MultipartFile file,
                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable)
            throws IOException {

        Message message = new Message(tag, text, user);

        saveFile(file, message);


        messageRepo.save(message);
        Page<Message> messages = messageRepo.findAll(pageable);
        model.addAttribute("url", "/news");
        model.addAttribute("page", messages);
        return "general_feed";

    }


    @GetMapping("/userMessages/{user}")
    public String userMessages(@AuthenticationPrincipal User CurrentUser,
                               @PathVariable User user,
                               Model model,
                               @RequestParam(required = false) Message message,
                               @RequestParam(required = false, defaultValue = "") String filter,
                               @PageableDefault(sort={"id"},direction=Sort.Direction.DESC) Pageable pageable)
            throws MissingPathVariableException {
        Page<Message> messages;

        if (message == null) {
//            if(filter != null && !filter.isEmpty()){
//               Iterable<Message> messageOf =messages.findByTag(filter);
//            }else{
//                Iterable<Message> messageOf =messages.findAll();
//            }
            model.addAttribute("filter", filter);
            messages = new PageImpl<Message>(user.getMessages(), pageable, user.getMessages().size());
            model.addAttribute("page", messages);
        } else {
            model.addAttribute("message", message);

        }

        model.addAttribute("url", "/userMessages/"+user.getId());
        model.addAttribute("isSub", user.getUser_subs().contains(CurrentUser));
        model.addAttribute("subs", user.getUser_subs().size());
        model.addAttribute("subscriptions", user.getSubscriptions().size());
        model.addAttribute("isCurrentUser", CurrentUser.equals(user));
        model.addAttribute("userPage", user);

        return "userMessages";
    }

    @PostMapping("/userMessages/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable User user,
                                @RequestParam(value = "id", required = false) Message message,
                                @RequestParam(value = "text", required = false) String text,
                                @RequestParam(value = "tag", required = false) String tag,
                                @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {

        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isNullOrEmpty(text)) {
                message.setText(text);
            }

            if (!StringUtils.isNullOrEmpty(tag)) {
                message.setTag(tag);
            }
            if (!file.isEmpty()) {
                if (message.getFilename() != null) {
                    deleteFile(message.getFilename());
                    saveFile(file, message);
                } else {
                    saveFile(file, message);
                }
            }
            messageRepo.save(message);
        }

        return "redirect:/userMessages/" + user.getId();

    }

    @GetMapping(value = "/userDelete/{user}")
    public String userMessageDelete(@AuthenticationPrincipal User CurrentUser,
                                    @PathVariable User user,
                                    Model model,
                                    @RequestParam(value = "message") Long id) throws IOException {

        if (CurrentUser.canModerate()) {
            fileRemove(id);
            messageRepo.deleteById(id);

        } else if (CurrentUser.equals(user)) {
            fileRemove(id);
            messageRepo.deleteById(id);
        }

        return "redirect:/userMessages/" + user.getId();
    }

}