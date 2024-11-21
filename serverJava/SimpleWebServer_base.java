import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleWebServer {

    private static Pilha pilha = new Pilha();

    public static void main(String[] args) {
        try {
            int port = 8080;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado na porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String requestLine = in.readLine();
            System.out.println("Requisição recebida: " + requestLine);

            String[] requestParts = requestLine.split(" ");
            String path = requestParts[1];

            // Rotas
            if (path.equals("/") || path.equals("/index.html")) {
                serveIndexHtml(out);
            } else if (path.startsWith("/push?element=")) {
                String element = path.split("=")[1];
                pilha.push(element);
                sendResponse(out, "Elemento '" + element + "' adicionado à pilha!");
            } else if (path.equals("/view")) {
                sendResponse(out, generateViewContent());
            } else if (path.equals("/pop")) {
                if (!pilha.pEmpty()) {
                    Object removed = pilha.removerFinal();
                    sendResponse(out, "Elemento '" + removed + "' removido do topo da pilha!");
                } else {
                    sendResponse(out, "A pilha está vazia!");
                }
            } else if (path.equals("/create-stack")) {
                pilha = new Pilha();
                sendResponse(out, "Nova pilha criada com sucesso!");
            } else if (path.equals("/clear-stack")) {
                pilha = new Pilha();
                sendResponse(out, "A pilha foi limpa com sucesso!");
            } else {
                sendResponse(out, "<h1>Erro 404: Página não encontrada!</h1>");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serveIndexHtml(PrintWriter out) throws IOException {
        File file = new File("pages/index.html");
        if (file.exists()) {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = fileReader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
            fileReader.close();
            sendResponse(out, htmlContent.toString());
        } else {
            sendResponse(out, "<h1>Erro 404: index.html não encontrado!</h1>");
        }
    }

    private static String generateViewContent() {
        StringBuilder content = new StringBuilder("<h1>Elementos na Pilha:</h1><ul>");
        for (int i = pilha.tamanho() - 1; i >= 0; i--) {
            content.append("<li>").append(pilha.pegar(i)).append("</li>");
        }
        content.append("</ul>");
        return content.toString();
    }

    private static void sendResponse(PrintWriter out, String content) {
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + content.length() + "\r\n" +
                "\r\n" +
                content;
        out.print(httpResponse);
        out.flush();
    }
}
