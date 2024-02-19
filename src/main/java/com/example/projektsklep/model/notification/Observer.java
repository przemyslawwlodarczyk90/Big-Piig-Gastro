package com.example.projektsklep.model.notification;


import com.example.projektsklep.model.entities.order.Observable;

public interface Observer {

    void update(Observable observable);

}