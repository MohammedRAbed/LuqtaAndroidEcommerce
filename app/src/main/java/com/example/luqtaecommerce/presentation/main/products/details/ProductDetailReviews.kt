package com.example.luqtaecommerce.presentation.main.products.details

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.review.ProductReview
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.ui.components.LuqtaButton
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ProductDetailReviews(
    reviewsState: ReviewUiState,
    addReviewState: AddReviewUiState,
    hasUserReviewed: Boolean,
    currentUser: User?,
    onAddReview: (rating: Int, comment: String) -> Unit,
    onClearAddedReviewState: () -> Unit
) {
    //val reviewsState by viewModel.productReviewsState.collectAsState()
    //val addReviewState by viewModel.addReviewState.collectAsState()
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {

    }
    LaunchedEffect(addReviewState) {
        addReviewState.review?.let {
            Toast.makeText(context, "تم إضافة التقييم بنجاح!", Toast.LENGTH_SHORT).show()
            rating = 0
            comment = ""
            onClearAddedReviewState()
        }

        addReviewState.error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            onClearAddedReviewState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        if (!hasUserReviewed) {
            // Add Review Form
            Text(
                text = "أضف تقييمك",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    IconButton(onClick = { rating = i }) {
                        Icon(
                            painter = painterResource(id = if (i <= rating) R.drawable.ic_star_filled else R.drawable.ic_star_unfilled),
                            contentDescription = null,
                            tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (rating > 0) "$rating/5" else "قيم المنتج")
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("اكتب تعليقك هنا") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(12.dp))

            LuqtaButton(
                enabled = rating in 1..5 && comment.isNotBlank(),
                text = "إرسال التقييم"
            ) {
                //viewModel.addReview(rating, comment)
                onAddReview(rating, comment)
                onClearAddedReviewState()
            }
            if (addReviewState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            }
        } else {
            Surface(
                color = Color(0xFF76F195),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "لقد قمت بإضافة تقييم إلى هذا المنتج",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF284B31),
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "التقييمات",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Reviews List
        when {
            reviewsState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            reviewsState.error != null -> {
                Text(
                    text = reviewsState.error ?: "فشل تحميل التقييمات",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            reviewsState.reviews.isEmpty() -> {
                Text(
                    text = "لا توجد تقييمات بعد",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            else -> {
                /*
                reviewsState.reviews.forEach { review ->
                    ReviewItem(review)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }*/

                val currentUserReview = if (hasUserReviewed) {
                    reviewsState.reviews.firstOrNull { it.user == currentUser!!.username }
                } else null

                if (currentUserReview != null) {
                    ReviewItem(currentUserReview)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }

                reviewsState.reviews
                    .filter { it.user != currentUser!!.username }
                    .forEach { review ->
                        ReviewItem(review)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
            }
        }
        /*when (reviewsState) {
            is Result.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is Result.Error -> {
                val msg = (reviewsState as? Result.Error)?.message ?: "فشل تحميل التقييمات"
                Text(text = msg, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is Result.Success -> {
                val reviews = reviewsState.data
                if (reviews.isEmpty()) {
                    Text(text = "لا توجد تقييمات بعد", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    reviews.forEach { review ->
                        ReviewItem(review)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }*/
    }
}

@Composable
fun ReviewItem(review: ProductReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = review.user,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row {
                for (i in 1..5) {
                    Icon(
                        painter = painterResource(id = if (i <= review.rating) R.drawable.ic_star_filled else R.drawable.ic_star_unfilled),
                        contentDescription = null,
                        tint = if (i <= review.rating) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = formatReviewDate(review.created), fontSize = 12.sp, color = Color.Gray)
        }
        Text(
            text = review.comment,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
        )
    }
}

fun formatReviewDate(isoDate: String): String {
    return try {
        val parsedDate = OffsetDateTime.parse(isoDate)
        val formatter =
            DateTimeFormatter.ofPattern("dd MMM yyyy") // or "dd/MM/yyyy", "MMM dd, yyyy"
        parsedDate.format(formatter)
    } catch (e: Exception) {
        isoDate // fallback
    }
}
