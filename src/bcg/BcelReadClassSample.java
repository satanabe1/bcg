package bcg;

/**
 * Apache Jakarta BCELを用いたクラスファイル(バイトコード)解析サンプル
 */
import java.io.IOException;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * Apache Jakarta BCELを用いたクラスファイル(バイトコード)解析サンプル。
 * 
 * 少しマジメに解析する例
 * 
 * @author IGA Tosiki
 */
public class BcelReadClassSample {
	/**
	 * 解析を行いたいクラスファイル名を指定します。
	 */
	private static String CLASS_MODULE = "./bin/BcelReadClassSample.class";

	public BcelReadClassSample(String classfile) {
		CLASS_MODULE = classfile;
	}

	public final void process() {
		try {
			final JavaClass javaClass = new ClassParser(CLASS_MODULE).parse();
			System.out.println("クラス名:" + javaClass.getClassName());
			System.out.println("親クラス:" + javaClass.getSuperclassName());

			final org.apache.bcel.classfile.Method[] methods = javaClass
					.getMethods();
			for (int indexMethod = 0; indexMethod < methods.length; indexMethod++) {

				if (!methods[indexMethod].getName().equals("getMethodName")) {
					continue;
				}

				final Method method = methods[indexMethod];
				System.out.println("メソッド:" + method.getName());
				final Code code = method.getCode();
				if (code == null) {
					continue;
				}

				/**
				 * メソッドのなかのバイトコードを解析
				 */
				final byte[] codes = code.getCode();
				for (int indexCode = 0; indexCode < codes.length; indexCode++) {
					final short opcode = (short) (codes[indexCode] < 0 ? ((short) codes[indexCode]) + 0x100
							: (short) codes[indexCode]);
					int oplen = Constants.NO_OF_OPERANDS[opcode];
					System.out.println(Constants.OPCODE_NAMES[opcode]);

					// オペレーション分進めます。
					indexCode += oplen;
				}
			}
		} catch (ClassFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}