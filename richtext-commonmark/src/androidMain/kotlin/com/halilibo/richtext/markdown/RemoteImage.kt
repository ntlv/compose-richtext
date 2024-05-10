package com.halilibo.richtext.markdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.AsyncImagePainter.State.Success
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

private val DEFAULT_IMAGE_SIZE = 64.dp

public val LocalRemoteImageModifierProvider: ProvidableCompositionLocal<Modifier> = compositionLocalOf { Modifier }

/** Implementation of RemoteImage by using Coil library for Android. */
@Composable
internal actual fun RemoteImage(
  url: String,
  contentDescription: String?,
  modifier: Modifier,
  contentScale: ContentScale
) {
  val painter = rememberAsyncImagePainter(
    ImageRequest.Builder(LocalContext.current)
      .data(data = url)
      .size(Size.ORIGINAL)
      .crossfade(true)
      .build()
  )

//  val density = LocalDensity.current

//  BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
//    val sizeModifier by remember(density, painter) {
//      derivedStateOf {
//        val painterIntrinsicSize = painter.state.painter?.intrinsicSize
//        if (painterIntrinsicSize != null &&
//          painterIntrinsicSize.isSpecified &&
//          painterIntrinsicSize.width != Float.POSITIVE_INFINITY &&
//          painterIntrinsicSize.height != Float.POSITIVE_INFINITY
//        ) {
//          val width = painterIntrinsicSize.width
//          val height = painterIntrinsicSize.height
//          val scale = if (width > constraints.maxWidth) {
//            constraints.maxWidth.toFloat() / width
//          } else {
//            1f
//          }
//
//          with(density) {
//            Modifier.size(
//              (width * scale).toDp(),
//              (height * scale).toDp()
//            )
//          }
//        } else {
//          // if size is not defined at all, Coil fails to render the image
//          // here, we give a default size for images until they are loaded.
//          Modifier.size(DEFAULT_IMAGE_SIZE)
//        }
//      }
//    }

  when (val state = painter.state) {
    is Empty -> BasicText(text = "Painter state \"EMPTY\", url: $url")
    is Error -> BasicText(text = "Image loading of url $url failed with: ${state.result.throwable}")
    is Loading -> BasicText(text = "Loading image: $url")
    is Success -> Image(
      painter = painter,
      contentDescription = contentDescription,
      modifier = LocalRemoteImageModifierProvider.current,
      contentScale = contentScale
    )
  }
}
