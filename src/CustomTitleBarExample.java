import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomTitleBarExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Custom Title Bar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // 居中显示

        // 创建自定义的标题栏面板
        TitleBarPanel titleBarPanel = new TitleBarPanel(frame);

        // 设置自定义的标题栏面板作为 JFrame 的装饰器
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        frame.setContentPane(titleBarPanel);

        frame.setVisible(true);
    }
}

// 自定义标题栏面板
class TitleBarPanel extends JPanel {
    private JFrame frame;
    private JLabel titleLabel;
    private JButton closeButton;

    public TitleBarPanel(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 30)); // 设置标题栏高度

        // 创建标题栏上的组件
        titleLabel = new JLabel(frame.getTitle());
        closeButton = new JButton("Close");

        // 设置标题栏的样式
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        setBackground(Color.DARK_GRAY);

        // 添加关闭按钮的点击事件处理
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 添加组件到标题栏面板
        add(titleLabel, BorderLayout.CENTER);
        add(closeButton, BorderLayout.EAST);
    }
}
