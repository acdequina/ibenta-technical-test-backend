package au.com.ibenta.test.service;

import au.com.ibenta.test.model.User;
import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserEntity> create(UserEntity user) throws InvalidInputException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        EmailValidator.validate(user.getEmail());
       return Mono.just(userRepository.save(user));
    }

    @Override
    public Mono<UserEntity> get(Long id) throws NotFoundException {
        Mono<UserEntity> monoUser = Mono.justOrEmpty(userRepository.findById(id));
        if(monoUser.equals(Mono.empty())) {
            throw new NotFoundException("User not found | id: "+id);
        }

        return monoUser;
    }

    @Override
    public Mono<UserEntity> update(UserEntity user) throws InvalidInputException {
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if(user.getEmail() != null) {
            EmailValidator.validate(user.getEmail());
        }

        return Mono.just(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Flux<UserEntity> list() {
        return Flux.fromIterable(userRepository.findAll());
    }

    public UserEntity mapUserToNewUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());

        return userEntity;
    }

}
