package com.example.demospringvertx;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    public List<User> getAllUsers() {
        User user1 = new User();
        user1.setName("name1");
        user1.setLastName("lastName1");

        User user2 = new User();
        user2.setName("name2");
        user2.setLastName("lastName2");

        return Arrays.asList(user1, user2);
    }
}
