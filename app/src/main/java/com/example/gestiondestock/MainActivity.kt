package com.example.gestiondestock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gestiondestock.ui.theme.GestionDeStockTheme

data class StockItem(
    val id: Int,
    val name: String,
    var quantity: Int,
    val price: Double
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionDeStockTheme {
                StockManagementApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockManagementApp() {
    val stockItems = remember { mutableStateListOf<StockItem>() }
    val newItemName = remember { mutableStateOf("") }
    val newItemQuantity = remember { mutableStateOf("") }
    val newItemPrice = remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Formulario para agregar nuevos items
            Text("Agregar nuevo producto", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = newItemName.value,
                onValueChange = { newItemName.value = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = newItemQuantity.value,
                onValueChange = { newItemQuantity.value = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = newItemPrice.value,
                onValueChange = { newItemPrice.value = it },
                label = { Text("Precio unitario") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    if (newItemName.value.isNotBlank() && 
                        newItemQuantity.value.isNotBlank() && 
                        newItemPrice.value.isNotBlank()) {
                        
                        val newItem = StockItem(
                            id = stockItems.size + 1,
                            name = newItemName.value,
                            quantity = newItemQuantity.value.toInt(),
                            price = newItemPrice.value.toDouble()
                        )
                        
                        stockItems.add(newItem)
                        newItemName.value = ""
                        newItemQuantity.value = ""
                        newItemPrice.value = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al stock")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            
            // Lista de productos
            Text("Inventario actual", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            
            if (stockItems.isEmpty()) {
                Text("No hay productos en stock", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(stockItems) { item ->
                        StockItemRow(item = item, 
                            onIncrease = { item.quantity++ },
                            onDecrease = { if (item.quantity > 0) item.quantity-- }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun StockItemRow(item: StockItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Precio: $${item.price}", style = MaterialTheme.typography.bodyMedium)
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onDecrease) {
                Text("-")
            }
            Text(text = "${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = onIncrease) {
                Text("+")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockManagementPreview() {
    GestionDeStockTheme {
        StockManagementApp()
    }
}
