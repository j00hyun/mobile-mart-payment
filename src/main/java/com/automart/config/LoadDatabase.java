package com.automart.config;

import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.product.domain.Product;
import com.automart.product.repository.ProductRepository;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
@Transactional
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            /*// User 생성
            log.info("Preloading" + userRepository.save(
                    User.builder()
                            .email("example@google.com")
                            .password("example")
                            .name("김떡순")
                            .tel("01012345678")
                            .snsType(AuthProvider.local)
                            .build()
            ));*/

            // Category 생성
            log.info("Preloading" + categoryRepository.save(
                    Category.builder()
                            .name("과일")
                            .build()
            ));

            // Product 생성
            log.info("Preloading" + productRepository.save(
                    Product.builder()
                            .name("사과")
                            .category(categoryRepository.findByName("과일").get())
                            .code(12345678)
                            .price(2000)
                            .cost(1000)
                            .stock(10)
                            .location("3번")
                            .imgUrl("exampleUrl")
                            .build()
            ));
        };
    }
}
