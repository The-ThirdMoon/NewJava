import java.awt.BorderLayout;

import javax.swing.JFrame;

//フレームクラス
public class MGFrame extends JFrame {
	private MGPanel panel = null;
	//ここからスタート
	public static void main(String[] args) {
		MGFrame mg01 = new MGFrame();
	}
	//コンストラクタ
	public MGFrame() {
		//スーパークラス呼び出し
		super();
		//×が押されたら終了
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//レイアウト設定
		super.setLayout(new BorderLayout());
		//パネル生成
		panel = new MGPanel();
		//フレームのコンテントペインを置き換える
		super.setContentPane(panel);
		//フレーム表示
		super.setVisible(true);
		//サイズの最適化
		super.pack();
	}//end MGFrame
}