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
    (1, 'Admin'),
    (2, 'User');

INSERT INTO Usuarios(login_name, salt, hash, cert, blk, grupo, unome)
VALUES 
    (
        'admin@inf1416.puc-rio.br',
        'XNBN88HP0P',
        'd3b2356a06544217b08c648d323b05eaccad5001',
        '24694716545896684029731904705327195985803679144228363491954477527965873183298008253342044909666678928652621870609847876094321918377736625397328779718636360329186588290565930395204711815558484311767794069673007474348582406231978151443386977502716889769008702219904513138015776347787820237315608190861564584447164485959753984940111737205571665450227347732963007698112580965846382037684936612460625547568778761106262868891235444910927914534830769251937491629784509624487023783492555209684120802640557526145055204013599100877237609938048819675715429998542683158060641181167013805793301541290463731752373194150117051635613',
        0,
        1,
        'Admin'
    )
    ;

INSERT INTO Mensagens(id, mensagem)
VALUES
    (1001, 'Sistema iniciado.'),
    (1002, 'Sistema encerrado.'),
    (2001, 'Autenticação etapa 1 iniciada.'),
    (2002, 'Autenticação etapa 1 encerrada.'),
    (2003, 'Login name <login_name> identificado com acesso liberado.'),
    (2004, 'Login name <login_name> identificado com acesso bloqueado.'),
    (2005, 'Login name <login_name> não identificado.'),
    (3001, 'Autenticação etapa 2 iniciada para <login_name>.'),
    (3002, 'Autenticação etapa 2 encerrada para <login_name>.'),
    (3003, 'Senha pessoal verificada positivamente para <login_name>.'),
    (3004, 'Primeiro erro da senha pessoal contabilizado para <login_name>.'),
    (3005, 'Segundo erro da senha pessoal contabilizado para <login_name>.'),
    (3006, 'Terceiro erro da senha pessoal contabilizado para <login_name>.'),
    (3007, 'Acesso do usuario <login_name> bloqueado pela autenticação etapa 2.'),
    (4001, 'Autenticação etapa 3 iniciada para <login_name>.'),
    (4002, 'Autenticação etapa 3 encerrada para <login_name>.'),
    (4003, 'Chave privada verificada positivamente para <login_name>.'),
    (4004, 'Chave privada verificada negativamente para <login_name> (caminho inválido).'),
    (4005, 'Chave privada verificada negativamente para <login_name> (frase secreta inválida).'),
    (4006, 'Chave privada verificada negativamente para <login_name> (assinatura digital inválida).'),
    (4007, 'Acesso do usuario <login_name> bloqueado pela autenticação etapa 3.'),
    (5001, 'Tela principal apresentada para <login_name>.'),
    (5002, 'Opção 1 do menu principal selecionada por <login_name>.'),
    (5003, 'Opção 2 do menu principal selecionada por <login_name>.'),
    (5004, 'Opção 3 do menu principal selecionada por <login_name>.'),
    (5005, 'Opção 4 do menu principal selecionada por <login_name>.'),
    (6001, 'Tela de cadastro apresentada para <login_name>.'),
    (6002, 'Botão cadastrar pressionado por <login_name>.'),
    (6003, 'Senha pessoal inválida fornecida por <login_name>.'),
    (6004, 'Caminho do certificado digital inválido fornecido por <login_name>.'),
    (6005, 'Confirmação de dados aceita por <login_name>.'),
    (6006, 'Confirmação de dados rejeitada por <login_name>.'),
    (6007, 'Botão voltar de cadastro para o menu principal pressionado por <login_name>.'),
    (7001, 'Tela de alteração da senha pessoal e certificado apresentada para <login_name>.'),
    (7002, 'Senha pessoal inválida fornecida por <login_name>.'),
    (7003, 'Caminho do certificado digital inválido fornecido por <login_name>.'),
    (7004, 'Confirmação de dados aceita por <login_name>.'),
    (7005, 'Confirmação de dados rejeitada por <login_name>.'),
    (7006, 'Botão voltar de carregamento para o menu principal pressionado por <login_name>.'),
    (8001, 'Tela de consulta de arquivos secretos apresentada para <login_name>.'),
    (8002, 'Botão voltar de consulta para o menu principal pressionado por <login_name>.'),
    (8003, 'Botão Listar de consulta pressionado por <login_name>.'),
    (8004, 'Caminho de pasta inválido fornecido por <login_name>.'),
    (8005, 'Arquivo de índice decriptado com sucesso para <login_name>.'),
    (8006, 'Arquivo de índice verificado (integridade e autenticidade) com sucesso para <login_name>.'),
    (8007, 'Falha na decriptação do arquivo de índice para <login_name>.'),
    (8008, 'Falha na verificação (integridade e autenticidade) do arquivo de índice para <login_name>.'),
    (8009, 'Lista de arquivos presentes no índice apresentada para <login_name>.'),
    (8010, 'Arquivo <arq_name> selecionado por <login_name> para decriptação.'),
    (8011, 'Acesso permitido ao arquivo <arq_name> para <login_name>.'),
    (8012, 'Acesso negado ao arquivo <arq_name> para <login_name>.'),
    (8013, 'Arquivo <arq_name> decriptado com sucesso para <login_name>.'),
    (8014, 'Arquivo <arq_name> verificado (integridade e autenticidade) com sucesso para <login_name>.'),
    (8015, 'Falha na decriptação do arquivo <arq_name> para <login_name>.'),
    (8016, 'Falha na verificação (integridade e autenticidade) do arquivo <arq_name> para <login_name>.'),
    (9001, 'Tela de saída apresentada para <login_name>.'),
    (9002, 'Saída não liberada por falta de one-time password para <login_name>.'),
    (9003, 'Botão sair pressionado por <login_name>.'),
    (9004, 'Botão voltar de sair para o menu principal pressionado por <login_name>.')
;
