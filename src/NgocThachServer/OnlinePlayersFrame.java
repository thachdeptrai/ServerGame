package NgocThachServer;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import player.Char;
import player.Player;

/**
 *
 * @author kagam
 */
public class OnlinePlayersFrame extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;

    public OnlinePlayersFrame() {
        setTitle("Danh Sách Online");
        setSize(300, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        Vector<String> headers = new Vector<>();
        headers.add("Tên Nhân Vật");
        Vector<Vector<String>> data = getOnlineCharacters();
        DefaultTableModel model = new DefaultTableModel(data, headers);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Vector<Vector<String>> getOnlineCharacters() {
        Vector<Vector<String>> data = new Vector<>();
        List<Player> characters = Client.gI().getPlayers();
        for (Player character : characters) {
            Vector<String> row = new Vector<>();
            row.add(character.setNameVip(character.name));
            data.add(row);
        }
        return data;
    }

    public static void display() {
        new OnlinePlayersFrame();
    }
}
