# Arquitetura e Design Patterns: Vox Pix API

Este documento detalha as decisões arquiteturais e padrões de projeto adotados no **Vox Pix API**.

---

## 1. Clean Architecture (Arquitetura Limpa)

O projeto é estruturado utilizando os princípios da Arquitetura Limpa dividida por features (`conta` e `pix`). O objetivo primário é o **desacoplamento tecnológico**: o coração do negócio (regras de depósito, saldo e transferências) é independente de frameworks, bancos de dados e interfaces web.

### Camadas de Cada Feature:
1. **Domain (Domínio Puro):**
   * Contém as entidades de negócio (`Conta`, `Transacao`), exceptions de negócio e estratégias (strategy de validação de limite).
   * **Regra:** Não importa nenhuma classe do Spring Boot, JPA ou qualquer biblioteca externa. É Java puro.
   * Contém as interfaces dos repositórios (Portas de Saída / Output Ports).
2. **Infrastructure (Infraestrutura):**
   * Contém os detalhes de tecnologia: entidades de banco relacional do Hibernate (`ContaEntity`), interfaces do Spring Data, adaptadores de repositório, clientes HTTP declarativos (Feign) para consumo de IA.
3. **Presentation (Apresentação):**
   * Contém os controladores REST, os DTOs de entrada e saída (Records), classes Facade e notificadores.

---

## 2. Padrões de Projeto Aplicados (Design Patterns)

O projeto demonstra o uso prático de vários padrões de projeto clássicos (GoF):

* **Adapter Pattern (Adaptador):**
  * Aplicado em `ContaRepositoryAdapter` e `TransacaoRepositoryAdapter`. Converte a interface do domínio pura para a chamada física do repositório Spring Data JPA. Isso permite alterar o banco de dados (ex: trocar JPA por JDBC ou MongoDB) sem mexer no domínio.
* **Strategy Pattern (Estratégia):**
  * Aplicado em `SeletorDeValidador` e validadores de limite. Seleciona dinamicamente a regra de limite Pix correta baseado no horário da transação (limite diurno vs noturno) sem acoplar IF/ELSEs no serviço principal de Pix.
* **Facade Pattern (Fachada):**
  * Aplicado em `PixFacade`. Centraliza e simplifica a interface do módulo Pix para a camada de apresentação, ocultando a complexidade de orquestração interna de serviços de voz, transcrição e validações.
* **Singleton Pattern (Instância Única):**
  * Gerenciado pelo Spring Boot através da injeção de dependências em escopo padrão (`@Component`, `@Service`, `@Repository`), garantindo instâncias únicas e controle de concorrência.
* **Builder Pattern:**
  * Utilizado na criação de instâncias de `Transacao` e `Conta` para facilitar a construção de objetos complexos e imutáveis.

---

## 3. Estratégia de Banco de Dados Híbrido e Ambientes (Spring Profiles)

Para garantir flexibilidade entre a execução local 100% livre de dependências externas e um deploy robusto de portfólio, a API adota uma estratégia de múltiplos perfis:

* **Perfil `dev` (H2 Database + Offline Mock AI):**
  * **Uso:** Testes rápidos locais na IDE e validação automática da plataforma DIO.
  * **Banco:** H2 Database em memória (reinicia limpo a cada execução).
  * **IA:** Mocks/Stubs em memória simulam as respostas da IA offline com latência zero e custo nulo.
* **Perfil `prod` (MySQL Database + Real AI Integration):**
  * **Uso:** Simulação real e deploy em containers (Docker).
  * **Banco:** MySQL executado através de container.
  * **IA:** Chamadas HTTP reais utilizando clientes OpenFeign integrando com a API da Groq (Whisper) e Google AI Studio (Gemini).

