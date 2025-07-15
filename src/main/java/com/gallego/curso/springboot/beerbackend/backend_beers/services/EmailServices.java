package com.gallego.curso.springboot.beerbackend.backend_beers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendWelcomeMail(String to, String name){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("¡Bienvenido a Cervezario!");
        message.setText("Hola " + name + ", \n\nGracias por registrarte en Cervezario. ");

        mailSender.send(message);
    }

    @Async
    public void sendContactMail(String su, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("correoprogramacionalexgallego@gmail.com");
        message.setSubject(su);
        message.setText(text);

        mailSender.send(message);
    }
}
