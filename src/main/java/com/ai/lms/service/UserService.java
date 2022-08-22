package com.ai.lms.service;

import com.ai.lms.entities.User;
import com.ai.lms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return user;
    }

    @Transactional
    @EventListener(classes = ContextRefreshedEvent.class)
    public void initializeUser() {
        if(userRepo.count() == 0) {
            var user = new User();
            user.setLoginId("admin");
            user.setName("Admin User");
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(User.Role.Admin);
            userRepo.save(user);

            var teacher = new User();
            teacher.setLoginId("TCH001");
            teacher.setName("May Zwei");
            teacher.setEmail("admin@gmail.com");
            teacher.setPassword(passwordEncoder.encode("teacher"));
            teacher.setRole(User.Role.Teacher);
            userRepo.save(teacher);

            var student = new User();
            student.setLoginId("STU001");
            student.setName("Swam Thu Marn");
            student.setEmail("swamthumarn@gmail.com");
            student.setPassword(passwordEncoder.encode("student"));
            student.setRole(User.Role.Student);
            userRepo.save(student);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByLoginId(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        } else {
          return   org.springframework.security.core.userdetails.User
                    .withUsername(user.getLoginId())
                    .password(user.getPassword())
                    .authorities(user.getRole().name())
            .build();
        }
    }
}
