package com.example.projektsklep.model.notification;

import com.example.projektsklep.model.entities.order.Observable;

/**
 * Interfejs Obserwatora używany w wzorcu projektowym obserwatora.
 * Definiuje metodę update, która jest wywoływana, gdy obserwowany obiekt (Observable) zgłasza zmianę.
 */
public interface Observer {

    /**
     * Metoda wywoływana przez obiekt obserwowalny (Observable), gdy nastąpi zmiana, którą obserwator powinien odnotować.
     * Pozwala na zaktualizowanie stanu obserwatora w odpowiedzi na zmianę stanu obiektu obserwowanego.
     *
     * @param observable Obiekt obserwowalny, który zgłosił zmianę.
     */
    void update(Observable observable);

}