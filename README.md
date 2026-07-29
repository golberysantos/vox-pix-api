# Vox Pix API: Voice-Activated Banking 🎙️💸

A **Vox Pix API** é uma solução moderna e segura de *Voice-Activated Banking*, desenvolvida como projeto final para o **Santander Bootcamp AI Java** da **DIO (Digital Innovation One)**. 

A aplicação utiliza inteligência artificial de transcrição de fala (**OpenAI Whisper via Groq**) e modelos de linguagem de larga escala (**Google Gemini 1.5 Flash**) para receber comandos de voz de clientes (arquivos de áudio), interpretá-los em português natural e orquestrar as respectivas operações bancárias (consultas de saldo e transferências Pix) no banco de dados.

---

## 🚀 Tecnologias Utilizadas

*   **Linguagem:** Java 21 (LTS)
*   **Framework Core:** Spring Boot 4.0.7
*   **Segurança:** Spring Security (HTTP Basic Auth)
*   **Cliente HTTP Declarativo:** Spring Cloud OpenFeign
*   **Banco de Dados:** 
    *   **H2 Database** (Em memória para desenvolvimento e avaliação imediata)
    *   **MySQL 8.0** (Persistência robusta em ambiente de produção)
*   **Containerização:** Docker e Docker Compose
*   **Provedores de Inteligência Artificial:**
    *   **Groq API (Whisper-large-v3):** Transcrição ultrarrápida de fala para texto (Speech-to-Text).
    *   **Google AI Studio (Gemini-1.5-flash):** Extração de intenções textuais e variáveis usando JSON Schema estruturado (*Structured Output*).

---

## 📐 Arquitetura e Design Patterns

O projeto foi estruturado seguindo os princípios da **Clean Architecture** (Arquitetura Limpa) e **Ports & Adapters** (Arquitetura Hexagonal), com desacoplamento total do coração do negócio (Java Puro) contra frameworks e bibliotecas externas.

### Padrões de Projeto Aplicados:
*   **Strategy Pattern:** Utilizado para seleção dinâmica de validadores de limites.
*   **Facade Pattern:** Centralizado no orquestrador de voz do domínio para mascarar a complexidade do pipeline de IA.
*   **Builder Pattern:** Usado na criação de instâncias complexas e imutáveis de `Conta` e `Transacao`.
*   **AAA Test Pattern (Arrange, Act, Assert):** Suite de 18 testes unitários e de integração estruturados de forma padronizada para máxima clareza e manutenibilidade.

---

## 🔒 Credenciais e Simulação de Identidades

A API possui segurança configurada via Spring Security básica. A carga de dados em memória inicializa contas fictícias associadas a credenciais Basic Auth específicas para testes locais:

| Usuário (Username) | Senha (Password) | Nome do Titular | CPF Associado | Saldo Inicial |
| :--- | :--- | :--- | :--- | :--- |
| **carlos** | `carlos123` | Carlos Silva | `123.456.789-00` | R$ 1.000,00 |
| **maria** | `maria123` | Maria Santos | `987.654.321-11` | R$ 2.000,00 |
| **joao** | `joao123` | João Souza | `111.222.333-44` | R$ 3.000,00 |
| **visitante** | `visitante123` | *Sem conta bancária mapeada* | - | - |

> 📌 **Nota:** A chamada HTTP com o usuário `visitante` retornará HTTP `403 Forbidden` nas rotas privadas, pois ele está autenticado, mas não possui conta mapeada no banco.

---

## 🛠️ Como Executar a Aplicação

A API adota a estratégia de múltiplos perfis (Spring Profiles) para permitir testes rápidos na IDE sem a necessidade de chaves de API:

### Perfil `dev` (H2 Database + Mocks Locais - Recomendado para Avaliação)
Este perfil é ativo por padrão e **não exige internet ou chaves de API externas**. Ele simula a IA localmente usando Regex, permitindo testar todo o fluxo imediatamente de graça.

1.  Clone o repositório e execute a build dos testes:
    ```bash
    ./mvnw clean test
    ```
2.  Inicie a aplicação localmente:
    ```bash
    ./mvnw spring-boot:run
    ```
3.  O console do banco H2 estará acessível em: `http://localhost:8080/h2-console` (URL JDBC: `jdbc:h2:mem:voxpixdb`, User: `sa`, Pass: `sa`).
4.  A documentação interativa e painel de testes do **Swagger UI** estará acessível em: `http://localhost:8080/swagger-ui/index.html`. Para testar os endpoints privados diretamente por lá, clique no botão **"Authorize"** e digite as credenciais (ex: `carlos` / `carlos123`).

### Perfil `prod` (MySQL + Integração de IAs Reais - Via Docker)
Este ambiente conecta-se ao banco de dados MySQL corporativo e executa as chamadas HTTP reais via OpenFeign para os endpoints do Gemini e da Groq.

1.  Abra o arquivo `docker-compose.yml` e preencha suas chaves de API reais da Groq e do Gemini nas variáveis de ambiente `GROQ_API_KEY` e `GEMINI_API_KEY`.
2.  Suba os containers utilizando o Docker Compose:
    ```bash
    docker compose up --build -d
    ```
3.  O banco MySQL e a API inicializarão na rede de containers, com a porta `8080` exposta no seu localhost.

---

## 📡 Endpoints da API

Todos os endpoints privados exigem cabeçalho de autenticação HTTP **Basic Auth** (utilizando as credenciais descritas na tabela de identidades).

### 🎙️ 1. Processar Comando de Voz
*   **Rota:** `POST /api/voz/processar`
*   **Content-Type:** `multipart/form-data`
*   **Parâmetros (Form):**
    *   `file`: O arquivo de áudio gravado (`.mp3` ou `.wav`).
*   **Comportamento do Perfil `dev` (Mock AI):**
    *   Se o nome do arquivo conter `saldo`, transcreve para *"Quero ver meu saldo, por favor"* e consulta o saldo do usuário.
    *   Se o nome do arquivo conter `maria`, transcreve para *"Transfira 150 reais para a Maria"* e realiza o Pix para a Maria.
    *   Se o nome do arquivo conter `joao`, transcreve para *"Faça um Pix de 300 reais para o João"*.

### 💵 2. Listar Dados da Minha Conta
*   **Rota:** `GET /api/contas/minha`
*   **Retorno:** DTO contendo o número da conta, titular, CPF e saldo do usuário logado.

### 📋 3. Listar Todas as Contas do Sistema
*   **Rota:** `GET /api/contas`
*   **Retorno:** Lista de todas as contas cadastradas com seus respectivos saldos (útil para ver os saldos mudando após comandos de voz).

### 📜 4. Histórico de Transações do Usuário
*   **Rota:** `GET /api/contas/minha/transacoes`
*   **Retorno:** Lista com o log detalhado de todas as transações de sucesso ou de falha originadas pelo cliente autenticado.

---

## 👨‍💻 Autor

Desenvolvido por **Golbery Santos**  
*GitHub:* [golberysantos](https://github.com/golberysantos)  
*Repositório Oficial:* [vox-pix-api](https://github.com/golberysantos/vox-pix-api)
