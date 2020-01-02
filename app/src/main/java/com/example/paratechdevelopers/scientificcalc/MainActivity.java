package com.example.paratechdevelopers.scientificcalc;
import java.lang.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    boolean isDegree = true;
    Ans ans = new Ans();
//    double ans=0;
    private EditText edt_disp;
    private Button btn_calculate,btn_ac,btn_ans,btn_c, btn_onDeg;
    private TextView txt_ans, txt_degRadId;
    static final double pi = 3.14159265358979323846264338327950288419716939937510582, eu = 2.7182818284590452353602874713527;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_ans = findViewById(R.id.txt_ans);
        edt_disp = findViewById(R.id.edt_disp);
        btn_calculate = findViewById(R.id.btn_calculate);
        btn_ac = findViewById(R.id.btn_ac);
        btn_ans = findViewById(R.id.btn_ans);
        btn_c = findViewById(R.id.btn_c);
        btn_onDeg = findViewById(R.id.btn_deg);
        txt_degRadId = findViewById(R.id.txt_degRadId);
        edt_disp.setShowSoftInputOnFocus(false);


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

        //to display answer on enter from keyboard
        edt_disp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onCalculate(null);
                }
                return false;
            }
        });
    }



    public void onBtn(View v){
        Button b = (Button)v;
//        String buttonText = edt_disp.getText().toString();
//        int x = edt_disp.getSelectionStart();
//
//        buttonText += b.getText().toString();
//        edt_disp.setText(buttonText);
//        edt_disp.setSelection(edt_disp.getText().length());
        StringBuffer txt = new StringBuffer(edt_disp.getText().toString());
        String buttonText = b.getText().toString();
        if(buttonText.equals("Ans"))
            buttonText = ""+ans.a;
        else if(buttonText.length()>1)
            buttonText+="(";
        int start = Math.max(edt_disp.getSelectionStart(), 0);
        int end = Math.max(edt_disp.getSelectionEnd(), 0);
        int x = Math.min(start, end);
        int y = Math.max(start, end);
        if(buttonText.equals("\u232b")){
            if(x>0){
                x=x-1;
            }
            buttonText="";
        }
        txt.replace(x,y,
                buttonText);
        edt_disp.setText(txt);
        edt_disp.setSelection(x+buttonText.length());



    }
    public void onCalculate(View v){
        String exp = edt_disp.getText().toString().trim();
        if(!exp.isEmpty()){
            calc(exp);
            if(!ans.isError){
                txt_ans.setText(""+ans.a);
            }
            else
                txt_ans.setText("Error");
        }
    }

    public  void onDeg(View v){
        if(isDegree){
            txt_degRadId.setText("RAD");
            btn_onDeg.setText("DEG");
            isDegree = false;
        }
        else{
            txt_degRadId.setText("DEG");
            btn_onDeg.setText("RAD");
            isDegree = true;
        }
    }

    public void onAClear(View v){
        txt_ans.setText("");
    }

    public void onClear(View v){
        edt_disp.setText("");
    }


    public void calc(String exp){
//        exp = exp.toLowerCase();
        exp = exp.replaceAll("sin","(S");
        exp = exp.replaceAll("cos","(C");
        exp = exp.replaceAll("tan","(T");
        exp = exp.replaceAll("log","(L");
        exp = exp.replaceAll("ln","(N");
//        exp = exp.replace("sin","S");
//        edt_disp.setText(exp);


        char[] tokens = exp.toCharArray();
        Stack<Double> values = new Stack<Double>();
        Stack<Character> ops = new Stack<Character>();

        for(int i = 0;i<tokens.length;i++){
            if(tokens[i]=='×'){
                tokens[i]='*';
            }
            else if(tokens[i]=='÷'){
                tokens[i]='/';
            }
            else if(tokens[i]=='{'){
                tokens[i]='(';
            }
            else if(tokens[i]=='}'){
                tokens[i]=')';
            }
        }

        for (int i = 0; i < tokens.length; i++)
        {


            if(tokens[i]=='π' || tokens[i]=='e'){
                if((i>0)&&((tokens[i-1]>='0'&&tokens[i-1]<='9')||tokens[i-1]=='π'||tokens[i-1]=='e'||tokens[i-1]==')')){
                    if(tokens[i]=='π'){
                        values.push(pi);
                        ops.push('*');
//                        if(applyOp(ops, values)){
//                            ans.isError=true;
//                            return;
//                        }

                    }

                    else if(tokens[i]=='e'){
                        values.push(eu);
                        ops.push('*');
//                        if(applyOp(ops, values)){
//                            ans.isError=true;
//                            return;
//                        }
                    }

                }
                else{
                    if(tokens[i]=='π')
                        values.push(pi);
                    else if(tokens[i]=='e')
                        values.push(eu);
                }
                if(((i+1)<tokens.length) && ((tokens[i+1]>='0'&&tokens[i+1]<='9')||tokens[i+1]=='(')){
                    tokens[i] = '*';

                }
            }

            else if(tokens[i]=='-'){
                if(i>0&&tokens[i-1]!='('&&tokens[i-1]!='*'&&tokens[i-1]!='/'&&tokens[i-1]!='^'){
                    while (!ops.empty() && (precedence('+')<= precedence(ops.peek()))){
                        if(applyOp(ops, values)){
                            ans.isError=true;
                            return;
                        }
                    }
                    ops.push('+');
                }
                values.push(-1.0);
                tokens[i] = 'o';


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
                            ans.isError = true;
                            return;
                        }

                    }

                    sbuf.append(tokens[i++]);
                }

                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            }

            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '('){
                if(!ops.empty() &&tokens[i-1]!='S'&& tokens[i-1]!='C'&& tokens[i-1]!='T'&& tokens[i-1]!='N'&& tokens[i-1]!='L'){
                    while (!ops.empty() && (precedence('b')< precedence(ops.peek()))){
                        if(applyOp(ops, values)){
                            ans.isError=true;
                            return;
                        }
                    }
                }
                if( i>0 && (tokens[i-1]>'0'&&tokens[i-1]<'9'))
                    ops.push('*');
                ops.push(tokens[i]);
            }


                // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')')
            {
//                boolean flg1 = false;
                while (ops.peek() != '('){
                    if(applyOp(ops, values)){
                        ans.isError=true;
                        return;
                    }
                    if(ops.isEmpty()){
                        ans.isError = true;
                        return;
                    }
                }
                ops.pop(); // pop (
                //
                char ch;
                if(ops.size()>0){
                    ch = ops.peek();
                    if(ch=='S'||ch=='C'||ch=='T'||ch=='s'||ch=='c'||ch=='t'||ch=='L'||ch=='N'){
                        if(applyOp(ops, values)){
                            ans.isError=true;
                            return;
                        }
                    }
                }
                if( i+1<tokens.length && ((tokens[i+1]>'0'&&tokens[i+1]<'9')||tokens[i+1]=='.'||(tokens[i+1]=='(')))
                    ops.push('*');

            }

            else if(tokens[i]=='!'){
                values.push(fact(values.pop()));
            }

            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == 'o' ||
                    tokens[i] == '*' || tokens[i] == '/' || tokens[i]=='%' || tokens[i] == '^'||tokens[i] == 'S'||tokens[i] == 'C'||tokens[i] == 'T'||tokens[i] == 's'||tokens[i] == 'c'||tokens[i] == 't'||tokens[i] == 'L'||tokens[i] == 'N')
            {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                if(tokens[i+1] == '+' || tokens[i+1] == 'o' ||
                        tokens[i+1] == '*' || tokens[i+1] == '/' || tokens[i+1]=='%' || tokens[i+1] == '^'){
                    ans.isError = true;
                    return;
                }
                while (!ops.empty() && (precedence(tokens[i])<= precedence(ops.peek()))){
                    if(applyOp(ops, values)){
                        ans.isError=true;
                        return;
                    }
                }
                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }

        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty()) {
            if(values.size()>0){
                if(applyOp(ops, values)){
                    ans.isError=true;
                    return;
                }
            }
            else{
                ans.isError = true;
                return;
            }
        }


        // Top of 'values' contains result, return it

        if(!values.empty()){
            ans.a = values.pop();
        }
        else{
            ans.isError = true;
            return;
        }
        if(values.empty()&&ops.empty()){
            ans.isError = false;
            return;
        }

        else{
            ans.isError = true;
            return;
        }
    }


    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public boolean applyOp(Stack<Character> ops, Stack<Double> val)
    {
        if(ops.isEmpty())
            return true;
        char op = ops.pop();
        if(val.isEmpty())
            return true;
        double a, b = val.pop();

        switch (op)
        {
            case 'L':{
                ops.pop();
                val.push(Math.log10(b));
                return false;
            }

            case 'N':{
                ops.pop();
                val.push(Math.log(b));
                return false;
            }

            case 'S':{
                ops.pop();
                if(isDegree)
                    b=Math.toRadians(b);
                val.push(Math.sin(b)) ;
                if(val.peek()<(Math.pow(10,-15))&&val.peek()>(-1*Math.pow(10,-15))){
                    val.pop();
                    val.push(0d);
                }
                return false;
            }

            case 'C':{
                ops.pop();
                if(isDegree)
                    b=Math.toRadians(b);
                val.push(Math.cos(b)) ;
                if(val.peek()<(Math.pow(10,-15))&&val.peek()>(-1*Math.pow(10,-15))){
                    val.pop();
                    val.push(0d);
                }
                return false;
            }

            case 'T':{
                ops.pop();
                if(isDegree)
                    b=Math.toRadians(b);
                val.push(Math.tan(b));
                if(val.peek()<(Math.pow(10,-15))&&val.peek()>(-1*Math.pow(10,-15))){
                    val.pop();
                    val.push(0d);
                }
                if(val.peek()>Math.pow(10,15)){
                    val.pop();
                    return true;
                }
                return false;
            }

            case 's':{
                ops.pop();
                val.push(Math.asin(b));
                return false;
            }
            case 'c':{
                ops.pop();
                val.push(Math.acos(b));
                return false;
            }

            case 't':{
                ops.pop();
                val.push(Math.atan(b));
                return false;
            }

            case '+':{
                if(val.isEmpty())
                    return true;
                a = val.pop();
                val.push(a + b) ;
                return false;
            }

            case '-':{
                if(val.isEmpty())
                    return true;
                a = val.pop();
                val.push(a - b) ;
                return false;
            }
            case '*':{
                if(val.isEmpty())
                    return true;
                a = val.pop();
                val.push(a * b) ;
                return false;
            }
            case 'o':{
                if(val.isEmpty())
                    return true;
                a = val.pop();
                val.push(a * b) ;
                return false;
            }
            case '/':{
                if(val.isEmpty()||b==0)
                    return true;
                a = val.pop();
                val.push(a / b) ;
                return false;
            }
            case '^':{
                if(val.isEmpty())
                    return true;
                a = val.pop();
                val.push(Math.pow(a,b)) ;
                return false;
            }
            case '%':{
                if(val.isEmpty()||b==0)
                    return true;
                a = val.pop();
                val.push(a % b) ;
                return false;
            }
            default: return true;
        }
//        return true;
    }

    public double fact(double x){
        double ans = 1;
        for(int i=1;i<=x;i++){
            ans*=i;
        }
        return ans;
    }
    int precedence(char op){
        if(op == '-')
            return 1;
        else if(op == '+')
            return 2;
        else if(op =='*')
            return 3;
        else if(op == '/' || op == '%')
            return 4;
        else if (op=='b')
            return 5;
        else if(op == '^')
            return 6;
        else if(op == 'S'||op == 'C'||op == 'T'||op == 'L'||op == 'N'||op == 's'||op == 'c'||op == 't')
            return 7;
        else if (op =='o')
            return 8;
        else return 0;
    }
}
//text select handle