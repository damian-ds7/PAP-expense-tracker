package com.example.expenseapi.web;

import com.example.expenseapi.dto.MembershipCreateDTO;
import com.example.expenseapi.pojo.*;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.TemporaryMembershipService;
import com.example.expenseapi.utils.AuthHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/membership")
@Tag(name = "Membership Controller", description = "Controller to manage memberships")
public class MembershipController extends GenericController<Membership, Long> {
    private final MembershipService membershipService ;
    private final TemporaryMembershipService temporaryMembershipService;
    public MembershipController(MembershipService service, MembershipService membershipService, TemporaryMembershipService temporaryMembershipService) {
        super(service);
        this.membershipService = membershipService;
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
    public ResponseEntity<TemporaryMembership> save(@RequestBody MembershipCreateDTO entity) {
        return new ResponseEntity<>(temporaryMembershipService.save(entity), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}/{groupName}")
    @Operation(summary = "Delete user from a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membership successfully deleted.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have admin privileges.",
                    content = @Content)
    })
    public ResponseEntity<HttpStatus> delete(@PathVariable String groupName, @PathVariable Long userId) {
        membershipService.deleteMembership(userId, groupName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    @Operation(summary = "Update a membership in a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership successfully updated.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have admin privileges.",
                    content = @Content)
    })
    public ResponseEntity<HttpStatus> update(@RequestBody Membership entity) {
        User user = AuthHelper.getUser();
        if (((MembershipService) service).getRole(user, entity.getGroup()).equals("admin")) {
            membershipService.update(entity.getId(), entity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
