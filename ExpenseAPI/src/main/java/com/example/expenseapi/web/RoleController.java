package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Role;
import com.example.expenseapi.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@Tag(name = "Role controller")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @RequestMapping("/all")
    public List<String> getAllRoles() {
        return roleService.getAll().stream()
                .map(Role::getName)
                .toList();
    }
}
