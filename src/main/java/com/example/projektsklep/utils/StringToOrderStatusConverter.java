package com.example.projektsklep.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.projektsklep.model.enums.OrderStatus;

/**
 * Klasa StringToOrderStatusConverter jest konwerterem Springowym, służącym do przekształcania
 * łańcuchów znaków (String) na obiekty typu OrderStatus (enum).
 * Jest to szczególnie przydatne w kontekście przetwarzania danych formularzy lub URL-i,
 * gdzie status zamówienia może być przekazany jako tekst, a następnie musi być użyty
 * w logice aplikacji jako typ wyliczeniowy OrderStatus.
 */
@Component
class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        try {
            // Próba przekonwertowania łańcucha znaków na wartość enum OrderStatus,
            // ignorując wielkość liter (toUpperCase), co zapewnia większą elastyczność.
            return OrderStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            // W przypadku niepoprawnej wartości źródłowej (source) zwracany jest null,
            // co pozwala na obsługę błędów konwersji bez przerywania działania aplikacji.
            return null;
        }
    }
}