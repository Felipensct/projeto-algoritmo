import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SimpleWebServer {

    private static List<String> passosRealizados = new ArrayList<>();
    private static int cadeiraAtual = 1;
    private static int totalCadeiras = 1;
    private static int etapaAtual = 0;

    private static final String[] sequenciaMontagem = {
            "Perna da mesa - 1", "Acento", "Arruela - 1", "Parafuso - 1",
            "Perna da mesa - 2", "Arruela - 2", "Parafuso - 2",
            "Perna da mesa - 3", "Arruela - 3", "Parafuso - 3",
            "Perna da mesa - 4", "Arruela - 4", "Parafuso - 4",
            "Suporte para encosto", "Encosto"
    };

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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            String path = requestParts[1];

            if (path.equals("/") || path.equals("/index.html")) {
                sendHtmlResponse(out, "pages/index.html");
            } else if (path.startsWith("/definirCadeiras")) {
                definirCadeiras(out, path);
            } else if (path.startsWith("/adicionarPasso")) {
                adicionarPasso(out, path);
            } else if (path.equals("/visualizarPassos")) {
                visualizarPassos(out);
            } else if (path.equals("/obterPassos")) {
                obterPassos(out);
            } else {
                sendResponse(out, "<h1>404 - Página não encontrada</h1>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void definirCadeiras(PrintWriter out, String path) {
        try {
            String[] params = path.split("\\?")[1].split("=");
            totalCadeiras = Integer.parseInt(params[1]);
            cadeiraAtual = 1;
            etapaAtual = 0;
            passosRealizados.clear();
            sendJsonResponse(out, "{\"status\":\"Número de cadeiras definido para " + totalCadeiras + "\"}");
        } catch (Exception e) {
            sendJsonResponse(out, "{\"status\":\"Erro ao definir número de cadeiras.\"}");
        }
    }

    private static void adicionarPasso(PrintWriter out, String path) {
        try {
            String passoCodificado = path.split("\\?")[1].split("=")[1];
            // Decodifica o valor do parâmetro passo
            String passo = URLDecoder.decode(passoCodificado, "UTF-8").trim();
            String esperado = sequenciaMontagem[etapaAtual].trim();

            if (esperado.equals(passo)) {
                passosRealizados.add("Cadeira " + cadeiraAtual + ": " + passo);
                etapaAtual++;
                if (etapaAtual == sequenciaMontagem.length) {
                    cadeiraAtual++;
                    etapaAtual = 0;
                    if (cadeiraAtual > totalCadeiras) {
                        sendJsonResponse(out, "{\"status\":\"Todas as cadeiras montadas!\"}");
                    } else {
                        sendJsonResponse(out, "{\"status\":\"Cadeira " + (cadeiraAtual - 1) + " completa!\"}");
                    }
                } else {
                    sendJsonResponse(out, "{\"status\":\"Passo adicionado: " + passo + "\"}");
                }
            } else {
                sendJsonResponse(out, "{\"status\":\"Erro: Esperado '" + esperado + "' mas foi recebido '" + passo + "'\"}");
            }
        } catch (Exception e) {
            sendJsonResponse(out, "{\"status\":\"Erro ao adicionar passo.\"}");
        }
    }

    private static void visualizarPassos(PrintWriter out) {
        StringBuilder tabela = new StringBuilder("<table border='1'><tr><th>Cadeira</th><th>Passos</th></tr>");
        for (String passo : passosRealizados) {
            tabela.append("<tr><td>").append(passo.split(":")[0])
                  .append("</td><td>").append(passo.split(":")[1].trim()).append("</td></tr>");
        }
        tabela.append("</table>");
        sendResponse(out, tabela.toString());
    }

    private static void obterPassos(PrintWriter out) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < sequenciaMontagem.length; i++) {
            json.append("\"").append(sequenciaMontagem[i]).append("\"");
            if (i < sequenciaMontagem.length - 1) json.append(",");
        }
        json.append("]");
        sendJsonResponse(out, json.toString());
    }

    private static void sendHtmlResponse(PrintWriter out, String filePath) {
        try {
            String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            sendResponse(out, htmlContent);
        } catch (IOException e) {
            sendResponse(out, "<h1>Erro ao carregar o arquivo HTML</h1>");
        }
    }

    private static void sendJsonResponse(PrintWriter out, String json) {
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n" +
                "\r\n" +
                json;
        out.print(httpResponse);
        out.flush();
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
