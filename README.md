# Barbearia API — Spring Boot

API REST em **Java 21 + Spring Boot 3** com PostgreSQL, pronta para o front-end React.

## Arquitetura

```
Controller (@RestController)  →  Service (@Service)  →  DAO (@Repository / JdbcTemplate)  →  PostgreSQL
```

- **Controller**: recebe HTTP, valida com `@Valid`, delega ao Service
- **Service**: regras de negócio, BCrypt nas senhas, transações (`@Transactional`)
- **DAO**: SQL com `JdbcTemplate`
- **ApiExceptionHandler**: converte exceções em respostas JSON (404, 409, 401, 400, 500)
- **CorsConfig**: libera o React (qualquer origem em dev)

## Como rodar

1. Ajuste usuário/senha do banco em `src/main/resources/application.properties`
2. Rode:

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080` (Tomcat embutido — não precisa instalar nada).

Para gerar o JAR: `mvn clean package` → `java -jar target/barbearia-api.jar`

## Endpoints

### Autenticação (JWT)
| Método | Rota | Body |
|---|---|---|
| POST | `/auth/login` | `{ "email", "senha", "tipo": "cliente" \| "funcionario" }` |

Resposta de sucesso (200):
```json
{ "token": "eyJhbGci...", "tipo": "cliente", "id": 30, "nome": "kidddd", "email": "..." }
```

O front guarda o `token` e o envia nas próximas requisições no cabeçalho:
```
Authorization: Bearer <token>
```

**Rotas públicas** (não exigem token): `POST /auth/login` e `POST /api/clientes` (cadastro).
**Todas as demais** exigem o cabeçalho `Authorization`. Sem token válido → `401`:
```json
{ "erro": "Token ausente ou inválido. Faça login em /auth/login." }
```

O token expira em 24h (configurável em `jwt.expiration`). A chave de assinatura
fica em `jwt.secret` no `application.properties` — troque por um valor forte em produção.

### Clientes
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/clientes` | Lista todos |
| GET | `/api/clientes/{id}` | Busca por ID |
| POST | `/api/clientes` | Cadastra `{ "nome", "email", "senha" }` |
| PUT | `/api/clientes/{id}` | Atualiza (senha opcional) |
| DELETE | `/api/clientes/{id}` | Remove |

### Funcionários
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/funcionarios` | Lista todos |
| GET | `/api/funcionarios/{id}` | Busca por ID |
| POST | `/api/funcionarios` | Cadastra `{ "nome_funcionario", "email", "senha", "imagem" }` |
| PUT | `/api/funcionarios/{id}` | Atualiza |
| DELETE | `/api/funcionarios/{id}` | Remove |

### Serviços
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/servicos` | Lista todos |
| GET | `/api/servicos/{id}` | Busca por ID |
| POST | `/api/servicos` | Cadastra `{ "servico", "preco" }` |
| PUT | `/api/servicos/{id}` | Atualiza |
| DELETE | `/api/servicos/{id}` | Remove |

### Agendamentos (tb_agendados)
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/agendamentos` | Lista (filtros: `?funcionario=` / `?usuario=`) |
| GET | `/api/agendamentos/{id}` | Busca por ID |
| POST | `/api/agendamentos` | Cria `{ "usuario", "funcionario", "servicos", "horario" }` |
| PUT | `/api/agendamentos/{id}` | Atualiza |
| PUT | `/api/agendamentos/{id}/desmarcar` | Move para `desmarcados` (transacional) |
| PUT | `/api/agendamentos/{id}/concluir` | Move para `atendidos` (transacional) |
| DELETE | `/api/agendamentos/{id}` | Remove sem histórico |

### Desmarcados
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/desmarcados` | Lista (filtro: `?funcionario=`) |
| PUT | `/api/desmarcados/{id}/visto` | Marca como visto (`views = 1`) |
| DELETE | `/api/desmarcados/{id}` | Remove |

### Atendidos
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/atendidos` | Lista (filtro: `?funcionario=`) |
| DELETE | `/api/atendidos/{id}` | Remove |

## Respostas de erro (padronizadas pelo ApiExceptionHandler)

| Status | Quando | Corpo |
|---|---|---|
| 400 | Validação do `@Valid` falhou | `{ "erro": "Dados inválidos", "campos": {...} }` |
| 401 | Login inválido | `{ "erro": "Email ou senha inválidos" }` |
| 404 | ID não existe | `{ "erro": "... não encontrado" }` |
| 409 | Email duplicado | `{ "erro": "Email já cadastrado" }` |
| 500 | Erro inesperado | `{ "erro": "Erro interno: ..." }` |

## Exemplo de uso no React

```js
const API = "http://localhost:8080/api";

// Login → guarda o token
const res = await fetch("http://localhost:8080/auth/login", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ email, senha, tipo: "cliente" }),
});
const { token } = await res.json();
localStorage.setItem("token", token);

// Listar serviços (rota protegida → envia o token)
const servicos = await fetch(`${API}/servicos`, {
  headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
}).then(r => r.json());

// Cabeçalhos reutilizáveis para rotas protegidas
const authHeaders = {
  "Content-Type": "application/json",
  Authorization: `Bearer ${localStorage.getItem("token")}`,
};

// Criar agendamento
await fetch(`${API}/agendamentos`, {
  method: "POST",
  headers: authHeaders,
  body: JSON.stringify({
    usuario: "Caio Vinicius",
    funcionario: "João",
    servicos: "Corte + Barba",
    horario: "2026-06-15 14:30",
  }),
});

// Desmarcar
await fetch(`${API}/agendamentos/5/desmarcar`, { method: "PUT", headers: authHeaders });
```

## Observação sobre as tabelas

As colunas de `clientes`, `funcionarios`, `servicos` e `desmarcados` vieram do pgAdmin.
As de **`tb_agendados`** e **`atendidos`** foram inferidas pelo padrão de `desmarcados`:

- `tb_agendados`: `id_agenda, usuario, funcionario, servicos, horario, status, views`
- `atendidos`: `id_atendidos, id_agenda, usuario, funcionario, servicos, horario`

Se forem diferentes, ajuste o Model e o DAO correspondentes. A tabela `login` não é
usada — a autenticação consulta `clientes`/`funcionarios` direto. O login aceita
hash BCrypt (`$2a$...`) e senhas antigas em texto puro.
