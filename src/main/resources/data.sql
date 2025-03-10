INSERT INTO `users` (id, `created_at`, email, password, first_name, last_name, role)
SELECT 1, CURRENT_TIMESTAMP, 'admin@gmail.com', '$2b$12$cVmV3NHc.ooMEgYTuaaGjuJJeBvOU1g4pMERdZAPrJRF61yYvOpKq', 'super', 'user', 'ROLE_ADMIN'
    WHERE NOT EXISTS (SELECT * FROM `users` WHERE `email`='admin@gmail.com');