CREATE DATABASE Weather;
CREATE USER 'WeatherServer'@'localhost' IDENTIFIED BY 't4jn3h4sL0';
GRANT ALL PRIVILEGES ON Weather.* TO 'WeatherServer'@'localhost';
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'WeatherServer'@'localhost';



odpalanie: mvn spring-boot:run

dopisanie w controller wiecej getow (i wiecej metod w )

stworzenie w model nowych obiektow (tych co jest w projekcie bazy danych)
do nich wlasne interface
i wlasne repa w data


