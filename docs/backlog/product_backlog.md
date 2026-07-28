# Product Backlog: Vox Pix API

Este documento contém o Backlog do Produto organizado por Histórias de Usuário (User Stories) e mapeado nas sprints de entrega.

---

## 1. Histórias de Usuário (User Stories)

### US01 - Autenticação Segura
> **Como** cliente do banco,  
> **Quero** que meu acesso seja autenticado de forma segura ao enviar ordens por voz,  
> **Para que** terceiros não possam consultar meu saldo ou movimentar meu dinheiro.

### US02 - Consulta de Saldo por Voz
> **Como** cliente do banco,  
> **Quero** falar *"Quero saber meu saldo"* ou *"Quanto eu tenho na conta?"*,  
> **Para que** o sistema responda meu saldo atualizado sem que eu precise navegar por menus.

### US03 - Transferência Pix por Voz
> **Como** cliente do banco,  
> **Quero** falar *"Transfira 100 reais para o Carlos"* ou *"Faça um Pix de 50 reais para o CPF 123.456.789-00"*,  
> **Para que** o sistema execute a transferência instantânea de forma automatizada.

---

## 2. Divisão das Sprints

### Sprint 1: Módulo de Contas e Persistência (Concluída)
* **Objetivo:** Estabelecer a base de dados de contas do banco.
* **Tarefas:**
  * [x] Criar entidade de domínio puro `Conta`.
  * [x] Criar porta de repositório `ContaRepository`.
  * [x] Criar entidade de persistência `ContaEntity` e repositório Spring Data JPA.
  * [x] Criar adaptador de repositório `ContaRepositoryAdapter`.
  * [x] Criar carga inicial de dados em memória (CommandLineRunner).
  * [x] Implementar testes unitários da classe `Conta`.

### Sprint 2: Segurança e Controle de Acesso (Concluída)
* **Objetivo:** Garantir a autenticação de chamadas HTTP.
* **Tarefas:**
  * [x] Configurar Spring Security 6.x / 4.x.
  * [x] Configurar usuários em memória para simulação de clientes bancários.
  * [x] Proteger rotas REST de acordo com perfis e permissões e testar com MockMvc e Basic Auth.

### Sprint 3: Clientes de Inteligência Artificial (OpenFeign) (Concluída)
* **Objetivo:** Mapear os clientes HTTP para consumo das APIs externas de IA.
* **Tarefas:**
  * [x] Criar interface OpenFeign para a API Whisper (conversão de áudio em texto) no perfil prod.
  * [x] Criar interface OpenFeign para a API Gemini (extração de intenção) no perfil prod.
  * [x] Desenvolver adaptadores e mocks locais (no perfil dev) para simulação 100% offline.

### Sprint 4: Orquestração e Endpoints REST (Controllers) (Concluída)
* **Objetivo:** Integrar os serviços e expor as rotas de voz.
* **Tarefas:**
  * [x] Desenvolver `BankingService` (debitos, creditos, logs de transacoes e H2/MySQL).
  * [x] Desenvolver `VoiceCommandService` (orquestrador de voz, transcricao e extracao).
  * [x] Criar controlador REST `/api/voz/processar` e `/api/contas` protegidos com Spring Security.

### Sprint 5: Docker e Entrega
* **Objetivo:** Empacotamento profissional do projeto.
* **Tarefas:**
  * [ ] Escrever `Dockerfile` com Multi-stage Build.
  * [ ] Escrever `docker-compose.yml`.
  * [ ] Documentar execução no README principal.
