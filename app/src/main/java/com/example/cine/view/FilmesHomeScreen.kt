@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class
)

package com.example.cine.view

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cine.model.entity.Diretor
import com.example.cine.model.entity.Filme
import com.example.cine.viewmodel.DiretorViewModel
import com.example.cine.viewmodel.FilmeViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmesHomeScreen(
    filmeViewModel: FilmeViewModel,
    diretorViewModel: DiretorViewModel,
    onAddDiretor: () -> Unit
) {
    val listaFilmes by filmeViewModel.listaFilmes
    val listaDiretores by diretorViewModel.listaDiretores

    var query by remember { mutableStateOf("") }
    var diretorFiltro by remember { mutableStateOf<Diretor?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showEditSheet by remember { mutableStateOf<Filme?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CineList", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Exportar JSON") },
                            onClick = {
                                menuExpanded = false
                                val json = buildJson(listaFilmes, listaDiretores)
                                shareText(context, "Filmes.json", json)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Gerar exemplos") },
                            onClick = {
                                menuExpanded = false
                                scope.launch {
                                    filmeViewModel.gerarExemplos(listaDiretores)
                                }
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Novo Filme") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                onClick = { showEditSheet = Filme(0, "", diretorFiltro?.id ?: (listaDiretores.firstOrNull()?.id ?: 0)) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search + filtros
            DockedSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { /* já filtra abaixo */ },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Buscar por título…") },
                trailingIcon = {
                    AssistChip(
                        onClick = onAddDiretor,
                        label = { Text("Adicionar Diretor") }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {}

            Spacer(Modifier.height(12.dp))

            // Chips de diretores (filtro)
            AnimatedVisibility(visible = listaDiretores.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = diretorFiltro == null,
                        onClick = { diretorFiltro = null },
                        label = { Text("Todos") }
                    )
                    listaDiretores.forEach { d ->
                        FilterChip(
                            selected = diretorFiltro?.id == d.id,
                            onClick = { diretorFiltro = d },
                            label = { Text(d.nome) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Lista filtrada
            val filtrada = remember(query, diretorFiltro, listaFilmes, listaDiretores) {
                listaFilmes.filter { f ->
                    (query.isBlank() || f.titulo.contains(query, ignoreCase = true)) &&
                            (diretorFiltro == null || f.diretorId == diretorFiltro!!.id)
                }
            }

            if (listaDiretores.isEmpty()) {
                EmptyState(
                    title = "Nenhum diretor cadastrado",
                    subtitle = "Cadastre pelo menos um diretor para começar.",
                    primary = "Adicionar Diretor",
                    onPrimary = onAddDiretor
                )
            } else if (filtrada.isEmpty()) {
                EmptyState(
                    title = "Nada encontrado",
                    subtitle = "Tente outro termo ou mude o filtro."
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filtrada, key = { it.id }) { filme ->
                        val diretor = listaDiretores.find { it.id == filme.diretorId }
                        SwipeableFilmCard(
                            filme = filme,
                            diretorNome = diretor?.nome ?: "Desconhecido",
                            onEdit = { showEditSheet = filme },
                            onDelete = {
                                val apagado = filme
                                filmeViewModel.excluirFilme(apagado)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Filme removido",
                                        actionLabel = "Desfazer",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        filmeViewModel.reinserir(apagado)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // BottomSheet de criação/edição
    if (showEditSheet != null) {
        EditFilmeBottomSheet(
            filmeInicial = showEditSheet!!,
            diretores = listaDiretores,
            onDismiss = { showEditSheet = null },
            onConfirm = { id, titulo, diretorId ->
                if (id == 0) {
                    val msg = filmeViewModel.salvarFilme(titulo, diretorId)
                    // snackbar é opcional; feedback já via Toast se preferir
                } else {
                    filmeViewModel.atualizarFilme(id, titulo, diretorId)
                }
                showEditSheet = null
            }
        )
    }
}

@Composable
private fun EmptyState(
    title: String,
    subtitle: String,
    primary: String? = null,
    onPrimary: (() -> Unit)? = null
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (primary != null && onPrimary != null) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onPrimary) { Text(primary) }
        }
    }
}

@Composable
private fun SwipeableFilmCard(
    filme: Filme,
    diretorNome: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberDismissState(confirmValueChange = { value ->
        if (value == DismissValue.DismissedToStart) {
            onDelete()
        }
        value != DismissValue.DismissedToStart // deixa o card voltar ao lugar
    })

    SwipeToDismiss(
        state = dismissState,
        background = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
        dismissContent = {
            ElevatedCard(onClick = onEdit) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(filme.titulo, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Direção: $diretorNome",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                }
            }
        },
        directions = setOf(DismissDirection.EndToStart)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFilmeBottomSheet(
    filmeInicial: Filme,
    diretores: List<Diretor>,
    onDismiss: () -> Unit,
    onConfirm: (id: Int, titulo: String, diretorId: Int) -> Unit
) {
    var titulo by remember(filmeInicial.id) { mutableStateOf(filmeInicial.titulo) }
    var expanded by remember { mutableStateOf(false) }
    var diretorSelecionado by remember(filmeInicial.id) {
        mutableStateOf(diretores.find { it.id == filmeInicial.diretorId } ?: diretores.firstOrNull())
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text(
                if (filmeInicial.id == 0) "Novo Filme" else "Editar Filme",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = diretorSelecionado?.nome ?: "Selecione um diretor",
                    onValueChange = {},
                    label = { Text("Diretor") },
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    diretores.forEach { d ->
                        DropdownMenuItem(
                            text = { Text(d.nome) },
                            onClick = {
                                diretorSelecionado = d
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                Button(
                    onClick = {
                        val dId = diretorSelecionado?.id ?: return@Button
                        onConfirm(filmeInicial.id, titulo, dId)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = titulo.isNotBlank() && diretorSelecionado != null
                ) { Text(if (filmeInicial.id == 0) "Salvar" else "Atualizar") }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ====== Helpers para Exportar JSON (sem dependência externa) ======
private fun buildJson(filmes: List<Filme>, diretores: List<Diretor>): String {
    val mapDiretores = diretores.associateBy { it.id }
    val items = filmes.joinToString(",") { f ->
        val d = mapDiretores[f.diretorId]
        """{"id":${f.id},"titulo":${quote(f.titulo)},"diretor":{"id":${d?.id ?: 0},"nome":${quote(d?.nome ?: "Desconhecido")}}}"""
    }
    return """{"filmes":[$items]}"""
}
private fun quote(s: String) = "\"" + s.replace("\"", "\\\"") + "\""

private fun shareText(context: android.content.Context, fileName: String, text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/json"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_TITLE, fileName)
    }
    context.startActivity(Intent.createChooser(sendIntent, "Compartilhar lista"))
}