package com.example.luqtaecommerce.presentation.main.categories

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.domain.model.product.Category
import org.koin.androidx.compose.koinViewModel
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.LoadErrorView
import com.example.luqtaecommerce.ui.components.LuqtaBackHeader
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.Purple500


@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = koinViewModel()
) {

    LaunchedEffect(Unit) {
        //delay(100) // Small delay to let UI settle
        viewModel.fetchCategories()
    }

    val categoriesState = viewModel.categories.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LuqtaBackHeader(title = "التصنيفات", navController = navController)
        }

        HorizontalDivider(color = LightPrimary, thickness = 1.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (categoriesState is Result.Loading) Arrangement.Center else Arrangement.Top
        ) {
            when (categoriesState) {
                is Result.Loading -> {
                    Box(modifier = Modifier.size(40.dp)) {
                        CircularProgressIndicator(color = Purple500)
                    }
                }
                is Result.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(categoriesState.data) { category ->
                            CategoryItem(category = category) {
                                navController.navigate(
                                    "${Screen.Products.route}/${category.slug}/${category.name}"/**/
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    LoadErrorView { viewModel.retryFetchingCategories() }

                    Log.e("Categories Error",
                        categoriesState.message ?: categoriesState.exception.localizedMessage
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            //.fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = LightPrimary,
                shape = CardDefaults.shape
            )
            .padding(vertical = 22.dp, horizontal = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // This is the key: disables ripple and other indications
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder for emoji text - you'll need a mapping or a way to get relevant emojis
            // For now, let's use a default or an initial letter.
            val emoji = getEmojiForCategory(category.name)
            Text(
                text = emoji,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = category.name,
                fontSize = if (category.name.length >= 8) 13.sp else 14.sp,
                maxLines = 1,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}


// Simple helper function to get an emoji based on category name.
// You might want a more robust mapping or a dedicated resource for this.
fun getEmojiForCategory(categoryName: String): String {
    return when (categoryName) {
        "أثاث" -> "🛋️" // Furniture
        "ملابس" -> "👕" // Clothes
        "إلكترونيات" -> "📱" // Electronics
        "موضة" -> "👜" // Fashion
        "سيارات" -> "🚗" // Industrial (Car for example)
        "كتب" -> "📚" // Home Decor
        "صحة" -> "🩺" // Health
        "عقارات" -> "🏠" // Construction & Real Estate
        "خدمات التصنيع" -> "📏" // Fabrication Service
        "معدات كهربائية" -> "🔌" // Electrical Equipment
        else -> "📦" // Default emoji
    }
}