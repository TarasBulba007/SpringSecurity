package com.tarasPlus.springmvc.service;

import com.tarasPlus.springmvc.model.Role;
import com.tarasPlus.springmvc.model.User;
import com.tarasPlus.springmvc.repositories.RoleRepository;
import com.tarasPlus.springmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private Role userRole;
    private Role adminRole;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAllUsers()  {
        return  userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean addUser(User user) {
        if (!isExistLogin(user.getUsername())) {
            addUserRole(user);
            user.setPassword(
                    bCryptPasswordEncoder.encode(
                            user.getPassword()));
            userRepository.saveAndFlush(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean addUserAdmin(User user, String access) {
        if (notNullDataUser(user) && !isExistLogin(user.getUsername())) {
            initRole(user, access);
            passwordEncode(user);
            userRepository.saveAndFlush(user);
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        if (notNullDataUser(user)) {
            passwordEncode(user);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        User userDelete = getUserById(user.getId());
        userRepository.delete(userDelete);
        userRepository.flush();
    }

    @Override
    @Transactional
    public void dropTable() {
        userRepository.deleteAll();
    }

    @Override
    @Transactional
    public void createTable()  {
    }

    @Override
    @Transactional
    public boolean isExistLogin(String login) {
        User byLogin = userRepository.findByLogin(login);
        if (byLogin!=null) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean notNullDataLoginPassword(User user) {
        return user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty();
    }

    @Override
    @Transactional
    public boolean notNullDataUser(User user) {
        return user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty() &&
                user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty();
    }

    @Override
    @Transactional
    public User editUserView(String edit, User user) {
        User editUser = null;
        if (!edit.isEmpty()) {
            editUser = getUserById(user.getId());
        }
        return editUser;
    }

    private void addUserRole(User user) {
        if (userRole==null) {
            userRole = roleRepository.findById(2);
        }
        user.getRoles().add(userRole);
    }

    private void addAdminRole(User user) {

        if (adminRole==null) {
            adminRole = roleRepository.findById(1);
        }

        user.getRoles().add(adminRole);
    }

    private void initRole(User user, String access) {

        if (access.contains("ADMIN")) {
            addAdminRole(user);
        } else {
            addUserRole(user);
        }
    }

    private void passwordEncode(User user) {
        if (user.getPassword().length()<30) {
            String pass = user.getPassword();
            user.setPassword(
                    bCryptPasswordEncoder
                            .encode(pass));
        }
    }
}