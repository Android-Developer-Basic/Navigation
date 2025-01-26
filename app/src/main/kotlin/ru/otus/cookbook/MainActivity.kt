package ru.otus.cookbook

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.otus.cookbook.databinding.ActivityMainBinding
import ru.otus.cookbook.ui.CookbookFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Проверка, чтобы фрагмент не добавлялся заново при пересоздании активности
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, CookbookFragment())  // Добавляем фрагмент в контейнер
//                .commit()
//        }

        onBackPressedDispatcher.addCallback(this) {
            if (!findNavController(R.id.fragment_container_view).popBackStack()) {
                finish()
            }
        }
    }
}