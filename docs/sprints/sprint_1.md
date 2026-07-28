# Sprint 1: Módulo de Contas e Persistência

**Período:** Ciclo Atual  
**Objetivo:** Implementar a estrutura de dados e regras de negócio de Contas Bancárias com persistência local em banco de dados H2.

---

## 1. Escopo de Arquivos da Sprint 1

### Módulo `conta` (Feature principal da Sprint)
* [x] **`domain/model/Conta.java`**: Modelo de domínio puro com regras de crédito e débito.
* [x] **`domain/repository/ContaRepository.java`**: Interface pura (Porta).
* [x] **`infrastructure/persistence/ContaEntity.java`**: Classe de mapeamento da tabela `tb_contas` no banco H2.
* [x] **`infrastructure/persistence/SpringDataContaRepository.java`**: Interface do Spring Data JPA.
* [x] **`infrastructure/persistence/ContaRepositoryAdapter.java`**: Tradutor/Adaptador entre a JPA e o domínio.
* [x] **`config/BancoCargaInicial.java`** (ou `CommandLineRunner` similar): Script em Java que insere contas fictícias (ex: Carlos, Maria, João) no banco H2 ao subir a API.

---

## 2. Critérios de Aceitação (Definition of Done - DoD)
1. **Compilação:** O projeto deve compilar sem erros via `mvn clean compile`.
2. **Carga Inicial:** O console H2 deve mostrar a tabela de contas criada e populada com os dados de teste na inicialização.
3. **Testes Unitários:** A classe de domínio `Conta` deve possuir testes unitários cobrindo 100% dos caminhos (crédito bem-sucedido, débito bem-sucedido, exceção de valor negativo e exceção de saldo insuficiente).
