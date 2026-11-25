package ru.otus.cookbook

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import okhttp3.OkHttpClient
import ru.otus.cookbook.data.RecipeRepository
import ru.otus.cookbook.data.cookbook

/**
 * Application for the Compose UI module. Provides [RecipeRepository] via [HasRecipeRepository].
 */
class App : Application(), HasRecipeRepository, ImageLoaderFactory {

    override val recipeRepository: RecipeRepository by lazy {
        RecipeRepository(recipes = cookbook)
    }

    override fun newImageLoader(): ImageLoader {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "CookbookCompose/1.0 (Android educational app; https://github.com/otus)"
                    )
                    .build()
                chain.proceed(request)
            }
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(client)
            .crossfade(true)
            .build()
    }
}
