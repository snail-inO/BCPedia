FROM openjdk:18-jdk as build
WORKDIR /workspace/bcpedia

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

COPY src ./src

RUN ./mvnw install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:18
VOLUME /tmp
ARG DEPENDENCY=/workspace/bcpedia/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","portfolioof.me.bcpedia.BcPediaApplication"]
