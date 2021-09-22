# TUNNEL MELAGRANA e COLLEGAMENTO SERIALE

### Reference Documentation
For further reference, please consider the following sections:

* [java-native/Jssc](https://github.com/java-native/jssc)
* [Native *.so](https://github.com/java-native/jssc/tree/master/src/main/resources-precompiled/natives)
* [jssc_Example](https://github.com/java-native/jssc/wiki/examples)

### Guides
IMPORTANTE: libreria nativa libjssc.so DEVE ESSERE  copiata all'interno della cartella '/lib' di java della macchina; es: 

Passi per utilizzo di questo server per collegamento seriale:

1. avviare server Spring-boot

2. avviare tunnel SSH su macchina locale con macchina iDempiere cliente (ad esempio Melagrana):

   ```tex
   ssh -nN -R 8088:127.0.0.1:8088 root@173.249.60.71 -p 3622
   ```

3. per eventuale TEST, avviare SOCAT

   ```tex
   sudo socat PTY,link=/dev/ttyS10,echo=0 -
   ```

4. eventuale CHMOD della porta

   ```tex
   sudo chmod 666 /dev/ttyS10 ...oppure ttyUSB0
   ```

5. (vecchia gestione, cancellare il LOCK_FILE con "sudo rm -f /var/lock/LCK..ttyS10")
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

