import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends Frame implements ActionListener {

    private static final int DEFAULT_PORT = 8081;

    private DatagramSocket socket;
    private byte[] buffer = new byte[1024];
    private long startTime;
    private long receivedBytes;
    private long receivedPackets;
    private long delay;
    private long lastReceivedTime;

    private Label receivedBytesLabel;
    private Label receivedPacketsLabel;
    private Label delayLabel;

    public Client() throws SocketException {
        super("Приемник трафика");
        setLayout(new FlowLayout());

        receivedBytesLabel = new Label("Принято байт: 0");
        add(receivedBytesLabel);

        receivedPacketsLabel = new Label("Принято пакетов: 0");
        add(receivedPacketsLabel);

        delayLabel = new Label("Задержка: 0");
        add(delayLabel);

        // Инициализация переменных
        startTime = System.currentTimeMillis();
        receivedBytes = 0;
        receivedPackets = 0;
        delay = 0;
        lastReceivedTime = 0;

        // Создаем сокет для приема пакетов
        socket = new DatagramSocket(DEFAULT_PORT);

        // Создаем поток для приема пакетов
        Thread receiverThread = new Thread(new Receiver());
        receiverThread.start();

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void start() {
    }

    private class Receiver implements Runnable {

        @Override
        public void run() {
            while (true) {
                // Принимаем пакет
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (IOException ex) {
                    break;
                }

                // Обновляем данные
                receivedBytes += packet.getLength();
                receivedPackets++;
                delay = System.currentTimeMillis() - lastReceivedTime;
                lastReceivedTime = System.currentTimeMillis();

                // Обновляем интерфейс
                receivedBytesLabel.setText("Принято байт: " + receivedBytes);
                receivedPacketsLabel.setText("Принято пакетов: " + receivedPackets);
                delayLabel.setText("Задержка: " + delay);
            }
        }
    }
}