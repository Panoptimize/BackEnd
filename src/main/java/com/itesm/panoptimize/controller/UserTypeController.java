package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.user_type.UserTypeCreateDTO;
import com.itesm.panoptimize.dto.user_type.UserTypeDTO;
import com.itesm.panoptimize.dto.user_type.UserTypeUpdateDTO;
import com.itesm.panoptimize.service.UserTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-type")
public class UserTypeController {
    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @Operation(summary = "Get user type by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User type found"),
        @ApiResponse(responseCode = "404", description = "User type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserTypeDTO> getUserType(@PathVariable Integer id) {
        return ResponseEntity.ok(userTypeService.getUserType(id));
    }

    @Operation(summary = "Get all user types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User types found"),
        @ApiResponse(responseCode = "404", description = "User types not found")
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllUserTypes(Pageable pageable) {
        return ResponseEntity.ok(userTypeService.getAllUserTypes(pageable));
    }

    @Operation(summary = "Create user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User type created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    public ResponseEntity<UserTypeDTO> createUserType(@RequestBody UserTypeCreateDTO userTypeCreateDTO) {
        return ResponseEntity.ok(userTypeService.createUserType(userTypeCreateDTO));
    }

    @Operation(summary = "Update user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User type updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserTypeDTO> updateUserType(@PathVariable Integer id, @RequestBody UserTypeUpdateDTO userTypeUpdateDTO) {
        return ResponseEntity.ok(userTypeService.updateUserType(id, userTypeUpdateDTO));
    }

    @Operation(summary = "Delete user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User type deleted"),
        @ApiResponse(responseCode = "404", description = "User type not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserType(@PathVariable Integer id) {
        userTypeService.deleteUserType(id);
        return ResponseEntity.ok().build();
    }
}
