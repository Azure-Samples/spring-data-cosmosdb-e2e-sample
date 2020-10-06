package com.azure.samples;

import com.azure.samples.models.User;
import com.azure.samples.repository.ReactiveUserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.azure.backend", "com.azure.autoconfigure"})
public class  ReactiveCrudCosmosDbApplication implements CommandLineRunner {

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveCrudCosmosDbApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        User testUser1 = new User("testId1", "testFirstName", "testLastName1");
        User testUser2 = new User("testId2", "testFirstName", "testLastName2");

        reactiveUserRepository.deleteAll();

        reactiveUserRepository.save(testUser1).flatMap(
                user -> {
                    log.info("Saving user : {}",  user);
                    return Mono.just(user);
                }).subscribe();

        reactiveUserRepository.save(testUser2).flatMap(
                user -> {
                    log.info("Saving user : {}",  user);
                    return Mono.just(user);
                }).subscribe();

        Flux<User> users = reactiveUserRepository.findByFirstName("testFirstName");
        users.map(u -> {
            log.info("findByFirstName() : {}", u);
            return u;
        }).subscribe();

        Flux<User> finalUser = reactiveUserRepository.getUsersByIdndLast(
                testUser1.getId(),testUser1.getLastName());

        finalUser.map(u -> {
            log.info("getUsersByIdndLast(): {}" , u);
            return u;
        }).subscribe();

    }

}
