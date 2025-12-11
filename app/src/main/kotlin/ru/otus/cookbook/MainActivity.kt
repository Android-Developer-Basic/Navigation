package ru.otus.cookbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.otus.cookbook.databinding.ActivityMainBinding
import ru.otus.cookbook.ui.CookbookFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.cookbook_fragment_container, CookbookFragment())
            .setReorderingAllowed(true)
            .commit()
    }
}