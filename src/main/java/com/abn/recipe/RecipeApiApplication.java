package com.abn.recipe;

import com.abn.recipe.entity.User;
import com.abn.recipe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@EnableSwagger2
@SpringBootApplication
@ComponentScan("com.abn.*")
@EntityScan("com.abn.*")
@EnableJpaRepositories("com.abn.*")
public class RecipeApiApplication implements WebMvcConfigurer {

    @Autowired
    UserRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @PostConstruct
    public void initialUsers(){

        this.users.save(User.builder()
                        .id(1L)
                .username("user")
                .password(this.passwordEncoder.encode("@#:OJFL:OI:#J@#@#:IJ#@#OJ#"))
                .roles(Arrays.asList( "ROLE_USER"))
                .build()
        );
        this.users.save(User.builder()
                .id(2L)
                .username("admin")
                .password(this.passwordEncoder.encode("@#:OJFL:OI:#J@#@#:IJ#@#OJ#"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );

        this.users.findAll().forEach(v -> System.out.println(" User :" + v.toString()));
    }

	public static void main(String[] args) {
		SpringApplication.run(RecipeApiApplication.class, args);
	}

}
