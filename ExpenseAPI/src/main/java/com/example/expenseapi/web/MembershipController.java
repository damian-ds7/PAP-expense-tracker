package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.UserService;
import jakarta.persistence.Inheritance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/membership")
public class MembershipController extends GenericController<Membership, Long> {
    private final UserService userService ;
    private final MembershipService membershipService ;
    public MembershipController(MembershipService service, UserService userService, MembershipService membershipService) {
        super(service);
        this.userService = userService;
        this.membershipService = membershipService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> save(@RequestBody Membership entity, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        String email = user.getUsername();
        Optional<User> mUser = userService.findByEmail(email);
        if (((MembershipService) service)
                .getMembershipsByUserId(mUser.get().getId())
                .getFirst()
                .getRole()
                .getName()
                .equals("admin")) {
            membershipService.save(entity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
