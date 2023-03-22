package panels.tokens;

import panels.PanelTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;

public class TokensPanel extends PanelTemplate {

    private final LinkedList<Token> tokenList = new LinkedList<>();
    private final String[] columns = {"Token", "Lexema", "Línea"};
    private final DefaultTableModel tokensTableModel = new DefaultTableModel(columns, 0);

    public TokensPanel() {
        super("Tokens");
        JTable tokensTable = new JTable(tokensTableModel);
        final JScrollPane scrollPane = new JScrollPane(tokensTable);
        tokensTable.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);
    }

    final public void emptyTokensList() {
        tokenList.clear();
    }

    final public void addToken(int token, String lexeme, int line) {
        tokenList.add(new Token(token, lexeme, line));

    }

    public void updateTable() {
        tokensTableModel.setRowCount(0);
        for (Token token : tokenList) {
            final Object[] tokenData = {token.token(), token.lexeme(), token.line()};
            tokensTableModel.addRow(tokenData);
        }
    }
}
