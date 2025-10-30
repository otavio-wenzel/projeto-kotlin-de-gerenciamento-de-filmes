package com.example.gerenciamentodefilmes.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gerenciamentodefilmes.model.database.AppDatabase
import com.example.gerenciamentodefilmes.viewmodel.DiretorViewModel
import com.example.gerenciamentodefilmes.viewmodel.DiretorViewModelFactory

class DiretorActivity : ComponentActivity() {
    private val diretorViewModel: DiretorViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).diretorDao()
        DiretorViewModelFactory(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiretorScreen(diretorViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiretorScreen(diretorViewModel: DiretorViewModel) {
    var nome by remember { mutableStateOf("") }
    var anosDeExperiencia by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Cadastro de Diretor", fontSize = 22.sp)

        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Diretor") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = anosDeExperiencia,
            onValueChange = { anosDeExperiencia = it },
            label = { Text("Anos de Experiência") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                val anos = anosDeExperiencia.toIntOrNull()
                if (nome.isNotBlank() && anos != null) {
                    diretorViewModel.inserirDiretor(nome, anos)
                    Toast.makeText(context, "Diretor cadastrado!", Toast.LENGTH_SHORT).show()
                    nome = ""
                    anosDeExperiencia = ""
                } else {
                    Toast.makeText(context, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Diretor")
        }
    }
}