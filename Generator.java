import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Generator extends Frame implements ActionListener {

    private static final int DEFAULT_PACKETS = 1000;
    private static final int DEFAULT_SIZE = 1024;
    private static final int DEFAULT_FREQUENCY = 1000;

    private int packets;
    private int size;
    private int frequency;

    private DatagramSocket socket;
    private byte[] buffer;

    private Label packetsLabel;
    private Label sizeLabel;
    private Label frequencyLabel;
    private TextField packetsField;
    private TextField sizeField;
    private TextField frequencyField;
    private Button startButton;
    private Button closeButton;

    public Generator() throws SocketException {
        super("Трафик-генератор");
        setLayout(new FlowLayout());

        packetsLabel = new Label("Количество пакетов:");
        add(packetsLabel);
        packetsField = new TextField(Integer.toString(DEFAULT_PACKETS));
        add(packetsField);

        sizeLabel = new Label("Размер пакетов:");
        add(sizeLabel);
        sizeField = new TextField(Integer.toString(DEFAULT_SIZE));
        add(sizeField);

        frequencyLabel = new Label("Частота отправки:");
        add(frequencyLabel);
        frequencyField = new TextField(Integer.toString(DEFAULT_FREQUENCY));
        add(frequencyField);

        startButton = new Button("Запустить");
        startButton.addActionListener(this);
        add(startButton);

        closeButton = new Button("Закрыть");
        closeButton.addActionListener(this);
        add(closeButton);

        pack();
        setVisible(true);

        // Создаем сокет для отправки пакетов
        socket = new DatagramSocket();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            try {
                packets = Integer.parseInt(packetsField.getText());
                size = Integer.parseInt(sizeField.getText());
                frequency = Integer.parseInt(frequencyField.getText());



                // Создаем буфер для пакетов
                buffer = new byte[size];

                // Запускаем поток для отправки пакетов
                Thread senderThread = new Thread(new Sender());
                senderThread.start();
            } catch (Exception ex) {
                // Обрабатываем исключение IOException
                if (ex instanceof IOException) {
                    System.err.println("Ошибка при закрытии сокета: " + ex.getMessage());
                } else {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == closeButton) {
            // Закрываем сокет
            socket.close();

            // Завершаем работу программы
            System.exit(0);
        }
    }

    public void start() {
    }

    private class Sender implements Runnable {

        @Override
        public void run() {
            try {
                // Генерируем пакеты
                for (int i = 0; i < packets; i++) {
                    // Заполняем буфер случайными данными
                    Random random = new Random();
                    random.nextBytes(buffer);

                    // Создаем пакет
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 8081);

                    // Отправляем пакет
                    socket.send(packet);

                    // Ждем некоторое время
                    Thread.sleep(1000 / frequency);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}