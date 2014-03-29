package bcg.grap;

import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import att.grappa.Attribute;
import att.grappa.Element;
import att.grappa.Graph;
import att.grappa.GrappaNexus;
import att.grappa.Node;
import att.grappa.Subgraph;

public class TestMain {

	public static void main(String[] args) throws IOException {
		System.out.println("hogehoge");
		att.grappa.Graph graph = new Graph("hoge");
		Subgraph subgraph;
		Object[] objs = null;
		Attribute[] attr = null;
		Element elm = graph.createElement(Node.NODE, objs, attr);
		Node node = (Node) elm;
		node.setName("namehogehoge");
		graph.addNode(node);
		graph.printGraph(new FileWriter(new File("grap.dot")));
		
		
		
		att.grappa.GrappaNexus nexus = new GrappaNexus(graph);
		nexus.updateImage();

		System.out.println(nexus.getImage());
		
		showImageDialog(nexus.getImage());
	}

	public static void showImageDialog(Image img) {
		Icon icon = new ImageIcon(img);

		JOptionPane.showMessageDialog(null, "←イメージ", // メッセージ
				"イメージ確認", // タイトル
				JOptionPane.PLAIN_MESSAGE, icon // 画像のアイコン
				);
	}

	public TestMain() {
		// TODO Auto-generated constructor stub
	}

}
