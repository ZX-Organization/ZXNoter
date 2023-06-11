import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomTitleBarExample {

    private JFrame frame;
    private JPanel titleBarPanel;

    private int dragOffsetX;
    private int dragOffsetY;

    public CustomTitleBarExample() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // 创建 JFrame
        frame = new JFrame();
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // 设置为无装饰窗口

        // 创建自定义标题栏面板
        titleBarPanel = createTitleBarPanel();
        frame.add(titleBarPanel, BorderLayout.NORTH);

        // 添加鼠标事件监听器
        addDragWindowListeners(titleBarPanel);

        // 添加内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        frame.add(contentPanel, BorderLayout.CENTER);

        // 添加窗口布局设置监听器
        addWindowLayoutListener();

        // 显示窗口
        frame.setVisible(true);
    }

    private JPanel createTitleBarPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new BorderLayout());

        // 创建标题文本
        JLabel titleLabel = new JLabel("Custom Title Bar");
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);

        // 创建按钮
        JButton closeButton = new JButton("X");
        closeButton.setFocusable(false);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(closeButton, BorderLayout.EAST);

        return panel;
    }

    private void addDragWindowListeners(Component component) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragOffsetX = e.getX();
                dragOffsetY = e.getY();
            }
        });

        component.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currentPos = frame.getLocation();
                int offsetX = e.getXOnScreen() - dragOffsetX;
                int offsetY = e.getYOnScreen() - dragOffsetY;
                Point newPos = new Point(offsetX, offsetY);
                frame.setLocation(newPos);
            }
        });
    }

    private void addWindowLayoutListener() {
        frame.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                checkWindowLayout();
            }
        });
    }

    private void checkWindowLayout() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle bounds = defaultScreen.getDefaultConfiguration().getBounds();
        int screenWidth = bounds.width;
        int screenHeight = bounds.height;
        int windowX = frame.getX();
        int windowY = frame.getY();
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();

        // 检查窗口是否靠近屏幕边缘
        if (windowX <= 10) {
            // 窗口靠近屏幕左边缘
            frame.setLocation(0, windowY);
        } else if (windowX >= screenWidth - windowWidth - 10) {
            // 窗口靠近屏幕右边缘
            frame.setLocation(screenWidth - windowWidth, windowY);
        }

        if (windowY <= 10) {
            // 窗口靠近屏幕上边缘
            frame.setLocation(windowX, 0);
        } else if (windowY >= screenHeight - windowHeight - 10) {
            // 窗口靠近屏幕下边缘
            frame.setLocation(windowX, screenHeight - windowHeight);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomTitleBarExample();
            }
        });
    }
}
