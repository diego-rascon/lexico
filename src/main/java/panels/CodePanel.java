package panels;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import panels.tokens.TokensPanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class CodePanel extends PanelTemplate {

    private int[][] matrix;
    private final RSyntaxTextArea codeArea = new RSyntaxTextArea();
    private final TokensPanel tokensPanel;
    private final CountersPanel countersPanel;

    public CodePanel(TokensPanel tokensPanel, CountersPanel countersPanel) {
        super("Código");

        final RTextScrollPane scrollPane = new RTextScrollPane(codeArea);

        this.tokensPanel = tokensPanel;
        this.countersPanel = countersPanel;

        codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
        codeArea.setCodeFoldingEnabled(true);
        codeArea.setBracketMatchingEnabled(true);
        codeArea.setAntiAliasingEnabled(true);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"
            ));
            theme.apply(codeArea);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cargar el tema del editor de código.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        add(scrollPane, BorderLayout.CENTER);
        loadMatrix();
    }

    public void compile() {
        tokensPanel.emptyTokensList();
        int i = 0;
        int state = 0;
        int lineNum = 1;
        StringBuilder lexeme = new StringBuilder();
        final String code = codeArea.getText() + "\n";

        while (i < code.length()) {
            final char character = code.charAt(i);
            int column = getColumn(character);
            state = matrix[state][column];
            if (state < 0) {
                tokensPanel.addToken(state, String.valueOf(lexeme.toString().trim()), lineNum);
                lexeme.setLength(0);
                state = 0;
                i--;
            } else if (state >= 500) {
                lexeme.setLength(0);
                state = 0;
                i--;
            } else {
                lexeme.append(character);
                if (character == '\n') lineNum++;
            }
            i++;
        }
        tokensPanel.updateTable();
    }

    private int getColumn(char character) {
        switch (character) {
            case '+' -> {
                return 0;
            }
            case '-' -> {
                return 1;
            }
            case '~' -> {
                return 2;
            }
            case '|' -> {
                return 3;
            }
            case '&' -> {
                return 4;
            }
            case '^' -> {
                return 5;
            }
            case ',' -> {
                return 6;
            }
            case '.' -> {
                return 7;
            }
            case ';' -> {
                return 8;
            }
            case ':' -> {
                return 9;
            }
            case '*' -> {
                return 10;
            }
            case '/' -> {
                return 11;
            }
            case '%' -> {
                return 12;
            }
            case '<' -> {
                return 13;
            }
            case '>' -> {
                return 14;
            }
            case '=' -> {
                return 15;
            }
            case '!' -> {
                return 16;
            }
            case '?' -> {
                return 17;
            }
            case '{' -> {
                return 18;
            }
            case '}' -> {
                return 19;
            }
            case '[' -> {
                return 20;
            }
            case ']' -> {
                return 21;
            }
            case '(' -> {
                return 22;
            }
            case ')' -> {
                return 23;
            }
            case '"' -> {
                return 24;
            }
            case '\'' -> {
                return 25;
            }
            default -> {
                if (Character.isDigit(character)) {
                    return 26;
                } else if (Character.isLetter(character)) {
                    return 27;
                }
                return 33;
            }
            case '@' -> {
                return 28;
            }
            case '_' -> {
                return 29;
            }
            case ' ' -> {
                return 30;
            }
            case '\n' -> {
                return 31;
            }
            case '\t' -> {
                return 32;
            }
        }
    }

    final void loadMatrix() {
        try {
            FileInputStream matrixFile = new FileInputStream("./src/main/resources/matrix.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(matrixFile);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                int columnCount = row.getLastCellNum();

                if (i == 1) matrix = new int[rowCount][columnCount - 1];

                for (int j = 1; j < columnCount; j++) {
                    Cell cell = row.getCell(j);
                    int cellValue = (int) cell.getNumericCellValue();
                    matrix[i - 1][j - 1] = cellValue;
                }
            }
        } catch (IOException e) {
            final Object[] options = {"OK"};
            final int selection = JOptionPane.showOptionDialog(
                    JOptionPane.getFrameForComponent(this),
                    "El archivo .xlsx de la matríz no pudo ser encontrado.",
                    "Error",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (selection == JOptionPane.OK_OPTION) System.exit(0);
        }
    }
}
