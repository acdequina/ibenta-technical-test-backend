package au.com.ibenta.template;

import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        UserEntity userJohn = new UserEntity("John", "Smith", "johnsmith@email.com", "1234");
        user = userRepository.save(userJohn);
    }

    @Test
    public void createTest() {
        UserEntity userEntity = new UserEntity("Klee", "Kloo", "kleekloo@email.com", "1234");
        webTestClient.post().uri("/user/create")
                .body(Mono.just(userEntity), UserEntity.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("Klee")
                .jsonPath("$.lastName").isEqualTo("Kloo")
                .jsonPath("$.email").isEqualTo("kleekloo@email.com")
                .jsonPath("$.password").isEqualTo("1234");
    }

    @Test
    public void getTest() {
        webTestClient.get().uri("/user/"+user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("John");

        webTestClient.get().uri("/user/"+user.getId()+1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateTest() {
        user.setLastName("Smithy");
        webTestClient.put().uri("/user/update")
                .body(Mono.just(user), UserEntity.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.lastName").isEqualTo("Smithy");

        user.setId(user.getId()+1);
        webTestClient.put().uri("/user/update")
                .body(Mono.just(user), UserEntity.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void deleteTest() {
        webTestClient.delete().uri("/user/delete/"+user.getId())
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get().uri("/user/"+user.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void listTest() {
        Flux<UserEntity> userFlux = webTestClient.get().uri("/user/")
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserEntity.class)
                .getResponseBody();

        StepVerifier.create(userFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

}
