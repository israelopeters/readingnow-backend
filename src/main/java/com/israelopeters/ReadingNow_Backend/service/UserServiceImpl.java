package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.InvalidUserException;
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
import org.springframework.core.env.Environment;
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

    @Autowired
    Environment env;

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
        // Check whether user is valid
        try {
            if (!isValidUser(userCreationDto)) {
                throw new InvalidUserException("Invalid user!");
            }
        } catch (NullPointerException ignored) {
            // Exception handling for when a user sends an invalid (non-UserCreationDto) request body to the endpoint
            throw new NullPointerException("Invalid user!");
        }

        // Check whether user already exists
        Optional<User> userCheck = userRepository.findByEmail(userCreationDto.getEmail());
        if (userCheck.isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        // Create user roles
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = assignRole();
        }

        // Update unmodified fields and encrypt password
        User user = dtoMapper.toUser(userCreationDto);
        user.setRoles(List.of(role));
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

    private boolean isValidUser(UserCreationDto userCreationDto) {
        return !userCreationDto.getFirstName().isBlank()
                & !userCreationDto.getLastName().isBlank()
                & !userCreationDto.getEmail().isBlank()
                & !userCreationDto.getPassword().isBlank();
    }

    public void addAdminUser() {
        User user = new User();
        try {
            if (userRepository.findByEmail(env.getProperty("ADMIN_USERNAME")).isPresent()) {
                throw new UserAlreadyExistsException("Admin already exists!");
            }
        } catch (UserAlreadyExistsException e) {
            return;
        }
        user.setEmail(env.getProperty("ADMIN_EMAIL"));
        user.setPassword(passwordEncoder.encode(
                env.getProperty("ADMIN_PASSWORD")));
        user.setFirstName(env.getProperty("ADMIN_FIRSTNAME"));
        user.setLastName(env.getProperty("ADMIN_LASTNAME"));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_ADMIN");
            role = roleRepository.save(role);
        }
        user.setRoles(List.of(role));
        user.setDateCreated(LocalDate.now());
        userRepository.save(user);
    }
}
