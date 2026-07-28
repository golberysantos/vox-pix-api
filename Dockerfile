# ===================================================================
# STAGE 1: COMPILACAO (BUILD)
# ===================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia os arquivos de definicao de dependencias
COPY pom.xml .

# Copia o codigo fonte da aplicacao
COPY src ./src

# Compila o projeto gerando o arquivo JAR (ignora os testes na compilacao do container)
RUN mvn clean package -DskipTests

# ===================================================================
# STAGE 2: EXECUCAO (RUNTIME)
# ===================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia apenas o artefato compilado do Stage 1
COPY --from=build /app/target/*.jar app.jar

# Porta exposta pelo container
EXPOSE 8080

# Comando de inicializacao da aplicacao
ENTRYPOINT ["java", "-jar", "app.jar"]
