package com.itis.android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.R
import com.itis.android_tasks.databinding.FragmentAddDataPageBinding
import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.service.impl.AnimeServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddDataPageFragment : Fragment(R.layout.fragment_add_data_page) {
    private var _binding: FragmentAddDataPageBinding? = null
    private val binding: FragmentAddDataPageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDataPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        with(binding) {
            addNameValidation()
            addDescValidation()
            addYearValidation()

            btnAddAnime.setOnClickListener {
                val anime = AnimeModel(
                    etNameInput.text.toString(),
                    Integer.parseInt(etReleasedInput.text.toString()),
                    etDescInput.text.toString()
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    with(AnimeServiceImpl) {
                        if (!isAnimeUnique(anime.name, anime.released)) {
                            requireActivity().runOnUiThread {
                                showMessageToast(getString(R.string.not_unique_anime))
                            }
                        } else {
                            saveAnime(anime)
                            requireActivity().runOnUiThread {
                                showMessageToast(getString(R.string.add_anime_success))
                                etNameInput.setText("")
                                etReleasedInput.setText("")
                                etDescInput.setText("")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showMessageToast(msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun validateAll(): Boolean {
        with(binding) {
            return (validateYear(etReleasedInput.text.toString()) &&
                    etDescInput.text.isNotEmpty() &&
                    etNameInput.text.isNotEmpty()
                    ).also {
                    btnAddAnime.isEnabled = it
                }
        }
    }

    private fun addNameValidation() {
        with(binding) {
            etNameInput.addTextChangedListener { input ->
                if (!input.isNullOrEmpty()) {
                    tilName.error = ""
                }
                validateAll()
            }

            etNameInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused && etNameInput.text.toString().isEmpty()) {
                    tilName.error = getString(R.string.empty_anime_name_error)
                }
            }
        }
    }

    private fun validateYear(year: String?): Boolean {
        if (year.isNullOrEmpty()) {
            return false
        }
        return Integer.parseInt(year) <= Calendar.getInstance().get(Calendar.YEAR)
    }

    private fun addYearValidation() {
        with(binding) {
            etReleasedInput.addTextChangedListener { input ->
                if (validateYear(input.toString())) {
                    tilReleased.error = ""
                }
                validateAll()
            }

            etReleasedInput.setOnFocusChangeListener { _, isFocused ->
                val year = etReleasedInput.text.toString()
                if (!isFocused) {
                    if (year.isEmpty()) {
                        tilReleased.error = getString(R.string.empty_released_error)
                    } else if (!validateYear(year)) {
                        tilReleased.error = getString(R.string.invalid_released_error)
                    }
                }
            }
        }
    }

    private fun addDescValidation() {
        with(binding) {
            etDescInput.addTextChangedListener { input ->
                if (!input.isNullOrEmpty()) {
                    tilDesc.error = ""
                }
                validateAll()
            }

            etDescInput.setOnFocusChangeListener { _, isFocused ->
                if (!isFocused && etDescInput.text.toString().isEmpty()) {
                    tilDesc.error = getString(R.string.empty_desc_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ADD_DATA_PAGE_FRAGMENT_TAG = "ADD_DATA_PAGE_FRAGMENT_TAG"
    }
}
