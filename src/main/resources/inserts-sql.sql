-- Usuários
INSERT INTO usuario (id, nome, email, cpf, senha, foto_de_perfil, perfil, is_admin, criado_em)
VALUES 
(1, 'Max Verstappen', 'max@verstappen.com', '111.111.111-11', 'redbull123', 'https://example.com/max.jpg', 'USUARIO', false, NOW()),
(44, 'Lewis Hamilton', 'lewis@hamilton.com', '222.222.222-22', 'mercedes123', 'https://example.com/lewis.jpg', 'USUARIO', false, NOW()),
(16, 'Charles Leclerc', 'charles@leclerc.com', '333.333.333-33', 'ferrari123', 'https://example.com/charles.jpg', 'USUARIO', false, NOW()),
(11, 'Sergio Perez', 'sergio@perez.com', '444.444.444-44', 'checo123', 'https://example.com/sergio.jpg', 'USUARIO', false, NOW()),
(14, 'Fernando Alonso', 'fernando@alonso.com', '555.555.555-55', 'aston123', 'https://example.com/fernando.jpg', 'USUARIO', false, NOW()),
(63, 'George Russell', 'george@russell.com', '666.666.666-66', 'gr63', 'https://example.com/george.jpg', 'USUARIO', false, NOW()),
(4, 'Lando Norris', 'lando@norris.com', '777.777.777-77', 'mclaren123', 'https://example.com/lando.jpg', 'USUARIO', false, NOW()),
(55, 'Carlos Sainz', 'carlos@sainz.com', '888.888.888-88', 'smoothoperator', 'https://example.com/carlos.jpg', 'USUARIO', false, NOW()),
(81, 'Oscar Piastri', 'oscar@piastri.com', '101.010.101-01', 'rookie123', 'https://example.com/oscar.jpg', 'USUARIO', false, NOW());

-- Pruus
INSERT INTO pruu (id, usuario_id, texto, imagem, quantidade_curtidas, quantidade_denuncias, criado_em, bloqueado)
VALUES 
(1, (SELECT id FROM usuario WHERE nome = 'Max Verstappen'), 'Vencemos mais uma! Red Bull está imbatível.', 'https://example.com/pruu1.jpg', 150, 0, NOW(), false),
(2, (SELECT id FROM usuario WHERE nome = 'Lewis Hamilton'), 'Ainda há muito pela frente. A luta continua.', 'https://example.com/pruu2.jpg', 120, 2, NOW(), false),
(3, (SELECT id FROM usuario WHERE nome = 'Charles Leclerc'), 'Foi um dia difícil, mas seguimos em frente. Forza Ferrari!', 'https://example.com/pruu3.jpg', 80, 1, NOW(), false),
(4, (SELECT id FROM usuario WHERE nome = 'Sergio Perez'), 'Feliz por representar o México no pódio mais uma vez!', 'https://example.com/pruu4.jpg', 90, 0, NOW(), false),
(5, (SELECT id FROM usuario WHERE nome = 'Fernando Alonso'), 'Experiência faz a diferença! Que corrida!', 'https://example.com/pruu5.jpg', 110, 0, NOW(), false),
(6, (SELECT id FROM usuario WHERE nome = 'George Russell'), 'Ainda aprendendo, mas confiante no futuro!', 'https://example.com/pruu6.jpg', 70, 0, NOW(), false),
(7, (SELECT id FROM usuario WHERE nome = 'Lando Norris'), 'Grande performance da McLaren. Vamos com tudo!', 'https://example.com/pruu7.jpg', 100, 0, NOW(), false),
(8, (SELECT id FROM usuario WHERE nome = 'Carlos Sainz'), 'Um pódio inesquecível. Muito obrigado à equipe.', 'https://example.com/pruu8.jpg', 95, 0, NOW(), false),
(9, (SELECT id FROM usuario WHERE nome = 'Oscar Piastri'), 'Primeira corrida incrível! Estou muito animado.', 'https://example.com/pruu10.jpg', 60, 0, NOW(), false);

-- Denúncias
INSERT INTO denuncia (id, pruu_id, usuario_id, motivo, status, criado_em)
VALUES 
(1, (SELECT id FROM pruu WHERE texto = 'Ainda há muito pela frente. A luta continua.'), (SELECT id FROM usuario WHERE nome = 'Max Verstappen'), 'SPAM', 'PENDENTE', NOW()),
(2, (SELECT id FROM pruu WHERE texto = 'Foi um dia difícil, mas seguimos em frente. Forza Ferrari!'), (SELECT id FROM usuario WHERE nome = 'George Russell'), 'CONTEUDO_INAPROPRIADO', 'PENDENTE', NOW()),
(3, (SELECT id FROM pruu WHERE texto = 'Corrida emocionante, obrigado aos fãs pelo apoio!'), (SELECT id FROM usuario WHERE nome = 'Lewis Hamilton'), 'DISCURSO_ODIO', 'PENDENTE', NOW()),
(4, (SELECT id FROM pruu WHERE texto = 'Primeira corrida incrível! Estou muito animado.'), (SELECT id FROM usuario WHERE nome = 'Charles Leclerc'), 'SPAM', 'PENDENTE', NOW()),
(5, (SELECT id FROM pruu WHERE texto = 'Feliz por representar o México no pódio mais uma vez!'), (SELECT id FROM usuario WHERE nome = 'Fernando Alonso'), 'CONTEUDO_INAPROPRIADO', 'PENDENTE', NOW());
