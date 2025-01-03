package com.example.expenseapi.web;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
@Tag(name="Group Controller", description = "Controller to manage group objects")
public class GroupController extends GenericController<Group, Long>{
    private final MembershipService membershipService;

    public GroupController(MembershipService membershipService, GroupService groupService) {
        super(groupService);
        this.membershipService = membershipService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BaseGroup>> getAllGroups(@AuthenticationPrincipal User user) {
        Optional<com.example.expenseapi.pojo.User> mUser = userService.findByEmail(user.getUsername());
        return new ResponseEntity<>(mUser.map(value -> membershipService.getBaseGroupsByUserId(value.getId())).orElse(null), HttpStatus.OK);
    }

    @GetMapping("all/active")
    public ResponseEntity<List<Group>> getActiveGroups(@AuthenticationPrincipal User user) {
        Optional<com.example.expenseapi.pojo.User> mUser = userService.findByEmail(user.getUsername());
        return new ResponseEntity<>(mUser.map(value -> membershipService.getGroupsByUserId(value.getId())).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/members/{group}")
    @Operation(summary = "Get members of a group")
    @ApiResponse(responseCode = "200", description = "List of group members.",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    public ResponseEntity<List<UserDTO>> getMembers(@PathVariable String group){
        return new ResponseEntity<>(membershipService.findUsers(group), HttpStatus.OK);
    }

    @GetMapping("/admins/{group}")
    @Operation(summary = "Get administrators of a group")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of group administrators.",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    public ResponseEntity<List<UserDTO>> getAdmins(@PathVariable String group){
        return new ResponseEntity<>(membershipService.findAdmins(group), HttpStatus.OK);
    }
}
