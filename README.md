# Urbanlink — API REST (Sprint 4)

Disciplina: **Domain Driven Design Using Java** — Challenge 2026 (FIAP, 1TDS)
Equipe: **Urbanlink**

Módulo da plataforma **SoulUp** que converte pontos acumulados por ações sustentáveis em créditos (vouchers) de transporte público e mede o impacto ambiental (CO2 evitado).

## Integrantes

- Felipe Falcão Ferreira
- Felipe Fernandes Cabral Mota
- Rafael Andres da Silva
- Vitor Reis Souza
- Vitor Rodrigues Ferreira

## Links da entrega

| Item | Link |
|---|---|
| Deploy da API (Render) | https://urbanlink-sboot.onrender.com |
| Repositório | https://github.com/Felipecmf/Urbanlink_sboot |
| Documentação (PDF) | [Urbanlink_DDD_Sprint4.pdf](Urbanlink_DDD_Sprint4.pdf) |
| Código-fonte (.zip) | [urbanlink-api.zip](urbanlink-api.zip) |

> O serviço usa o plano gratuito do Render: após um período sem acesso, a primeira requisição pode levar cerca de 1 minuto.
> Teste rápido: abra https://urbanlink-sboot.onrender.com/usuarios (deve retornar uma lista em JSON).

## Conteúdo do repositório

```
├── README.md
├── Urbanlink_DDD_Sprint4.pdf   documentação final
├── urbanlink-api.zip           código-fonte Java (projeto compactado)
└── urbanlink-api/              projeto Maven (usado no deploy)
    ├── pom.xml
    ├── Dockerfile
    ├── database/schema.sql     script de criação das tabelas (Oracle)
    └── src/main/java/br/com/urbanlink/
        ├── controller/         Resource: endpoints REST
        ├── service/            BO: regras de negócio e validações
        ├── dominio/            entidades e interfaces Repositorio*
        ├── infra/dao/          DAOs (JDBC) e ConnectionFactory
        ├── dto/                objetos de requisição
        ├── util/               Validador e Numeros
        └── config/             CorsFilter
```

## Tecnologias

Java 25 · Spring Boot 4.1.2 · Maven · Oracle (JDBC) · Docker · Render

## Endpoints

| Recurso | Operações |
|---|---|
| `/usuarios` | GET, GET `/{id}`, POST, POST `/premium`, PUT `/{id}`, PUT `/premium/{id}`, DELETE `/{id}`, POST `/{id}/pontos` |
| `/transportes` | GET, GET `/{id}`, POST, POST `/integrados`, PUT `/{id}`, PUT `/integrados/{id}`, DELETE `/{id}` |
| `/vouchers` | GET, GET `/usuario/{usuarioId}`, GET `/{codigo}`, POST, PUT `/{codigo}/utilizar`, PUT `/{codigo}` (status), DELETE `/{codigo}` |
| `/impactos` | GET, GET `/{usuarioId}`, POST, PUT `/{usuarioId}`, DELETE `/{usuarioId}` |

Status retornados: 200, 201, 204, 400, 404 e 500. A tabela completa está na documentação (PDF).

## Como executar localmente

1. Instale o **JDK 25** e o **IntelliJ IDEA** (o Maven já vem integrado).
2. Abra a pasta `urbanlink-api` (a que contém o `pom.xml`) e aguarde o Maven baixar as dependências.
3. Execute `database/schema.sql` no banco Oracle para criar as tabelas.
4. Defina as variáveis de ambiente em *Run > Edit Configurations > Environment variables*:

   | Variável | Descrição | Exemplo |
   |---|---|---|
   | `DB_URL` | URL JDBC do Oracle (opcional, tem valor padrão) | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
   | `DB_USER` | Usuário do banco | `RMxxxxxx` |
   | `DB_PASSWORD` | Senha do banco | *(senha pessoal)* |
   | `CORS_ORIGIN` | Origem(ns) do front-end permitidas, separadas por vírgula | `http://localhost:5173` |

   As credenciais não ficam no código.
5. Execute a classe `br.com.urbanlink.UrbanlinkApplication`.
6. Acesse http://localhost:8080/usuarios.

Testes unitários: `mvn test`.

## Deploy

A API é publicada no **Render** a partir do `Dockerfile` (Root Directory `urbanlink-api`), com as mesmas variáveis de ambiente configuradas no painel do serviço. O `.jar` em `target/` fica versionado, pois o `Dockerfile` o copia para a imagem.

## Regras de negócio principais

- 10 pontos equivalem a R$ 1,00.
- Usuários Premium têm multiplicador de bônus e limite mensal de resgates.
- Antifraude: no máximo 5 vouchers por usuário por dia; o voucher só pode ser usado pelo titular.
- Cancelar um voucher devolve os pontos ao usuário.
- Impacto ambiental: cada árvore equivale a 22 kg de CO2 por ano.

## Integração com o front-end

O front-end (React + Vite + TypeScript) consome esta API por requisições HTTP. Origem do front liberada via `CORS_ORIGIN`.
