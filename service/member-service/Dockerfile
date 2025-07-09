FROM eclipse-temurin:21-jre

WORKDIR /app

# 미리 빌드된 JAR 파일 복사
COPY service/member-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]