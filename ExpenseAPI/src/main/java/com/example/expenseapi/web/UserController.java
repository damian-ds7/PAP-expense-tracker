package com.example.expenseapi.web;

import com.example.expenseapi.dto.ChangePasswordDTO;
import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.dto.UserUpdateDTO;
import com.example.expenseapi.filter.UserFilter;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.UserService;
import com.example.expenseapi.utils.AuthHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name="User Controller", description = "Controller to manage user objects")
public class UserController extends GenericController<User, Long>{
    private final MembershipService membershipService;
    private final UserMapper userMapper;
    public UserController(UserService service, MembershipService membershipService, UserMapper userMapper) {
        super(service);
        this.membershipService = membershipService;
        this.userMapper = userMapper;
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
        return new ResponseEntity<>(((UserService)service).searchUsersDTO(filter, groupName), HttpStatus.OK);
    }

    @PutMapping("/changePass")
    @Operation(summary = "Changed password of logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully changed", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Password was not changed")
    })
    public ResponseEntity<UserDTO> changePass(@RequestBody ChangePasswordDTO passwordDTO) {
        if (passwordDTO.getNewPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(((UserService) service).changePassword(passwordDTO), HttpStatus.OK);
    }

    @PutMapping("/update")
    @Operation(summary = "Updates given user fields")
    @ApiResponse(responseCode = "200", description = "User changed", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(((UserService) service).update(userUpdateDTO), HttpStatus.OK);
    }

    @GetMapping("/isAdmin/{groupName}")
    @Operation(summary = "Check whether user is admin in given group")
    @ApiResponse(responseCode = "200", description = "Status of user")
    public ResponseEntity<Boolean> isAdmin(@PathVariable String groupName) {
        return new ResponseEntity<>(membershipService.isAdmin(groupName), HttpStatus.OK);
    }

    @GetMapping("/current")
    @Operation(summary = "Retrieves currently logged in user")
    @ApiResponse(responseCode = "200", description = "Logged-in user", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> getCurrent() {
        return new ResponseEntity<>(userMapper.userToUserDTO(AuthHelper.getUser()), HttpStatus.OK);
    }
}
