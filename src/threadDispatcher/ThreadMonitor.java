package threadDispatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ThreadMonitor extends ThreadedTask
{
    private final JFrame frame;
    private final JPanel panel;

    public ThreadMonitor()
    {
        JFrame frame = new JFrame("Dispatcher monitor");
        JPanel panel = new JPanel();
        JButton b1 = new JButton(new ButtonAction("add Sleeper", this::update1));
        JButton b2 = new JButton(new ButtonAction("add Sleeper in Queue", this::update2));
        JButton b3 = new JButton(new ButtonAction("add Counter", this::update3));
        JButton b4 = new JButton(new ButtonAction("add Counter in Queue", this::update4));
        JPanel buttonsPanel = new JPanel(new FlowLayout()); //панель для кнопок

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //располагаем компоненты сверху вниз

        buttonsPanel.add(b1);
        buttonsPanel.add(b2);
        buttonsPanel.add(b3);
        buttonsPanel.add(b4);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame = frame;
        this.panel = panel;
    }

    public static class ButtonAction extends AbstractAction
    {
        private final Runnable m_action;

        public ButtonAction (String actionName, Runnable action) {
            super(actionName);
            m_action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            m_action.run();
        }
    }

    private void update1() {
        ThreadDispatcher.getInstance().add(new SleepWorker());
    }

    private void update2() {
        ThreadDispatcher.getInstance().addInQueue(new SleepWorker());
    }

    private void update3() {
        ThreadDispatcher.getInstance().add(new Counter());
    }

    private void update4() {
        ThreadDispatcher.getInstance().addInQueue(new Counter());
    }

    protected void update()
    {
        this.panel.removeAll();
        this.panel.add(new JLabel("Name Id Status"));
        synchronized (ThreadDispatcher.threadedTask) {
            for (ThreadedTask item : ThreadDispatcher.threadedTask) {
                this.panel.add(new JLabel(item.getName() + " " + item.id + " " + item.status));
            }
        }
        this.panel.updateUI();
    }

    @Override
    public void start()
    {
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); //центрировать окно
        frame.setVisible(true);
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
        }
    }
}
