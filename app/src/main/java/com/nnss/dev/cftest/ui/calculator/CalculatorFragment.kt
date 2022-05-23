package com.nnss.dev.cftest.ui.calculator

import androidx.activity.OnBackPressedCallback
import com.nnss.dev.cftest.R
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.collectLA
import com.nnss.dev.cftest.databinding.FragmentCalculatorBinding
import com.nnss.dev.cftest.ui.adapter.HistoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import java.util.*

class CalculatorFragment :
    BaseFragment<FragmentCalculatorBinding, CalculatorViewModel>({
        FragmentCalculatorBinding.inflate(
            it
        )
    }) {
    private lateinit var input: StringBuilder
    private var hasOp = false
    private var showPad = true
    private lateinit var mAdapter: HistoryAdapter
    private lateinit var shuffled: List<Int>

    override val viewModel: CalculatorViewModel by viewModel()
    override fun backPressCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
    }

    override fun initViews() {
        input = StringBuilder()
        with(ui) {
            val nums = listOf(1,2,3,4,5,6,7,8,9,0)
            shuffled = nums.shuffled()
            val numButtons =
                listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            val opButtons = listOf(btnDiv, btnMultiply, btnMinus, btnPlus)

            for (i in shuffled.indices) {
                numButtons[i].text = getNumString(i)
            }

            numButtons.forEach { btn ->
                btn.setOnClickListener {
                    insert(btn.text.toString())
                }
            }

            opButtons.forEach { btn ->
                if (showPad){
                    btn.setOnClickListener {
                        onOperator(btn.text.toString())
                    }
                }
            }


            btnPeriod.setOnClickListener {
                insert(btnPeriod.text.toString())
            }

            btnCe.setOnClickListener {
                clear()
            }

            btnEquals.setOnClickListener {
                val currentValue = textValue.text.toString()
                if (currentValue.isNotEmpty() && currentValue.isValid()) {
                    input.append(" $currentValue")
                    when {
                        input.contains("/") -> {
                            textValue.text = doOperation("/")
                        }
                        input.contains("*") -> {
                            textValue.text = doOperation("*")
                        }
                        input.contains("-") -> {
                            textValue.text = doOperation("-")
                        }
                        input.contains("+") -> {
                            textValue.text = doOperation("+")
                        }
                    }
                    hasOp = true
                } else {
                    clear()
                }
            }

            btnToggle.setOnClickListener {
                if (showPad) {
                    showPad = false
                    btnToggle.setIconResource(R.drawable.ic_pad)
                    rvHistory.visibility = View.VISIBLE
                    numButtons.forEach {
                        it.visibility = View.INVISIBLE
                    }
                    btnCe.visibility = View.INVISIBLE
                    viewModel.getInputs()
                } else {
                    showPad = true
                    btnToggle.setIconResource(R.drawable.ic_history)
                    rvHistory.visibility = View.GONE
                    numButtons.forEach {
                        it.visibility = View.VISIBLE
                    }
                    btnCe.visibility = View.VISIBLE
                }
            }

            mAdapter = HistoryAdapter()

            rvHistory.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    override fun subscribe() {
        with(viewModel) {
            state.collectLA(viewLifecycleOwner) { result ->
                result.let { data ->
                    val sorted = data.sortedByDescending { it.id }
                    mAdapter.setItems(sorted)
                }
            }
        }
    }

    private fun getNumString(index: Int) : String {
        return shuffled[index].toString()
    }

    private fun insert(value: String) {
        if (hasOp) {
            ui.textValue.text = ""
            hasOp = false
        }
        ui.textValue.append(value)
    }

    private fun clear() {
        hasOp = false
        ui.textValue.text = ""
        input.clear()
    }

    private fun String.isValid(): Boolean {
        return !this.contains(" ") || !this.startsWith("-") || !this.startsWith("+") || !this.startsWith(
            "/"
        ) || !this.startsWith("*")
    }

    private fun onOperator(op: String) {
        hasOp = true
        input.append(" " + ui.textValue.text.toString())
        input.append(" $op")
    }

    private fun reduceZeros(result: String): String {
        val dResult = result.toDouble()
        val format = DecimalFormat("0.#")
        val finalvalue = format.format(dResult)

        input.append("\n = $finalvalue")
        viewModel.saveInput(input.toString())
        clear()
        return finalvalue
    }

    private fun doOperation(op: String): String {
        val splitVal = input.split(op)
        val num1 = splitVal[0].toDouble()
        val num2 = splitVal[1].toDouble()

        return when (op) {
            "/" -> {
                reduceZeros((num1 / num2).toString())
            }
            "*" -> {
                reduceZeros((num1 * num2).toString())
            }
            "-" -> {
                reduceZeros((num1 - num2).toString())
            }
            else -> {
                reduceZeros((num1 + num2).toString())
            }
        }

    }

}