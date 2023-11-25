package com.example.travelers_talks

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class popUpCPCancel : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_c_p_cancel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnYes: Button = view.findViewById(R.id.btnYes)
        btnYes.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
            val intent = Intent(requireContext(),MainActivity::class.java)
            intent.putExtra("opcionMain", 0)
            startActivity(intent)
        }

        val btnNo: Button = view.findViewById(R.id.btnNo)
        btnNo.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }
    }
}