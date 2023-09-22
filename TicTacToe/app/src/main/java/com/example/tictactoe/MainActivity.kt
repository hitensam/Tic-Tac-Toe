package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import android.text.Layout.Alignment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment as a
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            getGame(this);
        }
    }
}

@Composable
//@Preview
fun getGame(
    context: Context
) {
    var playerXScore = remember {
        mutableStateOf(0)
    }
    var playerYScore = remember {
        mutableStateOf(0)
    }
    var countSteps = remember {
        mutableStateOf(0)
    }

    var arr = remember {
        mutableStateOf(Array(3) {
            arrayOfNulls<String>(3)
        })
    }

    val currentPlayer = remember {
        mutableStateOf("O")
    }

    var initialBoard = remember {
        mutableStateOf(Array(3) {
            arrayOfNulls<String>(3)
        })
    }

    var initialPlayer = remember {
        mutableStateOf("O")
    }

    var lastWinning = remember {
        mutableStateOf("none")
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(

            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = a.CenterHorizontally
        )
        {
            Text(
                text = "Tic Tac Toe", color = Color.Black, fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal, fontSize = 55.sp,
            )
            Text(
                text = "Player O's Score: ${playerYScore.value}", modifier = Modifier.padding(6.dp),
                fontSize = 25.sp
            )
            Text(
                text = "Player X's Score: ${playerXScore.value}", modifier = Modifier.padding(6.dp),
                fontSize = 25.sp
            )

        }
    }
    Row(
        modifier = Modifier.padding(top=80.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = a.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column() {
                    for (col in 0..2) {
                        Row() {
                            for (row in 0..2) {
                                Button(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .height(100.dp)
                                        .width(100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black
                                    ), shape = CutCornerShape(0.dp),
                                    onClick = {

                                        if (arr.value[row][col] == null) {
                                            arr.value[row][col] = currentPlayer.value;
                                            countSteps.value = countSteps.value + 1;

                                            if (checkWin(
                                                    arr.value,
                                                    currentPlayer.value,
                                                    row,
                                                    col
                                                )
                                            ) {
                                                if (currentPlayer.value == "X") {
                                                    playerXScore.value = playerXScore.value + 1;
                                                    lastWinning.value = "X"
                                                } else {
                                                    playerYScore.value = playerYScore.value + 1;
                                                    lastWinning.value = "O"
                                                }
                                                Toast.makeText(
                                                    context,
                                                    "${currentPlayer.value} won!!",
                                                    Toast.LENGTH_SHORT
                                                ).show();
                                                val scope = CoroutineScope(Dispatchers.IO)

                                                scope.launch {
                                                    // Wait for 1 seconds.
                                                    delay(1000)
                                                    arr.value = (Array(3) {
                                                        arrayOfNulls<String>(3)
                                                    })
                                                    currentPlayer.value = lastWinning.value;
                                                    countSteps.value = 0;

                                                }
                                                countSteps.value = 0;
//                                            Toast.makeText(context, "countSteps ${countSteps.value}", Toast.LENGTH_SHORT).show()

                                            }

                                            Log.i("currentPlayer: ", "${currentPlayer.value}")
                                            Log.i("inArray ${row} ${col}", "${arr.value[row][col]}")
                                            if (currentPlayer.value == "O") {
                                                currentPlayer.value = "X"
                                            } else currentPlayer.value = "O"
                                        }
                                        if (countSteps.value == 9) {
                                            val scope = CoroutineScope(Dispatchers.IO)
                                            Toast.makeText(
                                                context,
                                                "It's a draw!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            scope.launch {
                                                // Wait for 5 seconds.
                                                delay(1000)
                                                arr.value = (Array(3) {
                                                    arrayOfNulls<String>(3)
                                                })
                                                if (lastWinning.value == "none")
                                                    currentPlayer.value = initialPlayer.value;
                                                else
                                                    currentPlayer.value = lastWinning.value
                                                countSteps.value = 0;

                                            }
                                            countSteps.value = 0;

                                        }

                                    }
                                ) {
                                    Text(
                                        text = arr.value[row][col] ?: "", color = Color.White,
                                        fontWeight = FontWeight.Bold, fontSize = 50.sp,
                                    )
                                }
                            }
                        }
                    }

                }
            }

            Row(modifier = Modifier.fillMaxSize(),
                ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
//                verticalArrangement = Arrangement.Center,
                horizontalAlignment = a.CenterHorizontally
            ) {


                Text(
                    text = "Current Player: ${currentPlayer.value}",
                    modifier = Modifier.padding(6.dp),
                    fontSize = 25.sp
                )
                Text(
                    text = "Total moves: ${countSteps.value}", modifier = Modifier.padding(6.dp),
                    fontSize = 25.sp
                )
                if (lastWinning.value != "none") {
                    Text(
                        text = "Last Won: ${lastWinning.value}", modifier = Modifier.padding(6.dp),
                        fontSize = 25.sp
                    )
                }
                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        arr.value = (Array(3) {
                            arrayOfNulls<String>(3)
                        })
                        if (lastWinning.value == "none")
                            currentPlayer.value = initialPlayer.value;
                        else
                            currentPlayer.value = lastWinning.value
                        countSteps.value = 0
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = CutCornerShape(0.dp),
                ) {
                    Text(
                        text = "RESET BOARD",
                        fontSize = 25.sp
                    )
                }
                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        arr.value = (Array(3) {
                            arrayOfNulls<String>(3)
                        })

                        currentPlayer.value = initialPlayer.value;
                        lastWinning.value = "none"
                        playerXScore.value = 0;
                        playerYScore.value = 0;
                        countSteps.value = 0;
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = CutCornerShape(0.dp),
                ) {
                    Text(
                        text = "RESET SCORE",
                        fontSize = 26.sp
                    )
                }
            }
        }
        }
    }
}

fun checkWin(arr: Array<Array<String?>>,currentSymbol: String, xPos: Int, yPos: Int): Boolean{

    var ifWon: Boolean = false;


    //checks for daigonal.
    if(xPos==0&&yPos==0 || xPos==1&&yPos==1 || xPos==2&&yPos==2){
        for(check in 0..2){
            ifWon = true;
            if(arr[check][check]!=currentSymbol){
                ifWon = false;
                break
            }
        }
    }
    if(ifWon){
        return true;
    }
    if(xPos==2&&yPos==0 || xPos==1&&yPos==1 || xPos==0&&yPos==2){
        if(arr[0][2] == arr[2][0] && arr[2][0] == arr[1][1] && arr[1][1] == currentSymbol){
            ifWon = true
        }
        else ifWon = false
    }
    if(ifWon){
        return true;
    }
    //checks for row
    for(check in 0..2){
        ifWon=true
        if(arr[check][yPos]==null ||arr[check][yPos]!=currentSymbol){
            ifWon=false;
            break;
        }
    }
    if(ifWon){
        return true;
    }
    //checks for column
    for(check in 0..2){
        ifWon = true;
        if(arr[xPos][check]==null ||arr[xPos][check]!=currentSymbol){
            ifWon = false;
            break;
        }
    }
    if(ifWon){
        return true;
    }

    return false;

}
