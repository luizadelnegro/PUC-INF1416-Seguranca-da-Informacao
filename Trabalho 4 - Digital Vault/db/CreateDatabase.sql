CREATE TABLE Usuarios(
    login_name varchar(200),
    salt varchar(200),
    hash varchar(200),
    cert blob,
    blk integer,
    grupo integer NOT NULL,
    unome varchar(200) NOT NULL,
    PRIMARY KEY (login_name)
);

CREATE TABLE Grupos(
    gid integer NOT NULL auto_increment,
    nome varchar(200),
    PRIMARY KEY (gid)
);

CREATE TABLE Mensagens(
    id integer NOT NULL,
    mensagem varchar(200),
    PRIMARY KEY (id)
);

CREATE TABLE Registros(
    id integer NOT NULL auto_increment,
    mensagem_id integer NOT NULL,
    dttime datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    login_name varchar(200),
    arq_name varchar(200),
    PRIMARY KEY (id)
);

SET character_set_client = utf8;
SET character_set_connection = utf8;
SET character_set_results = utf8;
SET collation_connection = utf8_general_ci;
SET GLOBAL time_zone = '-3:00';

INSERT INTO Grupos (gid, nome) 
VALUES 
    (1, 'administrator'),
    (2, 'usuario');

INSERT INTO Usuarios(login_name, salt, hash, cert, blk, grupo, unome)
VALUES 
    (
        'admin@inf1416.puc-rio.br',
        '6EU3IH4ANC',
        'fe698eb357498f188554cfde05eaedaafbf070ff',
        'MIID9jCCAt6gAwIBAgIBATANBgkqhkiG9w0BAQsFADCBhDELMAkGA1UEBhMCQlIxCzAJBgNVBAgMAlJKMQwwCgYDVQQHDANSaW8xDDAKBgNVBAoMA1BVQzEQMA4GA1UECwwHSU5GMTQxNjETMBEGA1UEAwwKQUMgSU5GMTQxNjElMCMGCSqGSIb3DQEJARYWY2FAZ3JhZC5pbmYucHVjLXJpby5icjAeFw0xOTA1MDMxNzE5MjhaFw0yMjA1MDIxNzE5MjhaMHsxCzAJBgNVBAYTAkJSMQswCQYDVQQIDAJSSjEMMAoGA1UECgwDUFVDMRAwDgYDVQQLDAdJTkYxNDE2MRYwFAYDVQQDDA1BZG1pbmlzdHJhdG9yMScwJQYJKoZIhvcNAQkBFhhhZG1pbkBpbmYxNDE2LnB1Yy1yaW8uYnIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDDnq2WpTioReNQ3EapxCdmUt9khsS2BHf/YB7tjGILCzQegnV1swvcH+xfd9FUjR7pORFSNvrfWKt93t3l2Dc0kCvVffh5BSnXIwwbW94O+E1Yp6pvpyflj8YI+VLy0dNCiszHAF5ux6lRZYcrM4KiJndqeFRnqRP8zWI5O1kJJMXzCqIXwmXtfqVjWiwXTnjU97xfQqKkmAt8Z+uxJaQxdZJBczmo/jQAIz1gx+SXA4TshU5Ra4sQYLo5+FgAfA2vswHGXA6ba3N52wydZ2IYUJL2/YmTyfxzRnsyuqbL+hcOw6bm+g0OEIIC7JduKpinz3BieiO15vameAJlqpedAgMBAAGjezB5MAkGA1UdEwQCMAAwLAYJYIZIAYb4QgENBB8WHU9wZW5TU0wgR2VuZXJhdGVkIENlcnRpZmljYXRlMB0GA1UdDgQWBBSeUNmquC0OBxDLGpUaDNxe1t2EADAfBgNVHSMEGDAWgBQjgTvDGSuVmdnK6jtr/hwkc8KCjjANBgkqhkiG9w0BAQsFAAOCAQEAYjji1ws77cw8uVhlUTkzVxyAaUKOgJx2zuvhR79MItH7L+7ocDrMB/tGCgoAhAM1gVeuyP2t0j9mmRuuFDEFvsFqmOoSDbLFkxr1G8StujUQDrLe+691qU5RNubP3XacRyPVTA1F/pSr/XUm4fymqDZyVcxqYPFewhQlL3VaD2bKeNWEAczgkOHkC3dDb9bCL4oDr1SsURKDWWg2XbZpuTO7IhxTYKwddKvsJTjizHIz6mi6JavHM7+xtB/ZvQaW04O9y5QI9EQPJsF3nybVNKWIR9UA4tWSfHmQ5J9cGk/bZBCqzvgmV8Wv7cMUB7q6mzGUP1a+HtNmSvQW9Uow3g==',
        0,
        1,
        'Administrator'
    )
    ;

