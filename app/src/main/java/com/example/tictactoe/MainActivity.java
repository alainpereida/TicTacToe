package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TURNOS_MAXIMOS = 9;
    private Jugador jugadorUno, jugadorDos;
    private boolean turno = true; //Cuando sea true sera turno de jugadorUno y false para jugadorDos
    private boolean bloqueo = false; //Para bloquear los demas botones
    private int turnos = 0;
    private Button[][] buttons = new Button[3][3];
    private TextView textJugadorUno, textJugadorDos, textPuntuacionJugadorUno, textPuntuacionJugadorDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generarJugadores();
        generarTablero();
    }

    @Override
    public void onClick(View v) {
        if (!bloqueo) {
            if (((Button) v).getText().equals(" ")) {
                if (turno) {
                    ((Button) v).setText(Character.toString(jugadorUno.getMarca()));
                    bloqueo = verificarTablero(jugadorUno);
                    if (bloqueo) {
                        jugadorUno.setJuegosGanados(jugadorUno.getJuegosGanados() + 1);
                        textPuntuacionJugadorUno.setText(String.valueOf(jugadorUno.getJuegosGanados()));
                        turnos = 0;
                    }
                } else {
                    ((Button) v).setText(Character.toString(jugadorDos.getMarca()));
                    bloqueo = verificarTablero(jugadorDos);
                    if (bloqueo) {
                        jugadorDos.setJuegosGanados(jugadorDos.getJuegosGanados() + 1);
                        textPuntuacionJugadorDos.setText(String.valueOf(jugadorDos.getJuegosGanados()));
                        turnos = 0;
                    }
                }
                turnos++;
                turno = !turno;
            }
        }

        if (turnos == TURNOS_MAXIMOS) {
            limpiarTablero();
            Toast.makeText(this, "Nadie Gano", Toast.LENGTH_SHORT).show();
            turnos = 0;
        }
    }

    private boolean verificarTablero(Jugador jugador) {
        boolean gano = false;

        //Verificamos renglones
        if (buttons[0][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[2][0].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[0][0], buttons[1][0], buttons[2][0]);
            return true;
        }

        if (buttons[0][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[2][1].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[0][1], buttons[1][1], buttons[2][1]);
            return true;
        }

        if (buttons[0][2].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][2].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[2][2].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[0][2], buttons[1][2], buttons[2][2]);
            return true;
        }

        //Verificamos columnas
        if (buttons[0][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[0][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[0][2].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[0][0], buttons[0][1], buttons[0][2]);
            return true;
        }

        if (buttons[1][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][2].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[1][0], buttons[1][1], buttons[1][2]);
            return true;
        }

        if (buttons[2][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[2][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[2][2].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[2][0], buttons[2][1], buttons[2][2]);
            return true;
        }

        //Verificamos esquinas
        if (buttons[0][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[2][2].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[0][0], buttons[1][1], buttons[2][2]);
            return true;
        }

        if (buttons[2][0].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[1][1].getText().toString().equals(Character.toString(jugador.getMarca()))
                && buttons[0][2].getText().toString().equals(Character.toString(jugador.getMarca()))) {
            pintarBotones(buttons[2][0], buttons[1][1], buttons[0][2]);
            return true;
        }

        return gano;
    }

    private void pintarBotones (final Button buttonUno, final Button buttonDos, final Button buttonTres) {
        Handler handler = new Handler();

        buttonUno.setBackgroundColor(getResources().getColor(R.color.colorGano));
        buttonDos.setBackgroundColor(getResources().getColor(R.color.colorGano));
        buttonTres.setBackgroundColor(getResources().getColor(R.color.colorGano));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonUno.setBackgroundColor(getResources().getColor(R.color.colorNatural));
                buttonDos.setBackgroundColor(getResources().getColor(R.color.colorNatural));
                buttonTres.setBackgroundColor(getResources().getColor(R.color.colorNatural));
                limpiarTablero();
            }
        }, 1500);
    }

    private void generarJugadores() {
        jugadorDos = new Jugador();
        jugadorUno = new Jugador(getIntent().getStringExtra("name"), 'X');
        Toast.makeText(this, "Bienvenido " + jugadorUno.getNombre(), Toast.LENGTH_SHORT).show();

        textJugadorUno = findViewById(R.id.jugadorUno);
        textJugadorDos = findViewById(R.id.jugadorDos);
        textPuntuacionJugadorUno = findViewById(R.id.puntiacionUno);
        textPuntuacionJugadorDos = findViewById(R.id.puntuacionDos);

        textJugadorUno.setText(jugadorUno.getNombre());
        textJugadorDos.setText(jugadorDos.getNombre());
    }

    private void limpiarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(" ");
            }
        }
        bloqueo = false;
    }

    private void generarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String boton = "imageButton" + (i + 1) + "_" + (j + 1);
                int resID = getResources().getIdentifier(boton, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setText(" ");
            }
        }
    }
}
