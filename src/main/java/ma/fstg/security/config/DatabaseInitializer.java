package ma.fstg.security.config;

import ma.fstg.security.entities.Role;
import ma.fstg.security.entities.User;
import ma.fstg.security.repositories.RoleRepository;
import ma.fstg.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            // Avec ddl-auto=update les données persistent entre deux démarrages :
            // on ne crée les rôles / utilisateurs que s'ils n'existent pas encore.
            Role adminRole = getOrCreateRole(roleRepo, "ROLE_ADMIN");
            Role userRole = getOrCreateRole(roleRepo, "ROLE_USER");

            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(new User(null, "admin", encoder.encode("1234"), true,
                        new ArrayList<>(List.of(adminRole, userRole))));
            }
            if (userRepo.findByUsername("user").isEmpty()) {
                userRepo.save(new User(null, "user", encoder.encode("1111"), true,
                        new ArrayList<>(List.of(userRole))));
            }
        };
    }

    private Role getOrCreateRole(RoleRepository roleRepo, String name) {
        Role role = roleRepo.findByName(name);
        return role != null ? role : roleRepo.save(new Role(null, name));
    }
}
