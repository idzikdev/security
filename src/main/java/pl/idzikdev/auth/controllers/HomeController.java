package pl.idzikdev.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.idzikdev.auth.models.MyUser;
import pl.idzikdev.auth.services.UserService;

import java.util.List;

@RestController
public class HomeController {
    @Autowired
    UserService service;

    public HomeController(UserService service) {
        this.service=service;
    }

    @GetMapping("/home")
    public String home(){
        return "Hello";
    }

    @GetMapping("/secret")
    @ResponseBody
    public String secret(){
        return "secret area";
    }

    @GetMapping("/users")
    public List<MyUser> getAllUsers(){
        return service.getRegisteredUsers();
    }

    @PostMapping("/signup")
    public void signup(@RequestBody MyUser user){
        service.signUp(user);

    }


}
