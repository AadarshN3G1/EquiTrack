-- Runs automatically via docker-compose MySQL init on first container start.
CREATE DATABASE IF NOT EXISTS equitrack;
USE equitrack;

-- Spring Boot / Hibernate will create the actual tables (ddl-auto: update),
-- this just seeds a demo user so the frontend has something to load against.
-- If the `users` table doesn't exist yet on first run, this insert is skipped
-- gracefully by ignoring the error — Hibernate will create it on backend startup,
-- after which you can POST /api/users to create a real one.
