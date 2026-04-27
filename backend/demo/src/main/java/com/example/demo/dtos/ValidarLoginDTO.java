package com.example.demo.dtos;

public class ValidarLoginDTO {
    private String rut;
    private String password; // Esta será la contraseña encriptada con AES desde el frontend

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}