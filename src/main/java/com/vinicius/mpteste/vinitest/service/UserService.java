package com.vinicius.mpteste.vinitest.service;

import com.vinicius.mpteste.vinitest.controllers.exceptions.AuthorizationException;
import com.vinicius.mpteste.vinitest.models.User;
import com.vinicius.mpteste.vinitest.models.dto.UserCreateDTO;
import com.vinicius.mpteste.vinitest.models.dto.UserUpdateDTO;
import com.vinicius.mpteste.vinitest.models.enuns.ProfileEnum;
import com.vinicius.mpteste.vinitest.repositoreis.UserRepository;
import com.vinicius.mpteste.vinitest.security.UserSpringSecurity;
import com.vinicius.mpteste.vinitest.service.exceptions.DataBindingViolationException;
import com.vinicius.mpteste.vinitest.service.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User findById(Long id){

        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Acesso Negado");
        }
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuario não encontrado! id: " + id + ", tipo: " + User.class.getName()
        ));

    }

    //Criar um usuario
    @Transactional
    public User create(User obj){

        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj){

        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){

        findById(id);
        try {
            this.userRepository.deleteById(id);
        }catch (Exception e){
            throw new DataBindingViolationException("Não é possível deletar pois há entidades relacionadas à essa id");
        }
    }

    //Verifica se tem alguem logado
    public static UserSpringSecurity authenticated(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }

    public List<User> findAll(User id){
        List<User> allUsers = this.userRepository.findAll();
        return allUsers;
    }
    //endregion
}
