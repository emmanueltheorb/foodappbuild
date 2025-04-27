package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.orb.bmdadmin.R
import com.orb.bmdadmin.data.ReservedFood
import com.orb.bmdadmin.data.ReservedFoodExample

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodOrderItem(
    modifier: Modifier = Modifier,
    food: ReservedFood,
    onFoodClicked: () -> Unit,
    onFoodHeld: () -> Unit
) {
    Card(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onFoodClicked()
                },
                onLongClick = {
                    onFoodHeld()
                }
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            FoodImage(imgUrl = food.imgUrl)
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .alpha(.2f), color = Color.Black
            ) {}
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = food.name,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier.width(20.dp))
                Text(
                    text = food.code,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Green
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FoodImage(
    modifier: Modifier = Modifier,
    imgUrl: String
) {
    GlideImage(
        modifier = modifier.fillMaxSize(),
        model = imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun FoodOrderItemPreview() {
    FoodOrderItem(
        food = ReservedFoodExample,
        onFoodClicked = {}
    ) { }
}