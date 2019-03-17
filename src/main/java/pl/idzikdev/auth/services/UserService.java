package pl.idzikdev.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.idzikdev.auth.models.MyUser;
import pl.idzikdev.auth.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public List<MyUser> getRegisteredUsers(){
        return repository.findAll();
    }

    public void signUp(MyUser user){
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }
    @Bean
    public BCryptPasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser=repository.findMyUsersByName(username);
        if (myUser==null){
            throw new UsernameNotFoundException(username);
        }
        //rola pusta
        return new User(myUser.getUsername(),myUser.getPassword(), Collections.emptySet());
    }
}
