package ru.netology.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

public class MainServlet extends HttpServlet {

    private static final String API_PATH = "/api/posts";
    private static final String API_ID_PATH = "/api/posts/";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    private PostController controller;

    @Override
    public void init() {
        initializeController();
    }

    private void initializeController() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try (var reader = req.getReader()) {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            switch (method) {
                case GET:
                    if (path.equals(API_PATH)) {
                        controller.all(resp);
                    } else if (path.startsWith(API_ID_PATH)) {
                        final var id = Long.parseLong(path.substring(API_ID_PATH.length()));
                        controller.getById(id, resp);
                    }
                    break;
                case POST:
                    if (path.equals(API_PATH)) {
                        controller.save(reader, resp);
                    }
                    break;
                case DELETE:
                    if (path.matches(API_ID_PATH + "\\d+")) {
                        final var id = Long.parseLong(path.substring(API_ID_PATH.length()));
                        controller.removeById(id, resp);
                    }
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    // java -jar target/dependency/webapp-runner.jar target/servlets-1.0-SNAPShOT.war
}