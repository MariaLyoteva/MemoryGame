@file:Suppress("DEPRECATION")

package com.example.memory1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.example.memory1.R.drawable.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val CARD_BACK_TEXT = "cardBack"
        const val DELAY_MILLIS = 650L
    }

    private lateinit var buttons: List<Button>
    private val icons: MutableList<Int> =
        List(2) { listOf(bat, koala, fox, leopard, bee, tiger) }.flatten().toMutableList()
    private val cardBack = dev_orange
    private var clicksCount = 0
    private var lastClickedIndex = -1
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeButtons()
        setupPlayAgainButton()
        resetGame()
    }


    private fun initializeButtons() {
        buttons = (1..12).map {
            findViewById<Button>(resources.getIdentifier("button$it", "id", packageName))
        }

        icons.shuffle()
        buttons.forEachIndexed { index, button ->
            button.setupCardButton(index)
        }
    }

    private fun Button.setupCardButton(index: Int) {
        setBackgroundResource(cardBack)
        text = CARD_BACK_TEXT
        textSize = 0.0F
        setOnClickListener { handleCardClick(index) }
    }

    private fun setupPlayAgainButton() {
        findViewById<Button>(R.id.playAgain).setOnClickListener { resetGame() }
    }


    private fun handleCardClick(index: Int) {
        if (buttons[index].text == CARD_BACK_TEXT) {
            buttons[index].revealCard(index)

            if (clicksCount == 1 && index != lastClickedIndex) {
                checkForMatch(index)
            } else {
                lastClickedIndex = index
                clicksCount++
            }
        }
    }

    private fun Button.revealCard(index: Int) {
        setBackgroundResource(icons[index])
        text = icons[index].toString()
    }

    private fun checkForMatch(currentIndex: Int) {
        if (icons[currentIndex] != icons[lastClickedIndex]) {
            hideCardsAfterDelay(currentIndex, lastClickedIndex)
        } else {
            disableMatchedCards(currentIndex, lastClickedIndex)
        }
        clicksCount = 0
    }

    private fun hideCardsAfterDelay(vararg indices: Int) {
        handler.postDelayed({
            indices.forEach { index ->
                buttons[index].resetToCardBack()
            }
        }, DELAY_MILLIS)
    }

    private fun disableMatchedCards(vararg indices: Int) {
        indices.forEach { index ->
            buttons[index].isClickable = false
        }
    }

    private fun Button.resetToCardBack() {
        setBackgroundResource(cardBack)
        text = CARD_BACK_TEXT
    }


    private fun resetGame() {
        icons.shuffle()
        buttons.forEach { button ->
            button.apply {
                setBackgroundResource(cardBack)
                text = CARD_BACK_TEXT
                textSize = 0.0F
                isClickable = true // Reset the button to be clickable
            }
        }
        clicksCount = 0
        lastClickedIndex = -1
    }
}



