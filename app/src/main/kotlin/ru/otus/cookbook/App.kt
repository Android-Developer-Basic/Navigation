package ru.otus.cookbook

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import okhttp3.OkHttpClient
import ru.otus.cookbook.data.RecipeRepository
import ru.otus.cookbook.data.cookbook

/**
 * The application. Provides access to dependencies.
 */
class App : Application(), HasRecipeRepository, ImageLoaderFactory {
    /**
     * The recipe repository.
     */
    override val recipeRepository: RecipeRepository by lazy {
        RecipeRepository(recipes = cookbook)
    }

    /**
     * Wikimedia Commons rejects OkHttp's default User-Agent with HTTP 403.
     * A descriptive agent is required for recipe image URLs to load.
     */
    override fun newImageLoader(): ImageLoader {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "Cookbook/1.0 (Android educational app; https://github.com/otus)"
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
