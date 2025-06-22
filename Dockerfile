# 베이지 이미지
FROM openjdk:17

LABEL maintainer="yesul"
LABEL version="0.0.1"

# 호스트 상에 만들어진 build된 jar파일의 경로 변수
ARG JAR_FILE_PATH=build/libs/*.jar
# 호스트 상에 만들어진 build된 jar 파일 이미지 안에 app.jar 이름의 파일로 복사
COPY ${JAR_FILE_PATH} app.jar

# 컨테이너 실행시 바로 수행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]