package com.example.nqueen;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button check;
    Spinner n_value;
    Button set;
    GridLayout gridLayout;
    int[][] gridMatrix;
    int n;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        n_value = findViewById(R.id.n_value);
        set = findViewById(R.id.set);
        gridLayout = findViewById(R.id.gridLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.n_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        n_value.setAdapter(adapter);

        n_value.setSelection(adapter.getPosition("4"));
        n = 4;
        createGrid(n);

        set.setOnClickListener(view -> {
            n = Integer.parseInt(n_value.getSelectedItem().toString());
            createGrid(n);

        });

        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logGridMatrix(n);
                boolean result = checkBoard(gridMatrix, n);
                if (result) {
                    Toast.makeText(getApplicationContext(), "You Won", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "You Lost", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createGrid(int n) {
        gridLayout.removeAllViews();
        gridMatrix = new int[n][n];
        gridLayout.setColumnCount(n);
        gridLayout.setRowCount(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int row = i;
                final int col = j;
                final ImageButton button = new ImageButton(this);
                button.setImageResource(android.R.color.transparent);
                button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                button.setAdjustViewBounds(true);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleImage(button, row, col);
                    }
                });
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.width = 0;
                param.height = 0;
                param.columnSpec = GridLayout.spec(j, 1f);
                param.rowSpec = GridLayout.spec(i, 1f);
                button.setLayoutParams(param);

                button.setBackgroundResource(R.drawable.cellbackground);

                gridLayout.addView(button);
            }
        }
    }

    private void toggleImage(ImageButton button, int i, int j) {
        if (gridMatrix[i][j] == 0) {
            button.setImageResource(R.drawable.image1);
            gridMatrix[i][j] = 1;
        } else {
            button.setImageResource(android.R.color.transparent);
            gridMatrix[i][j] = 0;
        }
        //logGridMatrix(n);  // Log the matrix every time a change is made
    }

    private boolean checkBoard(int[][] board, int n) {
        int queenCount = 0;

        // Iterate through each cell in the board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 1) {
                    queenCount++;
                    if (!isSafe(board, n, i, j)) {
                        // If placing this queen is not safe, return false
                        return false;
                    }
                }
            }
        }

        // After checking all queens, ensure exactly n queens are placed
        return queenCount == n;
    }


    public boolean isSafe(int[][] board, int n, int i, int j) {
        // Check row
        for (int c = 0; c < j; c++) {
            if (board[i][c] == 1) {
                return false;
            }
        }

        // Check upper-left diagonal
        int r = i - 1, c = j - 1;
        while (r >= 0 && c >= 0) {
            if (board[r][c] == 1) {
                return false;
            }
            r--;
            c--;
        }

        // Check lower-left diagonal
        r = i + 1;
        c = j - 1;
        while (r < n && c >= 0) {
            if (board[r][c] == 1) {
                return false;
            }
            r++;
            c--;
        }

        return true;
    }




    private void logGridMatrix(int n) {
        StringBuilder matrixString = new StringBuilder();

        for (int[] row : gridMatrix) {
            for (int cell : row) {
                matrixString.append(cell).append(" ");
            }
            matrixString.append("\n");
        }
        Log.d(TAG, "Grid Matrix:\n" + matrixString.toString());
    }
}
