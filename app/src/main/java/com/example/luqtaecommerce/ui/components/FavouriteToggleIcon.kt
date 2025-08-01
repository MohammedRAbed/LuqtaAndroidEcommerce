package com.example.luqtaecommerce.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun FavouriteToggleIcon(size: Dp = 24.dp) {
    var isToggled by rememberSaveable { mutableStateOf(false) }

    // Animate scale
    val scale by animateFloatAsState(
        targetValue = if (isToggled) 1.2f else 1f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "scale"
    )

    IconButton(
        onClick = { isToggled = !isToggled },
        modifier = Modifier
            .size(size+12.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(Color.Black, shape = CircleShape)
            .clip(CircleShape)
    ) {
        Icon(
            modifier = Modifier.size(size),
            painter = painterResource(id = if (isToggled) R.drawable.ic_fav_filled else R.drawable.ic_fav_outline),
            contentDescription = if (isToggled) "Remove from favorites" else "Add to favorites",
            tint = if (isToggled) RedFont else Color.White
        )
    }
}