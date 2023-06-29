<h1>CLEVERTEC (final task)</h1>

<p>CLEVERTEC final task - User microservice:</p>

<p>Требования:</p>

<ol>
<li>
Создать отдельный микросервис с реляционной базой (postgreSQL), хранящей 
информацию о пользователях/ролях.
</li>
</ol>

<h2>Что сделано:</h2>

<ol>
<li>
Создан отдельный микросервис с реляционной базой (postgreSQL), хранящей 
информацию о пользователях/ролях. Микросервис позволяет получать пользователя / 
добавлять пользователя по запросу. Добавление пользователя происходит с ROLE_SUBSCRIBER.
</li>

<li>
В микросервис добавлен spring security. Добавлен In-memory пользователь. 
Добавлен BasicAuthenticationFilter фильтр.
</li>
</ol>

<h3>Запуск и использование</h3>
<ol>
<li>Билдим user-microservice: .\gradlew clean build</li>
<li>Поднимаем user-microservice в докере: docker-compose up -d</li>
</ol>

<p>
P.S. User-микросервис должен быть запущен в Docker вторым!
</p>

<ol>
<li>GET:</li>
<p>http://localhost:8083/users?user_name=Mark</p>


<li>POST:</li>
<p>http://localhost:8083/users</p>
</ol>

<p>
P.S. запросы к ендпоинтам сервиса должны содержать header "Authorization" : "Basic hash(username:password)"
</p>
