Editor.md


**Tabla de Contenidos**
​
[TOC]
​
##Almundo Backend
​
- Se utilizo Maven, Spring y Java 8
​
###Extras
- En el caso de no haber empleados disponibles se añadiran los llamados a una cola de espera y a medida que se vayan liberando los empleados, iran tomando uno a uno cada llamado.
- Si llegara a haber mas de 10 llamados, quedan en cola de espera hasta haber podido suplir a todos ellos, si es que no encuentra la cantidad de empleados suficiente para atenderlos.
- Los Test Unitarios Extras estan en DispatcherExtraTest.java
​
#Unit Test
##DispatcherTest
En esta seccion se realizo el test solicitado de 10 llamados concurrentes
##DispatcherExtraTest
Los test realizados en esta ocasion son para determinar la respuesta de los llamados. En cada caso se analiza que no se filtren response inesperados.
​
Tabla de Contenidos

Almundo Backend
Extras
Unit Test
DispatcherTest
DispatcherExtraTest
Almundo Backend
Se utilizo Maven, Spring y Java 8
Extras
En el caso de no haber empleados disponibles se añadiran los llamados a una cola de espera y a medida que se vayan liberando los empleados, iran tomando uno a uno cada llamado.
Si llegara a haber mas de 10 llamados, quedan en cola de espera hasta haber podido suplir a todos ellos, si es que no encuentra la cantidad de empleados suficiente para atenderlos.
Los Test Unitarios Extras estan en DispatcherExtraTest.java
Unit Test
DispatcherTest
En esta seccion se realizo el test solicitado de 10 llamados concurrentes

DispatcherExtraTest
Los test realizados en esta ocasion son para determinar la respuesta de los llamados. En cada caso se analiza que no se filtren response inesperados.

Download & install
Latest version: v1.5.0，Update: 2015-06-09



 


Or Bower install:

bower install editor.md




Change logs:

CHANGES

Dependents
Projects :

CodeMirror
marked
jQuery
FontAwesome
github-markdown.css
KaTeX
Rephael.js
prettify.js
flowchart.js
sequence-diagram.js
Prefixes.scss

Development tools :

Brackets
Sass/Scss
Gulp.js
License
Editor.md follows the MIT License, Anyone can freely use.





Fork me on Github :








Editor.md