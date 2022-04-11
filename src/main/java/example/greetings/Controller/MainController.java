package example.greetings.Controller;

import com.mysql.cj.util.StringUtils;
import com.sun.istack.NotNull;
import example.greetings.Models.Message;
import example.greetings.Models.User;
import example.greetings.interfaces.MessageRepo;
import example.greetings.interfaces.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Controller
public class MainController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadpath;

    @Value("${delete.path}")
    private  String deletepath;



    private void saveFile(MultipartFile file, Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()){

            File fileDir =new File(uploadpath);
            if (!fileDir.exists()){

                fileDir.mkdir();
            }

            String uuidFile= UUID.randomUUID().toString();
            String resultFilename =uuidFile+"."+ file.getOriginalFilename();

            file.transferTo(new File(uploadpath+"/"+resultFilename));
            message.setFilename(resultFilename);
        }
    }

    private void deleteFile(@NotNull String fileName) throws IOException {
        if (fileName != null){
            Files.deleteIfExists(Paths.get(deletepath+"\\"+fileName));
        }
    }

    //удаляю по message.id  сам file
    private void fileRemove(Long id) throws IOException {
        Message message = messageRepo.findById(id).get();
        String filename =message.getFilename();
        if (filename != null){
            Files.deleteIfExists(Paths.get(deletepath+"\\"+filename));
        }
    }









    @GetMapping("/")
    public String greeting(
             Model model) {

        return "greeting";
    }

    @GetMapping (value ="/profile")
    public String profile(Model model){
        return "profile";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       @RequestParam(required = false, defaultValue = "") String filterName,
                       Model model) throws NullPointerException {

        Iterable<Message> messages = messageRepo.findAll();
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        if (filterName != null && !filterName.isEmpty()) {
            try {
                Long userName = userRepo.findByUsername(filterName).getId();
                return "redirect:/userMessages/" + userRepo.findByUsername(filterName).getId();
            } catch (NullPointerException e) {
                model.addAttribute("filterWarning", "Not found");
            }
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";

    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag,
                      Model model,
                      @RequestParam("file") MultipartFile file)
            throws IOException {

        Message message =new Message(tag,text,user);

        saveFile(file, message);


        messageRepo.save(message);
        Iterable<Message> messages= messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";

    }



    @GetMapping("/chat")
    public String chat(@AuthenticationPrincipal User user,
                       Model model){

        model.addAttribute("userName", user.getUsername());
        return "chat";
    }


    @GetMapping("/userMessages/{user}")
    public String userMessages(@AuthenticationPrincipal User CurrentUser,
                               @PathVariable User user,
                               Model model,
                               @RequestParam(required = false) Message message,
                               @RequestParam(required = false, defaultValue = "") String filter){

        Iterable<Message> messages =user.getMessages();

        if(message==null) {
//            if(filter != null && !filter.isEmpty()){
//               Iterable<Message> messageOf =messages.findByTag(filter);
//            }else{
//                Iterable<Message> messageOf =messages.findAll();
//            }
            model.addAttribute("filter", filter);

            model.addAttribute("messages", messages);
        }else{
            model.addAttribute("message",message);

        }
        model.addAttribute("isSub",user.getUser_subs().contains(user));
        model.addAttribute("subs",user.getUser_subs().size());
        model.addAttribute("subscriptions",user.getSubscriptions().size());
        model.addAttribute("isCurrentUser",CurrentUser.equals(user));
        model.addAttribute("userPage",user);

        return "userMessages";
    }

    @PostMapping("/userMessages/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable User user,
                                @RequestParam(value = "id",required = false) Message message,
                                @RequestParam(value= "text", required = false) String text,
                                @RequestParam(value ="tag",required = false) String tag,
                                @RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        if(message.getAuthor().equals(currentUser)){
            if(!StringUtils.isNullOrEmpty(text)){
                message.setText(text);
            }

            if(!StringUtils.isNullOrEmpty(tag)){
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

        return "redirect:/userMessages/"+user.getId();

    }

    @GetMapping(value = "/userDelete/{user}")
    public String userDelete(@AuthenticationPrincipal User CurrentUser,
                             @PathVariable User user,
                             Model model,
                             @RequestParam(value = "message") Long id) throws IOException {

        if(CurrentUser.isAdmin()){
            fileRemove(id);
            messageRepo.deleteById(id);

        } else if(CurrentUser.equals(user)){
            fileRemove(id);
            messageRepo.deleteById(id);
        }

        return "redirect:/userMessages/"+user.getId();
    }

}