package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.AuthDAO;
import com.example.soundarchive.exception.UnauthorizedAccessException;
import com.example.soundarchive.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private AuthDAO authDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        FanEntity fan = fanDao.findByUserName(username);
//        ArtistEntity artist = artistDao.findByArtistName(username);
        UserEntity user = authDAO.getUserbyUsername(username);

//        if(fan == null && artist == null) {
//            throw new UsernameNotFoundException(username + "not found.");
//        }
        if(!user.getActive()) throw new UnauthorizedAccessException("User " + username + " has been banned!");

        UserDetails userDetails = User.builder().username(user.getUsername()).password(user.getPassword()).roles("USER").build();

        return userDetails;
    }
}
