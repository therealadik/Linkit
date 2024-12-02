# Linkit

**Linkit** — это сервис для создания коротких ссылок, разработанный с использованием Spring Boot.

## Стек

- **Java Development Kit (JDK):** версия 17
- **Docker Compose:**
- **PostgreSQL:** версия 17
- **Spring Boot:** версия 3.3.5

## Установка

1. **Клонирование репозитория:**
   
   ```bash
   git clone https://github.com/therealadik/Linkit.git
   cd Linkit

3. **Сборка приложения с помощью Docker Compose:**
   
   ```bash
   docker-compose up --build

5. **Документация API**
   
   Документация API, сгенерированная с помощью Swagger, доступна по адресу: http://localhost:8080/swagger-ui/index.html

6. **Доступ к приложению:**
   
   После успешного запуска приложение будет доступно по адресу: http://localhost:8080

Уведомления пользователю посылаются через Console
При редактировании ссылки обязательно посылать UUID ownerUser, чтобы программа могла идентефицировать вас. 
UUID выдается при создании ссылки

