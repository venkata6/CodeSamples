package com.bharanisoft.peace;

import org.eclipse.paho.client.mqttv3.*;

/**
 * Created by varjunan on 8/6/17.
 */

public class Subscribe {

    public static void main(String[] args) throws MqttException {

        System.out.println("== START SUBSCRIBER ==");

        MqttClient client=new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        client.setCallback( new SimpleMqttCallBack() );
        client.connect();

        client.subscribe("iot_data");

    }

}
