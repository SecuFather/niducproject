package net.netii.niducproject;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class NActivity extends Activity implements OnClickListener{
	final private int startB = 0;
	final private int cardB = 1;
	final private int sizeB = 2;
	final private int sizeCount = 3;
	final private int finishB = 5;
	final private int buttonsByRow = 6;	
	final private int n = 8;
	private long time[] = new long[n];
	
    private LinearLayout layout;
    private LinearLayout[] row = new LinearLayout[n+1];
    private ToggleButton[] startShopping = new ToggleButton[n];
    private TextView[] cashdeskId = new TextView[n];
    private CheckBox[] cardPaid = new CheckBox[n];
    private RadioGroup[] shoppingSize = new RadioGroup[n];
    private Button[] finishShopping = new Button[n];    
    private EditText segment;
    private Button submit;
                   
    private DBHelper dbHelp;

	public void initWidgets(){
		layout = (LinearLayout)findViewById(R.id.main_layout);        	                      
        
        for(int i=0; i<n; ++i){        	
        	
        	row[i] = new LinearLayout(this);
        	row[i].setOrientation(LinearLayout.HORIZONTAL);
        	
        	cashdeskId[i] = new TextView(this);
        	cashdeskId[i].setText(String.valueOf(i));
        	cashdeskId[i].setTextSize(30);
        	row[i].addView(cashdeskId[i]);
        	
        	startShopping[i] = new ToggleButton(this);
        	startShopping[i].setId(buttonsByRow*i + startB);
        	startShopping[i].setOnClickListener(this);
        	row[i].addView(startShopping[i]);        	
        	
        	cardPaid[i] = new CheckBox(this);
        	cardPaid[i].setId(buttonsByRow*i+cardB);
        	cardPaid[i].setOnClickListener(this);
        	row[i].addView(cardPaid[i]);
        	        	
        	shoppingSize[i] = new RadioGroup(this);
        	shoppingSize[i].setOrientation(RadioGroup.HORIZONTAL);
        	for(int j=0; j<sizeCount; ++j){
        		RadioButton tmpRadio = new RadioButton(this);
        		tmpRadio.setId(buttonsByRow*i+j + sizeB);
        		shoppingSize[i].addView(tmpRadio);        		
        	}        	
        	shoppingSize[i].check(buttonsByRow*i + sizeB);
        	shoppingSize[i].setOnClickListener(this);
        	row[i].addView(shoppingSize[i]);
        	
        	finishShopping[i] = new Button(this);
        	finishShopping[i].setText("finish");
        	finishShopping[i].setId(buttonsByRow*i + finishB);
        	finishShopping[i].setOnClickListener(this);
        	row[i].addView(finishShopping[i]);
        	
        	layout.addView(row[i]);        	        	
        }
        row[n] = new LinearLayout(this);
    	row[n].setOrientation(LinearLayout.HORIZONTAL);
    	
    	segment = new EditText(this);
    	segment.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
    	segment.setWidth(80);    	
    	row[n].addView(segment);
    	
    	submit = new Button(this);
    	submit.setText("set");
    	submit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				try{ 
					int seg = Integer.parseInt(segment.getText().toString());
					NActivity.this.setCashdeskId(seg);
				}catch(NumberFormatException e){
					
				}				
			}
		});
    	row[n].addView(submit);
    	layout.addView(row[n]);
	}

    public void setCashdeskId(int seg) {		
		for(int i=0; i<n; ++i){
			cashdeskId[i].setText(String.valueOf(seg+i));
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.main);                
        initWidgets();        
        
        dbHelp = new DBHelper(this);                       
    }

    public String generateStr(int at){
    	Date d = Calendar.getInstance().getTime();
    	int size = shoppingSize[at].getCheckedRadioButtonId() % buttonsByRow - sizeB;
    	
    	return dbHelp.insertData(cashdeskId[at].getText().toString(),
    							 cardPaid[at].isChecked() ? "1" : "0",
    							 String.valueOf(size),
    							 (1900+d.getYear())+"-"+d.getMonth()+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds(),
    							 String.valueOf(time[at]));    					
    }

	@Override
	public void onClick(View v) {
		int x = v.getId() % buttonsByRow;
		int y = v.getId() / buttonsByRow;
		
		switch(x){
		case startB:
			if(startShopping[y].isChecked()){
				time[y] = Calendar.getInstance().getTime().getTime();
			}
			break;
		case cardB:
			
			break;
		case finishB:
			if(startShopping[y].isChecked()){
				time[y] = (Calendar.getInstance().getTime().getTime() - time[y])/1000;
				Toast.makeText(this, generateStr(y), Toast.LENGTH_LONG).show();
				startShopping[y].setChecked(false);
			}
			break;		
		}		
	}
}