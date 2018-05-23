# almundo-backend

**Tabla de Contenidos**

[TOC]

##Almundo Backend

- Se utilizo Maven, Spring y Java 8

###Extras
- En el caso de no haber empleados disponibles se añadiran los llamados a una cola de espera y a medida que se vayan liberando los empleados, iran tomando uno a uno cada llamado.
- Si llegara a haber mas de 10 llamados, quedan en cola de espera hasta haber podido suplir a todos ellos, si es que no encuentra la cantidad de empleados suficiente para atenderlos.
- Los Test Unitarios Extras estan en DispatcherExtraTest.java

#Unit Test
##DispatcherTest
En esta seccion se realizo el test solicitado de 10 llamados concurrentes
##DispatcherExtraTest
Los test realizados en esta ocasion son para determinar la respuesta de los llamados. En cada caso se analiza que no se filtren response inesperados.
