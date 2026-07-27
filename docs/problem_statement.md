# Documento de Visão e Escopo: Vox Pix API

Este documento estabelece o problema a ser resolvido, os requisitos de negócio e o escopo técnico do projeto **Vox Pix API (Voice-Activated Banking)**.

---

## 1. O Problema e o Contexto de Negócio

Nos aplicativos bancários modernos, a realização de tarefas cotidianas como consultar o saldo ou fazer um Pix exige que o usuário passe por múltiplos menus, telas de carregamento e cliques. Essa complexidade apresenta barreiras significativas:

* **Acessibilidade:** Usuários com deficiência visual, motora ou idosos enfrentam dificuldades extremas para navegar por layouts cheios de componentes visuais.
* **Agilidade no Dia a Dia:** Em cenários onde o usuário está em movimento, abrir o app e clicar em vários botões é lento e ineficiente.

### A Solução
O **Vox Pix API** resolve esse problema fornecendo uma interface de voz. O usuário envia um comando de voz simples (ex: *"Transfira 50 reais para a conta do Carlos"*) e o sistema transcreve, interpreta e executa a operação bancária com segurança e rapidez.

---

## 2. Tecnologias Utilizadas

Para garantir a robustez e modularidade do sistema, adotamos a seguinte stack de tecnologia:

* **Java 21 & Spring Boot 4.0.7:** Linguagem e framework base.
* **Spring Security:** Para garantir que apenas clientes autenticados (via HTTP Basic Auth) possam executar comandos de voz ou consultar suas respectivas contas.
* **Spring Data JPA & H2 Database:** Banco de dados relacional em memória para gerenciar o estado das contas e o log de transações.
* **Spring Cloud OpenFeign:** Para comunicação HTTP declarativa com as APIs externas de IA.
* **Google Gemini (IA de Processamento de Linguagem):** Interpreta o texto transcrito, classifica a intenção do usuário e extrai as variáveis de negócio em JSON.
* **OpenAI Whisper (IA de Reconhecimento de Fala):** Converte o áudio gravado (`.mp3` ou `.wav`) em texto de forma precisa.

---

## 3. Fluxo de Execução da API (Arquitetura)

O processamento de um comando de voz segue os seguintes passos estruturados:

```
[Cliente] --- (1. Envia Áudio + Basic Auth) ---> [VoiceController]
                                                      │
                                                      ▼
[Whisper API] <--- (2. Transcreve Áudio) -------- [VoiceCommandService]
                                                      │
                                                      ▼
[Gemini API] <--- (3. Extrai Intenção/Dados JSON) -- [VoiceCommandService]
                                                      │
                                                      ▼
[H2 Database] <--- (4. Executa Ação e Loga) ------ [BankingService]
                                                      │
                                                      ▼
[Cliente] <--- (5. Retorna Confirmação JSON) ----- [VoiceController]
```

### Comandos Suportados:
1. **`CONSULTA_SALDO`:** Retorna o saldo da conta do cliente autenticado.
2. **`REALIZAR_TRANSFERENCIA`:** Transfere um valor monetário da conta do cliente autenticado para a conta destino (identificada pelo CPF ou nome do titular).

---

## 4. Requisitos de Segurança
* Toda chamada de voz deve estar protegida. O Spring Security identificará o usuário e associará o comando de voz diretamente à conta bancária dele.
* Não é permitido executar transações sem autenticação válida.
