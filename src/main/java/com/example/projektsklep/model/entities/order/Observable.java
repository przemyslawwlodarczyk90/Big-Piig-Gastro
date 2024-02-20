package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.notification.Observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Interfejs definiujący metody dla obiektów obserwowalnych.
 * Pozwala na rejestrację, wyrejestrowanie obserwatorów oraz powiadamianie ich o zmianach.
 */
public interface Observable {

    // Zbiór zarejestrowanych obserwatorów, którzy będą powiadamiani o zmianach.

    Set<Observer> registeredObservers = new HashSet<Observer>();

    /**
     * Rejestruje nowego obserwatora, który będzie powiadamiany o zmianach.
     * @param observer Obserwator do zarejestrowania.
     */
    void registerObserver(Observer observer);

    /**
     * Wyrejestrowuje obserwatora, przestając powiadamiać go o zmianach.
     * @param observer Obserwator do wyrejestrowania.
     */
    void unregisterObserver(Observer observer);

    /**
     * Powiadamia wszystkich zarejestrowanych obserwatorów o zmianie.
     */
    void notifyObservers();
}