package hu.gyoridenes.alkatresz_raktar.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Ezzel fogjuk titkosítani a jelszavakat (Bcrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // A webes kapuőr beállításai
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // A H2 konzol miatt kell
                .authorizeHttpRequests(auth -> auth
                        // Mik azok az oldalak, amiket belépés nélkül is látni kell?
                        .requestMatchers("/login.html", "/register.html", "/api/register", "/style.css").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/parts/*/sell").authenticated()

                        // ADMIN joghoz kötött műveletek (Adatbázis módosítása és az admin oldal)
                        .requestMatchers("/admin.html").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/parts", "/api/categories").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/parts/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/parts/**", "/api/categories/**").hasRole("ADMIN")

                        // Minden más (pl. index.html, vásárlás, listázás) elérhető a sima belépett usereknek is
                        .anyRequest().authenticated()
                )
                // Hagyományos jelszavas belépés a saját formunkkal
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/index.html", true) // Belépés után a vásárlói térbe visz
                        .permitAll()
                )
                // Google OAuth2 belépés
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login.html") // Ha nincs belépve a user, a saját szép login oldalunkra dobja, ne a Spring csúnya alapértelmezettjére
                        .defaultSuccessUrl("/index.html", true)
                )
                // Kijelentkezés
                .logout(logout -> logout
                        .logoutSuccessUrl("/login.html?logout")
                        .permitAll()
                );

        return http.build();
    }
}