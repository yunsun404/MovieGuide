package servlet;

import java.io.IOException;
import java.util.List;

import dao.MypageDAO;
import dto.GenresDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class UpdateGenres
 */
@WebServlet("/updateGenres")
public class UpdateGenres extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public UpdateGenres() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        dto.UserDTO loginUser = (dto.UserDTO) session.getAttribute("loginUser");
        int userNo = loginUser.getUserNo();

        MypageDAO dao = new MypageDAO();
        List<GenresDTO> allGenres = dao.getAllGenreNames();
        List<GenresDTO> userGenres = dao.getGenrePreferences(userNo);

        request.setAttribute("allGenres", allGenres);
        request.setAttribute("userGenres", userGenres);
        request.getRequestDispatcher("updateGenres.jsp").forward(request, response);
    }


	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        dto.UserDTO loginUser = (dto.UserDTO) session.getAttribute("loginUser");
        int userNo = loginUser.getUserNo();

        if (userNo <= 0) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        String[] likes = request.getParameterValues("likeGenres");
        String[] dislikes = request.getParameterValues("dislikeGenres");
        String[] neutrals = request.getParameterValues("neutralGenres");

        MypageDAO dao = new MypageDAO();
        int result = dao.updateGenrePreferences(userNo, likes, dislikes, neutrals);

        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/mypage");
        } else {
            response.sendRedirect(request.getContextPath() + "/updateGenres?error=true");
        }
    }

}
