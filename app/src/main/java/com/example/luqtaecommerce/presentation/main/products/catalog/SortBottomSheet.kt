package com.example.luqtaecommerce.presentation.main.products.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.presentation.main.products.model.SortOption
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.PrimaryCyan

@Composable
fun SortBottomSheetContent(
    activeSortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit
) {
    var selectedOption by rememberSaveable { mutableStateOf(activeSortOption) }
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = "الترتيب حسب",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SortOption.entries.forEach { option ->
            SortOptionItem(
                option = option,
                isSelected = option == selectedOption,
                onClick = { selectedOption = option }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LuqtaButton(text = "تطبيق") {
            onSortOptionSelected(selectedOption)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun SortOptionItem(
    option: SortOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 12.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                colors = RadioButtonColors(
                    selectedColor = PrimaryCyan,
                    unselectedColor = Color.Black,
                    disabledUnselectedColor = GrayFont,
                    disabledSelectedColor = GrayFont
                ),
                selected = isSelected,
                onClick = onClick,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = option.label,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = LightPrimary)
    }
}