package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<UserEntity> create(UserEntity user);

    Mono<UserEntity> get(Long id) throws NotFoundException;

    Mono<UserEntity> update(UserEntity user);

    void delete(Long id);

    Flux<UserEntity> list();

}
