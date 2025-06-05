package com.example.gestly_app.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.gestly_app.R
import com.example.gestly_app.ui.theme.DarkBackground
import com.example.gestly_app.ui.theme.DarkCard
import com.example.gestly_app.ui.theme.DarkText
import com.example.gestly_app.ui.theme.LightText
import com.example.gestly_app.ui.theme.PrimaryColor
import com.example.gestly_app.ui.theme.SecondaryColor

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val stock: Int,
    val imageUrl: String,
    val purchasePrice: Double,
    val salePrice: Double,
    val supplier: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsPage(modifier: Modifier = Modifier) {
    // Lista de productos de ejemplo
    val sampleProducts = listOf(
        Product(
            id = 1,
            name = "Smartphone X",
            description = "Último modelo con cámara de 108MP",
            stock = 15,
            imageUrl = "https://example.com/phone.jpg",
            purchasePrice = 450.0,
            salePrice = 699.99,
            supplier = "TecnoImport"
        ),
        Product(
            id = 2,
            name = "Laptop Pro",
            description = "16GB RAM, SSD 512GB, i7 11th Gen",
            stock = 8,
            imageUrl = "https://example.com/laptop.jpg",
            purchasePrice = 800.0,
            salePrice = 1299.99,
            supplier = "CompuGlobal"
        ),
        Product(
            id = 3,
            name = "Auriculares Bluetooth",
            description = "Cancelación de ruido, 30h batería",
            stock = 25,
            imageUrl = "https://example.com/headphones.jpg",
            purchasePrice = 75.0,
            salePrice = 149.99,
            supplier = "AudioMasters"
        )
    )

    var products by remember { mutableStateOf(sampleProducts) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newProduct by remember { mutableStateOf(Product(0, "", "", 0, "", 0.0, 0.0, "")) }
    val sheetState = rememberModalBottomSheetState()

    // Filtrar productos basado en la búsqueda
    val filteredProducts = if (searchQuery.isEmpty()) {
        products
    } else {
        products.filter { product ->
            product.name.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true) ||
                    product.supplier.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text("Gestión de Productos", color = LightText)
                },
                actions = {
                    // Botón de añadir producto
                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir Producto",
                            tint = PrimaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = LightText
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Buscar productos...", color = DarkText) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = DarkText
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* Acción al buscar */ })
            )

            // Lista de productos
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredProducts) { product ->
                    ProductItem(
                        product = product,
                        onItemClick = { selectedProduct = product },
                        onDelete = { products = products.filter { it.id != product.id } }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Modal de detalles del producto
        selectedProduct?.let { product ->
            ProductDetailModal(
                product = product,
                onDismiss = { selectedProduct = null },
                onSave = { updatedProduct ->
                    products = products.map { if (it.id == updatedProduct.id) updatedProduct else it }
                    selectedProduct = null
                }
            )
        }

        // Diálogo para añadir nuevo producto
        if (showAddDialog) {
            AddProductDialog(
                product = newProduct,
                onDismiss = { showAddDialog = false },
                onSave = { product ->
                    val newId = (products.maxOfOrNull { it.id } ?: 0) + 1
                    products = products + product.copy(id = newId)
                    newProduct = Product(0, "", "", 0, "", 0.0, 0.0, "")
                    showAddDialog = false
                },
                onProductChange = { newProduct = it }
            )
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onItemClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .border(1.dp, SecondaryColor, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    color = LightText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = product.description,
                    color = DarkText,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Text(
                    text = "Stock: ${product.stock}",
                    color = if (product.stock > 5) Color.Green else Color.Red,
                    fontSize = 14.sp
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailModal(
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var editedProduct by remember { mutableStateOf(product) }
    val profit = editedProduct.salePrice - editedProduct.purchasePrice

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalles del Producto",
                        color = LightText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = LightText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen del producto (usando una imagen de placeholder por defecto)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SecondaryColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = editedProduct.imageUrl
                        ),
                        contentDescription = "Imagen del producto",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campos editables
                EditableProductField(
                    label = "Nombre",
                    value = editedProduct.name,
                    onValueChange = { editedProduct = editedProduct.copy(name = it) }
                )

                EditableProductField(
                    label = "Descripción",
                    value = editedProduct.description,
                    onValueChange = { editedProduct = editedProduct.copy(description = it) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EditableProductField(
                        label = "Stock",
                        value = editedProduct.stock.toString(),
                        onValueChange = {
                            editedProduct = editedProduct.copy(stock = it.toIntOrNull() ?: 0)
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )

                    EditableProductField(
                        label = "Proveedor",
                        value = editedProduct.supplier,
                        onValueChange = { editedProduct = editedProduct.copy(supplier = it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EditableProductField(
                        label = "Precio Compra",
                        value = editedProduct.purchasePrice.toString(),
                        onValueChange = {
                            editedProduct = editedProduct.copy(purchasePrice = it.toDoubleOrNull() ?: 0.0)
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )

                    EditableProductField(
                        label = "Precio Venta",
                        value = editedProduct.salePrice.toString(),
                        onValueChange = {
                            editedProduct = editedProduct.copy(salePrice = it.toDoubleOrNull() ?: 0.0)
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )
                }

                // Información de ganancia
                Text(
                    text = "Ganancia: $${"%.2f".format(profit)} (${"%.0f".format((profit / editedProduct.purchasePrice) * 100)}%)",
                    color = if (profit >= 0) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor)
                    ) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onSave(editedProduct) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                    ) {
                        Text("Guardar Cambios")
                    }
                }
            }
        }
    }
}

@Composable
fun EditableProductField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            color = LightText,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkBackground,
                unfocusedContainerColor = DarkBackground,
                disabledContainerColor = DarkBackground,
                focusedTextColor = LightText,
                unfocusedTextColor = LightText,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = SecondaryColor
            ),
            textStyle = TextStyle(color = LightText),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit,
    onProductChange: (Product) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Añadir Nuevo Producto",
                    color = LightText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                EditableProductField(
                    label = "Nombre",
                    value = product.name,
                    onValueChange = { onProductChange(product.copy(name = it)) }
                )

                EditableProductField(
                    label = "Descripción",
                    value = product.description,
                    onValueChange = { onProductChange(product.copy(description = it)) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EditableProductField(
                        label = "Stock",
                        value = product.stock.toString(),
                        onValueChange = {
                            onProductChange(product.copy(stock = it.toIntOrNull() ?: 0))
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )

                    EditableProductField(
                        label = "URL Imagen",
                        value = product.imageUrl,
                        onValueChange = { onProductChange(product.copy(imageUrl = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EditableProductField(
                        label = "Precio Compra",
                        value = product.purchasePrice.toString(),
                        onValueChange = {
                            onProductChange(product.copy(purchasePrice = it.toDoubleOrNull() ?: 0.0))
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )

                    EditableProductField(
                        label = "Precio Venta",
                        value = product.salePrice.toString(),
                        onValueChange = {
                            onProductChange(product.copy(salePrice = it.toDoubleOrNull() ?: 0.0))
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )
                }

                EditableProductField(
                    label = "Proveedor",
                    value = product.supplier,
                    onValueChange = { onProductChange(product.copy(supplier = it)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor)
                    ) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onSave(product) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                    ) {
                        Text("Guardar Producto")
                    }
                }
            }
        }
    }
}