FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/expenses.jar /expenses/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/expenses/app.jar"]
