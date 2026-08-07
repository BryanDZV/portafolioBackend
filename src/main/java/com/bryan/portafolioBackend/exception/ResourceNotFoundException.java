package com.bryan.portafolioBackend.exception;

//para mi error personalizado de recurso no encontrado, lo uso en el service cuando busco un proyecto por id y no lo encuentra
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);//con el super llamo al constructor de la clase padre RuntimeException y le paso el mensaje que quiero mostrar cuando se lance esta excepción
    }
}