INSERT INTO Mensagens(id, mensagem)
VALUES
    (1001, 'Sistema iniciado.'),
    (1002, 'Sistema encerrado.'),
    (2001, 'Autentica????o etapa 1 iniciada.'),
    (2002, 'Autentica????o etapa 1 encerrada.'),
    (2003, 'Login name <login_name> identificado com acesso liberado.'),
    (2004, 'Login name <login_name> identificado com acesso bloqueado.'),
    (2005, 'Login name <login_name> n??o identificado.'),
    (3001, 'Autentica????o etapa 2 iniciada para <login_name>.'),
    (3002, 'Autentica????o etapa 2 encerrada para <login_name>.'),
    (3003, 'Senha pessoal verificada positivamente para <login_name>.'),
    (3004, 'Primeiro erro da senha pessoal contabilizado para <login_name>.'),
    (3005, 'Segundo erro da senha pessoal contabilizado para <login_name>.'),
    (3006, 'Terceiro erro da senha pessoal contabilizado para <login_name>.'),
    (3007, 'Acesso do usuario <login_name> bloqueado pela autentica????o etapa 2.'),
    (4001, 'Autentica????o etapa 3 iniciada para <login_name>.'),
    (4002, 'Autentica????o etapa 3 encerrada para <login_name>.'),
    (4003, 'Chave privada verificada positivamente para <login_name>.'),
    (4004, 'Chave privada verificada negativamente para <login_name> (caminho inv??lido).'),
    (4005, 'Chave privada verificada negativamente para <login_name> (frase secreta inv??lida).'),
    (4006, 'Chave privada verificada negativamente para <login_name> (assinatura digital inv??lida).'),
    (4007, 'Acesso do usuario <login_name> bloqueado pela autentica????o etapa 3.'),
    (5001, 'Tela principal apresentada para <login_name>.'),
    (5002, 'Op????o 1 do menu principal selecionada por <login_name>.'),
    (5003, 'Op????o 2 do menu principal selecionada por <login_name>.'),
    (5004, 'Op????o 3 do menu principal selecionada por <login_name>.'),
    (5005, 'Op????o 4 do menu principal selecionada por <login_name>.'),
    (6001, 'Tela de cadastro apresentada para <login_name>.'),
    (6002, 'Bot??o cadastrar pressionado por <login_name>.'),
    (6003, 'Senha pessoal inv??lida fornecida por <login_name>.'),
    (6004, 'Caminho do certificado digital inv??lido fornecido por <login_name>.'),
    (6005, 'Confirma????o de dados aceita por <login_name>.'),
    (6006, 'Confirma????o de dados rejeitada por <login_name>.'),
    (6007, 'Bot??o voltar de cadastro para o menu principal pressionado por <login_name>.'),
    (7001, 'Tela de altera????o da senha pessoal e certificado apresentada para <login_name>.'),
    (7002, 'Senha pessoal inv??lida fornecida por <login_name>.'),
    (7003, 'Caminho do certificado digital inv??lido fornecido por <login_name>.'),
    (7004, 'Confirma????o de dados aceita por <login_name>.'),
    (7005, 'Confirma????o de dados rejeitada por <login_name>.'),
    (7006, 'Bot??o voltar de carregamento para o menu principal pressionado por <login_name>.'),
    (8001, 'Tela de consulta de arquivos secretos apresentada para <login_name>.'),
    (8002, 'Bot??o voltar de consulta para o menu principal pressionado por <login_name>.'),
    (8003, 'Bot??o Listar de consulta pressionado por <login_name>.'),
    (8004, 'Caminho de pasta inv??lido fornecido por <login_name>.'),
    (8005, 'Arquivo de ??ndice decriptado com sucesso para <login_name>.'),
    (8006, 'Arquivo de ??ndice verificado (integridade e autenticidade) com sucesso para <login_name>.'),
    (8007, 'Falha na decripta????o do arquivo de ??ndice para <login_name>.'),
    (8008, 'Falha na verifica????o (integridade e autenticidade) do arquivo de ??ndice para <login_name>.'),
    (8009, 'Lista de arquivos presentes no ??ndice apresentada para <login_name>.'),
    (8010, 'Arquivo <arq_name> selecionado por <login_name> para decripta????o.'),
    (8011, 'Acesso permitido ao arquivo <arq_name> para <login_name>.'),
    (8012, 'Acesso negado ao arquivo <arq_name> para <login_name>.'),
    (8013, 'Arquivo <arq_name> decriptado com sucesso para <login_name>.'),
    (8014, 'Arquivo <arq_name> verificado (integridade e autenticidade) com sucesso para <login_name>.'),
    (8015, 'Falha na decripta????o do arquivo <arq_name> para <login_name>.'),
    (8016, 'Falha na verifica????o (integridade e autenticidade) do arquivo <arq_name> para <login_name>.'),
    (9001, 'Tela de sa??da apresentada para <login_name>.'),
    (9002, 'Sa??da n??o liberada por falta de one-time password para <login_name>.'),
    (9003, 'Bot??o sair pressionado por <login_name>.'),
    (9004, 'Bot??o voltar de sair para o menu principal pressionado por <login_name>.')
;
