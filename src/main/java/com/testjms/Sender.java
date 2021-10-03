package com.testjms;

import javax.jms.*;
import javax.naming.InitialContext;

/**
 * Einfacher JMS-Sender
 */
public class Sender {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        // Der erste Schritt besteht dabei im „Auffinden“ des JMS-Providers
        ConnectionFactory factory = (ConnectionFactory)
                initialContext.lookup("ConnectionFactory");
        //Über das so erhaltene Interface kann der
        //Client über die Methode createConnection() eine javax.jms.Connection erzeugen
        //und hält damit die benötigte Verbindung zum Nachrichtendienst in der Hand.
        Connection connection = factory.createConnection();
        //Die Session wird den Empfang der Nachricht automatisch bestätigen, sobald der Client
        //die Nachricht der Queue entnommen hat.
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        connection.start();
        //Nun hat der Client also eine JMS-Sitzung geöffnet.
        TextMessage nachricht = session.createTextMessage();
        nachricht.setStringProperty("Bestellungstyp", "Internet");
        nachricht.setText("Ein EJB3-Buch bitte!");
        // JNDI name
        Destination ziel = (Destination)
                initialContext.lookup("queue/Praxisbuch");
        MessageProducer sender = session.createProducer(ziel);
        sender.send(nachricht);
    }
}
