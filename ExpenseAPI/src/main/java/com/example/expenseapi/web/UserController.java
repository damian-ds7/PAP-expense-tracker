package com.example.expenseapi.web;

import com.example.expenseapi.pojo.User;
import com.example.expenseapi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name="User Controller", description = "Controller to manage user objects")
public class UserController extends GenericController<User, Long>{
    public UserController(UserService service) {super(service);}
}
