package bcg;

import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

/**
 * クラスファイルの解析結果を文字列におこすクラス
 * 
 * @author s.watanabe
 */
public interface ITextOutputter {

	/**
	 * メソッドの解析結果を文字列にする
	 * 
	 * @param root
	 * @param dept
	 * @return
	 */
	public String text(MethodNode root, int dept);

	/**
	 * クラスの解析結果を文字列にする
	 * 
	 * @param klass
	 * @return
	 */
	public String text(ClassNode klass);

}
