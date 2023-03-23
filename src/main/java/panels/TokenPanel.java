package panels;

import model.Token;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;

public class TokenPanel extends PanelTemplate {

    private final LinkedList<Token> tokenList = new LinkedList<>();
    private final String[] columns = {"Estado", "Lexema", "Línea"};
    private final DefaultTableModel tokenTableModel = new DefaultTableModel(columns, 0);

    public TokenPanel(int padding) {
        super("Tokens", padding);

        final JTable tokenTable = new JTable(tokenTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tokenTable.getTableHeader().setReorderingAllowed(false);
        tokenTable.getColumnModel().getColumn(0).setPreferredWidth(1);
        tokenTable.getColumnModel().getColumn(2).setPreferredWidth(1);

        final DefaultTableCellRenderer centeredCell = new DefaultTableCellRenderer();
        centeredCell.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        tokenTable.getColumnModel().getColumn(0).setCellRenderer(centeredCell);
        tokenTable.getColumnModel().getColumn(2).setCellRenderer(centeredCell);

        final JScrollPane scrollPane = new JScrollPane(tokenTable);
        add(scrollPane);
    }

    final public void emptyTokensList() {
        tokenList.clear();
    }

    final public void addToken(int state, String lexeme, int line) {
        final Token newToken = new Token(state, lexeme, line);
        tokenList.add(newToken);
    }

    public void updateTable() {
        tokenTableModel.setRowCount(0);
        for (Token token : tokenList) {
            final Object[] tokenRow = {token.state(), token.lexeme(), token.line()};
            tokenTableModel.addRow(tokenRow);
        }
    }
}
