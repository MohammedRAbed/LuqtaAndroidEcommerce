package com.example.luqtaecommerce.presentation.main.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.presentation.auth.signup.SignupViewModel
import com.example.luqtaecommerce.ui.components.LuqtaBackHeader
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProfilePicScreen(
    navController: NavController,
    currentUser: User?,
    viewModel: ProfileViewModel = koinViewModel()
) {

    val profilePicUri by viewModel.profilePicUri.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val updatePicSuccessful by viewModel.updatePicSuccessful.collectAsState()

    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onProfilePicSelected(it)
        }
    }

    LaunchedEffect(updatePicSuccessful) {
        if (updatePicSuccessful) {
            Toast.makeText(context, "تمت إضافة صورة شخصية", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        LuqtaBackHeader(title = "إضافة صورة شخصية", navController = navController)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile picture preview
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profilePicUri != null) {
                    AsyncImage(
                        model = profilePicUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = stringResource(R.string.add_photo),
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            if (currentUser?.profilePic != null) {
                OutlinedButton(
                    onClick = { viewModel.onProfilePicRemoved() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.remove_photo))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.width(150.dp)) {
                LuqtaButton(text = "اعتماد الصورة", enabled = profilePicUri != null && !isUploading) {
                    viewModel.updateProfilePicture(profilePicUri!!, context)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isUploading)
                CircularProgressIndicator(color = PrimaryCyan)
        }
    }
}
