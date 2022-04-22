package ru.kata.spring.boot_security.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DTO.UserDto;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;

    @Lazy
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Transactional
    public Optional<UserDto> findUserById(Long userId) {
        return Optional.of(modelMapper.map(userRepository.findById(userId), UserDto.class));
    }

    @Transactional
    public Optional<List<UserDto>> allUsers() {
        List<UserDto> userDtoList = userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        return Optional.of(userDtoList);
    }


    @Transactional
    public void saveUser(UserDto userDto) {
        List<String> userUsernameList = userRepository.findAll()
                .stream()
                .map(userFromDb -> userFromDb.getUsername())
                .collect(Collectors.toList());
        if (!userUsernameList.contains(userDto.getUsername()) && !userDto.getUsername().equals("") && !userDto.getPassword().equals("")) {
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            User UUU = modelMapper.map(userDto, User.class);
            userRepository.save(modelMapper.map(userDto, User.class));
        }
    }

    @Transactional
    public void updateUser(UserDto userDto) {
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(modelMapper.map(userDto, User.class));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
