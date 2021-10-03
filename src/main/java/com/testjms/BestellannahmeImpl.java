package com.testjms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.*;
import java.util.Enumeration;

/**
 * Implementierung einer nachrichtenbasierten Bestellannahme
 *
 * Über die
 * onMessage()-Methode dieses Interfaces wird die Bean asynchron über den Eingang einer
 * neuen Nachricht informiert.
 */

@MessageDriven(
        name = "Bestellannahme",
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(
                        propertyName = "destination",
                        propertyValue = "queue/Praxisbuch") })
public class BestellannahmeImpl implements MessageListener {
    public void onMessage(Message nachricht) {
        try {
            MapMessage map = (MapMessage) nachricht;
            Enumeration<String> artikelnummern =
                    map.getMapNames();
            while (artikelnummern.hasMoreElements()) {
                String artikelnummer =
                        artikelnummern.nextElement();
                System.out.println(artikelnummer + ": "
                        + map.getInt(artikelnummer) + "Stück");
            }
        } catch (JMSException e) {
            throw new EJBException(e);
        }
    }
}
