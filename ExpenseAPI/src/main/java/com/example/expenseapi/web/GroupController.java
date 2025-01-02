package com.example.expenseapi.web;

import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController  {
    private final MembershipService membershipService;
    private final UserService userService;

    public GroupController(MembershipService membershipService, UserService userService) {
        this.membershipService = membershipService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BaseGroup>> getAllGroups(@AuthenticationPrincipal User user) {
        Optional<com.example.expenseapi.pojo.User> mUser = userService.findByEmail(user.getUsername());
        return new ResponseEntity<>(mUser.map(value -> membershipService.getBaseGroupsByUserId(value.getId())).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/members/{group}")
    public ResponseEntity<List<com.example.expenseapi.pojo.User>> getMembers(@PathVariable String group){
        return new ResponseEntity<>(membershipService.findUsers(group), HttpStatus.OK);
    }

    @GetMapping("/admins/{group}")
    public ResponseEntity<List<com.example.expenseapi.pojo.User>> getAdmins(@PathVariable String group){
        return new ResponseEntity<>(membershipService.findAdmins(group), HttpStatus.OK);
    }
}
