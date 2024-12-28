package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.TemporaryMembershipService;
import com.example.expenseapi.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
