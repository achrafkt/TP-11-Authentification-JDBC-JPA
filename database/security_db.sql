-- Etape 2 : creation de la base
CREATE DATABASE IF NOT EXISTS security_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE security_db;

-- Les tables (user, role, user_roles) sont creees automatiquement par Hibernate
-- grace a spring.jpa.hibernate.ddl-auto=update.

-- Etape 8 : requetes de verification apres le premier demarrage
-- SHOW TABLES;
-- SELECT id, username, password, active FROM user;   -- mots de passe en $2a$...
-- SELECT * FROM role;
-- SELECT * FROM user_roles;
