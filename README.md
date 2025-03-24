# Share and rent  
### *Микросервисное приложение для аренды вещей* 

## Функционал программы
Приложение позволяет пользователям искать, выкладывать и брать в аренду на время вещи для пользования, оставлять отзывы, делать запросы на добавление вещей.

### Программа включает в себя два микросервиса:
Основной — содержит всё необходимое для работы продукта.  
Gateway-шлюз — производит валидацию входящих запросов, фильтруя некорректные запросы.

Основной микросервис реализует следующие возможности для пользователей:

- Добавление вещи в каталог владельцем для сдачи в аренду (возможность добавить как по запросу пользователями такой вещи, так и самостоятельно).
- Редактирование информации о вещи ее владельцем (пользователем добавившим эту вещь).
- Просмотр информации о конкретной вещи любым пользователем.
- Просмотр владельцем списка всех его вещей.
- Поиск доступных для аренды вещей (по тексту).
- Добавление запроса на бронирование (возможность брать вещи в аренду на определенные даты) любым пользователем.
- Подтверждение или отклонение запроса на бронирование владельцем вещи.
- Получение информации о конкретном бронировании автором запроса или владельцем вещи.
- Получение списка бронирований автором запроса (фильтрация по статусу, сортировка от новых к старым).
- Получение списка бронирований для всех вещей владельца.
- Пользователи могут оставлять отзывы в форме комментариев на использованную вещь по окончании аренды.
- Пользователи могут просматривать комментарии других пользователей.
- Добавление нового запроса вещи. Например, если требуемая вещь отсутствует в каталоге.
- Получение списка всех запросов автором с данными об ответах на них (сортировка от новых к старым).
- Получение списка запросов других пользователей (сортировка от новых к старым).
- Просмотр информации любым пользователем о конкретном запросе вещи вместе с данными об ответах на него.
## 🧩 Стек-технологий 🧩

Проект в своей основе включает Java, Spring Boot, PostgreSQL, JPA, Hibernate, Maven, Docker.

В данном репозитории представлен бэкенд приложения. Работоспособность приложения протестирована с помощью модульных и интеграционных тестов. Также программа протестирована по WEB API с помощью Postman-тестов.

Упаковать и запустить приложение можно следующим образом:

- mvn clean install (сборка)
- docker-compose up (упаковка и запуск)
