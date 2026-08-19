# Estágio 1: Build (Compilação do projeto com Maven)
# Usando a versão oficial do Maven baseada no Eclipse Temurin 17
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Run (Imagem final otimizada para rodar a aplicação)
# Usando a imagem Alpine (super leve) do Eclipse Temurin 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]