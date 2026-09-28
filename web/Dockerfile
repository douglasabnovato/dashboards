# Imagem do módulo web: compila com Maven (core + web) e roda com JRE 21 como usuário sem privilégios
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY core core
COPY api api
COPY web web
RUN mvn -q -pl web -am -DskipTests package

FROM eclipse-temurin:21-jre
RUN useradd --system --create-home app
WORKDIR /app
COPY --from=build /app/web/target/dashboards-web-2.0.0.jar app.jar
USER app
ENV JAVA_OPTS="-XX:MaxRAMPercentage=55 -XX:+UseSerialGC -XX:MaxMetaspaceSize=192m -XX:ReservedCodeCacheSize=48m -XX:TieredStopAtLevel=1 -Xss512k -XX:+ExitOnOutOfMemoryError"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
# fim do Dockerfile
