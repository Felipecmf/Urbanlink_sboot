-- UrbanLink - criação das tabelas (Oracle). Os nomes de colunas devem bater com os DAOs.

CREATE TABLE usuario (
    id                  VARCHAR2(20)   PRIMARY KEY,
    nome                VARCHAR2(100)  NOT NULL,
    email               VARCHAR2(120)  NOT NULL UNIQUE,
    cpf                 VARCHAR2(11)   NOT NULL UNIQUE,
    saldo_pontos        NUMBER(12,2)   DEFAULT 0 NOT NULL CHECK (saldo_pontos >= 0),
    tipo                VARCHAR2(10)   DEFAULT 'COMUM' NOT NULL CHECK (tipo IN ('COMUM','PREMIUM')),
    multiplicador_bonus NUMBER(4,2),
    limite_resgates     NUMBER(3),
    resgates_mes        NUMBER(3)
);

CREATE TABLE transporte (
    id                  VARCHAR2(20)   PRIMARY KEY,
    nome                VARCHAR2(100)  NOT NULL,
    modalidade          VARCHAR2(50)   NOT NULL,
    tarifa_base         NUMBER(8,2)    NOT NULL CHECK (tarifa_base > 0),
    co2_evitado         NUMBER(8,3)    NOT NULL CHECK (co2_evitado >= 0),
    integrado           NUMBER(1)      DEFAULT 0 NOT NULL,
    sistema_bilhetagem  VARCHAR2(60),
    total_viagens       NUMBER(10)     DEFAULT 0 NOT NULL,
    aceita_voucher      NUMBER(1)      DEFAULT 0 NOT NULL
);

CREATE TABLE voucher (
    codigo         VARCHAR2(20)  PRIMARY KEY,
    usuario_id     VARCHAR2(20)  NOT NULL REFERENCES usuario (id),
    transporte_id  VARCHAR2(20)  NOT NULL REFERENCES transporte (id),
    valor_credito  NUMBER(8,2)   NOT NULL,
    pontos_usados  NUMBER(12,2)  NOT NULL,
    status         VARCHAR2(10)  NOT NULL CHECK (status IN ('ATIVO','UTILIZADO','EXPIRADO','CANCELADO')),
    data_geracao   TIMESTAMP     NOT NULL,
    antifraude     NUMBER(1)     DEFAULT 1 NOT NULL
);

CREATE TABLE impacto_ambiental (
    usuario_id        VARCHAR2(20)  PRIMARY KEY REFERENCES usuario (id) ON DELETE CASCADE,
    total_co2         NUMBER(12,3)  DEFAULT 0 NOT NULL,
    total_viagens     NUMBER(10)    DEFAULT 0 NOT NULL,
    equiv_arvores     NUMBER(10,2)  DEFAULT 0 NOT NULL,
    pontos_investidos NUMBER(14,2)  DEFAULT 0 NOT NULL
);

CREATE INDEX idx_voucher_usuario ON voucher (usuario_id, data_geracao);
