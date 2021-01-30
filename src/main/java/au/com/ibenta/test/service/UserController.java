package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<UserEntity>> create(@RequestBody UserEntity user) {
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<Mono<UserEntity>> get(@PathVariable("id") Long id) {
        try {
            Mono<UserEntity> monoUser = userService.list(id);
            return new ResponseEntity<>(monoUser, HttpStatus.OK);
        } catch (NotFoundException e) {
            //log error
            return new ResponseEntity<>(Mono.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mono<UserEntity>> update(@RequestBody UserEntity user) {
        try {
            userService.list(user.getId());
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        } catch (NotFoundException e) {
            //log error
            return new ResponseEntity<>(Mono.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.CREATED)
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @GetMapping
    public ResponseEntity<Flux<UserEntity>> list() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

}
