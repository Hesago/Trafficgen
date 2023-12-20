
public class Main {

    public static void main(String[] args) throws Exception {
        // Создаем генератор
        Generator generator = new Generator();

        // Создаем клиента
        Client client = new Client();

        // Запускаем генератор
        generator.start();

        // Запускаем клиента
        client.start();
    }
}