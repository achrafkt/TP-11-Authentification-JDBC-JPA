# TP 11 – Authentification JDBC / JPA avec Spring Security

Ce TP remplace l'authentification en mémoire (`InMemoryUserDetailsManager`) par une authentification qui lit les utilisateurs et leurs rôles dans une base **MySQL**, via **Spring Data JPA**. Les mots de passe sont hachés avec **BCrypt**.

## Technologies

- Java 17+ et Spring Boot 3.5 (Spring Security 6)
- Spring Data JPA / Hibernate et MySQL
- Thymeleaf et Lombok

## Structure

```
src/main/java/ma/fstg/security
├── entities/          User, Role (relation @ManyToMany EAGER)
├── repositories/      UserRepository, RoleRepository
├── services/          CustomUserDetailsService (implémente UserDetailsService)
├── config/            SecurityConfig (BCrypt, DaoAuthenticationProvider, règles d'accès)
│                      DatabaseInitializer (crée admin/user au démarrage)
└── web/               AuthController (/login, /register), HomeController (/home, dashboards)
src/main/resources
├── application.properties
├── templates/         login, register, home, admin/dashboard, user/dashboard
└── static/css/        style.css
database/security_db.sql
```

## Lancer le projet

1. Démarrer MySQL, par exemple via XAMPP ou WAMP, avec l'utilisateur `root` sans mot de passe. Si ton mot de passe est différent, modifie `spring.datasource.password` dans `application.properties`.
2. Créer la base avec `database/security_db.sql`. L'URL JDBC contient aussi `createDatabaseIfNotExist=true`, donc la base est créée automatiquement si elle n'existe pas.
3. Lancer l'application :
   ```bash
   ./mvnw spring-boot:run        # Linux / macOS / Git Bash
   mvnw.cmd spring-boot:run      # Windows
   ```
4. Ouvrir http://localhost:8080/login

## Comptes de test

| Utilisateur | Mot de passe | Rôles                  | Accès                                 |
|-------------|--------------|------------------------|---------------------------------------|
| admin       | 1234         | ROLE_ADMIN, ROLE_USER  | `/admin/dashboard`, `/user/dashboard` |
| user        | 1111         | ROLE_USER              | `/user/dashboard` (403 sur `/admin/**`) |

Tu peux aussi créer un nouveau compte (avec le rôle `ROLE_USER`) depuis `/register`.

## Vérification dans MySQL

```sql
USE security_db;
SHOW TABLES;                 -- user, role, user_roles
SELECT username, password FROM user;   -- les mots de passe commencent par $2a$
```

## Notes

- `DatabaseInitializer` ne crée les rôles et les utilisateurs que s'ils n'existent pas encore. Avec `ddl-auto=update`, les données sont conservées d'un démarrage à l'autre. Sans cette vérification, chaque redémarrage créerait des doublons et `findByUsername` échouerait.
- `DaoAuthenticationProvider` reçoit le `UserDetailsService` dans son constructeur. C'est la forme recommandée depuis Spring Security 6.3, car le constructeur vide et `setUserDetailsService()` sont dépréciés.
