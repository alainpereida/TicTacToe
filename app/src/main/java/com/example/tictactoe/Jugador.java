package com.example.tictactoe;

public class Jugador {

    private String nombre;
    private Character marca;
    private int juegosGanados;

    public Jugador() {
        this.nombre = "The Machine";
        this.marca = 'O';
        juegosGanados = 0;
    }

    public Jugador(String nombre, Character marca) {
        this.nombre = nombre;
        this.marca = marca;
        this.juegosGanados = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Character getMarca() {
        return marca;
    }

    public void setMarca(Character marca) {
        this.marca = marca;
    }

    public int getJuegosGanados() {
        return juegosGanados;
    }

    public void setJuegosGanados(int juegosGanados) {
        this.juegosGanados = juegosGanados;
    }
}
