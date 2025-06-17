package com.example.luqtaecommerce.presentation.main.products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    navController: NavController,
    viewModel: ProductsViewModel// = koinNavViewModel() // Use the same shared ViewModel instance
) {
    // This state is local to the screen for the TextField
    var textFieldValue by remember { mutableStateOf("") }
    val searchState by viewModel.searchUiState.collectAsState()

    // This effect ensures that when the user types, the ViewModel is notified.
    LaunchedEffect(textFieldValue) {
        viewModel.onSearchQueryChanged(textFieldValue)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        topBar = { /* ... TopAppBar as before ... */ },
        bottomBar = {
            LuqtaButton(
                text = "البحث",
                enabled = textFieldValue.isNotBlank()
            ) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("search_query", textFieldValue)
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "اسم المنتج",
                        color = GrayPlaceholder,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryCyan,
                    unfocusedBorderColor = GrayPlaceholder,
                    cursorColor = PrimaryCyan
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = PrimaryCyan
                )
            } else {
                if (searchState.suggestions.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "لم يتم العثور على نتائج", // Display product name
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                } else {
                    // Suggestions are now a list of Product objects
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(searchState.suggestions, key = { it.slug }) { product ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .clickable { textFieldValue = product.name },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = product.name, // Display product name
                                        modifier = Modifier
                                            .padding(vertical = 12.dp)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_send),
                                        contentDescription = "Send",
                                        tint = Color.Black
                                    )
                                }
                                HorizontalDivider(color = GrayPlaceholder)
                            }
                        }
                    }
                }
            }
        }
    }
}