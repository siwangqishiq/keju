package com.xinlan.keju;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xinlan.data.ItemsXmlParseHandler;
import com.xinlan.data.PaperXmlParseHandler;
import com.xinlan.data.SQLiteService;
import com.xinlan.data.Settings;
import com.xinlan.data.XinlanFileService;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;
import com.xinlan.untils.Randoms;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView papersListBtn;// �Ծ��б�ť
	private ImageView resumeBtn;
	private ImageView exitBtn;
	private ImageView importPaperBtn;
	private ImageView updateBtn;
	private ImageView descBtn;
	private ImageView aboutBtn;
	private Settings setting;

	private static final int INITPAPERSNUM = 3;
	private SQLiteService sqlService;
	private ArrayList<Paper> paperList;
	private ArrayList<Item> itemsList;
	private ProgressDialog loadDialog;
	private Handler handler;

	private static final int ABOUT = 1;
	private static final int EXIT = 2;
	private static final String version = "1.1";
	private static final String company = "�����Ƽ�";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		initComponent();
	}

	private void init() {
		sqlService = new SQLiteService(this);
		if (sqlService.getPapersCount() < INITPAPERSNUM)// ��һ�����У����ݿ��л�û������
		{
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Toast.makeText(MainActivity.this, "�Ծ����ݵ������!",
							Toast.LENGTH_SHORT).show();
				}
			};
			loadDialog = ProgressDialog.show(this, "����", "��һ���������������ݣ����Ե�...",
					true, true);
			new Thread(new Runnable() {
				public void run() {
					loadInitDefaultPapers();
					loadDialog.dismiss();
					handler.sendMessage(new Message());
				}
			}).start();
		}
	}

	private void loadInitDefaultPapers()// �����ʼʱ���Ծ���Ϣ
	{
		AssetManager assetManager = this.getAssets();
		paperList = new ArrayList<Paper>();// �������Ծ�List�ṹ
		try {
			String[] fileNames = assetManager.list("papers");
			for (int i = 0; i < fileNames.length; i++) {
				StringBuffer sb = new StringBuffer();
				BufferedReader buffer = null;
				String line = null;
				String fileName = "papers" + File.separator + fileNames[i];
				try {
					buffer = new BufferedReader(new InputStreamReader(
							assetManager.open(fileName), "utf-8"));
					while ((line = buffer.readLine()) != null) {
						sb.append(line);
					}// end while
					buffer.close();
					String content = sb.toString();
					addPaperParseXml(content);
					loadDefaultItems(content);
				} finally {
					if (buffer != null) {
						buffer.close();
					}
				}
			}// end for
			insertPapersIntoDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����Ĭ������
	 */
	private void loadDefaultItems(String content) {
		SAXParserFactory factory = SAXParserFactory.newInstance();// ����һ��SAXParser����
		itemsList = new ArrayList<Item>();
		try {
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ItemsXmlParseHandler(itemsList));// �������ݴ�����
			try {
				reader.parse(new InputSource(new StringReader(content)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		insertDefaultItems();
	}

	private void addPaperParseXml(String content)// ����xml�ļ������Ծ���Ϣ����List��
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();// ����һ��SAXParser����
		try {
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new PaperXmlParseHandler(paperList));// �������ݴ�����
			try {
				reader.parse(new InputSource(new StringReader(content)));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}// end addPaperParseXml

	/**
	 * �����ݿ��д�����Ϣ
	 */
	private void insertDefaultItems() {
		for (int i = 0; i < itemsList.size(); i++) {
			sqlService.insertItems(itemsList.get(i));
		}// end for
	}

	protected void initComponent() {
		setting = new Settings(this);
		exitButtonLogic();
		resumeBtnLogic();
		paperListButtonLogic();
		importNewPaperLogic();
		updateBrnLogic();
		descBtnLogic();
		aboutBtnLogic();
	}

	private void aboutBtnLogic() {
		aboutBtn = (ImageView) this.findViewById(R.id.mainabout);
		aboutBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AboutActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	/**
	 * ����������Ϣҳ��
	 */
	private void descBtnLogic() {
		descBtn = (ImageView) this.findViewById(R.id.maindescbtn);
		descBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DescActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	/**
	 * ȥ��վ������Դ��Ϣ
	 */
	private void updateBrnLogic() {
		updateBtn = (ImageView) this.findViewById(R.id.mainupdate);
		updateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, UpdateActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private void importNewPaperLogic()// �������Ծ�ť
	{
		importPaperBtn = (ImageView) this.findViewById(R.id.addNewPaper);
		importPaperBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ImportActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private void resumeBtnLogic() {
		resumeBtn = (ImageView) findViewById(R.id.mainresume);
		resumeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Integer[] ints = setting.getLastPosition();
				Intent intent = new Intent();
				intent.putExtra("paperId", ints[0]);
				intent.putExtra("itemNumber", ints[1]);
				intent.setClass(MainActivity.this, ItemActivity.class);
				startActivity(intent);
			}
		});
	}

	private void exitButtonLogic() {
		exitBtn = (ImageView) findViewById(R.id.exitBtn);

		exitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void insertPapersIntoDatabase()// ������������Ϣ�������ݿ���
	{
		for (int i = 0; i < paperList.size(); i++) {
			sqlService.insertPapers(paperList.get(i));
		}// end for
	}

	@Override
	public void finish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("��ȷ��Ҫ�뿪��");
		String retStr = Randoms.genInt(0, 30) == 15 ? "��ë,ߣ��ȥ��!" : "������!";
		builder.setPositiveButton(retStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.exit(0);
					}
				});
		builder.setNegativeButton("�ٶ࿴���!", null);
		builder.create().show();
	}

	private void paperListButtonLogic()// ��������Ծ��б�ť
	{
		papersListBtn = (ImageView) findViewById(R.id.paperListBtn);
		papersListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PapersListActivity.class);
				MainActivity.this.startActivity(intent);// ��ת���Ծ��б�ҳ��
			}
		});
	}

	/**
	 * ��Ӳ˵�����
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, ABOUT, 1, "�������");
		menu.add(1, EXIT, 2, "�˳�");
		return true;
	}

	/**
	 * �˵����¼���Ӧ
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ABOUT)// ����
		{
			String show = "�������:����Ա���Բ���ϵͳ\n" + "����汾:" + version + "\n������˾:"
					+ company + "\n��������:com.xinlan.Keju";
			Toast.makeText(this, show, Toast.LENGTH_LONG).show();
		} else if (item.getItemId() == EXIT)// �˳�
		{
			System.exit(0);
		}
		return true;
	}

}// end class