package com.example.travelers_talks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class popUpEPPost : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_e_p_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnYes: Button = view.findViewById(R.id.btnYes)
        btnYes.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }

        val btnNo: Button = view.findViewById(R.id.btnNo)
        btnNo.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }
    }
}