package edu.iu.hmbharad.primeservice.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.iu.hmbharad.primeservice.model.Customer;
import edu.iu.hmbharad.primeservice.repository.AuthenticationDBRepository;

import java.io.IOException;

@Service
public class AuthenticationService implements IAuthenticationService, UserDetailsService {

    private final AuthenticationDBRepository authenticationRepository;

    public AuthenticationService(AuthenticationDBRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public boolean register(Customer customer) throws IOException {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String passwordEncoded = bc.encode(customer.getPassword());
        customer.setPassword(passwordEncoded);
        try{
            authenticationRepository.save(customer);
            return true;
        }
        catch(Exception exception){
            System.out.println(exception);
            return false;
        }     
    }

    @Override
    public boolean login(String username, String password) throws IOException {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Customer customer = authenticationRepository.findByUsername(username);
            if(customer==null) {
                throw new UsernameNotFoundException("");
            }
            return User
                    .withUsername(username)
                    .password(customer.getPassword())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
