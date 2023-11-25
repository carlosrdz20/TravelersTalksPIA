package com.example.travelers_talks

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.Calendar


class popUpFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var myView : View

    private var day = 0
    private var month = 0
    private var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    var fechaInicial = "Initial Date"

    private var dayF = 0
    private var monthF = 0
    private var yearF = 0

    var savedDayF = 0
    var savedMonthF = 0
    var savedYearF = 0

    var fechaFinal = "Final Date"

    var myOption = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view

        val btnFechaInicial : Button = view.findViewById(R.id.btnFechaInicial)
        btnFechaInicial.setOnClickListener{
            getDateTimeCalendar(1)
            DatePickerDialog(requireContext(),this,year,month,day).show()
        }

        val textFechaInicial : TextView = view.findViewById(R.id.txtInitialDate)
        textFechaInicial.text = fechaInicial

        val btnFechaFinal : Button = view.findViewById(R.id.btnFechaFinal)
        btnFechaFinal.setOnClickListener{
            getDateTimeCalendar(2)
            DatePickerDialog(requireContext(),this,yearF,monthF,dayF).show()
        }

        val textFechaFinal : TextView = view.findViewById(R.id.txtFinalDate)
        textFechaFinal.text = fechaFinal

        val btnApply: Button = view.findViewById(R.id.btnApply)
        btnApply.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }

        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            // Llama a una función para cerrar el fragmento
            dismiss()
        }

        //Ponerle países al combobox
        val countries = listOf("USA", "Canada", "Mexico", "UK", "France", "Germany", "Spain")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countries)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.actvdropwon_field)
        autoCompleteTextView.setAdapter(adapter)
    }

    override fun onDateSet(p0: DatePicker?, Pyear: Int, Pmonth: Int, Pday: Int) {
        if(myOption === 1){
            savedDay = Pday
            savedMonth = Pmonth + 1
            savedYear = Pyear
            val txtFechaInicial : TextView = myView.findViewById(R.id.txtInitialDate)
            fechaInicial = "$savedDay - $savedMonth - $savedYear"
            txtFechaInicial.text = fechaInicial
        }else{
            savedDayF = Pday
            savedMonthF = Pmonth + 1
            savedYearF = Pyear
            val txtFechaFinal : TextView = myView.findViewById(R.id.txtFinalDate)
            fechaFinal = "$savedDayF - $savedMonthF - $savedYearF"
            txtFechaFinal.text = fechaFinal
        }
    }

    private fun getDateTimeCalendar(option : Int){
        myOption = option
        if(option === 1){
            val cal = Calendar.getInstance()
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)
        }else{
            val cal = Calendar.getInstance()
            dayF = cal.get(Calendar.DAY_OF_MONTH)
            monthF = cal.get(Calendar.MONTH)
            yearF = cal.get(Calendar.YEAR)
        }
    }
}