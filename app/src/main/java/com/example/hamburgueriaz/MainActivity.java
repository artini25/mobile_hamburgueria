package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText rEditName;
    private CheckBox rBoxBacon, rBoxCheese, rBoxOnionRings;
    private Button rButtonMenos, rButtonMais, rButtonEnviar;
    private TextView rQntd, rResumoPedido;
    private int quantidade = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rEditName = findViewById(R.id.EditName);
        rBoxBacon = findViewById(R.id.BoxBacon);
        rBoxCheese = findViewById(R.id.BoxCheese);
        rBoxOnionRings = findViewById(R.id.BoxOnionRings);
        rButtonMenos = findViewById(R.id.ButtonMenos);
        rButtonMais = findViewById(R.id.ButtonMais);
        rQntd = findViewById(R.id.Qntd);
        rResumoPedido = findViewById(R.id.ResumoPedido);
        rButtonEnviar = findViewById(R.id.ButtonEnviar);


        rQntd.setText(String.valueOf(quantidade));

        findViewById(R.id.ButtonMais).setOnClickListener(this::somar);
        findViewById(R.id.ButtonMenos).setOnClickListener(this::subtrair);
        findViewById(R.id.ButtonEnviar).setOnClickListener(this::enviarPedido);

    }

    public void somar(View view) {
        quantidade++;
        rQntd.setText(String.valueOf(quantidade));
    }

    public void subtrair(View view) {
        if (quantidade > 0) {
            quantidade--;
            rQntd.setText(String.valueOf(quantidade));
        }
    }

    private void enviarPedido(View view) {
        String nomeCliente = rEditName.getText().toString().trim();
        boolean temBacon = rBoxBacon.isChecked();
        boolean temQueijo = rBoxCheese.isChecked();
        boolean temOnion = rBoxOnionRings.isChecked();

        if (nomeCliente.isEmpty()) {
            Toast.makeText(this, "Por Favor, insira seu nome.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantidade == 0) {
            Toast.makeText(this, "Selecione pelo menos 1 hamburguer.", Toast.LENGTH_SHORT).show();
            return;
        }

        double precoFinal = calcularPrecoTotal(quantidade, temBacon, temQueijo, temOnion);

        String resumoPedido = "Nome do Cliente: " + nomeCliente + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnion ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço Final: R$ " + String.format("%.2f", precoFinal);

        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
        intentEmail.setData(Uri.parse("mailto:")); // Apenas aplicativos de e-mail responderão
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nomeCliente);
        intentEmail.putExtra(Intent.EXTRA_TEXT, resumoPedido);

        // Verifica se existe um app de e-mail instalado antes de abrir
        if (intentEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(intentEmail);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularPrecoTotal(int quantidade, boolean temBacon, boolean temQueijo, boolean temOnion) {
        double precoBase = 20.0;
        double adicionalBacon = temBacon ? 2.0 : 0.0;
        double adicionalQueijo = temQueijo ? 2.0 : 0.0;
        double adicionalOnion = temOnion ? 3.0 : 0.0;

        double precoUnitario = precoBase + adicionalBacon + adicionalQueijo + adicionalOnion;
        return precoUnitario * quantidade;
    }
}