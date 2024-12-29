package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.TemporaryMembershipService;
import com.example.expenseapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tempMembership")
public class TemporaryMembershipController extends GenericController<TemporaryMembership, Long> {
    private final MembershipService membershipService;
    private final TemporaryMembershipService temporaryMembershipService;
    private final UserService userService;
    public TemporaryMembershipController(TemporaryMembershipService service, MembershipService membershipService, UserService userService) {
        super(service);
        this.membershipService = membershipService;
        this.temporaryMembershipService = service;
        this.userService = userService;
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<HttpStatus> acceptInvitation(@AuthenticationPrincipal User user, @PathVariable Long id) {
        com.example.expenseapi.pojo.User mUser = userService.findByEmail(user.getUsername()).get();
        TemporaryMembership temporaryMembership = temporaryMembershipService.get(id);

        if (temporaryMembership.getUser().getId().equals(mUser.getId())) {
            Membership newMembership = new Membership(temporaryMembership);
            temporaryMembershipService.delete(id);
            membershipService.save(newMembership);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/decline/{id}")
    public ResponseEntity<HttpStatus> declineInvitation(@AuthenticationPrincipal User user, @PathVariable Long id) {
        com.example.expenseapi.pojo.User mUser = userService.findByEmail(user.getUsername()).get();
        TemporaryMembership temporaryMembership = temporaryMembershipService.get(id);

        if (temporaryMembership.getUser().getId().equals(mUser.getId())) {
            temporaryMembershipService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<TemporaryMembership>> getInvitations(@AuthenticationPrincipal User user) {
        com.example.expenseapi.pojo.User mUser = userService.findByEmail(user.getUsername()).get();
        return new ResponseEntity<>(temporaryMembershipService.getByUserId(mUser.getId()), HttpStatus.OK);
    }

}
