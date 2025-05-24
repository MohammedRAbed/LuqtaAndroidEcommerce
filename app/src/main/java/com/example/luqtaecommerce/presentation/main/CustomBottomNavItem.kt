package com.example.luqtaecommerce.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.presentation.navigation.BottomNavItem
import com.example.luqtaecommerce.presentation.navigation.bottomNavItems
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.PrimaryCyan


@Composable
fun CustomBottomNavItem(
    modifier: Modifier,
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // no ripple at all
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = if (isSelected) item.selectedIcon else item.icon),
                contentDescription = item.title,
                tint = if (isSelected) PrimaryCyan else GrayFont,
                modifier = Modifier.offset(y = 3.dp)
            )
            Text(
                text = item.title,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp,
                color = if (isSelected) Color.Black else GrayFont
            )
        }
    }
}

@Preview
@Composable
private fun CustomBottomNavItemPreview() {
    CustomBottomNavItem(modifier = Modifier, item = bottomNavItems[0], isSelected = false) {}
}
