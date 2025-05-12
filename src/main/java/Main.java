import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java Main <source_file.pas>");
            return;
        }

        // 1. Load input Pascal file
        String filePath = args[0];
        CharStream input = CharStreams.fromPath(Paths.get(filePath));

        // 2. Run ANTLR parser
        delphiLexer lexer = new delphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        delphiParser parser = new delphiParser(tokens);
        ParseTree tree = parser.program();

        // 3. Generate LLVM IR
        LLVMGenerator generator = new LLVMGenerator();
        String llvmIR = generator.generate(tree);

        // 4. Save to .ll file
        String outPath = filePath.replace(".pas", ".ll");
        Files.writeString(Paths.get(outPath), llvmIR);

        System.out.println("✅ LLVM IR saved to " + outPath);
    }
}
