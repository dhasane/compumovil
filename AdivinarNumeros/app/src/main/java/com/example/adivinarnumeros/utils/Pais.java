package com.example.adivinarnumeros.utils;

public class Pais {
    String nombre;
    String capital;
    String nom_int;
    String sigla;

    public Pais( String nombre, String capital, String nom_int, String sigla)
    {
        this.nombre = nombre;
        this.capital = capital;
        this.nom_int = nom_int;
        this.sigla = sigla;

        System.out.println(nombre + " " + capital + " " + nom_int + " " + sigla);
    }

    public String toString()
    {
        return this.nombre;
    }

    public String getNombre()
    {
        return this.nombre;
    }
    public String getCapital()
    {
        return this.capital;
    }
    public String getNom_int()
    {
        return this.nom_int;
    }
    public String getSigla()
    {
        return this.sigla;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
}
