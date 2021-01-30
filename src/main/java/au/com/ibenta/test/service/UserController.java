package au.com.ibenta.test.service;

import au.com.ibenta.test.model.User;
import au.com.ibenta.test.persistence.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<User>> create(@Valid @RequestBody User user) {
        try {
            UserEntity userEntity = userService.mapUserToNewUserEntity(user);
            return new ResponseEntity<>(userService.create(userEntity).map(User::new), HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(Mono.empty(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<Mono<User>> get(@PathVariable("id") Long id) {
        try {
            Mono<UserEntity> monoUser = userService.get(id);
            return new ResponseEntity<>(monoUser.map(User::new), HttpStatus.OK);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(Mono.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mono<User>> update(@Valid @RequestBody User user) {
        try {
            userService.get(user.getId());
            UserEntity userEntity = userService.mapUserToNewUserEntity(user);
            return new ResponseEntity<>(userService.update(userEntity).map(User::new), HttpStatus.OK);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(Mono.empty(), HttpStatus.NOT_FOUND);
        } catch (InvalidInputException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(Mono.empty(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.CREATED)
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @GetMapping
    public ResponseEntity<Flux<User>> list() {
        return new ResponseEntity<>(userService.list().map(User::new), HttpStatus.OK);
    }

}
