package bcg;

import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

public interface ITextOutputter {

	public String text(MethodNode root, int dept);

	public String text(ClassNode klass);

}
