# Projekt_Belot
Java projekt of the Croatian card game Belot (Bela) in 2 v 2 scenario with 1 player and 3 AI's

Run Project:
    navigate bash to: "Projekt_Belot\src\main\java\com\projektbelot"
    bash : <jshell -R-ea --enable-preview>
    jshell : </o lvp.java>
    jshell : </o [file to be run on the server like logo.java]>

Server Restart:
    bash : <netstat -ano | findstr :50001>
    locate the PID, something like this:
      "TCP    127.0.0.1:50001        0.0.0.0:0              ABH?REN         22716" <-- this int
    bash : taskkill /PID [this int] /F