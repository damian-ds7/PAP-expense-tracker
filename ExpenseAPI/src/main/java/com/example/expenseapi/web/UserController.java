package com.example.expenseapi.web;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.filter.UserFilter;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name="User Controller", description = "Controller to manage user objects")
public class UserController extends GenericController<User, Long>{
    public UserController(UserService service) {
        super(service);
    }

    @GetMapping("/search/{groupName}")
    @Operation(summary = "Retrieves users which satisfies conditions")
    @ApiResponse(responseCode = "200", description = "List of user objects satisfying conditions", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    public ResponseEntity<List<UserDTO>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surname", required = false) String surname,
            @PathVariable String groupName) {
        UserFilter filter = new UserFilter();
        filter.setName(name);
        filter.setSurname(surname);
        return new ResponseEntity<>(((UserService)service).searchUsersDTO(filter), HttpStatus.OK);
    }
}
