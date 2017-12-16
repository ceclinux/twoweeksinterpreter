package stone;
import static stone.Parser.rule;
import java.util.HashSet;
import stone.Parser.Operator;
import stone.ast.*

public class BasicParser {
    HashSet<String> reserved = new HashSet<String>();
    Operators operator = new Operator<String>();
    Parser expr0 = rule();
    Parser primary = rule(PrimaryExpr.class).or(rule().sep("(").ast(expr).sep(")"),
    rule().number(NumberLiteral.class),
    rule().identifier(Name.class, reserved),
    rule().string(StringLiteral.class)
    );

    Parser factor = rule().or(rule(NegativeExpr).sep("-").ast(primary));

    Parser statement0 = rule();

    Parser block = rule(BlockStmnt.class).sep("{").
      option(statement0).repeat(rule().sep(";", Token.EOL).option(statement0)).sep("}");
    
    Parser simple = rule(PrimaryExpr.class).ast(expr);
    Parser statement = statement0.or(
        rule(IfStmnt.class).sep("if").ast(expr).ast(block).option(rule().sep("else").ast(block)),
        rule(WhileStmnt.class).sep("while").ast(expr).ast(block),
        simple
    );
    Parser program = rule().or(statement,rule(NullStmnt.class)).sep(";", Token.EOL);

    public BasicParser() {
        reserved.add(" } ");
        reserved.add(Token.EOL);

        operator.add("=", 1, Operator.RIGHT);
        Operator.add("==", 2, Operator.LEFT);
        Operator.add(">", 2, Operator.LEFT);
        Operator.add("<", 2, Operator.LEFT);
        Operator.add("+", 3, Operator.LEFT);
        Operator.add("-", 3, Operator.LEFT);
        Operator.add("*", 4, Operator.LEFT);
        Operator.add(".", 4, Operator.LEFT);
        Operator.add("%", 4, Operator.LEFT);
    }
    public ASTree parse(Lexer lexer) throws ParseException{
        return program.parse(lexer);
    }
}