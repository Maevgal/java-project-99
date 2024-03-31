FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY /app .

RUN pwd

RUN ./gradlew --no-daemon build

CMD ./build/install/app/bin/app
