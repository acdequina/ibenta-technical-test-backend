package au.com.ibenta.template;

import au.com.ibenta.test.model.User;
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
        User mockUser = new User("Klee", "Kloo", "kleekloo@email.com", "1234");
        webTestClient.post().uri("/user/create")
                .body(Mono.just(mockUser), UserEntity.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(mockUser.getFirstName())
                .jsonPath("$.lastName").isEqualTo(mockUser.getLastName())
                .jsonPath("$.email").isEqualTo(mockUser.getEmail())
                .jsonPath("$.password").isEqualTo(null);

        mockUser.setEmail("kleekloo@email");
        webTestClient.post().uri("/user/create")
                .body(Mono.just(mockUser), UserEntity.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void getTest() {
        webTestClient.get().uri("/user/"+user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(user.getFirstName())
                .jsonPath("$.lastName").isEqualTo(user.getLastName())
                .jsonPath("$.email").isEqualTo(user.getEmail())
                .jsonPath("$.password").isEqualTo(null);

        webTestClient.get().uri("/user/"+user.getId()+1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateTest() {
        user.setFirstName("Johny");
        user.setLastName("Smithy");
        user.setEmail("johnysmithy@email.com");
        user.setPassword("5467");
        webTestClient.put().uri("/user/update")
                .body(Mono.just(user), UserEntity.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(user.getFirstName())
                .jsonPath("$.lastName").isEqualTo(user.getLastName())
                .jsonPath("$.email").isEqualTo(user.getEmail())
                .jsonPath("$.password").isEqualTo(null);

        user.setEmail("johnsmith@email");
        webTestClient.post().uri("/user/create")
                .body(Mono.just(user), UserEntity.class)
                .exchange()
                .expectStatus().isBadRequest();
        user.setEmail("johnsmith@email.com");

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
