# Usa uma imagem oficial do Java 21/24 para rodar a aplicação
FROM eclipse-temurin:21-jdk-alpine

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia o arquivo jar gerado pelo maven para dentro do container
COPY target/*.jar app.jar

# Expõe a porta que a aplicação usa
EXPOSE 8081

# Comando para rodar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]