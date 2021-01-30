package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserEntity> create(UserEntity user) {
       return Mono.just(userRepository.save(user));
    }

    @Override
    public Mono<UserEntity> list(Long id) throws NotFoundException {
        Mono<UserEntity> monoUser = Mono.justOrEmpty(userRepository.findById(id));
        if(monoUser.equals(Mono.empty())) {
            throw new NotFoundException("User not found | id: "+id);
        }

        return monoUser;
    }

    @Override
    public Mono<UserEntity> update(UserEntity user) {
        return Mono.just(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Flux<UserEntity> findAll() {
        return Flux.fromIterable(userRepository.findAll());
    }

}
