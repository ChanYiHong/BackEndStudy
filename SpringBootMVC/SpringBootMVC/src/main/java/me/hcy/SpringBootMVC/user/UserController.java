package me.hcy.SpringBootMVC.user;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

//    @PostMapping("/user")
//    public @ResponseBody User create(@RequestBody User user) {
//        return null;
//    }

    @PostMapping("/users/create")
    public User create(@RequestBody User user) {
        return user;
    }

}
