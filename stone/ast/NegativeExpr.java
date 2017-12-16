package stone.ast;
import java.util.List;

public class NegativeExpr extends ASTList {
    public NegativeExpr(List<ASTList> c) {super(c);}

    public ASTree operand() {return child(0);}

    public String toString(){
        return "-" + operand();
    }
}