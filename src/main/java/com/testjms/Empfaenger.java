package com.testjms;
import javax.jms.*;
import javax.naming.InitialContext;

/**
 *
 * Soll der Nachrichtenempfang nicht synchron, sondern asynchron erfolgen, muss der Empfänger das Interface javax.jms.MessageConsumer implementieren. Die in diesem Interface enthaltene Methode wird vom JMS-Server gerufen, wenn eine Nachricht für den
 * Empfänger vorliegt. Was hier also vorliegt, ist ein klassischer Callback beim Nachrichteneingang. Für den asynchronen Nachrichtenempfang registriert sich der Client nach der
 * Erzeugung des Message-Consumers über dessen Methode setMessageListener().
 * Sowohl die hier aufgeführten Empfänger als auch der Sender sind als Beispiele im
 * Begleitmaterial dieses Buches im Package jms enthalten. Bevor diese gestartet werden
 * können, ist allerdings ein einmaliger administrativer Eingriff in den JBoss-Server notwendig, um die verwendete Queue anzulegen. Hierzu muss die Datei <JBoss-Install>/
 * server/default/deploy/hornetq/hornetq-jms.xml um den folgenden Eintrag ergänzt
 * werden:
 * <queue name="Praxisbuch">
 *  <entry name="/queue/Praxisbuch"/>
 * </queue>
 * Ist die Queue einmal angelegt, können die einzelnen Programme über den Kommandozeilenaufruf ant run.sender, ant run.empfaenger.synchron oder ant run.empfaenger.
 * asynchron gestartet werden.

 */

/**
 * das Publish-Subscribe-Modell :
 * Sowohl die Erzeugung von Sender bzw. Empfänger als auch das Versenden oder Empfangen
 * von Nachrichten erfolgt nach dem gleichen Schema, wie im Falle des Punkt-zu-PunktModells. Die einzige Ausnahme bildet die bereits oben erwähnte Möglichkeit einer dauerhaften Registrierung. Hierzu wird an der Session die Methode createDurableSubscriber(Topic ziel, String name) bereitgestellt, über die ein Empfänger mit einem
 * frei wählbaren, für den jeweiligen Client aber eindeutigen Namen registriert werden kann.
 * Für einen solchen dauerhaften Empfänger werden eingestellte Nachrichten in dessen
 * Abwesenheit vom JMS-Server aufgehoben. Diese „verpassten“ Nachrichten bekommt der
 * Empfänger bei der nächsten Verbindung zum Server (identifiziert über den eindeutigen
 * Namen) zugestellt. Soll man eine solche dauerhafte Registrierung wieder beenden, muss
 * die Methode unsubscribe(String name) der Session verwendet werden.
 */


public class Empfaenger {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        // ConnectionFactory factory = (ConnectionFactory)
        //initialContext.lookup("TopicConnectionFactory");
        ConnectionFactory factory = (ConnectionFactory)
                initialContext.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        connection.start();
        // Destination postfach = (Destination) initialContext.lookup("topic/Praxisbuch");
        Destination postfach = (Destination)
                initialContext.lookup("queue/Praxisbuch");
        //createConsumer(Destination ziel, String messageSelektor)
        //session.createConsumer(postfach, "Bestellungstyp = 'Internet'");
        MessageConsumer empfaenger =
                session.createConsumer(postfach);
        Message nachricht = empfaenger.receive();
        System.out.println(((TextMessage) nachricht).getText());
    }
}
