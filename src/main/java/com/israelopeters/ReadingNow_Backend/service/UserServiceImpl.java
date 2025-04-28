package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.UserAlreadyExistsException;
import com.israelopeters.ReadingNow_Backend.exception.UserNotFoundException;
import com.israelopeters.ReadingNow_Backend.model.Role;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.model.dto.DtoMapper;
import com.israelopeters.ReadingNow_Backend.model.dto.UserCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.repository.RoleRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        return new ArrayList<>(userRepository
                .findAll()
                .stream()
                .map(user -> dtoMapper.toUserDto(user))
                .toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return dtoMapper.toUserDto(user.get());
        } else {
            throw new UserNotFoundException("User does not exist!");
        }
    }

    @Override
    public UserDto addUser(UserCreationDto userCreationDto) {
        // Check whether user already exists
        Optional<User> userCheck = userRepository.findByEmail(userCreationDto.getEmail());
        if (userCheck.isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        // Create user roles
        Role role = assignRole();
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);

        // Update unmodified fields and encrypt password
        User user = dtoMapper.toUser(userCreationDto);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));
        user.setDateCreated(LocalDate.now());

        // Persist user
        User savedUser = userRepository.save(user);
        return dtoMapper.toUserDto(savedUser);
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User does not exist!");
        }
    }

    private Role assignRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
