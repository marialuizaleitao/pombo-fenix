-- Inserts para a tabela de usuarios
INSERT INTO usuarios (id, nome, email, cpf, senha, foto_de_perfil, is_admin) VALUES
(1, 'Max Verstappen', 'max@verstappen.com', '123.456.789-00', 'redbull', 'https://example.com/max.jpg', false),
(44, 'Lewis Hamilton', 'lewis@hamilton.com', '234.567.890-01', 'mercedes', 'https://example.com/lewis.jpg', false),
(16, 'Charles Leclerc', 'charles@leclerc.com', '345.678.901-02', 'ferrari', 'https://example.com/charles.jpg', false),
(14, 'Fernando Alonso', 'fernando@alonso.com', '456.789.012-03', 'astonmartin', 'https://example.com/fernando.jpg', false),
(11, 'Sergio Perez', 'sergio@perez.com', '567.890.123-04', 'redbull', 'https://example.com/sergio.jpg', false),
(63, 'George Russell', 'george@russell.com', '678.901.234-05', 'mercedes', 'https://example.com/george.jpg', false),
(55, 'Carlos Sainz', 'carlos@sainz.com', '789.012.345-06', 'ferrari', 'https://example.com/carlos.jpg', false),
(4, 'Lando Norris', 'lando@norris.com', '890.123.456-07', 'mclaren', 'https://example.com/lando.jpg', false),
(81, 'Oscar Piastri', 'oscar@piastri.com', '901.234.567-08', 'mclaren', 'https://example.com/oscar.jpg', false);

-- Inserts para a tabela de pruus
INSERT INTO pruus (id, usuario_id, texto, imagem, quantidade_curtidas, quantidade_denuncias, bloqueado) VALUES
(1, (SELECT id FROM usuarios WHERE nome = 'Max Verstappen'), 'Nada como liderar de ponta a ponta!', 'https://example.com/pruu1.jpg', 120, 0, false),
(2, (SELECT id FROM usuarios WHERE nome = 'Lewis Hamilton'), 'As vitórias vêm com trabalho duro.', 'https://example.com/pruu2.jpg', 150, 0, false),
(3, (SELECT id FROM usuarios WHERE nome = 'Charles Leclerc'), 'Lutando até o final!', 'https://example.com/pruu3.jpg', 98, 0, false),
(4, (SELECT id FROM usuarios WHERE nome = 'Fernando Alonso'), 'A experiência é o maior trunfo.', 'https://example.com/pruu4.jpg', 130, 1, false),
(5, (SELECT id FROM usuarios WHERE nome = 'Sergio Perez'), 'Sempre em busca da melhor estratégia.', 'https://example.com/pruu5.jpg', 110, 0, false),
(6, (SELECT id FROM usuarios WHERE nome = 'George Russell'), 'Subindo na hierarquia!', 'https://example.com/pruu6.jpg', 90, 1, false),
(7, (SELECT id FROM usuarios WHERE nome = 'Carlos Sainz'), 'Correndo com o coração.', 'https://example.com/pruu7.jpg', 100, 0, false),
(8, (SELECT id FROM usuarios WHERE nome = 'Lando Norris'), 'Nunca subestime o inesperado.', 'https://example.com/pruu8.jpg', 85, 0, false),
(9, (SELECT id FROM usuarios WHERE nome = 'Oscar Piastri'), 'Um rookie, mas já com grande impacto!', 'https://example.com/pruu9.jpg', 95, 0, false);

-- Inserts para a tabela de denuncias
INSERT INTO denuncias (id, pruu_id, usuario_id, motivo, status) VALUES
(1, (SELECT id FROM pruus WHERE texto = 'A experiência é o maior trunfo.'), (SELECT id FROM usuarios WHERE nome = 'Lewis Hamilton'), 'DISCURSO_ODIO', 'PENDENTE'),
(2, (SELECT id FROM pruus WHERE texto = 'Subindo na hierarquia!'), (SELECT id FROM usuarios WHERE nome = 'Charles Leclerc'), 'SPAM', 'PENDENTE'),
(3, (SELECT id FROM pruus WHERE texto = 'Consistência é a chave.'), (SELECT id FROM usuarios WHERE nome = 'George Russell'), 'CONTEUDO_INAPROPRIADO', 'PENDENTE'),
(4, (SELECT id FROM pruus WHERE texto = 'Consistência é a chave.'), (SELECT id FROM usuarios WHERE nome = 'Oscar Piastri'), 'SPAM', 'PENDENTE'),
(5, (SELECT id FROM pruus WHERE texto = 'Nada como liderar de ponta a ponta!'), (SELECT id FROM usuarios WHERE nome = 'Carlos Sainz'), 'CONTEUDO_INAPROPRIADO', 'PENDENTE');
