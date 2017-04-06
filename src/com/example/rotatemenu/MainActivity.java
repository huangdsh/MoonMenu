package com.example.rotatemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rotatemenu.view.ArcMenu;
import com.example.rotatemenu.view.ArcMenu.OnMenuItemClickListener;

public class MainActivity extends Activity {

	ArcMenu arcMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		arcMenu = new ArcMenu(this);
		arcMenu = (ArcMenu) findViewById(R.id.areMenu);

		// 回调方法
		arcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onClick(View view, int position) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this,
						position + ":" + view.getTag(), Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

}
