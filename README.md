Задача №3 (WebServer) - ООП весна

Цель данного упражнения познакомить студента с особенностями веб-соектов в Java\С# и, в конечном итоге, построить веб-сервер будущего приложения. Далее задача формулируется для Java.

   1. В основе любого сетевого взаимодействия лежат сокеты Беркли, а их реализации существуют во всех языках программирования. В Java существует класс-абстракция над сокетами, а именно класс ServerSocket:

	ServerScoket server = new ServerSocket(int port) - единственный параметр - порт, который сервер начинает слушать. Если создание серверного сокета завершилось успешно, то это означает, что порт успешно "захвачен" и сервер начал свою работу. В прротивном случае будет выброшено исключение. Далее необходимо вызвать метод accept() - данный метод блокирует дальнейшее выполнение текущего потока до тех пор, пока не будет зарегистрировано входящее соединение. Пример кода:

	ServerScoket server = new ServerSocket(int port);
	Socket client = server.accept(); - здесь происходит блокировка потока пока не появится входящее соединение. Переменная client - клиентский сокет.

	InputStream in = client.getInputStream();    - здесь мы можем взять у клиентского сокета входящий поток и прочитать из него запрос подобно файлу. 
        OutputStream out = client.getOutputStream(); - здесь мы можем взять у клиентского сокета исходящий поток и записать в него ответ на запрос.

Соответственно, легко понять, что входящий поток на сервере - это исходящий поток на клиенте. А исходящий поток сервера - входящий клиента. В Java для чтения и записи из сокета используются потоки ввода-вывода. Следовательно, к ним можно применять различные "обертки" подобно работе с файлом. Необходимо учесть тот факт, что при пересылке большого количества данных необходимо использовать буферизированные "обертки" над потоками. Имеется ввиду случай, когда клиент еще не закончил писать запрос в поток, а сервер уже начал читать данные. В таком случае необходимы соглашения о формате и признаке конца запроса. 

	Пример клиентского сокета:
	
	Socket socket = new Socket(ipAddress, serverPort);
	OutputStream out = socket.getOutputStream(); - сюда пишем запрос
	InputStream in = socket.getInputStream();   - отсюда читаем ответ

Это основы, с которыми разобраться будет в общем случае несложно. Теперь несложно заметить, что построенный таким образом сервер является однопоточным - пока не будет вызван метод accept() - входящие соединения не будут приходить. Выходом из этой ситуации будет следующее:
	1. Поместить метод accept()  внутрь бесконечного цикла.
	2. Как только метод разблокируется, поместить клиентский сокет в другой поток, где будет обработан запрос, и благодаря пункту 1, сразу же перейти на новую итерацию цикла и снова вызвать метод accept()

Пример:

	ServerScoket server = new ServerSocket(int port);
	while(true)
	{
		Socket client = server.accept();
		<поместить client в другой поток> 
	}

	Таким образом будет построен многопоточный веб-сервер. 

Теперь можно переходить к формулировке задачи:
Дано: 1. FileWorker, способный считать хэш файлов.
      2. ThreadDispather, способный ставить задачи на выполнение.
      3. WebServer, многопоточный веб-сервер.

Необходимо с использованием все трех механизмов реализовать клиент-серверное приложение, в рамках которого поддерживаются два механизма работы:
1. Приложение занимает порт 8080 и отдает на этом порту http-контент. html-контент представляет собой файловый web-проводник. То есть необходимо через html-верстку реализовать на странице переходы по папкам вглубь, назад, отображать список файлов в директории, каждый файл необходимо позволить скачать нажатием на соответствующую иконку напротив строки файла. Пример, который можно посмотреть: https://download.qt.io - как могло бы выглядеть решение.

2. Приложение занимает порт 8081, на котором поднимается cli, позволяющая получать диагностику сервера:
	- list - получить список файлов в директории, на которую смотрит FileWorker;
	- hash <filename> - получить хэш соответствующего файла;
	- size <filename> - получить размер файла;
	- status - статус работы сервера(active, stoped);
	- stop - остановить именно http-сервер;
	- start - запустить http-сервер.

3. С помощью ThreadDispatcher сервер выводит список активных потоков в серверную консоль.

То есть предельно простая логика - каждая команда отправляется с клиента, на сервере помещается в отдельный поток, в потоке обрабатывается - результат отправляется ответом на запрос и выводится в консоли клиента. 

Главная цель упражения - необхождимо сообразно ООП построить основу серверной части будущего веб-прилолжения, правильно скомпоновав FileWorker, ThreadDispather, WebServer. Оцениваться будут прежде всего архитектура серверного приложения, наличие конфигурационных настроек(порт, ip-адрес...), механизмов корректной обработки запросов и ответов, механизма корректной остановки сервера. Клиент в данном случае имеет пока минимальную роль.

Тестирование: необходимо будет написать специальный тест, который будет с помощью ThreadDispather создавать одновременно запросы к серверу. На сервере же необходимо с помощью монитора потоков вывести список активных потоков, где должны быть более одного потока запроса. То есть на каждый запрос клиента монитор потоков должен реагировать. Ручное тестирование также будет проводиться.

         
														
