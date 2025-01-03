package com.example.expenseapi.web;

import com.example.expenseapi.pojo.BaseMembership;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.TemporaryMembershipService;
import com.example.expenseapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/membership")
@Tag(name = "Membership Controller", description = "Controller to manage memberships")
public class MembershipController extends GenericController<Membership, Long> {
    private final MembershipService membershipService ;
    private final TemporaryMembershipService temporaryMembershipService;
    private final UserService userService ;
    public MembershipController(MembershipService service, MembershipService membershipService, UserService userService, TemporaryMembershipService temporaryMembershipService) {
        super(service);
        this.membershipService = membershipService;
        this.userService = userService;
        this.temporaryMembershipService = temporaryMembershipService;
    }

    @PostMapping("/invite")
    @Operation(summary = "Invite a new member to a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation successfully created.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have admin privileges.",
                    content = @Content)
    })
    public ResponseEntity<HttpStatus> save(@RequestBody BaseMembership entity, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        String email = user.getUsername();
        User mUser = userService.findByEmail(email).get();
        if (((MembershipService) service).getRole(mUser, entity).equals("admin")) {
            temporaryMembershipService.save(new TemporaryMembership(entity));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete user from a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membership successfully deleted.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have admin privileges.",
                    content = @Content)
    })
    public ResponseEntity<HttpStatus> delete(@RequestBody Membership entity, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        String email = user.getUsername();
        User mUser = userService.findByEmail(email).get();
        if (((MembershipService) service).getRole(mUser, entity).equals("admin")) {
            membershipService.delete(entity.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/update")
    @Operation(summary = "Update a membership in a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership successfully updated.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have admin privileges.",
                    content = @Content)
    })
    public ResponseEntity<HttpStatus> update(@RequestBody Membership entity, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        String email = user.getUsername();
        User mUser = userService.findByEmail(email).get();
        if (((MembershipService) service).getRole(mUser, entity).equals("admin")) {
            membershipService.update(entity.getId(), entity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
