package com.microservice.credit.util;

import com.microservice.credit.model.Credit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaseError extends Credit {

    private  String mensajeError;
    private static ClaseError instance;



    private ClaseError(){

    }

    public static ClaseError getInstance(String mensaje){
        if(instance == null){
            instance = new ClaseError();
        }
        instance.mensajeError=mensaje;
        return instance;
    }
}
