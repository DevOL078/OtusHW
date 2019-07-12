#Результаты исследования сборщиков мусора

В исследовании сравнивались три различных сборщика мусора: 
<li>G1</li>
<li>SerialCollector</li>
<li>ParallelCollector</li>
<br>
Исходный код программы для исследования находится в файле: <code>src/main/java/ru/otus/gc/OutMemoryMain.java</code>.
Параметры для запуска программы:<br>
-Xms256m<br>
-Xmx256m<br>
-Xlog:gc=debug:file=./hw05-gc/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m<br>
-XX:+HeapDumpOnOutOfMemoryError<br>
-XX:HeapDumpPath=./hw05-gc/logs/dump<br>
<br>
Во всех трех экспериментах программа заканчивала работу с ошибкой <code>OutOfMemoryException</code>
через 4 минуты 40 секунд.<br>
<br>
Во время запуска программы расход памяти отслеживался программой VisualVM. Изображения с каждого эксперимента приведены в папке <code>img</code>.
<br>
Логи экспериментов приведены в папке <code>logs</code>.
<br>
<br>
Анализ логов был осуществлен с помощью программы, исходный код которой находится в файле
<code>src/main/java/ru/otus/gc/analysis</code>.
<br>
Вывод программы:<br>
================G1 analysis====================<br>
Young: 20
66.375 20.522 25.517 31.511 53.97 43.875 13.693 54.721 23.919 
34.053 30.282 19.159 8.291 52.192 19.934 19.985 18.868 21.54 
22.728 6.103<br>
Young middle value: 29.36189999999999<br>
Full: 2<br>
240.101 228.795<br> 
Full middle value: 234.44799999999998<br>
Remark: 4<br>
6.172 3.738 5.748 6.138<br>
Remark middle value: 5.449<br>
================Parallel analysis====================<br>
Young: 5<br>
76.743 152.759 193.038 141.69 4.907<br>
Young middle value: 113.82740000000001<br>
Full: 5<br>
458.035 509.95 542.674 647.434 814.925<br>
Full middle value: 594.6036<br>
Remark: 0<br>

================Serial analysis====================<br>
Young: 7<br>
118.539 196.184 265.859 414.78 89.214 796.812 785.146<br>
Young middle value: 380.9334285714285<br>
Full: 4<br>
414.501 495.178 784.966 704.451<br>
Full middle value: 599.774<br>
Remark: 0<br>
<br>

##Выводы
Благодаря принципу регионов, G1 тратит на сборки молодого поколения 
меньше времени (это видно по среднему значению), но делает это чаще. 
Полных сборок, наоборот, меньше, и времени на них тратится меньше. 
Объясняется это тем, что сбороки молодого поколения производятся 
интенсивнее и тем самым уменьшают нагрузки с полных нагрузок. 
По логам видно, что разница по памяти у G1 составляет несколько Мб, 
тогда как у остальных двух сборщиков разница составляет несколько десятков Мб.
Объяснение такое же - сборки чаще и быстрее, поэтому память разгружается по-немногу.

