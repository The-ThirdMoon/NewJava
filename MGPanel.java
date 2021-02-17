import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//パネルクラス
public class MGPanel extends JPanel {
	//マウスアダプタ
	private MGMouseAdapter mgma = null;
	//ピコピコハンマー座標
	private int px = 0;
	//ピコピコハンマー座標
	private int py = 0;
	//もぐら座標
	private int mx = 350;
	//もぐら座標
	private int my = 200;
	//ピコピコハンマー
	private BufferedImage[] imagePHs = null;
	//ピコピコハンマーの状態
	private int ph = 0;
	//もぐら
	private BufferedImage[] imageMs = null;
	//もぐらの状態
	private int m = 0;
	//もぐらの時間
	private int timeM = 0;
	//タイマー
	private java.util.Timer timerThis = null; 
	//フェーズ
	private int phase = 0;
	//背景
	private BufferedImage imageBackground = null;
	//得点
	private int score = 0;
	private Font fontScore  = new Font("ＭＳ ゴシック", Font.BOLD | Font.ITALIC, 24);
	//制限時間
	private int time = 1859;
	private Font fontTime = new Font("ＭＳ ゴシック", Font.BOLD, 48);
	//ゲームオーバー
	private Font fontGameOver = new Font("ＭＳ ゴシック", Font.BOLD, 48);
	//BGM
	private KSoundMidi midiMoguraDance = null;
	//効果音
	private KSoundWave waveMoguraDeru = null;
	private KSoundWave wavePicoHammerMiss = null;
	private KSoundWave wavePicoHammerHit = null;
	//コンストラクタ
	public MGPanel() {
		//スーパークラス呼び出し
		super();
		try  {
		//パネルサイズ
		super.setPreferredSize(new Dimension(800, 600));
		//レイアウト設定
		super.setLayout(null);
		//マウスアダプタ生成
		mgma = new MGMouseAdapter();
		//パネルにマウスリスナー追加
		super.addMouseListener(mgma);
		super.addMouseMotionListener(mgma);
		//背景を読み込む
		InputStream isBackground = this.getClass().getResourceAsStream("Background.jpg");
		imageBackground = ImageIO.read(isBackground);
		isBackground.close();
		//もぐらを読み込む
		imageMs = new BufferedImage[2];
		InputStream isMG00 = this.getClass().getResourceAsStream("M00.gif");
		imageMs[0] = ImageIO.read(isMG00);
		isMG00.close();
		InputStream isMG01 = this.getClass().getResourceAsStream("M01.gif");
		imageMs[1] = ImageIO.read(isMG01);
		isMG01.close();
		//ピコピコハンマーを読み込む
		imagePHs = new BufferedImage[2];
		InputStream isPH00 = this.getClass().getResourceAsStream("PH00.gif");
		imagePHs[0] = ImageIO.read(isPH00);
		isPH00.close();
		InputStream isPH01 = this.getClass().getResourceAsStream("PH01.gif");
		imagePHs[1] =ImageIO.read(isPH01);
		isPH01.close();
		//BGMを生成
		midiMoguraDance = new KSoundMidi(this, "MoguraDance.mid", false);
		//効果音を生成
		waveMoguraDeru = new KSoundWave(this, "MoguraDeru.wav", false);
		wavePicoHammerMiss = new KSoundWave(this, "PicoHammerMiss.wav", false);
		wavePicoHammerHit = new KSoundWave(this, "PicoHammerHit.wav", false);
		//初期化
		init();
		
		//タイマーを生成
		timerThis = new java.util.Timer();
		//タイマーをスタート
		timerThis.scheduleAtFixedRate(new TimerActionTimerTask(), 1000L, 16L);
		
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,"ERROR:" +  ex.toString());
		}
	}//end MGPanel
	/*
	 * 初期化*/
	public void init() {
		//フェーズをゲーム中にする
		phase = 0;
		//もぐらの状態
		m = 0;
		//得点
		score = 0;
		//時間
		time = 1859;
		//BGMを初期化
		midiMoguraDance.init();
		//BGMをスタート
		midiMoguraDance.start();
	}//end init
	
	/*実行メソッド*/
	public void run() {
		//フェーズ
		switch(phase) {
		case 0:
			//制限時間を減らす
			time--;
			//制限時間が来たら、
			if (time == 0) {
				//時間を戻す
				time = 300;
				//フェーズ変更
				phase = 1;
				//ブレイク
				break;
			}
		
		//やられている場合、
		if (timeM != 0) {
			//時間を−１する
			timeM--;
			//時間が０になったら
			if (timeM == 0) {
				//新しいもぐらを表示する
				m = 0;
				mx = (int)(Math.random() * 550);
				my = (int)(Math.random() * 450);
				waveMoguraDeru.start();
			}//end if 時間が０になったら
		}//end if やられている場合
		// ブレイク
		break;
		
		case 1:
			//制限時間を減らす
			time--;
			//制限時間が来たら、
			if (time == 0) {
				//初期化
				init();
			}
			break;
		}//end switch
	}//end run
	
	//描画メソッド
	/*ペイントする必要がある時に呼ばれます*/
	public void paint(Graphics g) {
		//背景を描画する
		g.drawImage(imageBackground, 0, 0, 800, 600, this);
		//もぐらを描画
		g.drawImage(imageMs[m], mx, my, 100, 100, this);
		//ピコピコハンマーを描画
		g.drawImage(imagePHs[ph], px, py, 100, 88, this);
		//得点を表示する
		g.setColor(Color.yellow);
		g.setFont(fontScore);
		g.drawString("得点：" + score, 0, 24);
		//ゲーム中の場合、
		if (phase == 0) {
			//制限時間を表示する
			g.setColor(Color.lightGray);
			g.setFont(fontTime);
			g.drawString("残り時間：" + time / 60, 300, 24);
			//ゲームオーバーの場合、
		}else if (phase == 1) {
			g.setColor(Color.lightGray);
			g.setFont(fontGameOver);
			g.drawString("ゲームオーバー", 240, 300);
			//コメント
			if (score >= 30) {
				g.drawString("おぬしやるのう", 240, 350);
			}else if (score >= 20) {
				g.drawString("もう一歩じゃな", 240, 350);
			}else if (score >= 10) {
				g.drawString("まだまだじゃな", 240, 350);
			}else {
				g.drawString("がんばるのじゃ", 240, 350);
			}
		}//end if ゲームオーバーの場合
		}//end paint
	//マウスアダプター
	private class MGMouseAdapter extends MouseAdapter {
		//マウスが押された時に呼ばれます
		public void mousePressed(MouseEvent me) {
			//ゲーム中の場合
			if(phase == 0) {
			//ピコピコハンマー叩く
			ph = 1;
			//場所を記憶する
			px = me.getX();
			py = me.getY();
			//やられていない場合
			if (m == 0) {
				//もぐらとの当たり判定
			if (px > mx - 50 && px < mx + 90 && py > my -70 && py < my + 60) {
				//やられた
				m = 1;
				timeM = 30;
				score++;
				//効果音をスタート
				wavePicoHammerHit.start();
				//外れた場合
			}else {
				//効果音をスタート
				wavePicoHammerMiss.start();
			}
		   }//end if やられていない場合
		 }// end if ゲーム中の場合
		}//end mousePressed
		/*
		 * マウスが離された時に呼ばれます*/
		public void mouseReleased(MouseEvent me) {
			//ピコピコハンマーを上げる
			ph = 0;
			//場所を記憶する
			px = me.getX();
			py = me.getY();
		}//end mouseReleased
		/*
		 * マウスが移動された時に呼ばれます*/
		public void mouseMoved(MouseEvent me) {
			//場所を記憶する
			px = me.getX();
			py = me.getY();
		}//end mouseMoved
		
		public void mouseDragged(MouseEvent me) {
			//場所を記憶する
			ph = me.getX();
			py = me.getY();
		}//end mouseDragged
	}//end MGMouseAdapter
	/*
	 * タイマークラス*/
	private class TimerActionTimerTask extends TimerTask {
		public void run() {
			//実行メソッド呼び出し
			MGPanel.this.run();
			//描画する
			repaint();
		}//end actionPerformed
	}//end TimerActionTimerTask
}
