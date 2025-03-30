package com.gamax.userservice.Controller;

import com.gamax.userservice.Entity.Admin;
import com.gamax.userservice.Entity.Client;
import com.gamax.userservice.Entity.User;
import com.gamax.userservice.Service.AdminService;
import com.gamax.userservice.Service.ClientService;
import com.gamax.userservice.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamax.userservice.TDO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ObjectMapper objectMapper;
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAll());
    }
    @GetMapping("TDO/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO userDTO = userService.getTDObyID(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("TDO/by-ids")
    public ResponseEntity<List<UserDTO>> getUsersByIds(@RequestBody List<Long> userIds) {
        List<UserDTO> userDTOs = userService.getTDDOUsersByIds(userIds);
        return ResponseEntity.ok(userDTOs);
    }
    @GetMapping("/{type}")
    public List<?> getAll(@PathVariable String type) {
        return switch (type.toLowerCase()) {
            case "user" -> userService.getAll();
            case "admin" -> adminService.getAll();
            case "client" -> clientService.getAll();
            default -> throw new IllegalArgumentException("Invalid entity type: " + type);
        };
    }
    @PostMapping("/{type}/add")
    public ResponseEntity<?> addUser(@PathVariable String type, @RequestBody String object) {
        try {//TODO: Upload image de profile
            switch (type.toLowerCase()) {
                case "user":
                    User user = objectMapper.readValue(object, User.class);
                    return ResponseEntity.status(201).body(userService.save(user));
                case "admin":
                    Admin admin = objectMapper.readValue(object, Admin.class);
                    return ResponseEntity.status(201).body(adminService.save(admin));
                case "client":
                    Client client = objectMapper.readValue(object, Client.class);
                    return ResponseEntity.status(201).body(clientService.save(client));
                default:
                    throw new IllegalArgumentException("Invalid user type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON payload: " + e.getMessage(), e);
        }
    }
    @PutMapping("/{type}/update")
    public ResponseEntity<?> updater_user(@RequestBody String object, @PathVariable String type) {
        try {
            switch (type.toLowerCase()) {
                case "user":
                    User user = objectMapper.readValue(object, User.class);
                    return ResponseEntity.status(201).body(userService.update(user.getUserId(),user));
                case "client":
                    Client client = objectMapper.readValue(object, Client.class);
                    return ResponseEntity.status(201).body(clientService.update(client.getUserId(),client));
                case "admin":
                    Admin admin = objectMapper.readValue(object, Admin.class);
                    return ResponseEntity.status(201).body(adminService.update(admin.getUserId(),admin));
                default:
                    throw new IllegalArgumentException("Invalid user type: " + type);
            }
        }catch (RuntimeException  e)
        {
            throw new RuntimeException("" + e.getMessage(), e);

        //    return ResponseEntity.status(404).body("Entity not Found");
        }catch (JsonProcessingException e){
            throw new RuntimeException("Failed to deserialize JSON payload: " + e.getMessage(), e);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.delete(user.getUserId()));
    }
    //TODO: ajouti Endpoint lil mise a jour mta3 image (remplaci il image ili uploaditha 3al server)
    //TODO: ajouti Endpoint tfase5 il image mil server
    //TODO: Cascade Delete (tab3eth notification lil les services lo5rin ili user tfase5 (etape final))
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}