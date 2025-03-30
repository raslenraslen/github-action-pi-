package com.gamax.userservice.Service;

import com.gamax.userservice.Entity.User;
import com.gamax.userservice.Repository.UserRepository;
import com.gamax.userservice.TDO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User> {
    @Autowired
    private UserRepository userRepository;
    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    public UserDTO getTDObyID(Long userId) {
        try {
            User user = getRepository().getById(userId);
            return converToTDO(user);
        }catch (Exception e){
            return null;
        }
    }
    private UserDTO converToTDO(User user){
        return UserDTO.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
    public List<UserDTO> getTDDOUsersByIds(List<Long> userIds) {
        List<User>users= getRepository().findAllById(userIds);
        return users.stream()
                .map(this::converToTDO)
                .collect(Collectors.toList());
    }
    public List<User> allUsers() {

        List<User> users = new ArrayList<>(userRepository.findAll());

        return users;
    }
}
