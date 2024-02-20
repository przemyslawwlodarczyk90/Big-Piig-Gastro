package com.example.projektsklep.model.notification;

import com.example.projektsklep.model.entities.order.Observable;
import com.example.projektsklep.model.entities.order.Order;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Klasa reprezentująca powiadomienie e-mail, które obserwuje zmiany w zamówieniu.
 */
public class Email implements Observer {

    private Order order; // Zamówienie, które jest obserwowane przez ten obiekt e-mail
    private ByteArrayOutputStream capturedOutput; // Strumień do przechwytywania danych wyjściowych (do celów demonstracyjnych)

    /**
     * Konstruktor inicjalizujący obiekt e-mail z danym zamówieniem.
     * @param order Zamówienie do obserwowania
     */
    public Email(Order order) {
        this.order = order;
        this.capturedOutput = new ByteArrayOutputStream();
    }

    /**
     * Metoda wywoływana, gdy obserwowane zamówienie informuje o zmianie.
     * W tym przypadku, metoda symuluje wysłanie e-maila poprzez przechwycenie i zapisanie wyjścia do strumienia.
     * @param observable Obiekt, który zgłosił zmianę
     */
    @Override
    public void update(Observable observable) {
        if (observable instanceof Order) {
            Order order = (Order) observable;
            PrintStream oldStream = System.out; // Zapisuje oryginalny strumień wyjściowy
            System.setOut(new PrintStream(capturedOutput)); // Ustawia nowy strumień wyjściowy do przechwytywania danych
            System.out.println("E-mail - zamówienie numer: " + order.getId() + " zmieniło status na: " + order.getOrderStatus());
            System.setOut(oldStream); // Przywraca oryginalny strumień wyjściowy
        }
    }

    /**
     * Zwraca przechwycony tekst, który symuluje treść wysłanego e-maila.
     * @return Przechwycony tekst
     */
    public String getCapturedOutput() {
        return capturedOutput.toString();
    }
}