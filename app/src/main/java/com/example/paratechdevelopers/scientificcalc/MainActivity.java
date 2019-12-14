package com.example.paratechdevelopers.scientificcalc;
import java.util.Stack;
import java.lang.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

class Ans{
    Double a;
    String msg;
    boolean isError;
    Ans(){
        a = 0.0;
        msg = "";
        isError = false;
    }
}

public class MainActivity extends AppCompatActivity {
    Ans ans = new Ans();
//    double ans=0;
    private EditText edt_disp;
    private Button btn_calculate,btn_ac,btn_ans,btn_c;
    private TextView txt_ans;
    static final double pi = 3.14159265358979323846264338327950288419716939937510582;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_ans = findViewById(R.id.txt_ans);
        edt_disp = findViewById(R.id.edt_disp);
        btn_calculate = findViewById(R.id.btn_calculate);
        btn_ac = findViewById(R.id.btn_ac);
        btn_ans = findViewById(R.id.btn_ans);

//        btn_calculate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSubmitted();
//            }
//        });
//        btn_ac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClear();
//            }
//        });
//        btn_ans.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onAns();
//            }
//        });
    }
    public void onCalculate(View v){
        String exp = edt_disp.getText().toString().trim();
        if(!exp.isEmpty()){
            ans = calc(exp);
            if(!ans.isError){
                txt_ans.setText(""+ans.a);
            }
            else
                txt_ans.setText("Error");
//
        }


    }
    public void onAClear(View v){
        txt_ans.setText("");
    }
    public void onClear(View v){
        edt_disp.setText("");
    }
    public void onAns(View v){
        String exp = edt_disp.getText().toString().trim();
        edt_disp.setText(exp+ans.a);
        edt_disp.setSelection(edt_disp.getText().length());
    }
    public Ans calc(String exp){
        Ans ret = new Ans();
        char[] tokens = exp.toCharArray();
        // Stack for numbers: 'values'
        // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {
            if(tokens[i]=='×'){
                tokens[i]='*';
            }
            if(tokens[i]=='÷'){
                tokens[i]='/';
            }
            // Current token is a whitespace, skip it
//            if (tokens[i] == ' ')
//                continue;

            if(tokens[i]=='π'){
                if((i-1>-1)&&((tokens[i-1]>='0'&&tokens[i-1]<='9')||tokens[i-1]=='π')){
                    values.push(applyOp('*',pi , values.pop()));
                    }
                else{
                    values.push(pi);
                }
                if(((i+1)<tokens.length) && ((tokens[i+1]>='0'&&tokens[i+1]<='9')||tokens[i+1]=='(')){
                    tokens[i]='*';
//                        i--;
//                        continue;
                }

            }

            // Current token is a number, push it to stack for numbers
            if ((tokens[i] >= '0' && tokens[i] <= '9' ) || tokens[i] == '.')
            {
                StringBuffer sbuf = new StringBuffer();
                boolean flag = false;
                // There may be more than one digits in number

                while
                (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.')){
                    if(tokens[i]=='.'){
                        if(flag==false)
                            flag = true;
                        else{
                            ret.msg="more than one decimal point";
                            ret.isError = true;
                            return ret;
                        }

                    }

                    sbuf.append(tokens[i++]);
                }

                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            }

            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')')
            {
//                boolean flg1 = false;
                while (ops.peek() != '('){
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    if(ops.isEmpty()){
                        ret.isError = true;
                        ret.a = 0.0;
                        return ret;
                    }
                }

                ops.pop(); // pop (
            }

            else if(tokens[i]=='!'){
                values.push(fact(values.pop()));
            }

            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '*' || tokens[i] == '/' || tokens[i]=='%' || tokens[i] == '^')
            {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && (precedence(tokens[i])< precedence(ops.peek())))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }

        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty() && !values.empty()){
            if(ops.peek() == '!')
                values.push(fact(values.pop()));
            else if(values.size()>=2)
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            else{
                ret.isError = true;
                return ret;
            }
        }


        // Top of 'values' contains result, return it

        if(!values.empty()){
            ret.a = values.pop();
        }
        else{
            ret.isError = true;
            return ret;
        }
        if(values.empty()&&ops.empty())
            return ret;
        else{
            ret.isError = true;
            ret.msg = "invalid expression";
            ret.a = 0.0;
            return ret;
        }
    }


    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public double applyOp(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case '^':
                return Math.pow(a,b);
            case '%':
                return a%b;
        }
        return 0;
    }

    public double fact(double x){
        double ans = 1;
        for(int i=1;i<=x;i++){
            ans*=i;
        }
        return ans;
    }
    int precedence(char op){
        if(op == '+' || op == '-')
            return 1;
        else if(op =='*' || op == '/')
            return 2;
        else if(op == '^')
            return 3;
        else return 0;
    }
}
